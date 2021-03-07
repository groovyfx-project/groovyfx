/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2021 the original author or authors.
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

import groovyx.javafx.event.GroovyEventHandler
import groovyx.javafx.factory.animation.KeyValueSubFactory
import javafx.animation.FadeTransition
import javafx.animation.FillTransition
import javafx.animation.ParallelTransition
import javafx.animation.PathTransition
import javafx.animation.PauseTransition
import javafx.animation.RotateTransition
import javafx.animation.ScaleTransition
import javafx.animation.SequentialTransition
import javafx.animation.StrokeTransition
import javafx.animation.Transition
import javafx.animation.TranslateTransition
import javafx.event.EventHandler
import javafx.scene.shape.Path
import javafx.util.Duration

/**
 * Handles JavaFX transitions
 * @author jimclarke
 */
class TransitionFactory extends AbstractFXBeanFactory {
    
    TransitionFactory(Class<Transition> beanClass) {
        super(beanClass);
    }
    
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if(Transition == beanClass)
              return value;
        Transition result = beanClass.newInstance();
        if(value != null ) {
            if(value instanceof Duration) {
                result.duration = value;
            } else if((result instanceof ParallelTransition || result instanceof SequentialTransition)) {
                if(value instanceof Transition) {
                    result.children.add(value);
                }else if(value instanceof List) {
                    result.children.addAll(value);
                }
            }
        }
        return result;
    }

    void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if((parent instanceof ParallelTransition || parent instanceof SequentialTransition)) {
            if(child instanceof Transition) {
                parent.children.add(child);
            }else if(child instanceof List) {
                parent.children.addAll(child);
            }
        }else if(parent instanceof PathTransition && child instanceof Path) {
            parent.path = child
        }else if(child instanceof GroovyEventHandler) {
            parent."${child.property}" = child;
        }else {
            super.setChild(build, parent, child);
        }
    }
    
    @Override
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if(node instanceof Transition) {
            def tween = attributes.remove("tween");
            if(tween) {
                node.interpolator = KeyValueSubFactory.getInterpolator(tween);
            }
            def interpolator = attributes.remove("interpolator");
            if(interpolator) {
                node.interpolator = KeyValueSubFactory.getInterpolator(interpolator);
            }
            def onFinished = attributes.remove("onFinished");
            if(onFinished != null) {
                if(onFinished instanceof Closure) {
                    onFinished = onFinished as EventHandler
                }
                node.onFinished = onFinished;
            }
            
            def cycleCount = attributes.remove("cycleCount");
            if(cycleCount != null) {
                if(cycleCount instanceof String && cycleCount.equalsIgnoreCase("indefinite") ) {
                    node.cycleCount = Transition.INDEFINITE;
                }else {
                    node.cycleCount = cycleCount;
                }
            }
            if (node instanceof FadeTransition) {

                List<Double> range = attributes.remove("range") as List<Double>
                if (range != null) {
                    node.fromValue = range[0];
                    node.toValue = range[1];
                }
                def fromValue = attributes.remove("from");
                if(fromValue != null) {
                    node.fromValue = fromValue;
                }
                def toValue = attributes.remove("to");
                if(toValue != null) {
                    node.toValue = toValue;
                }
                def byValue = attributes.remove("by");
                if(byValue != null) {
                    node.byValue = byValue;
                }
            } else if (node instanceof RotateTransition) {

                List<Double> range = attributes.remove("range") as List<Double>
                if (range != null) {
                    node.fromAngle = range[0];
                    node.toAngle = range[1];
                }
                def fromAngle = attributes.remove("from");
                if(fromAngle != null) {
                    node.fromAngle = fromAngle;
                }
                def toAngle = attributes.remove("to");
                if(toAngle != null) {
                    node.toAngle = toAngle;
                }
                def byAngle = attributes.remove("by");
                if(byAngle != null) {
                    node.byAngle = byAngle;
                }
            } else if (node instanceof ScaleTransition ||
                node instanceof TranslateTransition) {

                List<Double> range = attributes.remove("range") as List<Double>
                if (range != null) {
                    if(range.size() == 4) {
                        node.fromX = range[0];
                        node.fromY = range[1];
                        node.toX = range[2];
                        node.toY = range[3];
                    }else {
                        node.fromX = node.fromY = range[0];
                        node.toZ = node.toY = range[1];
                    }
                }
                def fromXY = attributes.remove("from");
                if(fromXY != null) {
                    node.fromX = node.fromY = fromXY;
                }
                def toXY = attributes.remove("to");
                if(toXY != null) {
                    node.toX = node.toY = toXY;
                }
                def byXY = attributes.remove("by");
                if(byXY != null) {
                    node.byX = node.byY = byXY;
                }
            }else if(node instanceof FillTransition || node instanceof StrokeTransition) {
                List range = attributes.remove("range") as List;
                if (range != null) {
                    node.fromValue = ColorFactory.get(range[0]);
                    node.toValue = ColorFactory.get(range[1]);
                }
                def fromValue = attributes.remove("from");
                if(fromValue != null) {
                    node.fromValue = ColorFactory.get(fromValue);
                }
                def toValue = attributes.remove("to");
                if(toValue != null) {
                    node.toValue = ColorFactory.get(toValue);
                }
            }
        }
        return super.onHandleNodeAttributes(builder, node, attributes);
    }
    
    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if(parent instanceof javafx.scene.Node) {
            setChildrenNode(parent, node);
        }
    }
    
    void setChildrenNode(Object parent, Object node) {
        if(!(node instanceof PauseTransition)) {
            if(node.metaClass.respondsTo(parent, "setNode") || parent.metaClass.hasProperty(parent, "node")) {
                node.node = parent;
            }else if(node.metaClass.respondsTo(parent, "setShape") || parent.metaClass.hasProperty(parent, "shape")) {
                node.shape = parent;
            }
        }
    }
    
}

