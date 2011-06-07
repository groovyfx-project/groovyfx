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

import javafx.scene.effect.*;

/**
 *
 * @author jimclarke
 */
class EffectFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
       Object effect;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Effect.class)) {
            effect = (Effect)value
        } else if (FactoryBuilderSupport.checkValueIsType(value, name, Light.class)) {
            effect = (Light)value
        } else if (FactoryBuilderSupport.checkValueIsType(value, name, EffectWrapper.class)) {
            effect = (EffectWrapper)value
        } else {
            switch(name) {
                case 'blend':
                    effect = new Blend();
                    break;
                case 'bloom':
                    effect = new Bloom();
                    break;
                case 'boxBlur':
                    effect = new BoxBlur();
                    break;
                case 'colorAdjust':
                    effect = new ColorAdjust();
                    break;
                case 'displacementMap':
                    effect = new DisplacementMap();
                    break;
                case 'dropShadow':
                    effect = new DropShadow();
                    break;
                case 'colorInput':
                    effect = new ColorInput();
                    break;
                case 'gaussianBlur':
                    effect = new GaussianBlur();
                    break;
                case 'glow':
                    effect = new Glow();
                    break;
                case 'innerShadow':
                    effect = new InnerShadow();
                    break;
                case 'lighting':
                    effect = new Lighting();
                    break;
                case 'motionBlur':
                    effect = new MotionBlur();
                    break;
                case 'perspectiveTransform':
                    effect = new PerspectiveTransform();
                    break;
                case 'reflection':
                    effect = new Reflection();
                    break;
                case 'sepiaTone':
                    effect = new SepiaTone();
                    break;
                case 'shadow':
                    effect = new Shadow();
                    break;
                case 'topInput': // Blend
                    effect = new EffectWrapper(property: "topInput");
                    break;
                case 'bottomInput': // Blend
                    effect = new EffectWrapper(property: "bottomInput");
                    break;
                case 'bumpInput': // Lighting
                    effect = new EffectWrapper(property: "bumpInput");
                    break;
                case 'contentInput': // Lighting
                    effect = new EffectWrapper(property: "contentInput");
                    break;
                case "distant": // Light
                    effect = new Light.Distant();
                    break;
                case "point": // Light
                    effect = new Light.Point();
                    break;
                case "spot": // Light
                    effect = new Light.Spot();
                    break;
            }
        }

        //FXHelper.fxAttributes(effect, attributes);
        return effect;
    }

    public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
        if(child instanceof Effect) {
            if(parent instanceof Bloom) {
                ((Bloom)parent).input = (Effect)child;
            }else if(parent instanceof BoxBlur) {
                ((BoxBlur)parent).input = (Effect)child;
            }else if(parent instanceof ColorAdjust) {
                ((ColorAdjust)parent).input = (Effect)child;
            }else if(parent instanceof DisplacementMap) {
                ((DisplacementMap)parent).input = (Effect)child;
            }else if(parent instanceof DropShadow) {
                ((DropShadow)parent).input = (Effect)child;
            }else if( parent instanceof GaussianBlur) {
                ((GaussianBlur)parent).input = (Effect)child;
            }else if( parent instanceof Glow) {
                ((Glow)parent).input = (Effect)child;
            }else if( parent instanceof InnerShadow) {
                ((InnerShadow)parent).input = (Effect)child;
            }else if( parent instanceof MotionBlur) {
                ((MotionBlur)parent).input = (Effect)child;
            }else if( parent instanceof PerspectiveTransform) {
                ((PerspectiveTransform)parent).input = (Effect)child;
            }else if( parent instanceof Reflection) {
                ((Reflection)parent).input = (Effect)child;
            }else if( parent instanceof SepiaTone) {
                ((SepiaTone)parent).input = (Effect)child;
            }else if( parent instanceof Shadow) {
                ((Shadow)parent).input = (Effect)child;
            }else if( parent instanceof EffectWrapper) {
                ((EffectWrapper)parent).effect = (Effect)child;
            }else {
                super.setChild(build, parent, child);
            }
        }else if (parent instanceof Blend) {
            if(child instanceof EffectWrapper) {
                EffectWrapper wrapper = (EffectWrapper)child;
                if(wrapper.property == "topInput") {
                    ((Blend)parent).setTopInput(wrapper.effect);
                }else if(wrapper.property == "bottomInput") {
                    ((Blend)parent).setBottomInput(wrapper.effect);
                }
            }
        }else if (parent instanceof Lighting) {
            if(child instanceof EffectWrapper) {
                EffectWrapper wrapper = (EffectWrapper)child;
                if(wrapper.property == "bumpInput") {
                    ((Lighting)parent).setBumpInput(wrapper.effect);
                }else if(wrapper.property == "contentInput") {
                    ((Lighting)parent).setContentInput(wrapper.effect);
                }
            }else if(child instanceof Light) {
                ((Lighting)parent).setLight((Light)child);
            }
        }else {
            super.setChild(build, parent, child);
        }
    }
}

