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

package groovyx.javafx

import java.util.logging.Logger
import groovyx.javafx.factory.*
import org.codehaus.groovy.runtime.MethodClosure
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.util.HashSet;
import javafx.stage.Stage;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExceptionNoStack;
import org.codehaus.groovyfx.javafx.binding.ClosureTriggerBinding;
/**
 *
 * @author jimclarke
 */
public class SceneGraphBuilder extends FactoryBuilderSupport {
    // local fields
    private static final Logger LOG = Logger.getLogger(SceneGraphBuilder.name)
    private static boolean headless = false

    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

    public static final String DELEGATE_PROPERTY_OBJECT_FILL = "_delegateProperty:fill";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_FILL = "fill";

    public static final String DELEGATE_PROPERTY_OBJECT_STROKE = "_delegateProperty:stroke";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_STROKE = "stroke";
    
    public static final String CONTEXT_SCENE_KEY = "CurrentScene";


    private static final Random random = new Random()

    private Scene currentScene;

    static HashSet<String> colors = new HashSet<String>();

    static {
        colors.add("aliceblue");
        colors.add("antiquewhite");
        colors.add("aqua");
        colors.add("aquamarine");
        colors.add("azure");
        colors.add("beige");
        colors.add("bisque");
        colors.add("black");
        colors.add("blanchedalmond");
        colors.add("blue");
        colors.add("blueviolet");
        colors.add("brown");
        colors.add("burlywood");
        colors.add("cadetblue");
        colors.add("chartreuse");
        colors.add("chocolate");
        colors.add("coral");
        colors.add("cornflowerblue");
        colors.add("cornsilk");
        colors.add("crimson");
        colors.add("cyan");
        colors.add("darkblue");
        colors.add("darkcyan");
        colors.add("darkgoldenrod");
        colors.add("darkgray");
        colors.add("darkgreen");
        colors.add("darkgrey");
        colors.add("darkkhaki");
        colors.add("darkmagenta");
        colors.add("darkolivegreen");
        colors.add("darkorange");
        colors.add("darkorchid");
        colors.add("darkred");
        colors.add("darksalmon");
        colors.add("darkseagreen");
        colors.add("darkslateblue");
        colors.add("darkslategray");
        colors.add("darkslategrey");
        colors.add("darkturquoise");
        colors.add("darkviolet");
        colors.add("deeppink");
        colors.add("deepskyblue");
        colors.add("dimgray");
        colors.add("dimgrey");
        colors.add("dodgerblue");
        colors.add("firebrick");
        colors.add("floralwhite");
        colors.add("forestgreen");
        colors.add("fuchsia");
        colors.add("gainsboro");
        colors.add("ghostwhite");
        colors.add("gold");
        colors.add("goldenrod");
        colors.add("gray");
        colors.add("green");
        colors.add("greenyellow");
        colors.add("grey");
        colors.add("honeydew");
        colors.add("hotpink");
        colors.add("indianred");
        colors.add("indigo");
        colors.add("ivory");
        colors.add("khaki");
        colors.add("lavender");
        colors.add("lavenderblush");
        colors.add("lawngreen");
        colors.add("lemonchiffon");
        colors.add("lightblue");
        colors.add("lightcoral");
        colors.add("lightcyan");
        colors.add("lightgoldenrodyellow");
        colors.add("lightgray");
        colors.add("lightgreen");
        colors.add("lightgrey");
        colors.add("lightpink");
        colors.add("lightsalmon");
        colors.add("lightseagreen");
        colors.add("lightskyblue");
        colors.add("lightslategray");
        colors.add("lightslategrey");
        colors.add("lightsteelblue");
        colors.add("lightyellow");
        colors.add("lime");
        colors.add("limegreen");
        colors.add("linen");
        colors.add("magenta");
        colors.add("maroon");
        colors.add("mediumaquamarine");
        colors.add("mediumblue");
        colors.add("mediumorchid");
        colors.add("mediumpurple");
        colors.add("mediumseagreen");
        colors.add("mediumslateblue");
        colors.add("mediumspringgreen");
        colors.add("mediumturquoise");
        colors.add("mediumvioletred");
        colors.add("midnightblue");
        colors.add("mintcream");
        colors.add("mistyrose");
        colors.add("moccasin");
        colors.add("navajowhite");
        colors.add("navy");
        colors.add("oldlace");
        colors.add("olive");
        colors.add("olivedrab");
        colors.add("orange");
        colors.add("orangered");
        colors.add("orchid");
        colors.add("palegoldenrod");
        colors.add("palegreen");
        colors.add("paleturquoise");
        colors.add("palevioletred");
        colors.add("papayawhip");
        colors.add("peachpuff");
        colors.add("peru");
        colors.add("pink");
        colors.add("plum");
        colors.add("powderblue");
        colors.add("purple");
        colors.add("red");
        colors.add("rosybrown");
        colors.add("royalblue");
        colors.add("saddlebrown");
        colors.add("salmon");
        colors.add("sandybrown");
        colors.add("seagreen");
        colors.add("seashell");
        colors.add("sienna");
        colors.add("silver");
        colors.add("skyblue");
        colors.add("slateblue");
        colors.add("slategray");
        colors.add("slategrey");
        colors.add("snow");
        colors.add("springgreen");
        colors.add("steelblue");
        colors.add("tan");
        colors.add("teal");
        colors.add("thistle");
        colors.add("tomato");
        colors.add("transparent");
        colors.add("turquoise");
        colors.add("violet");
        colors.add("wheat");
        colors.add("white");
        colors.add("whitesmoke");
        colors.add("yellow");
        colors.add("yellowgreen");

    }

