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
import javafx.application.Platform
import javafx.scene.media.MediaPlayerBuilder
import javafx.scene.SceneBuilder
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration
import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovyfx.javafx.binding.ClosureTriggerBinding
import groovyx.javafx.factory.*
import groovyx.javafx.factory.animation.*;
import javafx.scene.shape.*
import javafx.scene.chart.AreaChart
import javafx.scene.chart.BubbleChart
import javafx.scene.chart.BarChart
import javafx.scene.chart.ScatterChart

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableNumberValue;


import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;



/**
 *
 * @author jimclarke
 */
public class SceneGraphBuilder extends FactoryBuilderSupport {
    public static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

    public static final String DELEGATE_PROPERTY_OBJECT_FILL = "_delegateProperty:fill";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_FILL = "fill";

    public static final String DELEGATE_PROPERTY_OBJECT_STROKE = "_delegateProperty:stroke";
    public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_STROKE = "stroke";

    public static final String CONTEXT_SCENE_KEY = "CurrentScene";
    public static final String CONTEXT_DIVIDER_KEY = "CurrentDividers";

    private static final Logger LOG = Logger.getLogger(SceneGraphBuilder.name)
    private static final Random random = new Random()

    private Scene currentScene;

    public SceneGraphBuilder(boolean init = true) {
        super(init)
        initialize()
    }

    public SceneGraphBuilder(Stage primaryStage, boolean init = true) {
        super(init)
        this.variables.primaryStage = primaryStage
        initialize()
    }

    public Scene getCurrentScene() { return currentScene }
    public void setCurrentScene(Scene scene) { this.currentScene = scene}

    public SceneGraphBuilder defer(Closure c) {
        if (!(c instanceof MethodClosure)) {
            c = c.curry([this])
        }
        Platform.runLater(c);
        return this;
    }
    
    private static def propertyMap = [
        horizontal: Orientation.HORIZONTAL,
        vertical : Orientation.VERTICAL,
        ease_both: Interpolator.EASE_BOTH,
        easeboth: Interpolator.EASE_BOTH,
        easein: Interpolator.EASE_IN,
        ease_in: Interpolator.EASE_IN,
        easeout: Interpolator.EASE_OUT,
        ease_out: Interpolator.EASE_OUT,
        discrete: Interpolator.DISCRETE,
        linear: Interpolator.LINEAR,
        indefinite: Timeline.INDEFINITE
    ];

    def propertyMissing(String name) {
        if (name.startsWith("#")) {
            return Color.web(name);
        }

        String lname = name.toLowerCase();

        Color color = Color.NamedColors.namedColors[lname]
        if (color) { return color }
        def prop = propertyMap[lname];
        if(prop)
            return prop;
        
        throw new MissingPropertyException("Unrecognized property: ${name}", name, this.class);
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
        StageFactory factory = new StageFactory();
        registerFactory("stage", factory)
        registerFactory("popup", factory)
        registerFactory("fileChooser", factory)
        registerFactory("filter", new FilterFactory());
    }

    // register this one first
    public def registerNodes() {
        registerFactory("node", new CustomNodeFactory(Node.class, false));
        registerFactory("nodes", new CustomNodeFactory(List.class, true));
        registerFactory("container", new CustomNodeFactory(Parent.class, false));

        registerFactory("imageView", new NodeFactory());
        registerFactory("image", new ImageFactory());
        registerFactory("clip", new ClipFactory());
        registerFactory("fxml", new FXMLFactory());
    }

