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

import javafx.scene.text.*;

/**
 *
 * @author jimclarke
 */
class FontFactory {
    public static Font get(Object value) {
        if(value instanceof Font) {
            return (Font)value;
        }else  {
            String fontStr = value.toString().trim();
            return getFont(fontStr);
        }
    }

    static Font getFont(String str) {
        if(str.length() == 0)
            return Font.getDefault();
        FontPosture posture = FontPosture.REGULAR;
        FontWeight weight = FontWeight.NORMAL;
        float size = 0.0;
        String family = "";
        str = str.toLowerCase();

        int ndx = str.indexOf(' ');
        if(ndx > 0) {
            posture = FontPosture.findByName(str.substring(0, ndx));
            if(posture == null) {
                posture = FontPosture.REGULAR;
            }else {
                str = str.substring(ndx+1);
            }
        }
        
        // test for weight
        ndx = str.indexOf(' ');
        if(ndx > 0) {
            // two words?
            if(str.startsWith("extra") || 
                str.startsWith("ultra") ||
                str.startsWith("demi") ||
                str.startsWith("semi") 
            ) {
                ndx = str.indexOf(' ', ndx+1);
            }
            String w = str.substring(0, ndx);
            try {
                // try int value first
                int val = Integer.valueOf(w);
                weight = FontWeight.findByWeight(val);
                str = str.substring(ndx + 1);
            }catch(NumberFormatException ex) {
                // not an int try name
                weight = FontWeight.findByName(w);
                if(weight != null)
                    str = str.substring(ndx + 1);
                else
                    weight = FontWeight.NORMAL;
            }
            
        }
        String[]  args = str.split(" ");
        size = fontSize(args[0]);
        if(args.length > 1)
            family = args[1];
        else
            family = "Amble";


        //TODO NPE bug in Font.font
        return Font.font(family, weight, posture, size);
        //
        //TODO work around
        /*
        def fontName = family;
        if(weight == FontWeight.BOLD ) {
                fontName += " bold";
        }
        if(posture == FontPosture.ITALIC)
            fontName += " italic";
        return new Font(fontName, size);
        */
    }

    static float fontSize(String sizeArg) {
        //TODO units, px, mm, ex, in, xm, mm, pc

        if(sizeArg.endsWith("pt")){
            sizeArg = sizeArg.substring(0, sizeArg.length()-2)
        }

        return Float.parseFloat(sizeArg);
    }

}
