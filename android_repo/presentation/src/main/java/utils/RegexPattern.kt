package utils

object RegexPattern {
    const val EMAIL_PATTERN = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
    const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!~#$%^&*?])(?!.*[^!~#$%^&*?a-zA-Z0-9]).{8,16}$"
}
