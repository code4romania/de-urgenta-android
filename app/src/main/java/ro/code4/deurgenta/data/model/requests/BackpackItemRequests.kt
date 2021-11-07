package ro.code4.deurgenta.data.model.requests

import com.google.gson.annotations.Expose
import ro.code4.deurgenta.data.model.BackpackItem
import java.time.format.DateTimeFormatter

data class CreateBackpackItemRequest(
    @Expose val id: String,
    @Expose val backpackId: String,
    @Expose val name: String,
    @Expose val amount: Int,
    @Expose val categoryType: Int,
    @Expose val expirationDate: String?,
    @Expose val version: Int
)

fun BackpackItem.asNewBodyRequest(): CreateBackpackItemRequest =
    CreateBackpackItemRequest(
        id = id,
        backpackId = backpackId,
        name = name,
        amount = amount,
        categoryType = type.id,
        expirationDate = expirationDate?.let { formatter.format(it) },
        version = version
    )

data class UpdateBackpackItem(
    @Expose val id: String,
    @Expose val backpackId: String,
    @Expose val name: String,
    @Expose val amount: Int,
    @Expose val categoryType: Int,
    @Expose val expirationDate: String?,
    @Expose val version: Int
)

fun BackpackItem.asUpdateBodyRequest(): UpdateBackpackItem =
    UpdateBackpackItem(
        id,
        backpackId,
        name,
        amount,
        type.id,
        expirationDate?.let { formatter.format(it) },
        version
    )

private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
