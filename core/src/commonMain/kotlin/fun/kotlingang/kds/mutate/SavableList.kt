package `fun`.kotlingang.kds.mutate


internal class SavableList<T> (
    private val listSource: MutableList<T>,
    private val saveAction: SavableList<T>.() -> Unit
) : MutableList<T> by listSource {
    private fun save() = saveAction()
    
    override fun add(element: T): Boolean =
        listSource.add(element).apply { save() }

    override fun add(index: Int, element: T) {
        listSource.add(index, element)
        save()
    }

    override fun addAll(elements: Collection<T>): Boolean =
        listSource.addAll(elements).apply { save() }

    override fun addAll(index: Int, elements: Collection<T>): Boolean =
        listSource.addAll(index, elements).apply { save() }

    override fun clear() {
        listSource.clear()
        save()
    }

    private fun listIterator(iterator: MutableListIterator<T>) = object : MutableListIterator<T> by listIterator() {
        override fun add(element: T) {
            iterator.add(element)
            save()
        }
        override fun remove() {
            iterator.remove()
            save()
        }
        override fun set(element: T) {
            iterator.set(element)
            save()
        }
    }

    override fun listIterator(): MutableListIterator<T> =
        listIterator(listSource.listIterator())

    override fun listIterator(index: Int): MutableListIterator<T> =
        listIterator(listSource.listIterator(index))

    override fun remove(element: T): Boolean =
        listSource.remove(element).apply { save() }

    override fun removeAll(elements: Collection<T>): Boolean =
        listSource.retainAll(elements).apply { save() }

    override fun removeAt(index: Int): T =
        listSource.removeAt(index).apply { save() }

    override fun retainAll(elements: Collection<T>): Boolean =
        listSource.retainAll(elements).apply { save() }

    override fun set(index: Int, element: T): T =
        listSource.set(index, element).apply { save() }
}
