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

import java.util.List;
import javafx.scene.paint.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;
import javafx.scene.paint.Paint;

import javafx.scene.paint.RadialGradientBuilder
import javafx.scene.paint.LinearGradientBuilder;

import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.Value;
import com.sun.javafx.css.parser.CSSParser;

/**
 *
 * @author jimclarke
 */
public class ColorFactory {
    
    private static Map <String, Paint> colorCacheMap = new HashMap<String, Paint>();

    public static Paint get(Object value) {
        if(value instanceof Paint) {
            return (Paint)value;
        } else if(value instanceof RadialGradientBuilder || value instanceof LinearGradientBuilder) {
            return value.build();
        }else if(value instanceof List || value instanceof Object[]) {
            Object[]args;
            if(value instanceof List) {
                List list = (List)value;
                args = list.toArray();
            }else {
                args = (Object[])value;
            }
            if(args[0] instanceof String) {
                String cmd = (String)args[0];
                if(cmd.equalsIgnoreCase("rgb")) {
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    float a = 1.0F;
                    switch(args.length) {
                        // fall through
                        case 5:
                            a = toFloat(args[5]);
                        case 4:
                            b = toInt(args[3]);
                        case 3:
                            g = toInt(args[2]);
                        case 2:
                            r = toInt(args[1]);
                    }
                    return Color.rgb(r, g, b, a);

                }else if(cmd.equalsIgnoreCase("hsb")) {
                    int h = 0;
                    float s = 0.0F;
                    float b = 0.0F;
                    float a = 1.0F;
                    switch(args.length) {
                        // fall through
                        case 5:
                            a = toFloat(args[4]);
                        case 4:
                            b = toFloat(args[3]);
                        case 3:
                            s = toFloat(args[2]);
                        case 2:
                            h = toInt(args[1]);
                    }
                    return Color.hsb(h, s, b, a);
                }else {
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    float a = 1.0F;
                    switch(args.length) {
                        // fall through
                        case 4:
                            a = toFloat(args[3]);
                        case 3:
                            b = toInt(args[2]);
                        case 2:
                            g = toInt(args[1]);
                        case 1:
                            r = toInt(args[0]);
                    }
                    return Color.color(r, g, b, a);
                }
            }else {
                int r = 0;
                int g = 0;
                int b = 0;
                float a = 1.0F;
                switch(args.length) {
                    // fall through
                    case 4:
                        a = toFloat(args[3]);
                    case 3:
                        b = toInt(args[2]);
                    case 2:
                        g = toInt(args[1]);
                    case 1:
                        r = toInt(args[0]);
                }
                return Color.rgb(r, g, b, a);
            }
        }else if(value != null) {
            String color = value.toString().trim();

            if (color.startsWith('#')) {
                return Color.web(color)
            }
            
            if(color.endsWith(";")) {
                color = color.substring(0, color.length()-1);
            }

            Paint paint = colorCacheMap.get(color);
            if(paint == null) {
                Stylesheet p = CSSParser.getInstance().parse("* { -fx-fill: " + color + "; }");
                List declarations = p.getRules().get(0).getDeclarations();

                if (!declarations)
                    throw new IllegalArgumentException("Invalid fill syntax: '$color'")
                
                Value v = declarations.get(0).getCssValue();
                if(v.getConverter() == null)
                    paint = (Paint)v.getValue();
                else
                    paint = (Paint)v.getConverter().convert(v, null);
            }
            return paint;
        }else {
            return null;
        }

    }
    private static String numberPattern = "(\\d*\\.\\d+|\\d+\\.?)"
    private static String sizePattern = "((?:\\d*\\.\\d+|\\d+\\.?)%?)"
    private static String stopPattern = "(?:\\s+\\(${numberPattern}\\s*,\\s*([^\\)]+)\\))"
    private static String cyclePattern = "(?:\\s+(repeat|reflect))?";
    private static String linearPatternString = "linear\\s+\\(${sizePattern},${sizePattern}\\)\\s+to\\s+\\(${sizePattern},${sizePattern}\\)\\s+stops${stopPattern}${stopPattern}?${stopPattern}?${stopPattern}?${stopPattern}?${cyclePattern}";
    private static Pattern linearPattern = Pattern.compile(linearPatternString);

    private static String radialPatternString = "radial\\s+(?:\\(${sizePattern}\\s*,\\s*${sizePattern}\\)\\s*,\\s*)?${sizePattern}\\s+(?:focus\\s*\\(${sizePattern}\\s*,\\s*${sizePattern}\\)\\s*)?stops${stopPattern}${stopPattern}?${stopPattern}?${stopPattern}?${stopPattern}?${cyclePattern}";
    private static Pattern radialPattern = Pattern.compile(radialPatternString);


