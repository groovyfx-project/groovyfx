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

import javafx.animation.*;
import javafx.util.Duration;
import javafx.builders.AnimationAbstractBuilder;
import javafx.builders.FadeTransitionBuilder;
import javafx.builders.TransitionAbstractBuilder;
import groovyx.javafx.factory.animation.KeyValueSubFactory;
import javafx.scene.shape.Path;
import javafx.scene.Node;

/**
 * Handles JavaFX transitions
 * @author jimclarke
 */
class TransitionFactory extends AbstractGradientFactory {
    
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
	if (value != null && value instanceof Transition)
            return value;
        else {
            Transition result = null;
            switch(name) {
                case 'fadeTransition':
                    result = new FadeTransition();
                    break;
                case 'fillTransition':
                    result = new FillTransition();
                    break;
                case 'parallelTransition':
                    result = new ParallelTransition();
                    break;
                case 'sequentialTransition':
                    result = new SequentialTransition();
                    break;
                case 'pauseTransition':
                    result = new PauseTransition();
                    break;
                case 'pathTransition':
                    result = new PathTransition();
                    break;
                case 'rotateTransition':
                    result = new RotateTransition();
                    break;
                case 'scaleTransition':
                    result = new ScaleTransition();
                    break;
                case 'strokeTransition':
                    result = new StrokeTransition();
                    break;
                case 'translateTransition':
                    result = new TranslateTransition();
                    break;
                case 'transition':
                    result = value;
                    break;
            }
            if(result != null && value != null && value instanceof Duration) {
                result.duration = value;
            }
            return result;
        }
    }
    
    public void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        if((parent instanceof ParallelTransition || parent instanceof SequentialTransition) && child instanceof Transition) {
            parent.children.add(child);
        }else if(parent instanceof PathTransition && child instanceof Path) {
            parent.path = child
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
                    onFinished = new ClosureEventHandler(closure: onFinished);
                }
                node.onFinished = onFinished;
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
        return true;
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

