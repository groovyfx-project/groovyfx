/*
* Copyright 2011-2012 the original author or authors.
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

import groovyx.javafx.animation.TargetHolder
import groovyx.javafx.event.GroovyCallback
import groovyx.javafx.event.GroovyEventHandler
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker
import javafx.geometry.HPos
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaPlayerBuilder
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.web.HTMLEditor
import javafx.scene.web.WebView
import org.codehaus.groovy.runtime.MethodClosure

import java.util.logging.Logger

import groovyx.javafx.factory.*
import groovyx.javafx.factory.animation.*
import javafx.animation.*
import javafx.scene.*
import javafx.scene.chart.*
import javafx.scene.control.*
import javafx.scene.effect.*
import javafx.scene.layout.*
import javafx.scene.shape.*
import javafx.scene.transform.*
import javafx.stage.*
import groovy.swing.factory.CollectionFactory

import groovyx.javafx.canvas.*

/**
 *
 * @author jimclarke
 */
class SceneGraphBuilder extends FactoryBuilderSupport {
    static final String DELEGATE_PROPERTY_OBJECT_ID = "_delegateProperty:id";
    static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = "id";

    static final String DELEGATE_PROPERTY_OBJECT_FILL = "_delegateProperty:fill";
    static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_FILL = "fill";

    static final String DELEGATE_PROPERTY_OBJECT_STROKE = "_delegateProperty:stroke";
    static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_STROKE = "stroke";

    static final String CONTEXT_SCENE_KEY = "CurrentScene";
    static final String CONTEXT_DIVIDER_KEY = "CurrentDividers";

    private static final Logger LOG = Logger.getLogger(SceneGraphBuilder.name)
    private static final Random random = new Random()

    private Scene currentScene;

    static {
        GroovyFXEnhancer.enhanceClasses()
    }

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
    
    boolean isFxApplicationThread() {
        Platform.isFxApplicationThread();
    }
    
    protected Factory resolveFactory(Object name, Map attributes, Object value) {
        // First, see if parent factory has a factory,
        // if not, go to the builder.
        Factory factory = null;
        Factory parent = getParentFactory();
        if(parent != null && parent instanceof AbstractFXBeanFactory) {
            factory = parent.resolveFactory(name, attributes, value);
        }
        if(factory) {
            // This is actually done in super.resolveFactory
            // so we need to do it here for the child factory
            getProxyBuilder().getContext().put(CHILD_BUILDER, getProxyBuilder());
        }else {
            factory =  super.resolveFactory(name, attributes, value);
        }
        
        return factory
    }

    SceneGraphBuilder submit(WebView wv, Closure c) {
        //if (!(c instanceof MethodClosure)) {
        //    c = c.curry([this])
        //}
        def submitClosure = {
            if(wv.engine.loadWorker.state == Worker.State.SUCCEEDED) {
                c.call(wv);
            }else {
                def listener = new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observable,
                                Worker.State oldState, Worker.State newState) {
                        defer {
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
                    }
                };
                wv.engine.loadWorker.stateProperty().addListener(listener);
            }
        }
        
        if(Platform.isFxApplicationThread()) {
              submitClosure.call()
        }else {
            defer submitClosure
        }

