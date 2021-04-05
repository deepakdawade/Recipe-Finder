package com.devdd.recipe.utils.extensions


fun String.isInRange(low: Int, high: Int, allowDecimal: Boolean): Boolean {
    return if (allowDecimal) {
        toDoubleOrNull()?.let { value -> return value in low.toDouble()..high.toDouble() } ?: false
    } else {
        toLongOrNull()?.let { value -> return value in low..high } ?: false
    }
}


fun String.isValidDecimalFormat(): Boolean {
    return if (isBlank()) //empty or blank
        false
    else
        (startsWith('.') || endsWith('.')).not() // X. or .X instead of X.Y
}

fun String.isValidDiscount(optional: Boolean = false): Boolean {
    if (optional && isEmpty()) return true
    return /*matches(discountRegex)*/ true
}


fun String.firstAndLastName(): Pair<String, String> {
    val parts = split(" ").toMutableList()
    val firstName = parts.firstOrNull() ?: ""
    parts.removeAt(0)
    val lastName = parts.lastOrNull() ?: ""
    return Pair(firstName, lastName)
}

fun String.isValidFirebaseEvent(): Boolean {
    val hasSpaces = split(" ").isNotEmpty()
    return (length > 40 || hasSpaces)
}

private fun removeISDCode(phoneNumber: String): String {
    var number: String = phoneNumber

    // removes space if any
    if (number.contains(" ")) number = number.replace(" ", "")

    //removes
    if (number.startsWith("+")) number = number.replace("+91", "")

    return number
}

fun String.isInRange(low: Long, high: Long, allowDecimal: Boolean): Boolean {
    return if (allowDecimal) {
        toDoubleOrNull()?.let { value -> return value in low.toDouble()..high.toDouble() } ?: false
    } else {
        toLongOrNull()?.let { value -> return value in low..high } ?: false
    }
}