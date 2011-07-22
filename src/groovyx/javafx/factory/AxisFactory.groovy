package groovyx.javafx.factory

import javafx.scene.chart.Axis

/**
 * A factory for creating ValueAxis and CategoryAxis objects for XYCharts.
 *
 * @author Dean Iverson
 */
class AxisFactory extends NodeFactory {
    private final Class<? extends Axis> axisClass

    AxisFactory(Class<? extends Axis> axisClass) {
        this.axisClass = axisClass
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (FactoryBuilderSupport.checkValueIsType(name, value, axisClass)) {
            return value
        } else {
            return axisClass.newInstance()
        }
    }

    @Override
    boolean isLeaf() { true }
}
