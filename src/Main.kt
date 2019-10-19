import kotlin.random.Random

fun main(args: Array<String>) {
    val count = 10
    val step = 2 * (Int.MAX_VALUE / count)
    val nodes = (0 until count).map {
        Node("Node $it", Int.MIN_VALUE + it * step)
    }

    val service = Service(2, nodes)

    fun populate() {
        (0..10).forEach {
            val value = Random.nextInt(Int.MIN_VALUE, Int.MAX_VALUE)
            service.put("$value", "Value $value")
        }
    }

    populate()

    println(service)

    println("Node 6: ${nodes[6].keys()}")
    println("Node 8: ${nodes[8].keys()}")
    println("Node 0: ${nodes[0].keys()}")

    service.addNode(Node("Node 10", 1084993448))

    println("Node 6: ${nodes[6].keys()}")
    println("Node 8: ${nodes[8].keys()}")
    println("Node 0: ${nodes[0].keys()}")

    println(service)

    populate()

    println(service.nodes)
}