package fr.stardustenterprises.deface.api.patcher

data class ClassDefinition(val classObject: Class<Any>, val classBuffer: Array<Byte>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassDefinition

        if (classObject != other.classObject) return false
        if (!classBuffer.contentEquals(other.classBuffer)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = classObject.hashCode()
        result = 31 * result + classBuffer.contentHashCode()
        return result
    }
}