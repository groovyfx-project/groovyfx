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
import static groovyx.javafx.GroovyFX.start

/**
 *
 * @author jimclarke
 */
start {
    x1 = 100
    y1 = 100
    x2 = 300
    y2 = 300
    lw = 10
    f = yellow
    s = navy
    stage(title: "Canvas Demo", x: 100, y: 100, visible: true, style: "decorated", primary: true) {
        scene(id: "sc", fill: GROOVYBLUE, width: 400, height: 400) {
            canvas( id:"canvas", width: bind(sc.width()), height: bind(sc.height()) ) {
                fill(p: bind(this, "f"))
                fillRect (x:0,y:0, w:bind(canvas.width()), h:bind(canvas.height()))
                stroke(p: bind(this, "s"))
                lineWidth(lw: bind(this, "lw"))
                path()
                moveTo (x: bind(this, "x1"), y: bind(this,"y1"))
                lineTo (x: bind(this, "x2"), y: bind(this,"y2"))
                strokePath()
                onMouseClicked {
                    f = cyan
                    s = red
                    canvas.userData.draw()
                }
            }
        }
    }
    draw(canvas) {
        stroke red
        lineWidth 10
        path()
        moveTo 100,300
        lineTo 300, 100
        strokePath()
    }
    x = 50
    y = 50
    w = 100
    h = 50
    c = green
    d = draw() {
        fill(p: bind (this,"c"))
        fillOval(x:bind(this, "x"),y:bind(this, "y"), 
            w:bind(this, "w"), h:bind(this, "h"))
    }
    d.draw(canvas)
    
    c=purple
    x = 150
    y = 350
    d.draw(canvas)
}


