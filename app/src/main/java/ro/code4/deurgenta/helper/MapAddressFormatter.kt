package ro.code4.deurgenta.helper

import com.here.sdk.search.Address

fun formatAddress(address: Address): String {
    val builder = StringBuilder()
    builder.append(address.street)
    builder.append(address.postalCode)
    builder.append(", ")
    builder.append(address.city)
    return builder.toString()
}