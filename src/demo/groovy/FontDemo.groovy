import static groovyx.javafx.GroovyFX.start
import javafx.scene.text.Font

start {
    stage(title: 'GroovyFX', show: true) {
        scene {
            rectangle(width: 300, height: 100, fill: GREEN)
        }
    }//    stage(show: true) {
//        scene(fill: GROOVYBLUE) {
//            vbox {
//                text('Text with font "80pt"', font: '80pt')
//                hbox(spacing: 20) {
//                    text('Text with font("serif", 36)', font: Font.font('serif', 36))
//                    text('Text with font "36pt serif"', font: '36pt serif')
//                }
//                hbox(spacing: 20) {
//                    text('Text with font("sanserif", 24)', font: Font.font('sanserif', 24))
//                    text('Text with font "24pt sanserif"', font: '24pt sanserif')
//                }
//                hbox(spacing: 20) {
//                    text('Text with font("Courier New", 24)', font: Font.font('Courier New', 24))
//                    text('Text with font "24pt Courier New"', font: '24pt "Courier New"')
//                }
//            }
//        }
//    }
}