    public Stage stage;

    public SceneGraphBuilder(boolean init = true) {
        super(init)
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
    }
    
    public SceneGraphBuilder(Stage primaryStage, boolean init = true) {
        super(init)
        this.stage = primaryStage;
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
    }

    public Scene getCurrentScene() { return currentScene }
    public void setCurrentScene(Scene scene) { this.currentScene = scene}

    public SceneGraphBuilder defer(Closure c) {
        if (!(c instanceof MethodClosure)) {
            c = c.curry([this])
        }
        FX.deferAction(c);
        return this;
    }

    def propertyMissing(String name) {
        String lname = name.toLowerCase();
        if(lname.startsWith("#") || colors.contains(lname)) {
            return Color.web(name);
        }
        switch(lname) {
            case 'horizontal':
                return Orientation.HORIZONTAL;
            case 'vertical':
                return Orientation.VERTICAL;
        }
        throw new MissingPropertyException("Unrecognized property: ${name}", name, this.class);
    }
     protected void setNodeAttributes(Object node, Map attributes) {
        // set the properties
        //noinspection unchecked
        for (Map.Entry entry : (Set<Map.Entry>) attributes.entrySet()) {
            String property = entry.getKey().toString();
            Object value = entry.getValue();
            if(value instanceof ClosureTriggerBinding) {
                def ctb = (ClosureTriggerBinding)value;
                value = ctb.getSourceValue();
            }
            if(!FXHelper.fxAttribute(node, property, value))
                InvokerHelper.setProperty(node, property, value);
        }
    }
    def rgb(int r, int g, int b) {
        Color.rgb(r,g,b);
    }
    def rgb(int r, int g, int b, float alpha) {
        Color.rgb(r,g,b, alpha);
    }
    def hsb(int hue, float saturation, float brightness, float alpha) {
        Color.hsb(hue, saturation, brightness, alpha);
    }
    def hsb(int hue, float saturation, float brightness) {
        Color.hsb(hue, saturation, brightness);
    }
    public def registerStages() {
        registerFactory("stage", new StageFactory())
        
    }
    // register this one first
    public def registerNodes() {
        registerFactory("node", new CustomNodeFactory(Node.class, false));
        registerFactory("container", new CustomNodeFactory(Parent.class, false));
        def nf = new NodeFactory();
        addAttributeDelegate(nf.&bindingAttributeDelegate);
        registerFactory("imageView", nf);
        registerFactory("image", new ImageFactory());
    }

    public def registerContainers() {
        registerFactory("scene", new SceneFactory());
        registerFactory( 'stylesheets', new StylesheetFactory());

        ContainerFactory cf = new ContainerFactory();
        registerFactory( 'flowPane', cf)
        registerFactory( 'hbox', cf)
        registerFactory( 'vbox', cf)
        registerFactory( 'stackPane', cf)
        registerFactory( 'tilePane', cf)
        registerFactory( 'group', cf)
        registerFactory( 'gridPane', cf)
        registerFactory( 'constraint', new GridConstraintFactory());

        GridRowColumnFactory rcf = new GridRowColumnFactory();
        registerFactory( 'row', rcf)
        registerFactory( 'column', rcf)

    }

    public def registerBinding() {
        BindFactory bindFactory = new BindFactory();
        registerFactory("bind", bindFactory);
        addAttributeDelegate(bindFactory.&bindingAttributeDelegate);
    }


    public def registerThreading() {
        registerExplicitMethod "defer", this.&defer
    }
    
    public def registerTransforms() {
        TransformFactory tf = new TransformFactory();
        registerFactory( 'affine', tf);
        registerFactory( 'rotate', tf);
        registerFactory( 'scale', tf);
        registerFactory( 'shear', tf);
        registerFactory( 'translate', tf);
    }

    
    

