package idesupport

/**
Extract the names, types, and more info of all known nodes from the GDSL file.
@author Dierk Koenig
*/


File gdsl = new File("src/main/groovy/idesupport/groovyfx.gdsl")
if (! gdsl.exists()) {
    println "cannot find file for reading: $gdsl.absolutePath"
    return
}

def methods    = []
def properties = []

method   = { Map args -> methods    << args }
property = { Map args -> properties << args }
context  = { Map args -> this }
contributor   = { arg, closure -> arg.with closure }
enclosingCall = { true }
closureScope  = { null }

evaluate gdsl

methods.unique{it.name}.each {
    def type = it.type ?: ''
    def params = it.params ?: Collections.emptyMap()
    println "${it.name.padRight(22)} ${type.padRight(42)} (${params.keySet().join(', ')})"
}
println properties*.name
