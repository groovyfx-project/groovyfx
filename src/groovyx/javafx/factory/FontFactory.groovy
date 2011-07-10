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
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.Value;
import com.sun.javafx.css.parser.CSSParser;

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
    
    private static Map <String, Font> fontMap = new HashMap<String, Font>();

    static Font getFont(String str) {
        str = str.trim();
        if(str.length() == 0)
            return Font.getDefault();
        if(str.endsWith(";")) {
            str = str.substring(0, str.length()-1);
        }
        Font font = fontMap.get(str);
        if(font == null) {
            try {
                Stylesheet p = CSSParser.getInstance().parse("* { -fx-font: " + str + "; }");
                List declarations = p.getRules().get(0).getDeclarations();
                if(declarations.isEmpty()) {
                    p = CSSParser.getInstance().parse("* { -fx-font-size: " + str + "; }");
                    declarations = p.getRules().get(0).getDeclarations();
                    Value v = declarations.get(0).getCssValue();
                    def size = v.getConverter().convert(v, null);
                    font = new Font(size);
                }else {
                    Value v = declarations.get(0).getCssValue();
                    font = (Font)v.getConverter().convert(v, null);
                }
                if(font != null) {
                    fontMap.put(str, font)
                }
            }catch(IOException ex) {
                System.out.println("FontFactory.getFont() Exception: " + ex);
            }
        }
        return font;
    }

}
