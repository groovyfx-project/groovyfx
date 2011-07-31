/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package groovyx.javafx.beans;

import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.objectweb.asm.Opcodes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;



/**
 * Handles generation of code for the {@code @FXBindable} 
 * <p/>
 * Generally, it adds (if needed) a javafx.beans.property.Property type
 * <p/>
 * It also generates the setter and getter and wires the them through the
 * javafx.beans.property.Property.
 *
 * @author jimclarke (inspired by Danno Ferrin (shemnon) and Chris Reeved)
 */

@GroovyASTTransformation(phase= CompilePhase.CANONICALIZATION)
public class FXBindableASTTransformation implements ASTTransformation, Opcodes {
    protected static final ClassNode boundClassNode = ClassHelper.make(FXBindable.class);
    
    protected static final ClassNode objectPropertyClass = ClassHelper.make(ObjectProperty.class);
    protected static final ClassNode booleanPropertyClass = ClassHelper.make(BooleanProperty.class);
    protected static final ClassNode doublePropertyClass = ClassHelper.make(DoubleProperty.class);
    protected static final ClassNode floatPropertyClass = ClassHelper.make(FloatProperty.class);
    protected static final ClassNode intPropertyClass = ClassHelper.make(IntegerProperty.class);
    protected static final ClassNode longPropertyClass = ClassHelper.make(LongProperty.class);
    protected static final ClassNode stringPropertyClass = ClassHelper.make(StringProperty.class);
    protected static final ClassNode numberClassNode  = ClassHelper.make(Number.class);
    
    private static final Map<ClassNode, ClassNode> propertyTypeMap = new HashMap<ClassNode, ClassNode>();
    
    static {
        propertyTypeMap.put(ClassHelper.STRING_TYPE, stringPropertyClass);
        propertyTypeMap.put(ClassHelper.boolean_TYPE, booleanPropertyClass);
        propertyTypeMap.put(ClassHelper.Boolean_TYPE, booleanPropertyClass);
        propertyTypeMap.put(ClassHelper.double_TYPE, doublePropertyClass);
        propertyTypeMap.put(ClassHelper.Double_TYPE, doublePropertyClass);
        propertyTypeMap.put(ClassHelper.float_TYPE, floatPropertyClass);
        propertyTypeMap.put(ClassHelper.Float_TYPE, floatPropertyClass);
        propertyTypeMap.put(ClassHelper.int_TYPE, intPropertyClass);
        propertyTypeMap.put(ClassHelper.Integer_TYPE, intPropertyClass);
        propertyTypeMap.put(ClassHelper.long_TYPE, longPropertyClass);
        propertyTypeMap.put(ClassHelper.Long_TYPE, longPropertyClass);
        propertyTypeMap.put(ClassHelper.short_TYPE, intPropertyClass);
        propertyTypeMap.put(ClassHelper.Short_TYPE, intPropertyClass);
        propertyTypeMap.put(ClassHelper.byte_TYPE, intPropertyClass);
        propertyTypeMap.put(ClassHelper.Byte_TYPE, intPropertyClass);
        //propertyTypeMap.put(ClassHelper.char_TYPE, intPropertyClass);
        //propertyTypeMap.put(ClassHelper.Character_TYPE, intPropertyClass);
    }
    private static final Map<ClassNode, Expression> defaultReturnMap = new HashMap<ClassNode, Expression>();
    
    private static final Expression intZero = new ConstantExpression(new Integer(0), true);
    private static final Expression longZero = new ConstantExpression(new Long(0), true);
    private static final Expression floatZero = new ConstantExpression(new Float(0), true);
    private static final Expression doubleZero = new ConstantExpression(new Double(0), true);
    
