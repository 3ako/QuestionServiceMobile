package ru.zako.questionapp.test.question.answer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zako.questionapp.R
import ru.zako.questionapp.api.test.question.answer.AnswerDTO

class AnswerAdapter(private val answers: List<AnswerDTO>) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    class AnswerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textAnswer)
        val switch: Switch = itemView.findViewById(R.id.switchCorrect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_answer, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = answers[position]
        holder.textView.text = answer.text
        holder.switch.isChecked = answer.isCorrect

        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            answer.isCorrect = isChecked
        }
    }

    override fun getItemCount(): Int = answers.size
}
