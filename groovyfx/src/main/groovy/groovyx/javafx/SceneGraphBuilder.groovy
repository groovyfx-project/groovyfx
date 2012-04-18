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

import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.runtime.MethodClosure
import org.codehaus.groovyfx.javafx.binding.ClosureTriggerBinding
import groovy.util.*

import groovyx.javafx.factory.*
import groovyx.javafx.factory.animation.*
import groovyx.javafx.event.*
import groovyx.javafx.animation.*

import javafx.application.*
import javafx.event.*
import javafx.beans.*
import javafx.beans.property.*
import javafx.beans.value.*
import javafx.beans.binding.*
import javafx.animation.*
import javafx.concurrent.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.chart.*
import javafx.scene.effect.*
import javafx.scene.image.*
import javafx.scene.layout.*
import javafx.scene.media.*
import javafx.scene.paint.*
import javafx.scene.shape.*
import javafx.scene.text.*
import javafx.scene.web.*
import javafx.scene.transform.*

import javafx.geometry.*
import javafx.stage.*
import javafx.util.*

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

    SceneGraphBuilder(boolean init = true) {
        super(init)
        initialize()
    }

    SceneGraphBuilder(Stage primaryStage, boolean init = true) {
        super(init)
        this.variables.primaryStage = primaryStage
        initialize()
    }

    Stage getPrimaryStage() { return variables.primaryStage }

    Scene getCurrentScene() { return currentScene }
    void setCurrentScene(Scene scene) { this.currentScene = scene}

    SceneGraphBuilder defer(Closure c) {
        if (!(c instanceof MethodClosure)) {
            c = c.curry([this])
        }
        Platform.runLater(c);
        return this;
    }
    
    SceneGraphBuilder submit(WebView wv, Closure c) {
        //if (!(c instanceof MethodClosure)) {
        //    c = c.curry([this])
        //}
        
        if(wv.engine.loadWorker.state == Worker.State.SUCCEEDED) {
             c.call(wv);
        }else {
            def listener = new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, 
                            Worker.State oldState, Worker.State newState) {
                    switch(newState) {
                        case Worker.State.SUCCEEDED:
                            c.call(wv);
                            wv.engine.loadWorker.stateProperty().removeListener(this);
                            break;
                        case Worker.State.FAILED:
                            System.out.println(wv.engine.loadWorker.exception);
                            wv.engine.loadWorker.stateProperty().removeListener(this);
                            break;
                        case Worker.State.CANCELED:
                            System.out.println(wv.engine.loadWorker.message);
                            wv.engine.loadWorker.stateProperty().removeListener(this);
                            break;
                        default:
                            break;

                    }
                }
            };
             wv.engine.loadWorker.stateProperty().addListener(listener);
        }
        
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
        indefinite: Timeline.INDEFINITE,
        top: VPos.TOP,
        bottom: VPos.BOTTOM,
        left: HPos.LEFT,
        right: HPos.RIGHT,
        top_left: Pos.TOP_LEFT ,
        top_center: Pos.TOP_CENTER ,
        top_right: Pos.TOP_RIGHT ,
        center_left: Pos.CENTER_LEFT ,
        center: Pos.CENTER,
        center_right: Pos.CENTER_RIGHT ,
        bottom_left: Pos.BOTTOM_LEFT ,
        bottom_center: Pos.BOTTOM_CENTER ,
        bottom_right: Pos.BOTTOM_RIGHT ,
        baseline_center: Pos.BASELINE_CENTER ,
        baseline_right: Pos.BASELINE_RIGHT ,
        baseline_left: Pos.BASELINE_LEFT ,
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

    def rgba(int r, int g, int b, float alpha) {
        rgb(r,g,b, alpha);
    }

    def hsb(int hue, float saturation, float brightness, float alpha) {
        Color.hsb(hue, saturation, brightness, alpha);
    }

    def hsb(int hue, float saturation, float brightness) {
        Color.hsb(hue, saturation, brightness);
    }
    
    public void registerBeanFactory(String nodeName, String groupName, Class beanClass) {
        // poke at the type to see if we need special handling
        if(ContextMenu.isAssignableFrom(beanClass) ||
             MenuBar.isAssignableFrom(beanClass) ||  
             MenuButton.isAssignableFrom(beanClass) ||  
             SplitMenuButton.isAssignableFrom(beanClass)  ) {
            registerFactory nodeName, groupName, new MenuFactory(beanClass) 
        }else if(MenuItem.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new MenuItemFactory(beanClass)
        }else if(TreeItem.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new TreeItemFactory(beanClass)
        }else if(TableView.isAssignableFrom(beanClass) ||
            TableColumn.isAssignableFrom(beanClass) ){
            registerFactory nodeName, groupName, new TableFactory(beanClass)
        }else if(Labeled.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new LabeledFactory(beanClass)   
        } else if(Control.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new ControlFactory(beanClass) 
        } else if(Scene.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new SceneFactory(beanClass)  
        } else if(Tab.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new TabFactory(beanClass)
        } else if(Text.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new TextFactory(beanClass)
        } else if(Shape.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new ShapeFactory(beanClass)
        } else if(Transform.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new TransformFactory(beanClass)
        } else if(Effect.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new EffectFactory(beanClass)
        } else if(Parent.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new ContainerFactory(beanClass) 
        } else if(Window.isAssignableFrom(beanClass) ||
            DirectoryChooser.isAssignableFrom(beanClass) ||
            FileChooser.isAssignableFrom(beanClass)) {
                registerFactory nodeName, groupName, new StageFactory(beanClass) 
        } else if(XYChart.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new XYChartFactory(beanClass)   
        } else if(PieChart.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new PieChartFactory(beanClass)  
        } else if(Axis.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new AxisFactory(beanClass)  
        } else if(XYChart.Series.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new XYSeriesFactory(beanClass)  
        } else if (Node.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new NodeFactory(beanClass)
        } else {
            super.registerBeanFactory(nodeName, groupName, beanClass)
        }
    }
    
    

    public def registerStages() {
        StageFactory factory = new StageFactory();
        registerFactory "stage", new StageFactory(Stage)
        registerFactory "popup", new StageFactory(Popup)
        registerFactory "fileChooser", new StageFactory(FileChooser)
        registerFactory "directoryChooser", new StageFactory(DirectoryChooser)
        registerFactory "filter", new FilterFactory()
        
        registerFactory "onHidden",  new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory "onHiding",  new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory "onShowing",  new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory "onShown",  new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory "onCloseRequest",  new ClosureHandlerFactory(GroovyEventHandler) 
    }

    // register this one first
    public def registerNodes() {
        registerFactory "node", new CustomNodeFactory(javafx.scene.Node)
        registerFactory "nodes", new CustomNodeFactory(List, true)
        registerFactory "container", new CustomNodeFactory(Parent)

        registerFactory "imageView", new ImageViewFactory(ImageView)
        registerFactory "image", new ImageFactory(Image)
        registerFactory "clip", new ClipFactory(ClipHolder)
        registerFactory "fxml", new FXMLFactory()
    }

    public def registerContainers() {
        registerFactory "scene", new SceneFactory(Scene)
        registerFactory 'stylesheets', new StylesheetFactory(List)
        registerFactory 'resource', new ResourceFactory()
        
        registerFactory 'pane', new ContainerFactory(Pane)
        registerFactory 'region', new ContainerFactory(Region)
        registerFactory 'anchorPane', new ContainerFactory(AnchorPane)
        registerFactory 'borderPane', new ContainerFactory(BorderPane)
        registerFactory 'flowPane', new ContainerFactory(FlowPane)
        registerFactory 'hbox', new ContainerFactory(HBox)
        registerFactory 'vbox', new ContainerFactory(VBox)
        registerFactory 'stackPane', new ContainerFactory(StackPane)
        registerFactory 'tilePane', new ContainerFactory(TilePane)
        registerFactory 'group', new ContainerFactory(Group)
        registerFactory 'gridPane', new ContainerFactory(GridPane)

        registerFactory 'constraint', new GridConstraintFactory(GridConstraint)
        registerFactory 'rowConstraints', new GridConstraintFactory(RowConstraints)
        registerFactory 'columnConstraints', new GridConstraintFactory(ColumnConstraints)

        registerFactory 'row', new GridRowColumnFactory(GridRow);
        registerFactory 'column', new GridRowColumnFactory(GridColumn);
        
        registerFactory 'top',new BorderPanePositionFactory(BorderPanePosition)
        registerFactory 'bottom', new BorderPanePositionFactory(BorderPanePosition)
        registerFactory 'left', new BorderPanePositionFactory(BorderPanePosition)
        registerFactory 'right', new BorderPanePositionFactory(BorderPanePosition)
        registerFactory 'center', new BorderPanePositionFactory(BorderPanePosition)
    }

    public def registerBinding() {
        registerFactory "bind", new BindFactory();
        registerFactory "onChange", new ChangeFactory(ChangeListener)
        registerFactory "onInvalidate", new ChangeFactory(InvalidationListener)
    }

    public def registerThreading() {
        registerExplicitMethod "defer", this.&defer
    }

    public def registerMenus() {
        registerFactory 'menuBar', new MenuFactory(MenuBar)
        registerFactory 'contextMenu', new MenuFactory(ContextMenu)
        registerFactory 'menuButton', new MenuFactory(MenuButton)
        registerFactory 'splitMenuButton', new MenuFactory(SplitMenuButton)
        
        registerFactory 'menu', new MenuItemFactory(Menu);
        registerFactory 'menuItem', new MenuItemFactory(MenuItem);
        registerFactory 'checkMenuItem', new MenuItemFactory(CheckMenuItem);
        registerFactory 'customMenuItem', new MenuItemFactory(CustomMenuItem);
        registerFactory 'separatorMenuItem', new MenuItemFactory(SeparatorMenuItem);
        registerFactory 'radioMenuItem', new MenuItemFactory(RadioMenuItem);
    }

    public def registerCharts() {
        registerFactory 'pieChart', new PieChartFactory(PieChart)
        registerFactory 'lineChart', new XYChartFactory(LineChart)
        registerFactory 'areaChart', new XYChartFactory(AreaChart)
        registerFactory 'stackedAreaChart', new XYChartFactory(StackedAreaChart)
        registerFactory 'bubbleChart', new XYChartFactory(BubbleChart)
        registerFactory 'barChart', new XYChartFactory(BarChart)
        registerFactory 'stackedBarChart', new XYChartFactory(StackedBarChart)
        registerFactory 'scatterChart', new XYChartFactory(ScatterChart)
        registerFactory 'numberAxis', new AxisFactory(NumberAxis)
        registerFactory 'categoryAxis', new AxisFactory(CategoryAxis)
        registerFactory 'series', new XYSeriesFactory(XYChart.Series)
    }
    
    public def registerTransforms() {
        registerFactory 'affine', new TransformFactory(Affine)
        registerFactory 'rotate', new TransformFactory(Rotate)
        registerFactory 'scale', new TransformFactory(Scale)
        registerFactory 'shear', new TransformFactory(Shear)
        registerFactory 'translate', new TransformFactory(Translate)
    }

    public def registerShapes() {
        registerFactory 'arc',        new ShapeFactory(Arc)
        registerFactory 'circle',     new ShapeFactory(Circle)
        registerFactory 'cubicCurve', new ShapeFactory(CubicCurve)
        registerFactory 'ellipse',    new ShapeFactory(Ellipse)
        registerFactory 'line',       new ShapeFactory(Line)
        registerFactory 'polygon',    new ShapeFactory(Polygon)
        registerFactory 'polyline',   new ShapeFactory(Polyline)
        registerFactory 'quadCurve',  new ShapeFactory(QuadCurve)
        registerFactory 'rectangle',  new ShapeFactory(Rectangle)
        registerFactory 'svgPath',    new ShapeFactory(SVGPath)

        registerFactory 'path', new PathFactory(Path)

        registerFactory 'arcTo',        new PathElementFactory(ArcTo)
        registerFactory 'closePath',    new PathElementFactory(ClosePath)
        registerFactory 'cubicCurveTo', new PathElementFactory(CubicCurveTo)
        registerFactory 'hLineTo',      new PathElementFactory(HLineTo)
        registerFactory 'lineTo',       new PathElementFactory(LineTo)
        registerFactory 'moveTo',       new PathElementFactory(MoveTo)
        registerFactory 'quadCurveTo',  new PathElementFactory(QuadCurveTo)
        registerFactory 'vLineTo',      new PathElementFactory(VLineTo)

        registerFactory 'text', new TextFactory(Text)

        registerFactory 'linearGradient', new LinearGradientFactory()
        registerFactory 'radialGradient', new RadialGradientFactory()
        registerFactory 'stop',   new StopFactory()
        registerFactory 'fill',   new FillFactory()
        registerFactory 'stroke', new StrokeFactory()
    }

    public def registerControls() {

        // labeled
        registerFactory 'button', new LabeledFactory(Button)
        registerFactory 'checkBox', new LabeledFactory(CheckBox)
        registerFactory 'label', new LabeledFactory(Label)
        registerFactory 'choiceBox', new LabeledFactory(ChoiceBox)
        registerFactory 'hyperlink', new LabeledFactory(Hyperlink)
        registerFactory 'tooltip', new LabeledFactory(Tooltip)
        registerFactory 'radioButton', new LabeledFactory(RadioButton)
        registerFactory 'toggleButton', new LabeledFactory(ToggleButton)
        
        registerFactory "onSelect", new CellFactory()
        registerFactory "cellFactory", new CellFactory()
        registerFactory "onAction", new ClosureHandlerFactory(GroovyEventHandler);
        

        // regular controls
        registerFactory 'scrollBar', new ControlFactory(ScrollBar)
        registerFactory 'slider', new ControlFactory(Slider)
        registerFactory 'separator', new ControlFactory(Separator)
        registerFactory 'listView', new ControlFactory(ListView)
        registerFactory 'textArea', new ControlFactory(TextArea)
        registerFactory 'textField', new ControlFactory(TextField)
        registerFactory 'passwordField', new ControlFactory(PasswordField)
        registerFactory 'progressBar', new ControlFactory(ProgressBar)
        registerFactory 'progressIndicator', new ControlFactory(ProgressIndicator)
        registerFactory 'scrollPane', new ControlFactory(ScrollPane)
        
        registerFactory 'comboBox', new ControlFactory(ComboBox)
        
        
        registerFactory 'accordion', new ControlFactory(Accordion)
        registerFactory 'titledPane', new ControlFactory(TitledPane)
        registerFactory 'splitPane', new ControlFactory(SplitPane)
        registerFactory 'dividerPosition', new DividerPositionFactory(DividerPosition)
        registerFactory 'tabPane', new ControlFactory(TabPane)
        registerFactory 'tab', new TabFactory(Tab)
        registerFactory 'toolBar', new ControlFactory(ToolBar)
        
        
        registerFactory 'treeView', new ControlFactory(TreeView)
        registerFactory 'treeItem', new TreeItemFactory(TreeItem) 
        // popupControl
        
        //'indexedCell'
        //'cell'
        registerFactory 'tableView', new TableFactory(TableView)
        registerFactory 'tableColumn', new TableFactory(TableColumn)
        
        registerFactory 'title', new TitledFactory(TitledNode)
        registerFactory 'content', new TitledFactory(TitledContent)
        
        registerFactory 'graphic', new GraphicFactory(Graphic)

        // tree events
        TreeItemFactory.treeItemEvents.each {
            registerFactory it.key, new ClosureHandlerFactory(GroovyEventHandler);
        }
        
        registerFactory "onEditCancel", new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory "onEditCommit", new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory "onEditStart", new ClosureHandlerFactory(GroovyEventHandler)
    }

    public def registerEffects() {

        // Dummy node for attaching child effects
        registerFactory 'effect', new EffectFactory(Effect)

        registerFactory 'blend', new EffectFactory(Blend)
        registerFactory 'bloom', new EffectFactory(Bloom)
        registerFactory 'boxBlur', new EffectFactory(BoxBlur)
        registerFactory 'colorAdjust', new EffectFactory(ColorAdjust)
        registerFactory "colorInput", new EffectFactory(ColorInput)
        registerFactory 'displacementMap', new EffectFactory(DisplacementMap)
        registerFactory 'dropShadow', new EffectFactory(DropShadow)
        registerFactory 'gaussianBlur', new EffectFactory(GaussianBlur)
        registerFactory 'glow', new EffectFactory(Glow)
        registerFactory 'imageInput', new EffectFactory(ImageInput)
        registerFactory 'innerShadow', new EffectFactory(InnerShadow)
        registerFactory 'lighting', new EffectFactory(Lighting)
        registerFactory 'motionBlur', new EffectFactory(MotionBlur)
        registerFactory 'perspectiveTransform', new EffectFactory(PerspectiveTransform)
        registerFactory 'reflection', new EffectFactory(Reflection)
        registerFactory 'sepiaTone', new EffectFactory(SepiaTone)
        registerFactory 'shadow', new EffectFactory(Shadow)
        
        registerFactory 'topInput', new EffectFactory(EffectWrapper)
        registerFactory 'bottomInput', new EffectFactory(EffectWrapper)
        registerFactory 'bumpInput', new EffectFactory(EffectWrapper)
        registerFactory 'contentInput', new EffectFactory(EffectWrapper)
        registerFactory "distant", new EffectFactory(Light.Distant)
        registerFactory "point", new EffectFactory(Light.Point)
        registerFactory "spot", new EffectFactory(Light.Spot)
    }

    public def registerEventHandlers() {
        ClosureHandlerFactory eh = new ClosureHandlerFactory(GroovyEventHandler)
        
        for(property in AbstractNodeFactory.nodeEvents) {
            registerFactory property, eh
        }
    }

    public def registerWeb() {

        registerFactory 'webView', new WebFactory(WebView)
        registerFactory 'htmlEditor',new WebFactory(HTMLEditor)
        
        registerFactory 'onLoad', new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory 'onError', new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory 'onAlert', new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory 'onResized', new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory 'onVisibilityChanged', new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory 'createPopupHandler', new ClosureHandlerFactory(GroovyCallback)
        registerFactory 'confirmHandler', new ClosureHandlerFactory(GroovyCallback)
        registerFactory 'promptHandler', new ClosureHandlerFactory(GroovyCallback)
    }
    
    public def registerTransition() {
        
        registerFactory 'fadeTransition', new TransitionFactory(FadeTransition)
        registerFactory 'fillTransition', new TransitionFactory(FillTransition)
        registerFactory 'parallelTransition', new TransitionFactory(ParallelTransition)
        registerFactory 'pauseTransition', new TransitionFactory(PauseTransition)
        registerFactory 'rotateTransition', new TransitionFactory(RotateTransition);
        registerFactory 'scaleTransition', new TransitionFactory(ScaleTransition)
        registerFactory 'translateTransition', new TransitionFactory(TranslateTransition)
        registerFactory 'sequentialTransition', new TransitionFactory(SequentialTransition)
        registerFactory 'pathTransition', new TransitionFactory(PathTransition)
        registerFactory 'strokeTransition', new TransitionFactory(StrokeTransition)
        registerFactory 'transition', new TransitionFactory(Transition)
        
        registerFactory 'timeline', new TimelineFactory(Timeline)
        registerFactory "at", new KeyFrameFactory(KeyFrameWrapper)
        registerFactory "action", new ClosureHandlerFactory(GroovyEventHandler)
        registerFactory "change", new KeyValueFactory(TargetHolder)
        registerFactory "to", new KeyValueSubFactory(Object)
        registerFactory "tween", new KeyValueSubFactory(Interpolator)
    }
    
    public def registerMedia() {
        registerFactory 'mediaView', new MediaViewFactory(MediaView)
        registerFactory 'mediaPlayer', new MediaPlayerFactory(MediaPlayer);
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

    private static idDelegate = { FactoryBuilderSupport builder, node, Map attributes ->
        if (attributes.id) builder.setVariable(attributes.id, node)
    }

    private void initialize() {
        this[DELEGATE_PROPERTY_OBJECT_ID] = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID

        ExpandoMetaClass.enableGlobally()
        addPostNodeCompletionDelegate(postCompletionDelegate)
        //addAttributeDelegate(NodeFactory.attributeDelegate)
        addAttributeDelegate(BindFactory.bindingAttributeDelegate)
        addAttributeDelegate(idDelegate)

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
        
        ReadOnlyDoubleProperty.metaClass {
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
            
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
            
        }
        
        ReadOnlyFloatProperty.metaClass {
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
            
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
            
        }
        
        
        ReadOnlyIntegerProperty.metaClass {
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
            
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
            
        }
        
        
        ReadOnlyLongProperty.metaClass {
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
            
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
            
        }
        
        
        
        ReadOnlyBooleanProperty.metaClass {
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
            
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
        }
        
        ReadOnlyObjectProperty.metaClass {
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
        }
        ReadOnlyListProperty.metaClass {
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
        }
        ReadOnlyMapProperty.metaClass {
            onChange { Closure closure -> 
                delegate.addListener(closure as ChangeListener);
            }
            
            onInvalidate { Closure closure -> 
                delegate.addListener(closure as InvalidationListener);
            }
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
        
        Node.metaClass {
            
            onDragDetected << { Closure closure -> delegate.setOnDragDetected(closure as EventHandler)}
            onDragDone << { Closure closure -> delegate.setOnDragDone(closure as EventHandler)}
            onDragDropped << { Closure closure -> delegate.setOnDragDropped(closure as EventHandler)}
            onDragEntered << { Closure closure -> delegate.setOnDragEntered(closure as EventHandler)}
            onDragExited << { Closure closure -> delegate.setOnDragExited(closure as EventHandler)}
            onDragOver << { Closure closure -> delegate.setOnDragOver(closure as EventHandler)}
            onInputMethodTextChanged << { Closure closure -> delegate.setOnInputMethodTextChanged(closure as EventHandler)}
            onKeyPressed << { Closure closure -> delegate.setOnKeyPressed(closure as EventHandler)}
            onKeyReleased << { Closure closure -> delegate.setOnKeyReleased(closure as EventHandler)}
            onKeyTyped << { Closure closure -> delegate.setOnKeyTyped(closure as EventHandler)}
            onMouseDragEntered << { Closure closure -> delegate.setOnMouseDragEntered(closure as EventHandler)}
            onMouseClicked << { Closure closure -> delegate.setOnMouseClicked(closure as EventHandler)}
            onMouseDragExited << { Closure closure -> delegate.setOnMouseDragExited(closure as EventHandler)}
            onMouseDragged << { Closure closure -> delegate.setOnMouseDragged(closure as EventHandler)}
            onMouseDragOver << { Closure closure -> delegate.setOnMouseDragOver(closure as EventHandler)}
            onMouseDragReleased << { Closure closure -> delegate.setOnMouseDragReleased(closure as EventHandler)}
            onMouseEntered << { Closure closure -> delegate.setOnMouseEntered(closure as EventHandler)}
            onMouseExited << { Closure closure -> delegate.setOnMouseExited(closure as EventHandler)}
            onMouseMoved << { Closure closure -> delegate.setOnMouseMoved(closure as EventHandler)}
            onMousePressed << { Closure closure -> delegate.setOnMousePressed(closure as EventHandler)}
            onMouseReleased << { Closure closure -> delegate.setOnMouseReleased(closure as EventHandler)}
            onScroll << { Closure closure -> delegate.setOnScroll(closure as EventHandler)}
        }
        
        ComboBox.metaClass {
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            onSelect << { Closure closure -> 
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);}
        }
        ChoiceBox.metaClass {
            onSelect << { Closure closure -> 
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);}
        }
        ButtonBase.metaClass {
            onAction << { Closure closure -> delegate.setOnAction(closure as EventHandler)}
        }
        ListView.metaClass {
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            onSelect << { Closure closure -> 
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);}
        }
        TabPane.metaClass{
            onSelect << { Closure closure -> 
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);}
        }
        TableView.metaClass{
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            columnResizePolicy << { Closure closure -> delegate.setColumnResizePolicy(closure as Callback)}
            rowFactory << { Closure closure -> delegate.setRowFactory(closure as Callback)}
            onSelect << { Closure closure -> 
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);}
        }
        TableColumn.metaClass{
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            cellValueFactory << { Closure closure -> delegate.setCellValueFactory(closure as Callback)}
            onEditCancel << { Closure closure -> delegate.setOnEditCancel(closure as EventHandler)}
            onEditCommit << { Closure closure -> delegate.setOnEditCommit(closure as EventHandler)}
            onEditStart << { Closure closure -> delegate.setOnEditStart(closure as EventHandler)}
        }
        TreeView.metaClass{
            cellFactory << { Closure closure -> delegate.setCellFactory(closure as Callback)}
            onSelect << { Closure closure -> 
                delegate.selectionModel.selectedItemProperty().addListener(closure as ChangeListener);}
            onEditCancel << { Closure closure -> delegate.setOnEditCancel(closure as EventHandler)}
            onEditCommit << { Closure closure -> delegate.setOnEditCommit(closure as EventHandler)}
            onEditStart << { Closure closure -> delegate.setOnEditStart(closure as EventHandler)}
        }
        Tab.metaClass {
            onClose << { Closure closure -> delegate.setOnClose(closure as EventHandler)}
            onSelectionChanged << { Closure closure -> delegate.setOnSelectionChanged(closure as EventHandler)}
        }
        Menu.metaClass {
            onHidden  << { Closure closure -> delegate.setOnHidden(closure as EventHandler)}
            onHiding  << { Closure closure -> delegate.setOnHiding(closure as EventHandler)}
            onShowing  << { Closure closure -> delegate.setOnShowing(closure as EventHandler)}
            onShown  << { Closure closure -> delegate.setOnShown(closure as EventHandler)}
        }
        Window.metaClass {
            onHidden  << { Closure closure -> delegate.setOnHidden(closure as EventHandler)}
            onHiding  << { Closure closure -> delegate.setOnHiding(closure as EventHandler)}
            onShowing  << { Closure closure -> delegate.setOnShowing(closure as EventHandler)}
            onShown  << { Closure closure -> delegate.setOnShown(closure as EventHandler)}
            onCloseRequest  << { Closure closure -> delegate.setOnCloseRequest(closure as EventHandler)}
        }
        MenuItem.metaClass {
            onAction << { Closure closure -> delegate.setOnAction(closure as EventHandler)}
        }
        WebEngine.metaClass {
            confirmHandler << { Closure closure -> delegate.setConfirmHandler(closure as Callback)}
            createPopupHandler << { Closure closure -> delegate.setCreatePopupHandler(closure as Callback)}
            promptHandler << { Closure closure -> delegate.setPromptHandler(closure as Callback)}
            
            onAlert  << { Closure closure -> delegate.setOnAlert(closure as EventHandler)}
            onResized  << { Closure closure -> delegate.setOnResized(closure as EventHandler)}
            onStatusChanged  << { Closure closure -> delegate.setOnStatusChanged(closure as EventHandler)}
            onVisibilityChanged  << { Closure closure -> delegate.setOnVisibilityChanged(closure as EventHandler)}
            
        }
    }
}

