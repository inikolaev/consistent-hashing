
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ServiceTest {
    @Test
    fun testAddNode() {
        val keys = listOf("0", "1", "2", "3", "4", "5", "6")

        val nodes = keys.map { key ->
            Node("Node $key", key.hashCode())
        }

        val newNode = nodes[3]

        val service = Service(2, nodes.filter { it != newNode })

        keys.forEach { key ->
            service.put(key, "value of key $key")
        }

        assertEquals(3, nodes[0].storage.size)
        assertEquals(3, nodes[1].storage.size)
        assertEquals(3, nodes[2].storage.size)
        assertEquals(0, nodes[3].storage.size)
        assertEquals(4, nodes[4].storage.size)
        assertEquals(4, nodes[5].storage.size)
        assertEquals(4, nodes[6].storage.size)

        service.addNode(newNode)

        assertEquals(3, nodes[0].storage.size)
        assertEquals(3, nodes[1].storage.size)
        assertEquals(3, nodes[2].storage.size)
        assertEquals(3, nodes[3].storage.size)
        assertEquals(3, nodes[4].storage.size)
        assertEquals(3, nodes[5].storage.size)
        assertEquals(3, nodes[6].storage.size)

        nodes.forEach { node ->
            println("${node.name}: ${node.storage.keys} ")
        }
    }
}
