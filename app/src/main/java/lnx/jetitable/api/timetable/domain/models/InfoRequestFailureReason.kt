package lnx.jetitable.api.timetable.domain.models

enum class InfoRequestFailureReason {
    PARSING_ERROR,
    UNKNOWN_ERROR,
    NETWORK_ERROR,
    EMPTY_TOKEN,
    IO_FAIL
}