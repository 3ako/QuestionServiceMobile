package ru.zako.questionapp.test.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zako.questionapp.R
import ru.zako.questionapp.api.test.question.QuestionDTO

class QuestionAdapter(
    private val questions: List<QuestionDTO>,
    private val onQuestionClick: (QuestionDTO) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.textQuestion)

        fun bind(question: QuestionDTO) {
            questionText.text = question.text
            itemView.setOnClickListener { onQuestionClick(question) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size
}
