package ro.code4.deurgenta.analytics

enum class Event(val title: String) {
    SCREEN_OPEN("screen_open"),
    BUTTON_CLICK("button_click"),
    LOGIN_FAILED("login_failed")
}

enum class ParamKey(val title: String) {
    NAME("name")
}

data class Param(val key: ParamKey, val value: Any)