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

package groovyx.javafx.factory

import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.TextAlignment;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.Cursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.groovyfx.javafx.binding.ClosureTriggerBinding;
import org.codehaus.groovyfx.javafx.binding.FullBinding;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Node;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.control.ToggleGroup;
import java.math.BigDecimal;
import java.math.BigInteger;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;
import java.io.File;
/**
 *
 * @author jimclarke
 */
class FXHelper {
    
    private static Map<String, ToggleGroup> toggleGroups = new HashMap<String, ToggleGroup>();
    
    private static def cursorMap = [
        DEFAULT: Cursor.DEFAULT,
        CROSSHAIR: Cursor.CROSSHAIR,
        TEXT: Cursor.TEXT,
        WAIT: Cursor.WAIT,
        SW_RESIZE: Cursor.SW_RESIZE,
        SE_RESIZE: Cursor.SE_RESIZE,
        NW_RESIZE: Cursor.NW_RESIZE,
        NE_RESIZE: Cursor.NE_RESIZE,
        N_RESIZE: Cursor.N_RESIZE,
        S_RESIZE: Cursor.S_RESIZE,
        W_RESIZE: Cursor.W_RESIZE,
        E_RESIZE: Cursor.E_RESIZE,
        HAND: Cursor.HAND,
        MOVE: Cursor.MOVE,
        H_RESIZE: Cursor.H_RESIZE,
        V_RESIZE: Cursor.V_RESIZE,
        NONE: Cursor.NONE,
        CLOSED_HAND: Cursor.CLOSED_HAND,
        DISAPPEAR: Cursor.DISAPPEAR,
        OPEN_HAND: Cursor.OPEN_HAND,
    ];

    static Object getValue(value) {
        if (value instanceof ClosureTriggerBinding) {
            def v = value.getSourceValue();
            return v;
        } else {
            return value;
        }
    }
    
    private static double getAnchorValue ( value) {
        value = getValue(value);
        if(value instanceof Number){
            value = ((Number)value).doubleValue();
        }else {
            value = Double.parseDouble(value.toString());
        }
        return value;
    }
    
    private static def setTopAnchor = { delegate, value -> 
        AnchorPane.setTopAnchor((Node)delegate, getAnchorValue(value));
    }
    private static def setBottomAnchor = { delegate, value -> 
        AnchorPane.setBottomAnchor((Node)delegate, getAnchorValue(value));
    }
    private static def setRightAnchor = { delegate, value -> 
        AnchorPane.setRightAnchor((Node)delegate, getAnchorValue(value));
    }
    private static def setLeftAnchor = { delegate, value -> 
        AnchorPane.setLeftAnchor((Node)delegate, getAnchorValue(value));
    }
    
    private static anchorMap = [
        topAnchor: setTopAnchor,
        bottomAnchor: setBottomAnchor,
        rightAnchor: setRightAnchor,
        leftAnchor: setLeftAnchor
    ]
    
    
    
