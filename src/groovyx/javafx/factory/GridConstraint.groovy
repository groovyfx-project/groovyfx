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

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;

/**
 *
 * @author jimclarke
 */
class GridConstraint {
   Node node;
   int column;
   int row;
   int columnSpan = 1;
   int rowSpan = 1;
   HPos halignment = HPos.CENTER;
   VPos valignment = VPos.CENTER;
   Priority hgrow = Priority.NEVER;
   Priority vgrow = Priority.NEVER;
   Insets margin = new Insets(0,0,0,0);
   
   public void updateConstraints() {
       GridPane.setConstraints(node, column, row, columnSpan, rowSpan,
        halignment, valignment, hgrow, vgrow, margin);
   }
   
    public void clearConstraints() {
        GridPane.clearConstraings(node);
    }
}

