package ro.code4.deurgenta.data.model.requests

import com.google.gson.annotations.Expose
import ro.code4.deurgenta.data.model.BackpackItem
import ro.code4.deurgenta.data.model.BackpackItemType
import java.time.ZonedDateTime

class CreateNewBackpackRequests(@Expose val name: String)

data class UpdateBackpackItemResponse(
    @Expose val id: String,
    @Expose val backpackId: String,
    @Expose val name: String,
    @Expose val amount: Int,
    @Expose val categoryType: Int,
    @Expose val expirationDate: String?,
    @Expose val version: Int
)

fun UpdateBackpackItemResponse.toModel(): BackpackItem = BackpackItem(
    id = id,
    backpackId = backpackId,
    name = name,
    amount = amount,
    type = BackpackItemType.from(categoryType),
    expirationDate = if (expirationDate == null) null else ZonedDateTime.parse(expirationDate),
    version = version
)