    static Paint getLinearPaint(String style) {
        Matcher m = linearPattern.matcher(style);
        if(!m.matches()) {
            throw new RuntimeException("Failed to parse: ${style}")
        }
        float startX = getSize(m.group(1));
        float startY = getSize(m.group(2));
        float endX = getSize(m.group(3));
        float endY = getSize(m.group(4));
        String[] stopFrac = new String[5];
        String[] stopColor = new String[5];
        for(int i = 0; i < 5; i++) {
            stopFrac[i] = m.group(5+i*2);
            stopColor[i] = m.group(6+i*2);
        }
        String cycleMethod = m.group(15);

        def stops = [];
        for(int i = 0; i < 5; i++) {
            if(stopFrac[i] != null) {
                Stop stop = new Stop(getSize(stopFrac[i]), get(stopColor[i]));
                stops << stop;
            }else {
                break;
            }
        }
        def method = CycleMethod.NO_CYCLE;
        if(cycleMethod != "repeat")
            method = CycleMethod.REPEAT
        else if(cycleMethod == "reflect")
            method == CycleMethod.REFLECT;
        boolean proportional = m.group(1).endsWith("%");
        return new LinearGradient(startX, startY, endX, endY,
            proportional, method, (Stop[])stops.toArray());

    }
    static Paint getRadialPaint(String style) {
        Matcher m = radialPattern.matcher(style);
        if(!m.matches()) {
            throw new RuntimeException("Failed to parse: ${style}")
        }
        boolean proportional = true;
        float centerX = 0.5;
        float centerY = 0.5;
        float focusX = 0.5;
        float focusY = 0.5;
        String tmp = m.group(1);
        if(tmp != null)
            centerX = getSize(tmp);
        tmp = m.group(2);
        if(tmp != null)
            centerY = getSize(tmp);
        tmp = m.group(3);
        float radius = getSize(tmp);
        proportional = tmp.endsWith("%");
        tmp = m.group(4);
        if(tmp != null)
            focusX = getSize(tmp);
        tmp = m.group(5);
        if(tmp != null)
            focusY = getSize(tmp);


        String[] stopFrac = new String[5];
        String[] stopColor = new String[5];
        for(int i = 0; i < 5; i++) {
            stopFrac[i] = m.group(6+i*2);
            stopColor[i] = m.group(7+i*2);
        }
        String cycleMethod = m.group(16);

        def stops = [];
        for(int i = 0; i < 5; i++) {
            if(stopFrac[i] != null) {
                Stop stop = new Stop(getSize(stopFrac[i]), get(stopColor[i]));
                stops << stop;
            }else {
                break;
            }
        }
        def method = CycleMethod.NO_CYCLE;
        if(cycleMethod != "repeat")
            method = CycleMethod.REPEAT
        else if(cycleMethod == "reflect")
            method == CycleMethod.REFLECT;

        return new RadialGradient(focusX, focusY, centerX, centerY,
            radius, proportional, method, (Stop[])stops.toArray());
    }

    static Paint getRGBPaint(String cmd) {
        //TODO getRGBPaint
        String[] splitCmd = cmd.split("[()]");
        String[] args = splitCmd[1].split("[, ]+")
        int r = 0;
        int g = 0;
        int b = 0;
        float a = 1.0F;
        switch(args.length) {
            // fall through
            case 4:
                a = toFloat(args[3]);
            case 3:
                b = toInt(args[2]);
            case 2:
                g = toInt(args[1]);
            case 1:
                r = toInt(args[0]);
        }
        return Color.rgb(r, g, b, a);
    }
    static Paint getHSBPaint(String cmd) {
        String[] splitCmd = cmd.split("[()]");
        String[] args = splitCmd[1].split("[, ]+");
        int h = 0;
        float s = 0.0F;
        float b = 0.0F;
        float a = 1.0F;
        switch(args.length) {
            // fall through
            case 4:
                a = toFloat(args[3]);
            case 3:
                b = toFloat(args[2]);
            case 2:
                s = toFloat(args[1]);
            case 1:
                h = toInt(args[0]);
        }
        return Color.hsb(h, s, b, a);
    }
    static float getSize(Object val) {
        if(val == null)
            return 0;
        if(val instanceof Number) {
            return ((Number)val).floatValue();
        }else {
            String s =  val.toString();
            if(s.endsWith("%")) {
                float per = Float.parseFloat(s.substring(0,s.length()-1));
                return per/100.0F;
            }else {
                return Float.parseFloat(s);
            }
        }


    }
    static float toFloat(Object val) {
        if(val == null)
            return 0;
        if(val instanceof Number) {
            return ((Number)val).floatValue();
        }else {
            String s =  val.toString();
            if(s.endsWith("%")) {
                float per = Float.parseFloat(s.substring(0,s.length()-1));
                return 255.0F * per/100.0F;
            }else if(s.startsWith("#")) {
                return Integer.parseInt(s.substring(1).toUpperCase(), 16);
            }else if(s.startsWith("0x")) {
                return Integer.parseInt(s.substring(2).toUpperCase(), 16);
            }else {
                return Float.parseFloat(s);
            }
        }
    }
    static int toInt(Object val) {
         if(val == null)
            return 0;
        if(val instanceof Number) {
            return ((Number)val).intValue();
        }else {
            String s =  val.toString();
            if(s.endsWith("%")) {
                float per = Float.parseFloat(s.substring(0,s.length()-1));
                return (int)(255.0F * per/100.0F);
            }else if(s.startsWith("#")) {
                return Integer.parseInt(s.substring(1).toUpperCase(), 16);
            }else if(s.startsWith("0x")) {
                return Integer.parseInt(s.substring(2).toUpperCase(), 16);
            }else {
                return Integer.parseInt(s);
            }
        }
    }
}