    public def registerContainers() {
        registerFactory( "scene", new SceneFactory());
        registerFactory( 'stylesheets', new StylesheetFactory());

        ContainerFactory cf = new ContainerFactory();
        registerFactory( 'pane', cf)
        registerFactory( 'anchorPane', cf)
        registerFactory( 'borderPane', cf)
        registerFactory( 'flowPane', cf)
        registerFactory( 'hbox', cf)
        registerFactory( 'vbox', cf)
        registerFactory( 'stackPane', cf)
        registerFactory( 'tilePane', cf)
        registerFactory( 'group', cf)
        registerFactory( 'gridPane', cf)

        GridConstraintFactory gcf = new GridConstraintFactory()
        registerFactory( 'constraint', gcf);
        registerFactory( 'rowConstraints', gcf);
        registerFactory( 'columnConstraints', gcf);

        GridRowColumnFactory rcf = new GridRowColumnFactory();
        registerFactory( 'row', rcf)
        registerFactory( 'column', rcf)
        
        BorderPanePositionFactory bppf = new BorderPanePositionFactory();
        registerFactory( 'top', bppf)
        registerFactory( 'bottom', bppf)
        registerFactory( 'left', bppf)
        registerFactory( 'right', bppf)
        registerFactory( 'center', bppf)
    }

    public def registerBinding() {
        BindFactory bindFactory = new BindFactory();
        ChangeFactory changeFactory = new ChangeFactory();
        
        registerFactory("bind", bindFactory);
        registerFactory("onChange", changeFactory);
    }

    public def registerThreading() {
        registerExplicitMethod "defer", this.&defer
    }

    public def registerMenus() {
        MenuFactory mf = new MenuFactory();
        MenuItemFactory mif = new MenuItemFactory();
        
        registerFactory( 'menuBar', mf);
        registerFactory( 'contextMenu', mf);
        registerFactory( 'menuButton', mf);
        registerFactory( 'splitMenuButton', mf);
        
        registerFactory( 'menu', mif);
        registerFactory( 'menuItem', mif);
        registerFactory( 'checkMenuItem', mif);
        registerFactory( 'customMenuItem', mif);
        registerFactory( 'separatorMenuItem', mif);
        registerFactory( 'radioMenuItem', mif);
    }

    public def registerCharts() {
        registerFactory('pieChart', new PieChartFactory())
        registerFactory('lineChart', new XYChartFactory(LineChart))
        registerFactory('areaChart', new XYChartFactory(AreaChart))
        registerFactory('bubbleChart', new XYChartFactory(BubbleChart))
        registerFactory('barChart', new XYChartFactory(BarChart))
        registerFactory('scatterChart', new XYChartFactory(ScatterChart))
        registerFactory('numberAxis', new AxisFactory(NumberAxis))
        registerFactory('categoryAxis', new AxisFactory(CategoryAxis))
        registerFactory('series', new XYSeriesFactory())
    }
    
    public def registerTransforms() {
        TransformFactory tf = new TransformFactory();
        registerFactory('affine', tf);
        registerFactory('rotate', tf);
        registerFactory('scale', tf);
        registerFactory('shear', tf);
        registerFactory('translate', tf);
    }

    public def registerShapes() {
        registerFactory('arc',        new ShapeFactory(Arc))
        registerFactory('circle',     new ShapeFactory(Circle))
        registerFactory('cubicCurve', new ShapeFactory(CubicCurve))
        registerFactory('ellipse',    new ShapeFactory(Ellipse))
        registerFactory('line',       new ShapeFactory(Line))
        registerFactory('polygon',    new ShapeFactory(Polygon))
        registerFactory('polyline',   new ShapeFactory(Polyline))
        registerFactory('quadCurve',  new ShapeFactory(QuadCurve))
        registerFactory('rectangle',  new ShapeFactory(Rectangle))
        registerFactory('svgPath',    new ShapeFactory(SVGPath))

        registerFactory('path', new PathFactory(Path))

        registerFactory('arcTo',        new PathElementFactory(ArcTo))
        registerFactory('closePath',    new PathElementFactory(ClosePath))
        registerFactory('cubicCurveTo', new PathElementFactory(CubicCurveTo))
        registerFactory('hLineTo',      new PathElementFactory(HLineTo))
        registerFactory('lineTo',       new PathElementFactory(LineTo))
        registerFactory('moveTo',       new PathElementFactory(MoveTo))
        registerFactory('quadCurveTo',  new PathElementFactory(QuadCurveTo))
        registerFactory('vLineTo',      new PathElementFactory(VLineTo))

        registerFactory('text', new TextFactory())

        registerFactory('linearGradient', new LinearGradientFactory())
        registerFactory('radialGradient', new RadialGradientFactory())
        registerFactory('stop',   new StopFactory())
        registerFactory('fill',   new FillFactory())
        registerFactory('stroke', new StrokeFactory())
    }

