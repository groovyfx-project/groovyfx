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
import javafx.event.EventHandler;
/**
*
* @author jimclarke
*/
class FXHelper {

    static Object getValue(value) {
    if (value instanceof ClosureTriggerBinding) {
            def v = value.getSourceValue();
            return v;
        } else {
            return value;
        }
    }

    public static boolean fxAttribute(delegate, key, value) {
        def metaProperty = delegate.getClass().metaClass.getMetaProperty(key);
        if(metaProperty) {
            if(metaProperty.getType().isEnum()) {
                value = getValue(value);
                if(!value.getClass().isEnum()) {
                    value = Enum.valueOf(metaProperty.getType(),value.toString().trim().toUpperCase())
                }
                metaProperty.setProperty(delegate, value);
                return true;
            }else if(Paint.class.isAssignableFrom(metaProperty.getType())) {
                def paint = ColorFactory.get(getValue(value));
                metaProperty.setProperty(delegate, paint);
                return true;
            }else if(Font.class.isAssignableFrom(metaProperty.getType())) {
                def font = FontFactory.get(getValue(value));
                metaProperty.setProperty(delegate, font);
                return true;
            }else if(ObservableList.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if(value != null) {
                    ObservableList list = metaProperty.getProperty(delegate);
                    if(list == null) {
                        list = FXCollections.observableArrayList();
                        metaProperty.setProperty(delegate, list);
                    }
                    if(value instanceof java.util.List) {
                        list.addAll(value);
                    }else if(!list.contains(value)) {
                        list.add(value);
                    }
                }
                return true;
            }else if(Sequence.class.isAssignableFrom(metaProperty.getType())) {
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
            }else if(Insets.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if(List.class.isAssignableFrom(value.getClass())) {
                        value = new Insets(value[0], value[1],value[2],value[3]);
                    }else if (!Insets.class.isAssignableFrom(value.getClass())) {
                    if(value.getString().toUpperCase() == 'EMPTY') {
                            value = Insets.EMPTY;
                        }
                    }
                    metaProperty.setProperty(delegate, value);
                    return true;
            }else if(BoundingBox.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if(List.class.isAssignableFrom(value.getClass())) {
                    if(value.getSize() == 4) // 2D
                        value = new BoundingBox(value[0], value[1],value[2],value[3]);
                        else // 3D
                        value = new BoundingBox(value[0], value[1],value[2],value[3], value[4], value[5]);
                    }
                    metaProperty.setProperty(delegate, value);
                    return true;
            }else if(Dimension2D.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if(List.class.isAssignableFrom(value.getClass())) {
                        value = new Dimension2D(value[0], value[1]);
                    }
                    metaProperty.setProperty(delegate, value);
                    return true;
            }else if(Point2D.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if(List.class.isAssignableFrom(value.getClass())) {
                        value = new Point2D(value[0], value[1]);
                    }
                    metaProperty.setProperty(delegate, value);
                    return true;
            }else if(Point3D.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if(List.class.isAssignableFrom(value.getClass())) {
                        value = new Point3D(value[0], value[1], value[2]);
                    }
                    metaProperty.setProperty(delegate, value);
                    return true;
            }else if(Rectangle2D.class.isAssignableFrom(metaProperty.getType())) {
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
            }else if(Image.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if (!Image.class.isAssignableFrom(value.getClass())) {
                        value = new Image(value.toString());
                    }
                    metaProperty.setProperty(delegate, value);
                    return true;
            }else if(Cursor.class.isAssignableFrom(metaProperty.getType())) {
                value = getValue(value);
                if (!Cursor.class.isAssignableFrom(value.getClass())) {
                    switch(value.toString().toUpperCase()) {
                        case 'DEFAULT':
                        value = Cursor.DEFAULT;
                        break;
                        case 'CROSSHAIR':
                        value = Cursor.CROSSHAIR;
                        break;
                        case 'TEXT':
                        value = Cursor.TEXT;
                        break;
                        case 'WAIT':
                        value = Cursor.WAIT;
                        break;
                        case 'SW_RESIZE':
                        value = Cursor.SW_RESIZE;
                        break;
                        case 'SE_RESIZE':
                        value = Cursor.SE_RESIZE;
                        break;
                        case 'NW_RESIZE':
                        value = Cursor.NW_RESIZE;
                        break;
                        case 'NE_RESIZE':
                        value = Cursor.NE_RESIZE;
                        break;
                        case 'N_RESIZE':
                        value = Cursor.N_RESIZE;
                        break;
                        case 'S_RESIZE':
                        value = Cursor.S_RESIZE;
                        break;
                        case 'W_RESIZE':
                        value = Cursor.W_RESIZE;
                        break;
                        case 'E_RESIZE':
                        value = Cursor.E_RESIZE;
                        break;
                        case 'HAND':
                        value = Cursor.HAND;
                        break;
                        case 'MOVE':
                        value = Cursor.MOVE;
                        break;
                        case 'H_RESIZE':
                        value = Cursor.H_RESIZE;
                        break;
                        case 'V_RESIZE':
                        value = Cursor.V_RESIZE;
                        break;
                        case 'NONE':
                        value = Cursor.NONE;
                        break;

                    }
                }
                metaProperty.setProperty(delegate, value);
                return true;
            }else if(Orientation.class.isAssignableFrom(metaProperty.getType())) {
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
                
            }else if(EventHandler.class.isAssignableFrom(metaProperty.getType())) {
                ClosureEventHandler handler = new ClosureEventHandler(closure: value);
                metaProperty.setProperty(delegate, handler);
                return true;
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

}

