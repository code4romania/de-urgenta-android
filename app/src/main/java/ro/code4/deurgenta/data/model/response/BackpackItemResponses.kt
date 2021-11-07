package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType
import java.time.ZonedDateTime

data class BackpackItemResponse(
    @Expose val id: String,
    @Expose val backpackId: String,
    @Expose val name: String,
    @Expose val amount: Int,
    @Expose val expirationDate: String?,
    @Expose val categoryType: Int,
    @Expose val version: Int
)

fun BackpackItemResponse.toModel(): BackpackItem = BackpackItem(
    id = id,
    backpackId = backpackId,
    name = name,
    amount = amount,
    expirationDate = if (expirationDate != null) ZonedDateTime.parse(expirationDate) else null,
    type = BackpackItemType.from(categoryType),
    version = version
)

data class SaveBackpackItemResponse(
    @Expose val id: String,
    @Expose val backpackId: String,
    @Expose val name: String,
    @Expose val amount: Int,
    @Expose val expirationDate: String?,
    @Expose val categoryType: Int,
    @Expose val version: Int
)

fun SaveBackpackItemResponse.toModel() = BackpackItem(
    id = id,
    backpackId = backpackId,
    name = name,
    amount = amount,
    expirationDate = if (expirationDate != null) ZonedDateTime.parse(expirationDate) else null,
    type = BackpackItemType.from(categoryType),
    version = version
)