    public def registerControls() {
        LabeledFactory lf = new LabeledFactory();
        ControlFactory cf = new ControlFactory();
        TableFactory tf = new TableFactory();
        TitledFactory titledF = new TitledFactory();
        DividerPositionFactory df = new DividerPositionFactory();
        TabFactory tabf = new TabFactory();
        GraphicFactory gf = new GraphicFactory();
        TreeItemFactory treeItemf = new TreeItemFactory();
        TreeItemEventFactory treeItemEventf = new TreeItemEventFactory();
        TreeViewEventFactory treeViewEventf = new TreeViewEventFactory();

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
        registerFactory( 'textArea', cf)
        registerFactory( 'textField', cf)
        registerFactory( 'passwordField', cf)
        registerFactory( 'progressBar', cf)
        registerFactory( 'progressIndicator', cf)
        registerFactory( 'scrollPane', cf)
        registerFactory( 'tableView', tf)
        
        
        registerFactory( 'accordion', cf); // children node to panes list
        registerFactory( 'titledPane', cf); // Node title, Node content
        registerFactory( 'splitPane', cf); // left and right nodes
        registerFactory( 'dividerPosition', df);
        registerFactory( 'tabPane', cf); // add tabs
        registerFactory( 'tab', tabf);
        registerFactory( 'toolBar', cf); // items
        
        
        registerFactory( 'treeView', cf);
        registerFactory( 'treeItem', treeItemf);
        // popupControl
        
        //'indexedCell'
        //'cell'

        registerFactory( 'tableColumn', tf)
        
        registerFactory( 'title', titledF)
        registerFactory( 'content', titledF)
        
        registerFactory( 'graphic', gf);

        // tree events
        registerFactory( "onBranchCollapse", treeItemEventf)
        registerFactory( "onBranchExpand",treeItemEventf)
        registerFactory( "onChildrenModification",treeItemEventf)
        registerFactory( "onGraphicChanged",treeItemEventf)
        registerFactory( "onTreeItemCountChange",treeItemEventf)
        registerFactory( "onTreeNotification",treeItemEventf)
        registerFactory( "onValueChanged",treeItemEventf)
        
        registerFactory( "onEditCancel", treeViewEventf)
        registerFactory( "onEditCommit", treeViewEventf)
        registerFactory( "onEditStart", treeViewEventf)
    }

    public def registerEffects() {
        def ef = new EffectFactory()

        // Dummy node for attaching child effects
        registerFactory( 'effect', ef)

        registerFactory( 'blend', ef)
        registerFactory( 'bloom', ef)
        registerFactory( 'boxBlur', ef)
        registerFactory( 'colorAdjust', ef)
        registerFactory( "colorInput", ef)
        registerFactory( 'displacementMap', ef)
        registerFactory( 'dropShadow', ef)
        registerFactory( 'gaussianBlur', ef)
        registerFactory( 'glow', ef)
        registerFactory( 'imageInput', ef)
        registerFactory( 'innerShadow', ef)
        registerFactory( 'lighting', ef)
        registerFactory( 'motionBlur', ef)
        registerFactory( 'perspectiveTransform', ef)
        registerFactory( 'reflection', ef)
        registerFactory( 'sepiaTone', ef)
        registerFactory( 'shadow', ef)
        
        registerFactory( 'topInput', ef)
        registerFactory( 'bottomInput', ef)
        registerFactory( 'bumpInput', ef)
        registerFactory( 'contentInput', ef)
        registerFactory( "distant", ef)
        registerFactory( "point", ef)
        registerFactory( "spot", ef)
    }

