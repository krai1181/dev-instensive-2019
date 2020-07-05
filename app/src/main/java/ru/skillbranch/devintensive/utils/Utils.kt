package ru.skillbranch.devintensive.utils

import java.util.*


object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        if (fullName.isNullOrBlank()) return null to null
        val parts = fullName.split(" ")
        val firstName = parts.getOrNull(0)
        val lastName = parts.getOrNull(1)
        return firstName to lastName
    }


    fun transliteration(payload: String, divider: String = " "): String {
        val russianAlphabet = listOf('а','б','в','г','д','е','ё','ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ц','ч','ш','щ',
            'ъ','ы','ь','э','ю','я')
        val latinAlphabet = listOf("a","b","v","g","d","e","e","zh","z","i","i","k","l","m","n","o","p","r","s","t","u","f",
            "h","c","ch","sh","sh'","","i","","e","yu","ya")
        val alph = russianAlphabet.zip(latinAlphabet).toMap()

        val pairs = payload.split(divider)
        var firstName = pairs[0].toLowerCase(Locale.getDefault())
        var lastName = pairs[1].toLowerCase(Locale.getDefault())
        for (c in firstName) {
            if (!alph.keys.contains(c)) break
            firstName = firstName.replace(c.toString(), alph[c].toString())
        }
        for (ch in lastName) {
            if (!alph.keys.contains(ch)) break
            lastName = lastName.replace(ch.toString(), alph[ch].toString())
        }

        return "${firstName.capitalize()} ${lastName.capitalize()}".replace(" ", "_")
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return if (firstName.isNullOrBlank().and(lastName.isNullOrBlank())) null
        else "${firstName?.get(0)?.toUpperCase()}${lastName?.get(0)?.toUpperCase()} "
    }



}




