package ru.zako.questionapp.api.test.question.answer

data class AnswerDTO(val id: Long, val questionId: Long, val text: String, var isCorrect: Boolean) {

}
