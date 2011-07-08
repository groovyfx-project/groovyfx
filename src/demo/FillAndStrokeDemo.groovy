package demo

import groovyx.javafx.GroovyFX
import groovyx.javafx.SceneGraphBuilder

GroovyFX.start {
    def sg = new SceneGraphBuilder()
    sg.stage(title: "Fill & Stroke (GroovyFX)", width: 600, height: 700, visible: true) {
        scene {
            def width = 240
            def height = 180
            fill linearGradient(endX: 0, stops: [[0.0, white], [1.0, gray]])

            tilePane(hgap: 10, vgap: 20, padding: 10) {
                def redToBlack = linearGradient(stops: [[0, red], [1, black]])
                def blackToRed = linearGradient(stops: [[0, black], [1, red]])

                rectangle(width: width, height: height, strokeWidth: 10, fill: green, stroke: rgb(0, 0, 255))

                rectangle(width: width, height: height, strokeWidth: 10) {
                    fill(blue)
                    stroke("#008000")
                }

                rectangle(width: width, height: height, strokeWidth: 10,
                        fill: linearGradient(stops: [[0, green], [1, yellow]]),
                        stroke: linearGradient(stops: [[0.0, blue], [1.0,red]]))

                rectangle(width: width, height: height, strokeWidth: 10, fill: redToBlack, stroke: blackToRed)

                rectangle(width: width, height: height, strokeWidth: 10) {
                    fill linearGradient(stops: [[0, orange], [1, black]])
                    stroke radialGradient(center: [0.5, 0.5], stops: [[0, black], [1, orange]])
                }

                rectangle(width: width, height: height, strokeWidth: 10) {
                    fill(blackToRed)
                    stroke(redToBlack)
                }
            }
        }
    }
}