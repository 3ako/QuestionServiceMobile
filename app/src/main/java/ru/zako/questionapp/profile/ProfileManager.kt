package ru.zako.questionapp.profile

object ProfileManager {
    private var _email: String? = null
    private var _id: Long? = null

    val email: String?
        get() = _email

    val id: Long?
        get() = _id

    fun set(profile: ProfileDTO) {
        _email = profile.email
        _id = profile.id
    }

}