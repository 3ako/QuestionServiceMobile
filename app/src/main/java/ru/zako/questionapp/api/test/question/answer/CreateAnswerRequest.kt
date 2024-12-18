package ru.zako.questionapp.api.test.question.answer

data class CreateAnswerRequest(val questionId: Long, val text: String, val isCorrect: Boolean) {
}