    private static def doPaint = { delegate, metaProperty, value ->
         def paint = ColorFactory.get(getValue(value));
         metaProperty.setProperty(delegate, paint);
         return true;
    };
    private static def doFont = { delegate, metaProperty, value ->
        def font = FontFactory.get(getValue(value));
        metaProperty.setProperty(delegate, font);
        return true;   
    };
    private static def doFile = { delegate, metaProperty, value ->
        def file = value;
        if(file instanceof String || file instanceof URI )
            file = new File(value);
        else if(file instanceof URL) {
            file = new File(value.toURI());
        }
        metaProperty.setProperty(delegate, file);
        return true;   
    };
    private static def doObservableList = { delegate, metaProperty, value ->
        value = getValue(value);
        if(value != null) {
            ObservableList list = metaProperty.getProperty(delegate);

            if(list == null) {
                list = FXCollections.observableArrayList();
                metaProperty.setProperty(delegate, list);
            }

            if(value instanceof java.util.List) {
                if(delegate instanceof javafx.scene.shape.Polygon ||
                    delegate instanceof javafx.scene.shape.Polyline) {
                    // need to convert to double
                    // not sure if you can tell  this from meta data?
                        for(v in value) {
                            list.add(v.doubleValue());
                        }
                }else {
                    list.addAll(value);
                }
            }else if(!list.contains(value)) {
                list.add(value);
            }
        }
        return true;    
    };
    private static def doSequence = { delegate, metaProperty, value ->
        value = getValue(value);
        if(value != null) {
            Sequence seq = metaProperty.getProperty(delegate);
            if(seq != null) {
                if(value instanceof java.util.List) {
                    for(v in value) {
                        if(!seq.contains(v)) {
                            seq.add(v);
                        }
                    }
                }else if(!seq.contains(value)) {
                    seq.add(value);
                }
            }else {
                seq = FXCollections.sequence();
                seq.addAll(value);
                metaProperty.setProperty(delegate, seq);
            }
        }
        return true;    
    };
    private static def doInsets = { delegate, metaProperty, value ->
        value = getValue(value);
        if (Number.class.isAssignableFrom(value.getClass())) {
            value = new Insets(value, value, value, value)
        } else if(List.class.isAssignableFrom(value.getClass())) {
            switch (value.size()) {
                case 0:
                    value = Insets.EMPTY
                    break
                case 1:
                    //top, right,bottom, left
                    value = new Insets(value[0], value[0], value[0], value[0])
                    break
                case 2:
                    value = new Insets(value[0], value[1], value[0], value[1])
                    break
                case 3:
                    value = new Insets(value[0], value[1], value[2], value[1])
                    break
                default:
                    value = new Insets(value[0], value[1], value[2], value[3])
                    break
            }
        } else if(value.toString().toUpperCase() == 'EMPTY') {
            value = Insets.EMPTY;
        }
        metaProperty.setProperty(delegate, value);
        return true;    
    };
    private static def doBoundingBox = { delegate, metaProperty, value ->
        value = getValue(value);
        if(List.class.isAssignableFrom(value.getClass())) {
            if(value.getSize() == 4) // 2D
            value = new BoundingBox(value[0], value[1],value[2],value[3]);
            else // 3D
            value = new BoundingBox(value[0], value[1],value[2],value[3], value[4], value[5]);
        }
        metaProperty.setProperty(delegate, value);
        return true;    
    };
    private static def doDimension2D = { delegate, metaProperty, value ->
        value = getValue(value);
        if(List.class.isAssignableFrom(value.getClass())) {
            value = new Dimension2D(value[0], value[1]);
        }
        metaProperty.setProperty(delegate, value);
        return true;    
    };
    private static def doPoint2D = { delegate, metaProperty, value ->
        value = getValue(value);
        if(List.class.isAssignableFrom(value.getClass())) {
            value = new Point2D(value[0], value[1]);
        }
        metaProperty.setProperty(delegate, value);
    };
    private static def doPoint3D = { delegate, metaProperty, value ->
        value = getValue(value);
        if(List.class.isAssignableFrom(value.getClass())) {
            value = new Point3D(value[0], value[1], value[2]);
        }
        metaProperty.setProperty(delegate, value);
        return true;    
    };
    private static def doRectangle2D = { delegate, metaProperty, value ->
        value = getValue(value);
        if(List.class.isAssignableFrom(value.getClass())) {
            value = new Rectangle2D(value[0], value[1],value[2],value[3]);
        }else if (!Rectangle2D.class.isAssignableFrom(value.getClass())) {
            if(value.getString().toUpperCase() == 'EMPTY') {
                value = Rectangle2D.EMPTY;
            }
        }
        metaProperty.setProperty(delegate, value);
        return true;    
    };
    private static def doImage = { delegate, metaProperty, value ->
        value = getValue(value);
        if (!Image.class.isAssignableFrom(value.getClass())) {
            value = new Image(value.toString());
        }
        metaProperty.setProperty(delegate, value);
                
        return true;    
    };
    private static def doCursor = { delegate, metaProperty, value ->
        value = getValue(value);
        if (!Cursor.class.isAssignableFrom(value.getClass())) {
            value = cursorMap[value.toString().toUpperCase()];
            if(value == null) value = Cursor.DEFAULT;
        }
        metaProperty.setProperty(delegate, value);
        return true;     
    };
    private static def doOrientation = { delegate, metaProperty, value ->
        value = getValue(value);
        if (!Cursor.class.isAssignableFrom(value.getClass())) {
            switch(value.toString().toUpperCase()) {
                case 'VERTICAL':
                value = Orientation.VERTICAL;
                break;
                case 'HORIZONTAL':
                value = Orientation.HORIZONTAL;
                break;
            }
        }
        metaProperty.setProperty(delegate, value);
        return true;    
    };
    private static def doEventHandler = { delegate, metaProperty, value ->
        if(value instanceof Closure)
            value = new ClosureEventHandler(closure: value);
        metaProperty.setProperty(delegate, value);
        return true;
            
    };
    private static def doToggleGroup = { delegate, metaProperty, value ->
        ToggleGroup group = null;
        if(value instanceof String) {
            group = toggleGroups.get(value);
            if(group == null) {
                group = new ToggleGroup();
                toggleGroups.put(value, group);
            }
        }else {
            group = value;
        }
        metaProperty.setProperty(delegate, group);
        return true;
            
    };
    
    private static def doNothing  = { delegate, metaProperty, value ->
        return false;
    };
    
    private static def doEnum  = { delegate, metaProperty, value ->
        value = getValue(value);
        if(!value.getClass().isEnum()) {
            value = Enum.valueOf(metaProperty.getType(),value.toString().trim().toUpperCase())
        }
        metaProperty.setProperty(delegate, value);
        return true;
    };
    
