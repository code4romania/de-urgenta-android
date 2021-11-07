package ro.code4.deurgenta.data.model.response

import com.google.gson.annotations.Expose
import ro.code4.deurgenta.data.model.Backpack

data class CreateNewBackpackResponse(
    @Expose val id: String,
    @Expose val name: String,
    @Expose val numberOfContributors: Int,
    @Expose val maxNumberOfContributors: Int
)

fun CreateNewBackpackResponse.toModel(): Backpack = Backpack(
    id = id,
    name = name,
    numberOfContributors = numberOfContributors,
    maxNumberOfContributors = maxNumberOfContributors
)

data class BackpackResponse(
    @Expose val id: String,
    @Expose val name: String,
    @Expose val numberOfContributors: Int,
    @Expose val maxNumberOfContributors: Int
)

fun BackpackResponse.toModel(): Backpack = Backpack(
    id = id,
    name = name,
    numberOfContributors = numberOfContributors,
    maxNumberOfContributors = maxNumberOfContributors
)
