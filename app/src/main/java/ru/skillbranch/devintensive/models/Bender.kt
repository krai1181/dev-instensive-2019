package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val extra = validation(answer)
        if (!extra.isNullOrEmpty()) {
            return "$extra ${question.question}" to status.color
        }
        if (question == Question.IDLE){
            return question.question to status.color
        }
        return if (question.answer.contains(answer.toLowerCase())) {

            question = question.nextQuestion()
            "Отлично - ты справился \n ${question.question}" to status.color

        } else {
            if (status != Status.CRITICAL) {
                status = status.nextStatus()
                "Это не правильный ответ\n ${question.question}" to status.color
            } else {
                status = Status.NORMAL
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color

            }
        }


    }

    private fun validation(answer: String): String? {
        return when (question) {
            Question.NAME -> {
                if (!answer.first().isUpperCase()) "Имя должно начинаться с заглавной буквы \n" else null
            }
            Question.PROFESSION -> if (!answer.first().isLowerCase()) "Профессия должна начинаться со строчной буквы\n" else null
            Question.MATERIAL -> if (answer.matches(".*\\d+.*".toRegex())) "Материал не должен содержать цифр \n" else null
            Question.BDAY -> if(!answer.matches("[0-9]+".toRegex())) "Год моего рождения должен содержать только цифры\n" else null
            Question.SERIAL -> if (!answer.matches("[0-9]+".toRegex()).and(answer.length == 7))
                "Серийный номер содержит только цифры, и их 7\n" else null
            Question.IDLE -> null

        }
    }


    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex)
                values()[this.ordinal + 1]
            else
                values()[0]
        }
    }

    enum class Question(val question: String, val answer: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }
}