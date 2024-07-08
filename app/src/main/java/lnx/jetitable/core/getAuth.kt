package lnx.jetitable.core

fun AppAuthHandler(corporateEmail: String, password: String): Boolean {
    return corporateEmail.endsWith("@snu.edu.ua") && password.all { it.isDigit() }
}