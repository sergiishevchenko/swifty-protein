package com.music42.swiftyprotein.util

object VdwRadii {
    private val radiiAngstrom = mapOf(
        "H" to 1.20f,
        "C" to 1.70f,
        "N" to 1.55f,
        "O" to 1.52f,
        "F" to 1.47f,
        "P" to 1.80f,
        "S" to 1.80f,
        "CL" to 1.75f,
        "BR" to 1.85f,
        "I" to 1.98f,
        "FE" to 2.00f,
        "MG" to 1.73f,
        "ZN" to 1.39f,
        "CA" to 2.31f,
        "NA" to 2.27f,
        "K" to 2.75f
    )

    fun radiusAngstrom(element: String): Float {
        val key = element.uppercase().trim()
        return radiiAngstrom[key] ?: 1.70f
    }
}
