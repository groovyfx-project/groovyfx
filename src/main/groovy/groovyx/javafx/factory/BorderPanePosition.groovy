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

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node

/**
 *
 * @author jimclarke
 */
class BorderPanePosition {
    public String property;
    public Pos align;
    public Insets margin;
    public List<Node> nodes = new ArrayList<Node>();
        
    public void addNode(Node node) {
        nodes.add(node);
    }
    public Node getNode() {
        switch(nodes.size()) {
            case 0:
                return null;
            case 1:
                return nodes.get(0);
            default:
                Group grp = new Group();
                grp.getChildren().setAll(nodes);
                return grp;
        }
    }
}