    static {
        defaultReturnMap.put(ClassHelper.STRING_TYPE, ConstantExpression.NULL);
        defaultReturnMap.put(ClassHelper.boolean_TYPE, ConstantExpression.FALSE);
        defaultReturnMap.put(ClassHelper.Boolean_TYPE, ConstantExpression.FALSE);
        defaultReturnMap.put(ClassHelper.double_TYPE, doubleZero);
        defaultReturnMap.put(ClassHelper.Double_TYPE, doubleZero);
        defaultReturnMap.put(ClassHelper.float_TYPE, floatZero);
        defaultReturnMap.put(ClassHelper.Float_TYPE, floatZero);
        defaultReturnMap.put(ClassHelper.int_TYPE, intZero);
        defaultReturnMap.put(ClassHelper.Integer_TYPE, intZero);
        defaultReturnMap.put(ClassHelper.long_TYPE, longZero);
        defaultReturnMap.put(ClassHelper.Long_TYPE, longZero);
        defaultReturnMap.put(ClassHelper.short_TYPE, intZero);
        defaultReturnMap.put(ClassHelper.Short_TYPE, intZero);
        defaultReturnMap.put(ClassHelper.byte_TYPE, intZero);
        defaultReturnMap.put(ClassHelper.Byte_TYPE, intZero);
        defaultReturnMap.put(ClassHelper.char_TYPE, intZero);
        defaultReturnMap.put(ClassHelper.Character_TYPE, intZero);
    }

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

    
    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
        }
        AnnotationNode node = (AnnotationNode) nodes[0];
        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        
        ClassNode declaringClass = parent.getDeclaringClass();
        if (parent instanceof FieldNode) {
            if ((((FieldNode)parent).getModifiers() & Opcodes.ACC_FINAL) != 0) {
                source.getErrorCollector().addErrorAndContinue(
                            new SyntaxErrorMessage(new SyntaxException(
                                "@groovyfx.beans.FXBindable cannot annotate a final property.",
                                node.getLineNumber(),
                                node.getColumnNumber()),
                                source));
            }
            addJavaFXProperty(source, node, declaringClass, (FieldNode) parent);
         } else {
            addJavaFXPropertyToClass(source, node, (ClassNode) parent);
            
         }
    
    }
    
    private void addJavaFXProperty(SourceUnit source, AnnotationNode node, ClassNode declaringClass, FieldNode field) {
         String fieldName = field.getName();
        for (PropertyNode propertyNode : declaringClass.getProperties()) {
            if (propertyNode.getName().equals(fieldName)) {
                if (field.isStatic()) {
                    //noinspection ThrowableInstanceNeverThrown
                    source.getErrorCollector().addErrorAndContinue(
                                new SyntaxErrorMessage(new SyntaxException(
                                    "@groovy.beans.Bindable cannot annotate a static property.",
                                    node.getLineNumber(),
                                    node.getColumnNumber()),
                                    source));
                } else {
                    createPropertyGetterSetter(source, node, declaringClass, propertyNode);
                }
                return;
            }
        }
        //noinspection ThrowableInstanceNeverThrown
        source.getErrorCollector().addErrorAndContinue(
                new SyntaxErrorMessage(new SyntaxException(
                        "@groovyfx.beans.XBindable must be on a property, not a field.  Try removing the private, protected, or public modifier.",
                        node.getLineNumber(),
                        node.getColumnNumber()),
                        source));

    }
    
    private void addJavaFXPropertyToClass(SourceUnit source, AnnotationNode node, ClassNode classNode) {
        for (PropertyNode propertyNode : classNode.getProperties()) {
            FieldNode field = propertyNode.getField();
            // look to see if per-field handlers will catch this one...
            if (hasBindableAnnotation(field)
                || ((field.getModifiers() & Opcodes.ACC_FINAL) != 0)
                || field.isStatic() )
            {
                // explicitly labeled properties are already handled,
                // don't transform final properties
                // don't transform static properties
                // VetoableASTTransformation will handle both @Bindable and @Vetoable
                continue;
            }
            createPropertyGetterSetter(source, node, classNode, propertyNode);
        }

    }

    private void createPropertyGetterSetter(SourceUnit source, AnnotationNode node, ClassNode classNode, PropertyNode propertyNode) {
        Expression initExp = propertyNode.getInitialExpression();
        propertyNode.getField().setInitialValueExpression(null);
        ClassNode originalType = createProperty(classNode, propertyNode);
        String setterName = "set" + MetaClassHelper.capitalize(propertyNode.getName());
        if (classNode.getMethods(setterName).isEmpty()) {
            Expression fieldExpression = new FieldExpression(propertyNode.getField());
            Statement setterBlock = createSetterStatement(propertyNode, fieldExpression);

            // create method void <setter>(<type> fieldName)
            createSetterMethod(classNode, originalType, propertyNode, setterName, setterBlock);
        } else {
            wrapSetterMethod(classNode, propertyNode.getName());
        }
        
        String getterName = "get" + MetaClassHelper.capitalize(propertyNode.getName());
        if (classNode.getMethods(getterName).isEmpty()) {
            Expression fieldExpression = new FieldExpression(propertyNode.getField());
            Statement getterBlock = createGetterStatement(propertyNode, originalType, fieldExpression, initExp);

            // create method void <setter>(<type> fieldName)
            createGetterMethod(classNode, originalType, propertyNode, getterName, getterBlock);
        } else {
            wrapGetterMethod(classNode, propertyNode.getName());
        }
        createPropertyAccessor(classNode, propertyNode, initExp);
    }
    
    private ClassNode createProperty(ClassNode classNode, PropertyNode propertyNode) {
        ClassNode type = propertyNode.getType();
        
        ClassNode newType = propertyTypeMap.get(type);
        
        // For the ObjectProperty, we need to add the generic type to it.
        if(newType == null) {
            newType = ClassHelper.make(ObjectProperty.class);
            ClassNode genericType = type;
            if(genericType.isPrimaryClassNode()) {
                genericType = ClassHelper.getWrapper(genericType);
            }
            newType.setGenericsTypes(new GenericsType[] { new GenericsType(genericType) });
        }
        propertyNode.getField().setType(newType);
        return type;
    }
    
    protected void createSetterMethod(ClassNode declaringClass, ClassNode type, PropertyNode propertyNode, String setterName, Statement setterBlock) { 
        Parameter[] setterParameterTypes = {new Parameter(type, "value")};
        int mod = propertyNode.getModifiers() | Opcodes.ACC_FINAL;
        MethodNode setter =
                new MethodNode(setterName, mod, ClassHelper.VOID_TYPE, setterParameterTypes, ClassNode.EMPTY_ARRAY, setterBlock);
        setter.setSynthetic(false);
        // add it to the class
        declaringClass.addMethod(setter);

    }
    
    private void wrapSetterMethod(ClassNode classNode, String propertyName) {
            System.out.println("wrapSetterMethod");
    }
    
    protected void createGetterMethod(ClassNode declaringClass, ClassNode type, PropertyNode propertyNode, String getterName, Statement getterBlock) { 
        int mod = propertyNode.getModifiers() | Opcodes.ACC_FINAL;
        MethodNode getter =
                new MethodNode(getterName, mod, type, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, getterBlock);
        getter.setSynthetic(false);
        // add it to the class
        declaringClass.addMethod(getter);
    }
    
    private void wrapGetterMethod(ClassNode classNode, String propertyName) {
        System.out.println("wrapGetterMethod");
    }
    
    private void createPropertyAccessor(ClassNode classNode, PropertyNode propertyNode, Expression initExp) {
        String accessorName = propertyNode.getName() + "Property";
        FieldExpression fld = new FieldExpression(propertyNode.getField());
        
        ArgumentListExpression ctorArgs = initExp == null ? 
                ArgumentListExpression.EMPTY_ARGUMENTS :
                new ArgumentListExpression(initExp);
        
        
        BlockStatement block = new BlockStatement();
        
        IfStatement ifStmt = new IfStatement(
                new BooleanExpression(
                    new BinaryExpression(
                        fld,
                        Token.newSymbol(Types.COMPARE_EQUAL, 0, 0),
                        ConstantExpression.NULL
                    )
                ),
                new ExpressionStatement(
                    new BinaryExpression(
                        fld,
                        Token.newSymbol(Types.EQUAL, 0, 0),
                        new ConstructorCallExpression(propertyNode.getType(), 
                            ctorArgs)
                    )
                ),
                EmptyStatement.INSTANCE
            );
        block.addStatement(ifStmt);
        block.addStatement(new ReturnStatement( fld));
        
        MethodNode accessor =
                new MethodNode(accessorName, 
                    propertyNode.getModifiers(), 
                    propertyNode.getType(), 
                    Parameter.EMPTY_ARRAY, 
                    ClassNode.EMPTY_ARRAY, block);
        accessor.setSynthetic(true);
        // add it to the class
        classNode.addMethod(accessor);
    }
    
    protected Statement createSetterStatement(PropertyNode propertyNode, Expression fieldExpression) {
        // create statementBody
        VariableExpression property = new VariableExpression("$property");
        BlockStatement block = new BlockStatement();
        
        block.addStatement(new ExpressionStatement(
                new DeclarationExpression(property,
                    Token.newSymbol(Types.EQUALS, 0, 0),
                    new MethodCallExpression(
                        VariableExpression.THIS_EXPRESSION, 
                            propertyNode.getName() + "Property", 
                                ArgumentListExpression.EMPTY_ARGUMENTS))));

        block.addStatement(new ExpressionStatement(new MethodCallExpression(
                    property,
                    "setValue",
                    new ArgumentListExpression(
                            new Expression[]{ new VariableExpression("value") }))));
        
        return block;


    }
    
    protected Statement createGetterStatement(PropertyNode propertyNode, ClassNode type, Expression fieldExpression, Expression initExp) {
        VariableExpression property = new VariableExpression("$property");
        
        Expression defaultReturn = defaultReturnMap.get(type);
        if(defaultReturn == null)
             defaultReturn = ConstantExpression.NULL;
        
        BlockStatement elseBlock = new BlockStatement();
        elseBlock.addStatement(new ExpressionStatement(
                            new DeclarationExpression(property,
                                Token.newSymbol(Types.EQUALS, 0, 0),
                                new MethodCallExpression(
                                    VariableExpression.THIS_EXPRESSION, 
                                        propertyNode.getName() + "Property", 
                                            ArgumentListExpression.EMPTY_ARGUMENTS))));
        elseBlock.addStatement(new ReturnStatement(new ExpressionStatement(new MethodCallExpression(
                                property,
                                "getValue",
                                ArgumentListExpression.EMPTY_ARGUMENTS
                    ))));
        
        IfStatement ifStmt = new IfStatement(
                new BooleanExpression(
                    new BinaryExpression(
                        new ConstantExpression(propertyNode.getName()),
                        Token.newSymbol(Types.COMPARE_EQUAL, 0, 0),
                        ConstantExpression.NULL
                    )
                ),
                new ReturnStatement(
                    initExp!=null?initExp:defaultReturn
                ),
                elseBlock
        );
        return ifStmt;
    }
}
