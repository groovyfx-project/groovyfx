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

import javafx.scene.effect.*

/**
 *
 * @author jimclarke
 */
class EffectFactory extends AbstractFactory {
    public static final String ATTACH_EFFECT_KEY = '__attachEffect'

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        Effect effect;
        if (FactoryBuilderSupport.checkValueIsType(value, name, Effect.class)) {
            effect = (Effect) value
            if (name == 'effect') {
                builder.context[ATTACH_EFFECT_KEY] = true
            }
        } else if (FactoryBuilderSupport.checkValueIsType(value, name, Light.class)) {
            effect = (Light) value
        } else if (FactoryBuilderSupport.checkValueIsType(value, name, EffectWrapper.class)) {
            effect = (EffectWrapper) value
        } else {
            switch (name) {
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
                case 'colorInput':
                    effect = new ColorInput();
                    break;
                case 'displacementMap':
                    effect = new DisplacementMap();
                    break;
                case 'dropShadow':
                    effect = new DropShadow();
                    break;
                case 'gaussianBlur':
                    effect = new GaussianBlur();
                    break;
                case 'glow':
                    effect = new Glow();
                    break;
                case 'imageInput':
                    effect = new ImageInput();
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

        return effect;
    }

    public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof Effect) {
            if (parent instanceof Bloom) {
                ((Bloom) parent).input = (Effect) child;
            } else if (parent instanceof BoxBlur) {
                ((BoxBlur) parent).input = (Effect) child;
            } else if (parent instanceof ColorAdjust) {
                ((ColorAdjust) parent).input = (Effect) child;
            } else if (parent instanceof DisplacementMap) {
                ((DisplacementMap) parent).input = (Effect) child;
            } else if (parent instanceof DropShadow) {
                ((DropShadow) parent).input = (Effect) child;
            } else if (parent instanceof GaussianBlur) {
                ((GaussianBlur) parent).input = (Effect) child;
            } else if (parent instanceof Glow) {
                ((Glow) parent).input = (Effect) child;
            } else if (parent instanceof InnerShadow) {
                ((InnerShadow) parent).input = (Effect) child;
            } else if (parent instanceof MotionBlur) {
                ((MotionBlur) parent).input = (Effect) child;
            } else if (parent instanceof PerspectiveTransform) {
                ((PerspectiveTransform) parent).input = (Effect) child;
            } else if (parent instanceof Reflection) {
                ((Reflection) parent).input = (Effect) child;
            } else if (parent instanceof SepiaTone) {
                ((SepiaTone) parent).input = (Effect) child;
            } else if (parent instanceof Shadow) {
                ((Shadow) parent).input = (Effect) child;
            } else if (parent instanceof EffectWrapper) {
                ((EffectWrapper) parent).effect = (Effect) child;
            } else if (parent instanceof ImageInput) {
                parent.source = child; // image
            } else {
                super.setChild(build, parent, child);
            }
        } else if (parent instanceof Blend) {
            // do nothing
        } else if (parent instanceof Lighting) {
            if (child instanceof Light) {
                parent.light = child;
            }
        } else {
            super.setChild(builder, parent, child);
        }
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child && builder.context.remove(ATTACH_EFFECT_KEY)) {
            FXHelper.setPropertyOrMethod(parent, "effect", child)
        }
    }

    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        if (parent instanceof Blend && node instanceof EffectWrapper) {
            if (node.property == "topInput") {
                parent.topInput = node.effect;
            } else if (node.property == "bottomInput") {
                parent.bottomInput = node.effect;
            }
        } else if (parent instanceof Lighting && node instanceof EffectWrapper) {
            if (node.property == "bumpInput") {
                parent.bumpInput = node.effect;
            } else if (node.property == "contentInput") {
                parent.contentInput = node.effect;
            }
        }
    }
}

