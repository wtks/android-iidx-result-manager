package work.wtks.android.iidxresultmanager.data.valueobject

enum class RandomOption {
    NULL,
    OFF,
    RANDOM,
    R_RANDOM,
    S_RANDOM,
    H_RANDOM,
    MIRROR;

    val displayText: String
        get() = when (this) {
            NULL -> ""
            OFF -> "OFF"
            RANDOM -> "RANDOM"
            R_RANDOM -> "R-RANDOM"
            S_RANDOM -> "S-RANDOM"
            H_RANDOM -> "H-RANDOM"
            MIRROR -> "MIRROR"
        }
}