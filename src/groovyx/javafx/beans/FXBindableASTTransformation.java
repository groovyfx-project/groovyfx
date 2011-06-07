/*
* Copyright 2011 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package groovyx.javafx.beans;

import groovy.beans.Vetoable;
import groovy.beans.VetoableASTTransformation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.objectweb.asm.Opcodes;


/**
 * Handles generation of code for the {@code @Bindable} annotation when {@code @Vetoable}
 * is not present.
 * <p/>
 * Generally, it adds (if needed) a PropertyChangeSupport field and
 * the needed add/removePropertyChangeListener methods to support the
 * listeners.
 * <p/>
 * It also generates the setter and wires the setter through the
 * PropertyChangeSupport.
 * <p/>
 * If a {@link Vetoable} annotation is detected it does nothing and
 * lets the {@link VetoableASTTransformation} handle all the changes.
 *
 * @author Danno Ferrin (shemnon)
 * @author Chris Reeves
 * @author Jim Clarke - FX version
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class FXBindableASTTransformation implements ASTTransformation, Opcodes {

    protected static ClassNode boundClassNode = new ClassNode(FXBindable.class);
    protected ClassNode pcsClassNode = new ClassNode(ObservableValue.class);

    /**
     * Convenience method to see if an annotated node is {@code @Bindable}.
     *
     * @param node the node to check
     * @return true if the node is bindable
     */
    public static boolean hasBindableAnnotation(AnnotatedNode node) {
        for (AnnotationNode annotation : node.getAnnotations()) {
            if (boundClassNode.equals(annotation.getClassNode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the bulk of the processing, mostly delegating to other methods.
     *
     * @param nodes   the ast nodes
     * @param source  the source unit for the nodes
     */
    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
        }
        AnnotationNode node = (AnnotationNode) nodes[0];
        AnnotatedNode parent = (AnnotatedNode) nodes[1];

        ClassNode declaringClass = parent.getDeclaringClass();
        if (parent instanceof FieldNode) {
            if ((((FieldNode) parent).getModifiers() & Opcodes.ACC_FINAL)  != 0) {
                source.getErrorCollector().addErrorAndContinue(
                        new SyntaxErrorMessage(new SyntaxException(
                        "@groovyx.javafx.beans.FXBindable cannot annotate a final property.",
                        node.getLineNumber(),
                        node.getColumnNumber()),
                        source));
            }

            addListenerToProperty(source, node, declaringClass, (FieldNode) parent);
        } else if (parent instanceof ClassNode) {
            addListenerToClass(source, node, (ClassNode) parent);
        }
    }

    private void addListenerToProperty(SourceUnit source, AnnotationNode node, ClassNode declaringClass, FieldNode field) {
        String fieldName = field.getName();
        for (PropertyNode propertyNode : declaringClass.getProperties()) {
            if (propertyNode.getName().equals(fieldName)) {
                if (field.isStatic()) {
                    //noinspection ThrowableInstanceNeverThrown
                    source.getErrorCollector().addErrorAndContinue(
                            new SyntaxErrorMessage(new SyntaxException(
                            "@groovyx.javafx.beans.FXBindable cannot annotate a static property.",
                            node.getLineNumber(),
                            node.getColumnNumber()),
                            source));
                } else {
                    createListenerSetter(source, node, declaringClass, propertyNode);
                }
                return;
            }
        }
        //noinspection ThrowableInstanceNeverThrown
        source.getErrorCollector().addErrorAndContinue(
                new SyntaxErrorMessage(new SyntaxException(
                "@groovyx.javafx.beans.FXBindable must be on a property, not a field.  Try removing the private, protected, or public modifier.",
                node.getLineNumber(),
                node.getColumnNumber()),
                source));
    }

    private void addListenerToClass(SourceUnit source, AnnotationNode node, ClassNode classNode) {
        for (PropertyNode propertyNode : classNode.getProperties()) {
            FieldNode field = propertyNode.getField();
            // look to see if per-field handlers will catch this one...
            if (hasBindableAnnotation(field)
                    || ((field.getModifiers() & Opcodes.ACC_FINAL ) != 0)
                    || field.isStatic()
                    ) {
                // explicitly labeled properties are already handled,
                // don't transform final properties
                // don't transform static properties
                // VetoableASTTransformation will handle both @Bindable and @Vetoable
                continue;
            }
            createListenerSetter(source, node, classNode, propertyNode);
        }
    }

    /*
     * Wrap an existing setter.
     */
    private void wrapSetterMethod(ClassNode classNode, String propertyName) {
        String getterName = "get" + MetaClassHelper.capitalize(propertyName);
        MethodNode setter = classNode.getSetterMethod("set" + MetaClassHelper.capitalize(propertyName));

        if (setter != null) {
            // Get the existing code block
            Statement code = setter.getCode();

            VariableExpression oldValue = new VariableExpression("$oldValue");
            VariableExpression newValue = new VariableExpression("$newValue");
            BlockStatement block = new BlockStatement();

            // create a local variable to hold the old value from the getter
            block.addStatement(new ExpressionStatement(
                    new DeclarationExpression(oldValue,
                    Token.newSymbol(Types.EQUALS, 0, 0),
                    new MethodCallExpression(VariableExpression.THIS_EXPRESSION, getterName, ArgumentListExpression.EMPTY_ARGUMENTS))));

            // call the existing block, which will presumably set the value properly
            block.addStatement(code);

            // get the new value to emit in the event
            block.addStatement(new ExpressionStatement(
                    new DeclarationExpression(newValue,
                    Token.newSymbol(Types.EQUALS, 0, 0),
                    new MethodCallExpression(VariableExpression.THIS_EXPRESSION, getterName, ArgumentListExpression.EMPTY_ARGUMENTS))));

            // add the firePropertyChange method call
            block.addStatement(new ExpressionStatement(new MethodCallExpression(
                    VariableExpression.THIS_EXPRESSION,
                    "fireChangedEvent",
                    new ArgumentListExpression(
                    new Expression[]{
                        new VariableExpression(propertyName.toUpperCase()),
                        }))));

            // replace the existing code block with our new one
            setter.setCode(block);
        }
    }

    private void createListenerSetter(SourceUnit source, AnnotationNode node, ClassNode classNode, PropertyNode propertyNode) {
        String setterName = "set" + MetaClassHelper.capitalize(propertyNode.getName());
        if (classNode.getMethods(setterName).isEmpty()) {
            Expression fieldExpression = new FieldExpression(propertyNode.getField());
            Statement setterBlock = createBindableStatement(propertyNode, fieldExpression);

            // create method void <setter>(<type> fieldName)
            createSetterMethod(classNode, propertyNode, setterName, setterBlock);
        } else {
            wrapSetterMethod(classNode, propertyNode.getName());
        }
    }

    /**
     * Creates a statement body similar to:
     * <code>this.firePropertyChange("field", field, field = value)</code>
     *
     * @param propertyNode           the field node for the property
     * @param fieldExpression a field expression for setting the property value
     * @return the created statement
     */
    protected Statement createBindableStatement(PropertyNode propertyNode, Expression fieldExpression) {
        // create statementBody
        return new ExpressionStatement(
                new MethodCallExpression(
                VariableExpression.THIS_EXPRESSION,
                "firePropertyChange",
                new ArgumentListExpression(
                new Expression[]{
                    new ConstantExpression(propertyNode.getName()),
                    fieldExpression,
                    new BinaryExpression(
                    fieldExpression,
                    Token.newSymbol(Types.EQUAL, 0, 0),
                    new VariableExpression("value"))})));
    }

    /**
     * Creates a setter method with the given body.
     *
     * @param declaringClass the class to which we will add the setter
     * @param propertyNode          the field to back the setter
     * @param setterName     the name of the setter
     * @param setterBlock    the statement representing the setter block
     */
    protected void createSetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String setterName, Statement setterBlock) {
        Parameter[] setterParameterTypes = {new Parameter(propertyNode.getType(), "value")};
        MethodNode setter =
                new MethodNode(setterName, propertyNode.getModifiers(), ClassHelper.VOID_TYPE, setterParameterTypes, ClassNode.EMPTY_ARRAY, setterBlock);
        setter.setSynthetic(true);
        // add it to the class
        declaringClass.addMethod(setter);
    }

}