    public def registerInputListeners() {
        MouseHandlerFactory mf = new MouseHandlerFactory()
        KeyHandlerFactory kf = new KeyHandlerFactory()
        ActionHandlerFactory af = new ActionHandlerFactory()

        registerFactory('onMouseClicked', mf)
        registerFactory('onMouseDragged', mf)
        registerFactory('onMouseEntered', mf)
        registerFactory('onMouseExited', mf)
        registerFactory('onMouseMoved', mf)
        registerFactory('onMousePressed', mf)
        registerFactory('onMouseReleased', mf)
        registerFactory('onDragDetected', mf)
        registerFactory('onDragDone', mf)
        registerFactory('onDragEntered', mf)
        registerFactory('onDragExited', mf)
        registerFactory('onDragOver', mf)
        registerFactory('onDragDropped', mf)
        registerFactory('onMouseWheelMoved', mf) // only works for scene.

        registerFactory('onKeyPressed', kf)
        registerFactory('onKeyReleased', kf)
        registerFactory('onKeyTyped', kf)

        registerFactory('onAction', af)
    }

    public def registerWeb() {
        // javafx.io package has been removed.
        //HttpFactory hf = new HttpFactory();
        //registerFactory( 'httpRequest', hf)
        //registerFactory( 'httpHeader', hf)

        WebFactory wf = new  WebFactory();
        registerFactory( 'webView', wf)
        registerFactory( 'webEngine', wf)
        registerFactory( 'htmlEditor', wf)
    }
    
    public def registerTransition() {
        TransitionFactory tf = new TransitionFactory();
        TimelineFactory tlf = new TimelineFactory();
        KeyFrameFactory kf = new KeyFrameFactory();
        KeyFrameActionFactory kfa = new KeyFrameActionFactory();
        KeyValueFactory kv = new KeyValueFactory();
        KeyValueSubFactory kvs = new KeyValueSubFactory();
        
        registerFactory( 'fadeTransition', tf);
        registerFactory( 'fillTransition', tf);
        registerFactory( 'parallelTransition', tf);
        registerFactory( 'pauseTransition', tf);
        registerFactory( 'rotateTransition', tf);
        registerFactory( 'scaleTransition', tf);
        registerFactory( 'translateTransition', tf);
        registerFactory( 'sequentialTransition', tf);
        registerFactory( 'pathTransition', tf);
        registerFactory( 'strokeTransition', tf);
        registerFactory( 'transition', tf);
        
        registerFactory( 'timeline', tlf);
        registerFactory("at", kf)
        registerFactory("action", kfa)
        registerFactory("change", kv)
        registerFactory("to", kvs)
        registerFactory("tween", kvs)
    }
    
