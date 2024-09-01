package lnx.jetitable.misc

fun getAcademicYear(year: Int, month: Int): String {
    return if (month >= 8) {
        "${year}/${year + 1}"
    } else {
        "${year - 1}/${year}"
    }
}

fun getSemester(month: Int): String {
    return if (month in 8..12 || month in 1..2) {
        "1"
    } else {
        "2"
    }
}