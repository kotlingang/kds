package `fun`.kotlingang.kds.mutate


internal class SavableSet<T> (
    private val setSource: MutableSet<T>,
    private val saveAction: SavableSet<T>.() -> Unit
) : MutableSet<T> by setSource {
    override fun add(element: T): Boolean = setSource.add(element).apply {
        saveAction()
    }

    override fun addAll(elements: Collection<T>): Boolean = setSource.addAll(elements).apply {
        saveAction()
    }

    override fun iterator(): MutableIterator<T> {
        val iterator = setSource.iterator()

        return object : MutableIterator<T> by iterator {
            override fun remove() {
                iterator.remove()
                saveAction()
            }
        }
    }

    override fun clear() {
        setSource.clear()
        saveAction()
    }

    override fun remove(element: T): Boolean = setSource.remove(element).apply {
        saveAction()
    }

    override fun removeAll(elements: Collection<T>): Boolean = setSource.removeAll(elements).apply {
        saveAction()
    }

    override fun retainAll(elements: Collection<T>): Boolean = setSource.retainAll(elements).apply {
        saveAction()
    }
}