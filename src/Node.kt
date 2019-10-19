
data class Node(val name: String, val hash: Int) {
    val storage = mutableMapOf<String, String>()

    fun get(key: String): String? = storage[key]
    fun put(key: String, value: String) = storage.put(key, value)
    fun delete(key: String) {
        storage.remove(key)
    }

    fun delete(predicate: (String) -> Boolean) {
        storage.filterKeys(predicate).forEach {
            storage.remove(it.key)
        }
    }

    fun keys() = storage.keys

    fun keys(predicate: (String) -> Boolean) = storage.keys.filter(predicate)

    override fun toString(): String {
        return "Node(name = $name, hash = $hash, size = ${storage.size})"
    }
}