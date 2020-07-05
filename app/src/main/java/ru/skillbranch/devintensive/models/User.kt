package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User private constructor(
    val id: String,
    var firstName : String?,
    var lastName : String?,
    var avatar : String?,
    var rating : Int = 0,
    var respect : Int = 0,
    val lastVisit : Date? = Date(),
    var isOnline : Boolean = false

) {

    constructor(id: String) : this(id, null, null, null, 0, 0, null, false)
    constructor(id: String, firstName: String?, lastName: String?) : this(
        id,
        firstName,
        lastName,
        null,
        0,
        0,
        null,
        false
    )


    init {
        print(
            "It is Alive! \nHis full name is " +
                    "${if (firstName.isNullOrEmpty().or(lastName.isNullOrEmpty())) "empty "
                    else "$firstName $lastName"} \n"
        )
    }

    data class Builder(
        var id: String = "0",
        var firstName: String? = null,
        var lastName: String? = null,
        var avatar: String? = null,
        var rating: Int = 0,
        var respect: Int = 0,
        var lastVisit: Date? = Date(),
        var isOnline: Boolean = false
    ) {
        fun id(id: String) = apply { this.id = id }
        fun firstName(firstName: String) = apply { this.firstName = firstName }
        fun lastName(lastName: String) = apply { this.lastName = lastName }
        fun avatar(avatar: String) = apply { this.avatar = avatar }
        fun rating(rating: Int) = apply { this.rating = rating }
        fun respect(respect: Int) = apply { this.respect = respect }
        fun lastVisit(lastVisit: Date) = apply { this.lastVisit = lastVisit }
        fun isOnline(isOnline: Boolean) = apply { this.isOnline = isOnline }
        fun build() = User(id, firstName, lastName, avatar, rating, respect, lastVisit, isOnline)
    }


    companion object Factory {
        private var userId = -1
        fun makeUser(fullName: String?): User {
            userId++
            return when {
                fullName.isNullOrEmpty() -> User("$userId")
                else -> {
                    val (firstName, lastName) = Utils.parseFullName(fullName)
                    User("$userId", firstName, lastName)
                }

            }
        }


    }


}