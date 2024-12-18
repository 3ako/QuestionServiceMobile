package ru.zako.questionapp.api.test.question

data class CreateQuestionRequest(val testId: Long, val text: String, val maxTry: Int) {
}