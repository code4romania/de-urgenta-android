package ro.code4.deurgenta.configuration

class FieldDescription(
    val name: String,
    val type: String,
    val sourceProperty: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldDescription) return false
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}