    private static def classMap = [
        (String.class): doNothing,
        (Double.class): doNothing,
        (Double.TYPE): doNothing,
        (Short.class): doNothing,
        (Short.TYPE): doNothing,
        (Integer.class): doNothing,
        (Integer.TYPE): doNothing,
        (Float.class): doNothing,
        (Float.TYPE): doNothing,
        (Long.class): doNothing,
        (Long.TYPE): doNothing,
        (Boolean.class): doNothing,
        (Boolean.TYPE): doNothing,
        (Byte.class): doNothing,
        (Byte.TYPE): doNothing,
        (Character.class): doNothing,
        (Character.TYPE): doNothing,
        (BigDecimal.class): doNothing,
        (BigInteger.class): doNothing,
        (Runnable.class): doNothing,
        
        // class types that do have special attribute processing
        (Paint.class): doPaint,
        (Font.class): doFont,
        (File.class): doFile,
        (ObservableList.class): doObservableList,
        (Sequence.class): doSequence, // do we need this any more??????
        (Insets.class): doInsets,
        (BoundingBox.class): doBoundingBox,
        (Dimension2D.class): doDimension2D,
        (Point2D.class): doPoint2D,
        (Point3D.class): doPoint3D,
        (Rectangle2D.class): doRectangle2D,
        (Image.class): doImage,
        (Cursor.class): doCursor,
        (Orientation.class): doOrientation,
        (EventHandler.class): doEventHandler,
        (ToggleGroup.class): doToggleGroup,
    ];

    public static boolean fxAttribute(delegate, key, value) {
        def setAnchor = anchorMap[key];
        if(setAnchor) {
            setAnchor(delegate, value);
            return true;
        }
        
        def metaProperty = delegate.getClass().metaClass.getMetaProperty(key);
        
        if(value instanceof FullBinding) {
            value.update();
            return true;
        }
        if(metaProperty) {
            // first do quick check from map
            def closure = classMap.get(metaProperty.getType());
            if(closure != null) {
                return closure(delegate, metaProperty, value);
            }
            // if not in map we need to see if it is a subclass.
            if(metaProperty.getType().isEnum()) {
                return doEnum(delegate, metaProperty, value);
            }
            //TODO Temporary to tell us if we should add a class to the map.
            System.out.println("FXHelper unMapped class '${metaProperty.getType()}' for '$key' property")
            
            if(Paint.class.isAssignableFrom(metaProperty.getType())) {
                return doPaint(delegate, metaProperty, value);
            }else if(Font.class.isAssignableFrom(metaProperty.getType())) {
                return doFont(delegate, metaProperty, value);
            }else if(ObservableList.class.isAssignableFrom(metaProperty.getType())) {
                return doObservableList(delegate, metaProperty, value);
            }else if(Sequence.class.isAssignableFrom(metaProperty.getType())) {
                return doSequence(delegate, metaProperty, value);
            }else if(Insets.class.isAssignableFrom(metaProperty.getType())) {
                return doInsets(delegate, metaProperty, value);
            }else if(BoundingBox.class.isAssignableFrom(metaProperty.getType())) {
                return doBoundingBox(delegate, metaProperty, value);
            }else if(Dimension2D.class.isAssignableFrom(metaProperty.getType())) {
                return doDimension2D(delegate, metaProperty, value);
            }else if(Point2D.class.isAssignableFrom(metaProperty.getType())) {
                return doPoint2D(delegate, metaProperty, value);
            }else if(Point3D.class.isAssignableFrom(metaProperty.getType())) {
                return doPoint3D(delegate, metaProperty, value);
            }else if(Rectangle2D.class.isAssignableFrom(metaProperty.getType())) {
                return doRectangle2D(delegate, metaProperty, value);
            }else if(Image.class.isAssignableFrom(metaProperty.getType())) {
                return doImage(delegate, metaProperty, value);
            }else if(Cursor.class.isAssignableFrom(metaProperty.getType())) {
                return doCursor(delegate, metaProperty, value);
            }else if(Orientation.class.isAssignableFrom(metaProperty.getType())) {
                return doOrientation(delegate, metaProperty, value);
            }else if(EventHandler.class.isAssignableFrom(metaProperty.getType())) {
                return doEventHandler(delegate, metaProperty, value);
            }else if(ToggleGroup.class.isAssignableFrom(metaProperty.getType())) {
                return doToggleGroup(delegate, metaProperty, value);
            }
        }
        
        return false;
    }

    public static void fxAttributes(delegate, attributes) {
        def removelist = [];
        for (i in attributes) {
            if(fxAttribute(delegate, i.getKey(), i.getValue())) {
                removelist.add(i.getKey());
            }
        }
        for(i in removelist) {
            attributes.remove(i);
        }
    }

    public static void setPropertyOrMethod(node, String name, Object value) {
        if (node.metaClass.respondsTo(node, "set$name") || node.metaClass.hasProperty(node, name))
            node[name] = value
        else if (node.metaClass.respondsTo(node, name))
            node."$name"(value)
    }
}

