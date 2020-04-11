/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2011-2020 the original author or authors.
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
import javafx.geometry.Point2D
import javafx.scene.image.Image

import javax.xml.bind.DatatypeConverter

import static groovyx.javafx.GroovyFX.start

/**
 * Inspired by Dirk Weber, "An experiment: Canvas vs. SVG vs. Flash"
 * http://www.eleqtriq.com/2010/02/canvas-svg-flash/
 * 
 * @author jimclarke
 */
String imgData = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAdElEQVQImQFpAJb/AWYzAA8PFBlgBw0QUPj08gDv6eVgBA4VHHAfLDeACw8UAPHr5AAB//+wAgwPFHAUHCUAHio1AA4UGgAJDhIwBPn48/D07+oQAQEBAPj08QD6+fbgAysK78D+/fs48+/qGO7o4dDt5dpYZmUs8b4SyMYAAAAASUVORK5CYII=";
Image img_particle;

byte[] imgBytes = DatatypeConverter.parseBase64Binary(imgData);

    
double numRotations = 42000;
double numParticles = 700;
double baseRadius = 120;
double outerRadius = 100;

double wDelta; //a variable for storing the angle between joints
double tangDelta = 40;
Point2D center;
fps = "";
strokeColor = null;
start {
    
    def polar = { radius,  angle ->
        double cos=Math.cos(angle);
        double sin=Math.sin(angle);
        Point2D point= new Point2D(radius*cos, radius*sin-1);
        return point;
    }

    def addPoint= {  pt1,  pt2 ->
        double x=pt1.x+pt2.x;
        double y=pt1.y+pt2.y;
        Point2D point = new Point2D(x, y);
        return point;
    }

    def calculate = {int counter ->
        double nRotations = numRotations/10000;
        double wDelt= wDelta*counter;

        Point2D pBaseRadius = polar(baseRadius, wDelt);
        pBaseRadius = addPoint(pBaseRadius, center);
        double alpha=outerRadius/nRotations*wDelt;

        Point2D pOuterRadius = polar(outerRadius, alpha);
        Point2D pos=addPoint(pOuterRadius, pBaseRadius);

        double tangW=Math.atan(tangDelta/outerRadius);
        double tangRad=Math.sqrt(Math.pow(outerRadius,2)+Math.pow(tangDelta,2));
        Point2D ptangent=polar(tangRad, alpha+tangW);
        ptangent=addPoint(ptangent,pBaseRadius);
        return [pos, ptangent];

    }
    def checkIntersection = { Point2D p1, Point2D p2, Point2D p3, Point2D p4->
        double numerator_uA=(p4.x-p3.x)*(p1.y-p3.y)-(p4.y-p3.y)*(p1.x-p3.x);
        double denominator_uA=(p4.y-p3.y)*(p2.x-p1.x)-(p4.x-p3.x)*(p2.y-p1.y);

        double uA=numerator_uA/denominator_uA;
        double numerator_uB=(p2.x-p1.x)*(p1.y-p3.y)-(p2.y-p1.y)*(p1.x-p3.x);
        double denominator_uB=denominator_uA;
        double uB=numerator_uB/denominator_uB;

        //Are lines parallel, overlap?
        Point2D s1 = (denominator_uA==0)?p1:
                new Point2D(
                    (p1.x+uA*(p2.x-p1.x)), 
                    (p1.y+uA*(p2.y-p1.y)));
        return s1;
    }
    def drawCurveSegment = {gc, Point2D[] points, Point2D pos->
        Point2D handler=checkIntersection(points[0], points[1], points[2], points[3])
        gc.stroke = strokeColor;
        gc.lineWidth = 1
        gc.beginPath()
        gc.moveTo(points[0].x, points[0].y)
        gc.quadraticCurveTo(handler.x, handler.y, points[2].x, points[2].y)
        gc.stroke()
        gc.drawImage(img_particle, pos.x-2.5, pos.y-2.5, 5, 5)
    }


    def drawCanvas = { gc ->
        long start = System.currentTimeMillis();
        gc.clearRect(0, 0, canvas.width, canvas.height)

        center = new Point2D(canvas.width/2.0, canvas.height/2.0);
        wDelta = (2*Math.PI)/numParticles;
        Point2D pos;
        Point2D[] pts = new Point2D[4];
        Point2D[] tmp = calculate(0);
        pts[0] = tmp[0];
        pts[1] = tmp[1];

        for(int i=1; i<numParticles; i++){
            tmp=calculate(i);
            pos = pts[2] = tmp[0];
            pts[3] = tmp[1];    
            drawCurveSegment(gc, pts, pos);
            pts[0] = pts[2];
            pts[1] = pts[3];
        }
        long end = System.currentTimeMillis();
        long elpased = end-start;
        double time = Math.floor(10000.0/elpased)/10.0;
        fps = time+" fps";
    }

    img_particle = image(DatatypeConverter.parseBase64Binary(imgData));
    strokeColor = rgb(95,68,34,0.4)
    stage(title: "SpirographFX", x: 100, y: 100, visible: true) {
        scene(id: "scene", fill: groovyblue, width: 600, height: 640) {
            vbox(spacing: 10) {
                canvas(id:"canvas", width: bind(scene.width()),
                    height: bind(scene.height() - 200 )) {
                    operation drawCanvas
                    onInvalidate("width") {
                        canvas.userData.draw()
                    }
                    onInvalidate("height") {
                        canvas.userData.draw()
                    }
                }
                gridPane(id:"sliderPane", hgap: 20, vgap: 10, padding: 5) {
                    columnConstraints(prefWidth: 150, halignment: "right")
                    columnConstraints(prefWidth: 400, hgrow: 'always')

                    label("Number of Rotations", row: 0, column: 0)
                    slider(row: 0, column: 1, min: -50000, max: 50000, value: 42000, 
                            showTickMarks: true, showTickLabels: true,
                            minorTickCount: 4, majorTickUnit: 10000.0 ) {
                        onChange("value") { observable, oldValue, newValue ->
                            numRotations = newValue
                            canvas.userData.draw()
                        }
                    }

                    label("Number of Particles", row: 1, column: 0)
                    slider(row: 1, column: 1, min: 700, max: 1400, value: 700, 
                            showTickMarks: true, showTickLabels: true,
                            minorTickCount: 4, majorTickUnit: 100.0 ) {
                        onChange("value") { observable, oldValue, newValue ->
                            numParticles = newValue
                            canvas.userData.draw()
                        }
                    }

                    label("Base Radius", row: 2, column: 0)
                    slider(row: 2, column: 1, min: -300, max: 300, value: 120, 
                            showTickMarks: true, showTickLabels: true,
                            minorTickCount: 4, majorTickUnit: 100.0 ) {
                        onChange("value") { observable, oldValue, newValue ->
                            numRotations = newValue
                            canvas.userData.draw()
                        }
                    }

                    label("Outer Radius", row: 3, column: 0)
                    slider(row: 3, column: 1, min: -200, max: 200, value: 100, 
                            showTickMarks: true, showTickLabels: true,
                            minorTickCount: 4, majorTickUnit: 100.0 ) {
                        onChange("value") { observable, oldValue, newValue ->
                            numRotations = newValue
                            canvas.userData.draw()
                        }
                    }

                    label( row:4, column:0, columnSpan: 2, text: bind (this, "fps"))
                }
            }
        }
    }
    canvas.userData.draw()
    
}