    public def registerShapes() {
        ShapeFactory sf = new ShapeFactory();
        PathElementFactory pef = new PathElementFactory();

        registerFactory( 'arc', sf)
        registerFactory( 'circle', sf)
        registerFactory( 'cubicCurve', sf)
        registerFactory( 'ellipse', sf)
        registerFactory( 'line', sf)
        registerFactory( 'polygon', sf)
        registerFactory( 'polyline', sf)
        registerFactory( 'quadCurve', sf)
        registerFactory( 'rectangle', sf)
        registerFactory( 'shapeIntersect', sf)
        registerFactory( 'shapeSubtract', sf)
        registerFactory( 'SVGPath', sf)
        registerFactory( 'path', new PathFactory())
        registerFactory( 'arcTo', pef)
        registerFactory( 'closePath', pef)
        registerFactory( 'cubicCurveTo', pef)
        registerFactory( 'hLineTo', pef)
        registerFactory( 'lineTo', pef)
        registerFactory( 'moveTo', pef)
        registerFactory( 'quadCurveTo', pef)
        registerFactory( 'vLineTo', pef)
        registerFactory( 'text', new TextFactory())
    }

    public def registerControls() {
        LabeledFactory lf = new LabeledFactory();
        ControlFactory cf = new ControlFactory();
        TableFactory tf = new TableFactory();

        // labeled
        registerFactory( 'button', lf)
        registerFactory( 'checkBox', lf)
        registerFactory( 'label', lf)
        registerFactory( 'choiceBox', lf)
        registerFactory( 'hyperlink', lf)
        registerFactory( 'tooltip', lf)
        registerFactory( 'radioButton', lf)
        registerFactory( 'toggleButton', lf)

        // regular controls
        registerFactory( 'scrollBar', cf)
        registerFactory( 'slider', cf)
        registerFactory( 'separator', cf)
        registerFactory( 'listView', cf)
        registerFactory( 'passwordBox', cf)
        registerFactory( 'textBox', cf)
        registerFactory( 'progressBar', cf)
        registerFactory( 'progessIndicator', cf)
        registerFactory( 'scrollPane', cf)
        registerFactory( 'tableView', cf)
        //'indexedCell'
        //'cell'

        registerFactory( 'tableColumn', tf)
        registerFactory( 'cellFactory', tf)


    }

    public def registerEffects() {
        def ef = new EffectFactory();
        registerFactory( 'blend', ef);
        registerFactory( 'bloom', ef);
        registerFactory( 'boxBlur', ef)
        registerFactory( 'colorAdjust', ef);
        registerFactory( 'displacementMap', ef);
        registerFactory( 'dropShadow', ef);
        registerFactory( 'flood', ef);
        registerFactory( 'gaussianBlur', ef);
        registerFactory( 'glow', ef);
        registerFactory( 'identity', ef);
        registerFactory( 'innerShadow', ef);
        registerFactory( 'invertMask', ef);
        registerFactory( 'lighting', ef);
        registerFactory( 'motionBlur', ef);
        registerFactory( 'perspectiveTransform', ef);
        registerFactory( 'reflection', ef);
        registerFactory( 'sepiaTone', ef);
        registerFactory( 'shadow', ef);
        registerFactory( 'topInput', ef);
        registerFactory( 'bottomInput', ef);
        registerFactory( "distant", ef);
        registerFactory( "point", ef);
        registerFactory( "spot", ef);
    }


    public def registerInputListeners() {
        MouseHandlerFactory mf = new MouseHandlerFactory();
        KeyHandlerFactory kf = new KeyHandlerFactory();
        registerFactory( 'onMouseClicked', mf)
        registerFactory( 'onMouseDragged', mf)
        registerFactory( 'onMouseEntered', mf)
        registerFactory( 'onMouseExited', mf)
        registerFactory( 'onMouseMoved', mf)
        registerFactory( 'onMousePressed', mf)
        registerFactory( 'onMouseReleased', mf)
        registerFactory( 'onMouseWheelMoved', mf)
        registerFactory( 'onKeyPressed', kf)
        registerFactory( 'onKeyReleased', kf)
        registerFactory( 'onKeyTyped', kf)
    }

    public def registerWeb() {
        HttpFactory hf = new HttpFactory();
        WebFactory wf = new  WebFactory();

        registerFactory( 'httpRequest', hf)
        registerFactory( 'httpHeader', hf)

        registerFactory( 'webView', wf)
        registerFactory( 'webEngine', wf)

    }



    /**
     * Compatibility API.
     *
     * @param c run this closure in the builder
     */
    public Object build(Closure c) {
        c.setDelegate(this)
        return c.call()
    }



}

