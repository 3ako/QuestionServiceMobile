package ru.zako.questionapp.api.test.question

import java.util.Date

data class QuestionDTO(val id: Long, val text: String, val testId: Long, val maxTry: Int, val createDate: Date){
}