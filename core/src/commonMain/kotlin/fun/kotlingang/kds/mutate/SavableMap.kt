package `fun`.kotlingang.kds.mutate


internal class SavableMap<K, V> (
    private val mapSource: MutableMap<K, V>,
    private val saveAction: SavableMap<K, V>.() -> Unit
) : MutableMap<K, V> by mapSource {
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>

    init {
        val set = SavableSet (
            setSource = mapSource.entries,
            saveAction = { saveAction() }
        )
        entries = object : MutableSet<MutableMap.MutableEntry<K, V>> by set {
            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> {
                val iterator = set.iterator()

                return object : MutableIterator<MutableMap.MutableEntry<K, V>> by iterator {
                    override fun next(): MutableMap.MutableEntry<K, V> {
                        val nextEntry = iterator.next()
                        return object : MutableMap.MutableEntry<K, V> by nextEntry {
                            override fun setValue(newValue: V): V = nextEntry.setValue(newValue).apply {
                                saveAction()
                            }
                        }
                    }
                }
            }
        }
    }

    override val keys: MutableSet<K> =
        object : MutableSet<K> by SavableSet (
            setSource = mapSource.keys,
            saveAction = { saveAction() }
        ) {}

    override val values: MutableList<V> =
        object : MutableList<V> by SavableList (
            listSource = mapSource.values.toMutableList(),
            saveAction = { saveAction() }
        ) {}

    override fun clear() {
        mapSource.clear()
        saveAction()
    }

    override fun put(key: K, value: V): V? = mapSource.put(key, value).apply {
        saveAction()
    }

    override fun putAll(from: Map<out K, V>): Unit = mapSource.putAll(from).let {
        saveAction()
    }

    override fun remove(key: K): V? = mapSource.remove(key).apply {
        saveAction()
    }
}