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

import java.util.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.objectweb.asm.Opcodes;


/**
 * Handles generation of code for the {@code @FXBindable}
 * <p/>
 * Generally, it adds (if needed) a javafx.beans.property.Property type
 * <p/>
 * It also generates the setter and getter and wires the them through the
 * javafx.beans.property.Property.
 *
 * TODO: Support for read only properties
 * TODO: Support for eager and lazy properties
 *       
 * @author jimclarke (inspired by Danno Ferrin (shemnon) and Chris Reeved)
 * @author Dean Iverson
 */

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class FXBindableASTTransformation implements ASTTransformation, Opcodes {
    protected static final ClassNode boundClassNode = ClassHelper.make(FXBindable.class);

    protected static final ClassNode objectPropertyClass = ClassHelper.make(ObjectProperty.class, true);
    protected static final ClassNode booleanPropertyClass = ClassHelper.make(BooleanProperty.class);
    protected static final ClassNode doublePropertyClass = ClassHelper.make(DoubleProperty.class);
    protected static final ClassNode floatPropertyClass = ClassHelper.make(FloatProperty.class);
    protected static final ClassNode intPropertyClass = ClassHelper.make(IntegerProperty.class);
    protected static final ClassNode longPropertyClass = ClassHelper.make(LongProperty.class);
    protected static final ClassNode stringPropertyClass = ClassHelper.make(StringProperty.class);
    protected static final ClassNode listPropertyClass = ClassHelper.make(ListProperty.class);
    protected static final ClassNode mapPropertyClass = ClassHelper.make(MapProperty.class);
    protected static final ClassNode setPropertyClass = ClassHelper.make(SetProperty.class);
    
    protected static final ClassNode simpleBooleanPropertyClass = ClassHelper.make(SimpleBooleanProperty.class);
    protected static final ClassNode simpleDoublePropertyClass = ClassHelper.make(SimpleDoubleProperty.class);
    protected static final ClassNode simpleFloatPropertyClass = ClassHelper.make(SimpleFloatProperty.class);
    protected static final ClassNode simpleIntPropertyClass = ClassHelper.make(SimpleIntegerProperty.class);
    protected static final ClassNode simpleLongPropertyClass = ClassHelper.make(SimpleLongProperty.class);
    protected static final ClassNode simpleStringPropertyClass = ClassHelper.make(SimpleStringProperty.class);
    protected static final ClassNode simpleListPropertyClass = ClassHelper.make(SimpleListProperty.class);
    protected static final ClassNode simpleMapPropertyClass = ClassHelper.make(SimpleMapProperty.class);
    protected static final ClassNode simpleSetPropertyClass = ClassHelper.make(SimpleSetProperty.class);
    protected static final ClassNode simpleObjectPropertyClass = ClassHelper.make(SimpleObjectProperty.class, true);
//    protected static final ClassNode numberClassNode = ClassHelper.make(Number.class);
    
    protected static final ClassNode observableListClass = ClassHelper.make(ObservableList.class, true);
    protected static final ClassNode observableMapClass = ClassHelper.make(ObservableMap.class, true);
    protected static final ClassNode observableSetClass = ClassHelper.make(ObservableSet.class, true);
    protected static final ClassNode fxCollectionsType = ClassHelper.make(FXCollections.class, true);
    protected static final ClassNode listType = ClassHelper.make(List.class, true);
    protected static final ClassNode mapType = ClassHelper.make(Map.class, true);
    protected static final ClassNode setType = ClassHelper.make(Set.class, true);

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
    
    private static final Map<ClassNode, ClassNode> propertyImplMap = new HashMap<ClassNode, ClassNode>();
    
    static {
        propertyImplMap.put(booleanPropertyClass, simpleBooleanPropertyClass);
        propertyImplMap.put(doublePropertyClass, simpleDoublePropertyClass);
        propertyImplMap.put(floatPropertyClass, simpleFloatPropertyClass);
        propertyImplMap.put(intPropertyClass, simpleIntPropertyClass);
        propertyImplMap.put(longPropertyClass, simpleLongPropertyClass);
        propertyImplMap.put(stringPropertyClass, simpleStringPropertyClass);
        propertyImplMap.put(listPropertyClass, simpleListPropertyClass);
        propertyImplMap.put(mapPropertyClass, simpleMapPropertyClass);
        propertyImplMap.put(setPropertyClass, simpleSetPropertyClass);
        //propertyImplMap.put(objectPropertyClass, simpleObjectPropertyClass);
    }

//    private static final Expression intZero = new ConstantExpression(0, true);
//    private static final Expression longZero = new ConstantExpression(0L, true);
//    private static final Expression floatZero = new ConstantExpression(0.0f, true);
//    private static final Expression doubleZero = new ConstantExpression(0.0d, true);
//
//    private static final Map<ClassNode, Expression> defaultReturnMap = new HashMap<ClassNode, Expression>();
//
//    static {
//        defaultReturnMap.put(ClassHelper.STRING_TYPE, ConstantExpression.NULL);
//        defaultReturnMap.put(ClassHelper.boolean_TYPE, ConstantExpression.FALSE);
//        defaultReturnMap.put(ClassHelper.Boolean_TYPE, ConstantExpression.FALSE);
//        defaultReturnMap.put(ClassHelper.double_TYPE, doubleZero);
//        defaultReturnMap.put(ClassHelper.Double_TYPE, doubleZero);
//        defaultReturnMap.put(ClassHelper.float_TYPE, floatZero);
//        defaultReturnMap.put(ClassHelper.Float_TYPE, floatZero);
//        defaultReturnMap.put(ClassHelper.int_TYPE, intZero);
//        defaultReturnMap.put(ClassHelper.Integer_TYPE, intZero);
//        defaultReturnMap.put(ClassHelper.long_TYPE, longZero);
//        defaultReturnMap.put(ClassHelper.Long_TYPE, longZero);
//        defaultReturnMap.put(ClassHelper.short_TYPE, intZero);
//        defaultReturnMap.put(ClassHelper.Short_TYPE, intZero);
//        defaultReturnMap.put(ClassHelper.byte_TYPE, intZero);
//        defaultReturnMap.put(ClassHelper.Byte_TYPE, intZero);
//        defaultReturnMap.put(ClassHelper.char_TYPE, intZero);
//        defaultReturnMap.put(ClassHelper.Character_TYPE, intZero);
//    }

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
     * This ASTTransformation method is called when the compiler encounters our annotation.
     * @param nodes An array of nodes.  Index 0 is the annotation that triggered the call, index 1
     *              is the annotated node.
     * @param sourceUnit The SourceUnit describing the source code in which the annotation was placed.
     */
    @Override
    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
        }

        AnnotationNode node = (AnnotationNode) nodes[0];
        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        ClassNode declaringClass = parent.getDeclaringClass();

        if (parent instanceof FieldNode) {
            int modifiers = ((FieldNode) parent).getModifiers();
            if ((modifiers & Opcodes.ACC_FINAL) != 0) {
                String msg = "@groovyfx.beans.FXBindable cannot annotate a final property.";
                generateSyntaxErrorMessage(sourceUnit, node, msg);
            }
            addJavaFXProperty(sourceUnit, node, declaringClass, (FieldNode) parent);
        } else {
            addJavaFXPropertyToClass(sourceUnit, node, (ClassNode) parent);
        }
    }

    /**
     * Adds a JavaFX property to the class in place of the original Groovy property.  A pair of synthetic
     * getter/setter methods is generated to provide pseudo-access to the original property.
     *
     * @param source The SourceUnit in which the annotation was found
     * @param node The node that was annotated
     * @param declaringClass The class in which the annotation was found
     * @param field The field upon which the annotation was placed
     */
    private void addJavaFXProperty(SourceUnit source, AnnotationNode node, ClassNode declaringClass, FieldNode field) {
        String fieldName = field.getName();
        for (PropertyNode propertyNode : declaringClass.getProperties()) {
            if (propertyNode.getName().equals(fieldName)) {
                if (field.isStatic()) {
                    //noinspection ThrowableInstanceNeverThrown
                    String message = "@groovy.beans.Bindable cannot annotate a static property.";
                    generateSyntaxErrorMessage(source, node, message);
                } else {
                    createPropertyGetterSetter(declaringClass, propertyNode);
                }
                return;
            }
        }

        //noinspection ThrowableInstanceNeverThrown
        String message = "@groovyfx.beans.FXBindable must be on a property, not a field.  Try removing the private, " +
                         "protected, or public modifier.";
        generateSyntaxErrorMessage(source, node, message);
    }

    /**
     * Iterate through the properties of the class and convert each eligible property to a JavaFX property.
     * 
     * @param source The SourceUnit
     * @param node The AnnotationNode
     * @param classNode The declaring class
     */
    private void addJavaFXPropertyToClass(SourceUnit source, AnnotationNode node, ClassNode classNode) {
        for (PropertyNode propertyNode : classNode.getProperties()) {
            FieldNode field = propertyNode.getField();
            // look to see if per-field handlers will catch this one...
            if (hasBindableAnnotation(field)
                || ((field.getModifiers() & Opcodes.ACC_FINAL) != 0)
                || field.isStatic()) {
                // explicitly labeled properties are already handled,
                // don't transform final properties
                // don't transform static properties
                // VetoableASTTransformation will handle both @Bindable and @Vetoable
                continue;
            }
            createPropertyGetterSetter(classNode, propertyNode);
        }
    }

    /**
     * Creates the JavaFX property and three methods for accessing the property and a pair of
     * getter/setter methods for accessing the original (now synthetic) Groovy property.  For
     * example, if the original property was "String firstName" then these three methods would
     * be generated:
     *
     *     public String getFirstName()
     *     public void setFirstName(String value)
     *     public StringProperty firstNameProperty()
     *
     * @param classNode The declaring class in which the property will appear
     * @param originalProp The original Groovy property
     */
    private void createPropertyGetterSetter(ClassNode classNode, PropertyNode originalProp) {
        Expression initExp = originalProp.getInitialExpression();
        originalProp.getField().setInitialValueExpression(null);

        PropertyNode fxProperty = createFXProperty(originalProp);

        String setterName = "set" + MetaClassHelper.capitalize(originalProp.getName());
        if (classNode.getMethods(setterName).isEmpty()) {
            Statement setterBlock = createSetterStatement(createFXProperty(originalProp));
            createSetterMethod(classNode, originalProp, setterName, setterBlock);
        } else {
            wrapSetterMethod(classNode, originalProp.getName());
        }

        String getterName = "get" + MetaClassHelper.capitalize(originalProp.getName());
        if (classNode.getMethods(getterName).isEmpty()) {
            Statement getterBlock = createGetterStatement(createFXProperty(originalProp));
            createGetterMethod(classNode, originalProp, getterName, getterBlock);
        } else {
            wrapGetterMethod(classNode, originalProp.getName());
        }

        // We want the actual name of the field to be different from the getter (Prop vs Property) so
        // that the getter takes precedence when we say this.somethingProperty.
        FieldNode fxFieldShortName = createFieldNodeCopy(originalProp.getName() + "Prop", null, fxProperty.getField());
        createPropertyAccessor(classNode, createFXProperty(originalProp), fxFieldShortName, initExp);

        classNode.removeField(originalProp.getName());
        classNode.addField(fxFieldShortName);
    }

    /**
     * Creates a new PropertyNode for the JavaFX property based on the original property.  The new property
     * will have "Property" appended to its name and its type will be one of the *Property types in JavaFX.
     *
     * @param orig The original property
     * @return A new PropertyNode for the JavaFX property
     */
    private PropertyNode createFXProperty(PropertyNode orig) {
        ClassNode origType = orig.getType();
        ClassNode newType = propertyTypeMap.get(origType);

        // For the ObjectProperty, we need to add the generic type to it.
        if (newType == null) {
            if(origType.equals(observableListClass) || origType.declaresInterface(observableListClass)) {
                newType =  ClassHelper.make(SimpleListProperty.class, true);
                GenericsType[] genericTypes = origType.getGenericsTypes();
                newType.setGenericsTypes(genericTypes);
            }else if(origType.equals(observableMapClass) ||origType.declaresInterface(observableMapClass)) {
                newType = ClassHelper.make(SimpleMapProperty.class, true);
                GenericsType[] genericTypes = origType.getGenericsTypes();
                newType.setGenericsTypes(genericTypes);
            }else if(origType.equals(observableSetClass) ||origType.declaresInterface(observableSetClass)) {
                newType = ClassHelper.make(SimpleSetProperty.class, true);
                GenericsType[] genericTypes = origType.getGenericsTypes();
                newType.setGenericsTypes(genericTypes);
            }else { // Object Type
                //newType = ClassHelper.makeWithoutCaching(ObjectProperty.class, true);
            	newType = new ClassNode(ObjectProperty.class);
                ClassNode genericType = origType;
                if (genericType.isPrimaryClassNode()) {
                    genericType = ClassHelper.getWrapper(genericType);
                }
                newType.setGenericsTypes(new GenericsType[]{new GenericsType(genericType)});
            }
        }

        FieldNode fieldNode = createFieldNodeCopy(orig.getName() + "Property", newType, orig.getField());
        return new PropertyNode(fieldNode, orig.getModifiers(), orig.getGetterBlock(), orig.getSetterBlock());
    }

    /**
     * Creates a setter method and adds it to the declaring class.  The setter has the form:
     *
     *     void <setter>(<type> fieldName)
     *
     * @param declaringClass The class to which the method is added
     * @param propertyNode The property node being accessed by this setter
     * @param setterName The name of the setter method
     * @param setterBlock The code body of the method
     */
    protected void createSetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String setterName,
                                      Statement setterBlock) {
        Parameter[] setterParameterTypes = {new Parameter(propertyNode.getType(), "value")};
        int mod = propertyNode.getModifiers() | Opcodes.ACC_FINAL;

        MethodNode setter = new MethodNode(setterName, mod, ClassHelper.VOID_TYPE, setterParameterTypes,
                                           ClassNode.EMPTY_ARRAY, setterBlock);
        setter.setSynthetic(true);
        declaringClass.addMethod(setter);
    }
            
    

    /**
     * If the setter already exists, this method should wrap it with our code and then a call to the original
     * setter.
     *
     * TODO: Not implemented yet
     *
     * @param classNode The declaring class to which the method will be added
     * @param propertyName The name of the original Groovy property
     */
    private void wrapSetterMethod(ClassNode classNode, String propertyName) {
        System.out.println(
                String.format("wrapSetterMethod for '%s', property '%s' not yet implemented",
                classNode.getName(), propertyName));
    }

    /**
     * Creates a getter method and adds it to the declaring class.
     *
     * @param declaringClass The class to which the method is added
     * @param propertyNode The property node being accessed by this getter
     * @param getterName The name of the getter method
     * @param getterBlock The code body of the method
     */
    protected void createGetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String getterName,
                                      Statement getterBlock) {
        int mod = propertyNode.getModifiers() | Opcodes.ACC_FINAL;
        MethodNode getter = new MethodNode(getterName, mod, propertyNode.getType(), Parameter.EMPTY_ARRAY,
                                           ClassNode.EMPTY_ARRAY, getterBlock);
        getter.setSynthetic(true);
        declaringClass.addMethod(getter);
    }

    /**
     * If the getter already exists, this method should wrap it with our code.
     *
     * TODO: Not implemented yet -- what to do with the returned value from the original getter?
     *
     * @param classNode The declaring class to which the method will be added
     * @param propertyName The name of the original Groovy property
     */
    private void wrapGetterMethod(ClassNode classNode, String propertyName) {
        System.out.println(
                String.format("wrapGetterMethod for '%s', property '%s' not yet implemented",
                classNode.getName(), propertyName));
    }
    

    /**
     * Creates the body of a property access method that returns the JavaFX *Property instance.  If
     * the original property was "String firstName" then the generated code would be:
     *
     *     if (firstNameProperty == null) {
     *         firstNameProperty = new javafx.beans.property.StringProperty()
     *     }
     *     return firstNameProperty
     *
     * @param classNode The declaring class to which the JavaFX property will be added
     * @param fxProperty The new JavaFX property
     * @param fxFieldShortName
     * @param initExp The initializer expression from the original Groovy property declaration
     */
    private void createPropertyAccessor(ClassNode classNode, PropertyNode fxProperty, FieldNode fxFieldShortName,
                                        Expression initExp) {
        FieldExpression fieldExpression = new FieldExpression(fxFieldShortName);

        ArgumentListExpression ctorArgs = initExp == null ?
            ArgumentListExpression.EMPTY_ARGUMENTS :
            new ArgumentListExpression(initExp);

        BlockStatement block = new BlockStatement();
        ClassNode fxType = fxProperty.getType();
        ClassNode implNode = propertyImplMap.get(fxType);
        if(implNode == null) {
            if(fxType.getTypeClass() == simpleListPropertyClass.getTypeClass()) {
                if(initExp != null) {
                    if(initExp instanceof ListExpression ||
                       (initExp instanceof CastExpression && 
                            (((CastExpression)initExp).getType().equals(listType) || 
                            ((CastExpression)initExp).getType().declaresInterface(listType))) ||
                       (initExp instanceof ConstructorCallExpression && 
                            (((ConstructorCallExpression)initExp).getType().equals(listType) || 
                            ((ConstructorCallExpression)initExp).getType().declaresInterface(listType)))
                    ) {
                        ctorArgs = new ArgumentListExpression(
                            new MethodCallExpression(
                                new ClassExpression(fxCollectionsType), 
                                "observableList", 
                                ctorArgs)
                        );
                    }
                }
                implNode = fxType;
            }else if( fxType.getTypeClass() == simpleMapPropertyClass.getTypeClass()) {
                if(initExp != null ) {
                    if(initExp instanceof MapExpression ||
                       (initExp instanceof CastExpression && 
                            (((CastExpression)initExp).getType().equals(mapType) || 
                            ((CastExpression)initExp).getType().declaresInterface(mapType))) ||
                       (initExp instanceof ConstructorCallExpression && 
                            (((ConstructorCallExpression)initExp).getType().equals(mapType) || 
                            ((ConstructorCallExpression)initExp).getType().declaresInterface(mapType)))
                    ) {
                        ctorArgs = new ArgumentListExpression(
                            new MethodCallExpression(
                                new ClassExpression(fxCollectionsType), 
                                "observableMap", 
                                ctorArgs)
                        );
                    }
                }
                implNode = fxType;
            }else if( fxType.getTypeClass() == simpleSetPropertyClass.getTypeClass()) {
                if(initExp != null) {
                    if((initExp instanceof CastExpression && 
                            (((CastExpression)initExp).getType().equals(setType) || 
                            ((CastExpression)initExp).getType().declaresInterface(setType))) ||
                       (initExp instanceof ConstructorCallExpression && 
                            (((ConstructorCallExpression)initExp).getType().equals(setType) || 
                            ((ConstructorCallExpression)initExp).getType().declaresInterface(setType)))
                    ) {
                        ctorArgs = new ArgumentListExpression(
                            new MethodCallExpression(
                                new ClassExpression(fxCollectionsType), 
                                "observableSet", 
                                ctorArgs)
                        );
                    }
                }
                implNode = fxType;
            }else {
                //implNode = ClassHelper.make(SimpleObjectProperty.class, true);
            	implNode = new ClassNode(SimpleObjectProperty.class);
                GenericsType[] origGenerics = fxProperty.getType().getGenericsTypes();
                //List<GenericsType> copyGenericTypes = new ArrayList<GenericsType>();
                //for()
                implNode.setGenericsTypes(origGenerics);
            }
        }
        Expression initExpression = new ConstructorCallExpression(implNode, ctorArgs);
        
        IfStatement ifStmt = new IfStatement(
            new BooleanExpression(
                new BinaryExpression(
                    fieldExpression,
                    Token.newSymbol(Types.COMPARE_EQUAL, 0, 0),
                    ConstantExpression.NULL
                )
            ),
            new ExpressionStatement(
                new BinaryExpression(
                    fieldExpression,
                    Token.newSymbol(Types.EQUAL, 0, 0),
                    initExpression
                )
            ),
            EmptyStatement.INSTANCE
        );
        block.addStatement(ifStmt);
        block.addStatement(new ReturnStatement(fieldExpression));

        String getterName = getFXPropertyGetterName(fxProperty);
        MethodNode accessor = new MethodNode(getterName, fxProperty.getModifiers(), fxProperty.getType(),
                                             Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block);
        accessor.setSynthetic(true);
        classNode.addMethod(accessor);
        
        // Create the xxxxProperty() method that merely calls getXxxxProperty()
        block = new BlockStatement();
        
        VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION;
        ArgumentListExpression emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS;
        
        MethodCallExpression getProperty = new MethodCallExpression(thisExpression, getterName, emptyArguments);
        block.addStatement(new ReturnStatement(getProperty));
        
        String javaFXPropertyFunction = fxProperty.getName();
        
        accessor = new MethodNode(javaFXPropertyFunction, fxProperty.getModifiers(), fxProperty.getType(),
                                             Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block);
        accessor.setSynthetic(true);
        classNode.addMethod(accessor);
        
        // Create the xxxx() method that merely calls getXxxxProperty()
        block = new BlockStatement();
        
        thisExpression = VariableExpression.THIS_EXPRESSION;
        emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS;
        
        getProperty = new MethodCallExpression(thisExpression, getterName, emptyArguments);
        block.addStatement(new ReturnStatement(getProperty));
        javaFXPropertyFunction = fxProperty.getName().replace("Property", "");
        
        accessor = new MethodNode(javaFXPropertyFunction, fxProperty.getModifiers(), fxProperty.getType(),
                                             Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block);
        accessor.setSynthetic(true);
        classNode.addMethod(accessor);
        
        
    }

    /**
     * Creates the body of a setter method for the original property that is actually backed by a
     * JavaFX *Property instance:
     *
     *     Object $property = this.someProperty()
     *     $property.setValue(value)
     *
     * @param fxProperty The original Groovy property that we're creating a setter for.
     * @return A Statement that is the body of the new setter.
     */
    protected Statement createSetterStatement(PropertyNode fxProperty) {
        String fxPropertyGetter = getFXPropertyGetterName(fxProperty);
        VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION;
        ArgumentListExpression emptyArgs = ArgumentListExpression.EMPTY_ARGUMENTS;

        MethodCallExpression getProperty = new MethodCallExpression(thisExpression, fxPropertyGetter, emptyArgs);

        ArgumentListExpression valueArg = new ArgumentListExpression(new Expression[]{new VariableExpression("value")});
        MethodCallExpression setValue = new MethodCallExpression(getProperty, "setValue", valueArg);

        return new ExpressionStatement(setValue);
    }
      

    /**
     * Creates the body of a getter method for the original property that is actually backed by a
     * JavaFX *Property instance:
     *
     *     Object $property = this.someProperty()
     *     return $property.getValue()
     *
     * @param fxProperty The new JavaFX property.
     * @return A Statement that is the body of the new getter.
     */
    protected Statement createGetterStatement(PropertyNode fxProperty) {
        String fxPropertyGetter = getFXPropertyGetterName(fxProperty);
        VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION;
        ArgumentListExpression emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS;

        // We're relying on the *Property() method to provide the return value - is this still needed??
//        Expression defaultReturn = defaultReturnMap.get(originalProperty.getType());
//        if (defaultReturn == null)
//            defaultReturn = ConstantExpression.NULL;

        MethodCallExpression getProperty = new MethodCallExpression(thisExpression, fxPropertyGetter, emptyArguments);
        MethodCallExpression getValue = new MethodCallExpression(getProperty, "getValue", emptyArguments);

        return new ReturnStatement(new ExpressionStatement(getValue));
    }

    /**
     * Generates a SyntaxErrorMessage based on the current SourceUnit, AnnotationNode, and a specified
     * error message.
     *
     * @param sourceUnit The SourceUnit
     * @param node The node that was annotated
     * @param msg The error message to display
     */
    private void generateSyntaxErrorMessage(SourceUnit sourceUnit, AnnotationNode node, String msg) {
        SyntaxException error = new SyntaxException(msg, node.getLineNumber(), node.getColumnNumber());
        sourceUnit.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(error, sourceUnit));
    }

    /**
     * Creates a copy of a FieldNode with a new name and, optionally, a new type.
     *
     * @param newName The name for the new field node.
     * @param newType The new type of the field.  If null, the old FieldNode's type will be used.
     * @param f The FieldNode to copy.
     * @return The new FieldNode.
     */
    private FieldNode createFieldNodeCopy(String newName, ClassNode newType, FieldNode f) {
        if (newType == null)
            newType = f.getType();
        newType = newType.getPlainNodeReference();

        return new FieldNode(newName, f.getModifiers(), newType, f.getOwner(), f.getInitialValueExpression());
    }

    /**
     * Generates the correct getter method name for a JavaFX property.
     * 
     * @param fxProperty The property for which the getter should be generated.
     * @return The getter name as a String.
     */
    private String getFXPropertyGetterName(PropertyNode fxProperty) {
        return "get" + capitalize(fxProperty.getName());
    }

    /**
     * Capitalize the first letter of the given string.
     *
     * @param string The source string
     * @return The capitalized string
     */
    private String capitalize(String string) {
        if (string == null || string.equals(""))
            return string;

        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
}
