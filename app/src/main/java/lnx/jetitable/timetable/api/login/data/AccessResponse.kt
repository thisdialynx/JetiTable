package lnx.jetitable.timetable.api.login.data

import org.json.JSONObject

data class AccessResponse(
    val access: List<Int>,
    val accessToken: String,
    val status: String,
    val user: User
)

data class User(
    val fullName: String = "",
    val userId: Int = 0,
    val status: String = "",
    val fullNameId: Int = 0,
    val key: String = "",
    val group: String = "",
    val groupId: String = "",
    val isFullTime: Int = 0,
    val facultyCode: Int = 0
)

fun parseAccessResponse(jsonString: String): AccessResponse {
    val jsonObject = JSONObject(jsonString)

    val access = jsonObject.getJSONArray("access").let { array ->
        List(array.length()) { array.getInt(it)}
    }
    val accessToken = jsonObject.getString("accessToken")
    val status = jsonObject.getString("status")

    val userJsonString = jsonObject.getString("user")
    val userJsonObject = JSONObject(userJsonString)

    val user = User(
        fullName = userJsonObject.getString("fio"),
        userId = userJsonObject.getInt("id_user"),
        status = userJsonObject.getString("status"),
        fullNameId = userJsonObject.getInt("id_fio"),
        key = userJsonObject.getString("key"),
        group = userJsonObject.getString("group"),
        groupId = userJsonObject.getString("id_group"),
        isFullTime = userJsonObject.getInt("denne"),
        facultyCode = userJsonObject.getInt("kod_faculty")
    )

    return AccessResponse(
        access = access,
        accessToken = accessToken,
        status = status,
        user = user
    )
}