    public def registerMedia() {
        MediaViewFactory mf = new MediaViewFactory();
        MediaPlayerFactory pf = new MediaPlayerFactory();

        registerFactory( 'mediaView', mf)
        registerFactory( 'player', pf)
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
    
    private def postCompletionDelegate = { FactoryBuilderSupport builder, Object parent, Object node ->
        if(node instanceof MediaPlayerBuilder || node instanceof SceneBuilder) {
            node = node.build();
            if(parent instanceof MediaView && node instanceof MediaPlayer) {
                parent.mediaPlayer = node;
            } else if(parent instanceof Stage && node instanceof Scene) {
                parent.scene = node
            }
        // If a non-builder is passed in do the parent check.
        } else if(parent instanceof MediaView && node instanceof MediaPlayer) {
            parent.mediaPlayer = node;
        } else if(parent instanceof Stage && node instanceof Scene) {
            parent.scene = node
        } else if(node instanceof FXMLLoaderBuilder) {
            node = node.build();
        }
     }

    private void initialize() {
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID

        ExpandoMetaClass.enableGlobally()
        addPostNodeCompletionDelegate(postCompletionDelegate)
        addAttributeDelegate(NodeFactory.attributeDelegate);
        addAttributeDelegate(BindFactory.bindingAttributeDelegate);

        Color.NamedColors.namedColors.put("groovyblue", Color.rgb(99, 152, 170))

        Number.metaClass{
            getM = {-> Duration.minutes(delegate)}
            getS = {-> Duration.seconds(delegate)}
            getMs = {-> Duration.millis(delegate)}
            getH = {-> Duration.hours(delegate)}
            
            // FX Properties
            plus << { ObservableNumberValue operand -> operand.add(delegate)}
            minus << { ObservableNumberValue operand -> 
                new SimpleDoubleProperty(delgate).subtract(operand)
            }
            multiply << { ObservableNumberValue operand -> operand.multiply(delegate)}
            div << { ObservableNumberValue operand -> 
                new SimpleDoubleProperty(delgate).divide(operand)
            }
        }
        
        DoubleProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
        }
        
        FloatProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
        }
        
        
        IntegerProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
        }
        
        
        LongProperty.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
        }
        
        BooleanProperty.metaClass {
            // or, and, and xor are already in the class groovy symbols | and &
            
            negative << {   delegate.not() }
            
            eq << { Boolean operand -> 
                delegate.isEqualTo(new SimpleBooleanProperty(operand))
            }
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Boolean operand -> 
                delegate.isNotEqualTo(new SimpleBooleanProperty(operand))
            }
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
            xor << { Boolean operand -> 
                delegate.isNotEqualTo(new SimpleBooleanProperty(operand))
            }
            xor << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
            
        }
        
        DoubleBinding.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
        }

        FloatBinding.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
        }

        IntegerBinding.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
        }

        LongBinding.metaClass {
            plus << { Number operand -> delegate.add(operand)}
            plus << { ObservableNumberValue operand -> delegate.add(operand)}
            
            minus << { Number operand -> delegate.subtract(operand)}
            minus << { ObservableNumberValue operand -> delegate.subtract(operand)}
            
            // multiply is already defined.
            //multiply << { Number operand -> delegate.multiply(operand)}
            //multiply << { ObservableNumberValue operand -> delegate.multiply(operand)}
            
            div << { Number operand -> delegate.divide(operand)}
            div << { ObservableNumberValue operand -> delegate.divide(operand)}
            
            negative << {   delegate.negate() }
            
            // aliases
            gt << { Number operand -> delegate.greaterThan(operand)}
            gt << { ObservableNumberValue operand -> delegate.greaterThan(operand)}
            
            ge << { Number operand -> delegate.greaterThanOrEqualTo(operand)}
            ge << { ObservableNumberValue operand -> delegate.greaterThanOrEqualTo(operand)}
            
            lt << { Number operand -> delegate.lessThan(operand)}
            lt << { ObservableNumberValue operand -> delegate.lessThan(operand)}
            
            le << { Number operand -> delegate.lessThanOrEqualTo(operand)}
            le << { ObservableNumberValue operand -> delegate.lessThanOrEqualTo(operand)}
            
            eq << { Number operand -> delegate.isEqualTo(operand)}
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Number operand -> delegate.isNotEqualTo(operand)}
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
        }
        
        BooleanBinding.metaClass {
            // or, and, and xor are already in the class groovy symbols | and &
            
            negative << {   delegate.not() }
            
            eq << { Boolean operand -> 
                def prop = new SimpleBooleanProperty();
                prop.set(operand);
                delegate.isEqualTo(prop)
            }
            eq << { ObservableNumberValue operand -> delegate.isEqualTo(operand)}
            
            ne << { Boolean operand -> 
                def prop = new SimpleBooleanProperty();
                prop.set(operand);
                delegate.isNotEqualTo(prop)
            }
            ne << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
            xor << { Boolean operand -> 
                def prop = new SimpleBooleanProperty();
                prop.set(operand);
                delegate.isNotEqualTo(prop)
            }
            xor << { ObservableNumberValue operand -> delegate.isNotEqualTo(operand)}
            
            
        }
    }
}

