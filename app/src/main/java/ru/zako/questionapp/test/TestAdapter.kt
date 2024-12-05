package ru.zako.questionapp.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.zako.questionapp.R
import ru.zako.questionapp.api.test.TestDTO

class TestAdapter(
    private val tests: List<TestDTO>,
    private val onItemClick: (TestDTO) -> Unit
) : RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textTitle)
        val questions: TextView = itemView.findViewById(R.id.textQuestions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_test, parent, false)
        return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val test = tests[position]
        holder.title.text = test.title
        holder.questions.text = "Id: ${test.id}"
        holder.itemView.setOnClickListener { onItemClick(test) }
    }

    override fun getItemCount(): Int = tests.size
}