        return this;
    }

    private static Map propertyMap = [
            // TODO remove lowercase versions
            horizontal: Orientation.HORIZONTAL,
            vertical: Orientation.VERTICAL,
            ease_both: Interpolator.EASE_BOTH,
            easeboth: Interpolator.EASE_BOTH,
            easein: Interpolator.EASE_IN,
            ease_in: Interpolator.EASE_IN,
            easeout: Interpolator.EASE_OUT,
            ease_out: Interpolator.EASE_OUT,
            discrete: Interpolator.DISCRETE,
            linear: Interpolator.LINEAR,
            indefinite: Timeline.INDEFINITE,
            // the following commented out because they cause
            // trouble with nodes that have the same name :-(
            //top: VPos.TOP,
            //bottom: VPos.BOTTOM,
            //left: HPos.LEFT,
            //right: HPos.RIGHT,
            top_left: Pos.TOP_LEFT,
            top_center: Pos.TOP_CENTER,
            top_right: Pos.TOP_RIGHT,
            center_left: Pos.CENTER_LEFT,
            center: Pos.CENTER,
            center_right: Pos.CENTER_RIGHT,
            bottom_left: Pos.BOTTOM_LEFT,
            bottom_center: Pos.BOTTOM_CENTER,
            bottom_right: Pos.BOTTOM_RIGHT,
            baseline_center: Pos.BASELINE_CENTER,
            baseline_right: Pos.BASELINE_RIGHT,
            baseline_left: Pos.BASELINE_LEFT,
            HORIZONTAL: Orientation.HORIZONTAL,
            VERTICAL: Orientation.VERTICAL,
            EASEBOTH: Interpolator.EASE_BOTH,
            EASE_BOTH: Interpolator.EASE_BOTH,
            EASEIN: Interpolator.EASE_IN,
            EASE_IN: Interpolator.EASE_IN,
            EASEOUT: Interpolator.EASE_OUT,
            EASE_OUT: Interpolator.EASE_OUT,
            DISCRETE: Interpolator.DISCRETE,
            LINEAR: Interpolator.LINEAR,
            INDEFINITE: Timeline.INDEFINITE,
            TOP: VPos.TOP,
            BOTTOM: VPos.BOTTOM,
            LEFT: HPos.LEFT,
            RIGHT: HPos.RIGHT,
            TOP_LEFT: Pos.TOP_LEFT,
            TOP_CENTER: Pos.TOP_CENTER,
            TOP_RIGHT: Pos.TOP_RIGHT,
            CENTER_LEFT: Pos.CENTER_LEFT,
            CENTER: Pos.CENTER,
            CENTER_RIGHT: Pos.CENTER_RIGHT,
            BOTTOM_LEFT: Pos.BOTTOM_LEFT,
            BOTTOM_CENTER: Pos.BOTTOM_CENTER,
            BOTTOM_RIGHT: Pos.BOTTOM_RIGHT,
            BASELINE_CENTER: Pos.BASELINE_CENTER,
            BASELINE_RIGHT: Pos.BASELINE_RIGHT,
            BASELINE_LEFT: Pos.BASELINE_LEFT
    ];

    def propertyMissing(String name) {
        if (name.startsWith("#")) {
            return Color.web(name);
        }

        throw new MissingPropertyException("Unrecognized property: ${name}", name, this.class);
    }
    
    Color rgb(int r, int g, int b) {
        Color.rgb(r,g,b);
    }

    Color rgb(int r, int g, int b, float alpha) {
        Color.rgb(r,g,b, alpha);
    }

    Color rgba(int r, int g, int b, float alpha) {
        rgb(r,g,b, alpha);
    }

    Color hsb(int hue, float saturation, float brightness, float alpha) {
        Color.hsb(hue, saturation, brightness, alpha);
    }

    Color hsb(int hue, float saturation, float brightness) {
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
        } else if (CanvasOperation.isAssignableFrom(beanClass)) {
            registerFactory nodeName, groupName, new CanvasOperationFactory(beanClass)
        } else {
            super.registerBeanFactory(nodeName, groupName, beanClass)
        }
    }

    void registerStages() {
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
    void registerNodes() {
        registerFactory "bean", new CustomNodeFactory(Object)
        registerFactory "node", new CustomNodeFactory(javafx.scene.Node)
        registerFactory "nodes", new CustomNodeFactory(List, true)
        registerFactory "container", new CustomNodeFactory(Parent)

        registerFactory "imageView", new ImageViewFactory(ImageView)
        registerFactory "image", new ImageFactory(Image)
        registerFactory "clip", new ClipFactory(ClipHolder)
        registerFactory "fxml", new FXMLFactory()

        registerFactory "bean", new CustomNodeFactory(Object)
        registerFactory "fxaction", new ActionFactory()
        registerFactory "actions", new CollectionFactory()
        registerFactory "noparent", new CollectionFactory()
    }

    void registerContainers() {
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
    
    void registerCanvas() {
        CanvasFactory cf = new CanvasFactory();
        
        registerFactory "canvas", cf
        
        cf.registerFactory "appendSVGPath", new CanvasOperationFactory(AppendSVGPathOperation)
        cf.registerFactory "applyEffect", new CanvasOperationFactory(ApplyEffectOperation)
        cf.registerFactory "arc", new CanvasOperationFactory(ArcOperation)
        cf.registerFactory "arcTo", new CanvasOperationFactory(ArcToOperation)
        cf.registerFactory "path", new CanvasOperationFactory(BeginPathOperation)
        cf.registerFactory "bezierCurveTo", new CanvasOperationFactory(BezierCurveToOperation)
        cf.registerFactory "clearRect", new CanvasOperationFactory(ClearRectOperation)
        cf.registerFactory "clip", new CanvasOperationFactory(ClipOperation)
        cf.registerFactory "closePath", new CanvasOperationFactory(ClosePathOperation)
        cf.registerFactory "drawImage", new CanvasOperationFactory(DrawImageOperation)
        cf.registerFactory "effect", new CanvasOperationFactory(SetEffectOperation)
        cf.registerFactory "fillPath", new CanvasOperationFactory(FillOperation)
        cf.registerFactory "fill", new CanvasOperationFactory(SetFillOperation)
        cf.registerFactory "fillArc", new CanvasOperationFactory(FillArcOperation)
        cf.registerFactory "fillOval", new CanvasOperationFactory(FillOvalOperation)
        cf.registerFactory "fillPolygon", new CanvasOperationFactory(FillPolygonOperation)
        cf.registerFactory "fillRect", new CanvasOperationFactory(FillRectOperation)
        cf.registerFactory "fillRoundRect", new CanvasOperationFactory(FillRoundRectOperation)
        cf.registerFactory "fillRule", new CanvasOperationFactory(SetFillRuleOperation)
        cf.registerFactory "fillText", new CanvasOperationFactory(FillTextOperation)
        cf.registerFactory "font", new CanvasOperationFactory(SetFontOperation)
        cf.registerFactory "globalAlpha", new CanvasOperationFactory(SetGlobalAlphaOperation)
        cf.registerFactory "globalBlendMode", new CanvasOperationFactory(SetGlobalBlendModeOperation)
        cf.registerFactory "lineCap", new CanvasOperationFactory(SetLineCapOperation)
        cf.registerFactory "lineJoin", new CanvasOperationFactory(SetLineJoinOperation)
        cf.registerFactory "lineTo", new CanvasOperationFactory(LineToOperation)
        cf.registerFactory "lineWidth", new CanvasOperationFactory(SetLineWidthOperation)
        cf.registerFactory "miterLimit", new CanvasOperationFactory(SetMiterLimitOperation)
        cf.registerFactory "moveTo", new CanvasOperationFactory(MoveToOperation)
        cf.registerFactory "quadraticCurveTo", new CanvasOperationFactory(QuadraticCurveToOperation)
        cf.registerFactory "rect", new CanvasOperationFactory(RectOperation)
        cf.registerFactory "restore", new CanvasOperationFactory(RestoreOperation)
        cf.registerFactory "rotate", new CanvasOperationFactory(RotateOperation)
        cf.registerFactory "save", new CanvasOperationFactory(SaveOperation)
        cf.registerFactory "scale", new CanvasOperationFactory(ScaleOperation)
        cf.registerFactory "stroke", new CanvasOperationFactory(SetStrokeOperation)
        cf.registerFactory "strokePath", new CanvasOperationFactory(StrokeOperation)
        cf.registerFactory "strokeArc", new CanvasOperationFactory(StrokeArcOperation)
        cf.registerFactory "strokeLine", new CanvasOperationFactory(StrokeLineOperation)
        cf.registerFactory "strokeOval", new CanvasOperationFactory(StrokeOvalOperation)
        cf.registerFactory "strokePolygon", new CanvasOperationFactory(StrokePolygonOperation)
        cf.registerFactory "strokePolyline", new CanvasOperationFactory(StrokePolylineOperation)
        cf.registerFactory "strokeRect", new CanvasOperationFactory(StrokeRectOperation)
        cf.registerFactory "strokeRoundRect", new CanvasOperationFactory(StrokeRoundRectOperation)
        cf.registerFactory "strokeText", new CanvasOperationFactory(StrokeTextOperation)
        cf.registerFactory "textAlign", new CanvasOperationFactory(SetTextAlignOperation)
        cf.registerFactory "textBaseline", new CanvasOperationFactory(SetTextBaselineOperation)
        cf.registerFactory "setTransform", new CanvasOperationFactory(SetTransformOperation)
        cf.registerFactory "transform", new CanvasOperationFactory(TransformOperation)
        cf.registerFactory "translate", new CanvasOperationFactory(TranslateOperation)
        cf.registerFactory "operation", new CanvasClosureOperationFactory(ClosureOperation)
        
        DrawFactory df = new DrawFactory();
        
        registerFactory "draw", df
        df.childFactories = cf.childFactories
        
    }

    void registerBinding() {
        BindFactory bf = new BindFactory();
        registerFactory "bind", bf;
        
        registerFactory "onChange", new ChangeFactory(ChangeListener)
        registerFactory "onInvalidate", new ChangeFactory(InvalidationListener)
    }

    void registerThreading() {
        registerExplicitMethod "defer", this.&defer
    }

    void registerMenus() {
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

    void registerCharts() {
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

    void registerTransforms() {
        registerFactory 'affine', new TransformFactory(Affine)
        registerFactory 'rotate', new TransformFactory(Rotate)
        registerFactory 'scale', new TransformFactory(Scale)
        registerFactory 'shear', new TransformFactory(Shear)
        registerFactory 'translate', new TransformFactory(Translate)
    }

    void registerShapes() {
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

        PathFactory pathFactory = new PathFactory(Path)
        registerFactory 'path', pathFactory

        pathFactory.registerFactory 'arcTo',        new PathElementFactory(ArcTo)
        pathFactory.registerFactory 'closePath',    new PathElementFactory(ClosePath)
        pathFactory.registerFactory 'cubicCurveTo', new PathElementFactory(CubicCurveTo)
        pathFactory.registerFactory 'hLineTo',      new PathElementFactory(HLineTo)
        pathFactory.registerFactory 'lineTo',       new PathElementFactory(LineTo)
        pathFactory.registerFactory 'moveTo',       new PathElementFactory(MoveTo)
        pathFactory.registerFactory 'quadCurveTo',  new PathElementFactory(QuadCurveTo)
        pathFactory.registerFactory 'vLineTo',      new PathElementFactory(VLineTo)

        registerFactory 'text', new TextFactory(Text)

        registerFactory 'linearGradient', new LinearGradientFactory()
        registerFactory 'radialGradient', new RadialGradientFactory()
        registerFactory 'stop',   new StopFactory()
        registerFactory 'fill',   new FillFactory()
        registerFactory 'stroke', new StrokeFactory()
    }

    void registerControls() {

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

    void registerEffects() {

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

    void registerEventHandlers() {
        ClosureHandlerFactory eh = new ClosureHandlerFactory(GroovyEventHandler)

        for(property in AbstractNodeFactory.nodeEvents) {
            registerFactory property, eh
        }
    }

    void registerWeb() {

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

    void registerTransition() {

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

        TimelineFactory tf =  new TimelineFactory(Timeline)
        registerFactory 'timeline', tf
        
        KeyFrameFactory kf = new KeyFrameFactory(KeyFrameWrapper)
        tf.registerFactory "at", kf
        
        
        KeyValueFactory kvf = new KeyValueFactory(TargetHolder);
        kf.registerFactory "change",kvf
        kvf.registerFactory "to", new KeyValueSubFactory(Object)
        kvf.registerFactory "tween", new KeyValueSubFactory(Interpolator)
        
        // applies to Timeline and KeyFrame
        registerFactory "onFinished", new ClosureHandlerFactory(GroovyEventHandler)
        
        
    }

    void registerMedia() {
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

    private static postCompletionDelegate = { FactoryBuilderSupport builder, Object parent, Object node ->
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

        addPostNodeCompletionDelegate(postCompletionDelegate)
        addAttributeDelegate(NodeFactory.attributeDelegate)
        //addAttributeDelegate(BindFactory.bindingAttributeDelegate)
        addAttributeDelegate(idDelegate)

        Color.NamedColors.namedColors.put("groovyblue", Color.rgb(99, 152, 170))

        Color.NamedColors.namedColors.each { name, color ->
            setVariable(name, color) // would love to remove this one
            setVariable(name.toUpperCase(), color)
        }

        propertyMap.each { name, value ->
            setVariable(name, value)
        }
    }
}

