
class Service(val replication: Int, nodes: List<Node>) {
    val nodes = nodes.sortedBy(Node::hash).toMutableList()

    fun getNodeByIndex(index: Int) = nodes[if (index >= 0) index % nodes.size else nodes.size + index]

    fun getNode(key: String) = getNode(key.hashCode())

    fun getNode(hash: Int): Int {
        var lo = 0
        var hi = nodes.size
        var mi = (hi - lo) / 2
        var node = 0

        while (lo < hi) {
            if (nodes[mi].hash == hash) {
                node = mi
                break
            }

            if (nodes[mi].hash < hash && mi < nodes.lastIndex && nodes[mi + 1].hash >= hash) {
                node = mi + 1
                break
            }

            if (nodes[mi].hash < hash) {
                lo = mi + 1
            }

            if (nodes[mi].hash > hash) {
                hi = mi
            }

            mi = (lo + hi) / 2
        }

        return node
    }

    fun get(key: String): String? = nodes[getNode(key)].get(key)
    fun put(key: String, value: String) {
        var hash = key.hashCode()
        (-1 until replication).forEach {
            val node = nodes[getNode(hash)]
            node.put(key, value)
            hash = node.hash + 1
        }
    }
    fun delete(key: String) = nodes[getNode(key)].delete(key)

    fun addNode(node: Node) {
        val nextNodeIndex = getNode(node.hash)
        val nextNode = getNodeByIndex(nextNodeIndex)
        val keys = nextNode.keys { key ->
            key.hashCode() <= node.hash
        }

        // Copy keys from other node and become a replica for `replication` previous nodes
        keys.forEach { key ->
            nextNode.get(key)?.let { value ->
                node.put(key, value)
            }
        }

        // Add node
        nodes.add(node)
        nodes.sortBy(Node::hash)

        // Remove replicated keys from nodes following new node
        (nextNodeIndex .. nextNodeIndex + replication).forEach { index ->
            val replicaNode = getNodeByIndex(index + 1)
            val masterNode = getNodeByIndex(index - replication)

            val replicaNodeHash = replicaNode.hash
            val masterNodeHash = masterNode.hash

            replicaNode.delete { key ->
                val hash = key.hashCode()
                val x = replicaNodeHash <= masterNodeHash
                val y = hash > replicaNodeHash
                val z = hash <= masterNodeHash

                (x && y && z) || (y || z)
            }
        }
    }

    override fun toString(): String {
        return nodes.joinToString(separator = "\n") + "\n"
    }
}