package ro.code4.deurgenta.configuration

open class VariantConfigFieldsExtension {
    internal val fields = mutableSetOf<FieldDescription>()

    fun addField(propertyName: String, fieldName: String, fieldType: String) {
        fields.add(FieldDescription(fieldName, fieldType, propertyName))
    }
}
