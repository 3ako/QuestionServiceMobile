package ru.zako.questionapp.test

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.zako.questionapp.R
import ru.zako.questionapp.api.RetrofitClient
import ru.zako.questionapp.api.test.question.CreateQuestionRequest
import ru.zako.questionapp.test.question.QuestionAdapter
import ru.zako.questionapp.test.question.answer.AnswerListFragment
import java.util.Date

class TestDetailFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addQuestionButton: Button = view.findViewById(R.id.buttonAddQuestion)
        addQuestionButton.setOnClickListener { showAddQuestionDialog() }
        recyclerView = view.findViewById(R.id.recyclerViewQuestions)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val titleTextView: TextView = view.findViewById(R.id.textDetailTitle)
        val questionsTextView: TextView = view.findViewById(R.id.textDetailQuestions)
        val dateTextView: TextView = view.findViewById(R.id.textDetailDate)

        titleTextView.text = arguments?.getString("title")
        questionsTextView.text = "Вопросов в одной выдачи: ${arguments?.getInt("questions")}"
        dateTextView.text = "Создан: ${arguments?.getLong("date")?.let { Date(it).toString() }}"

        val id =  arguments?.getLong("test_id")!!
        loadQuestions(id)
    }


    private fun showAddQuestionDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_question, null)
        AlertDialog.Builder(requireContext())
            .setTitle("Добавить вопрос")
            .setView(dialogView)
            .setPositiveButton("Создать") { _, _ ->
                val questionText = dialogView.findViewById<EditText>(R.id.editQuestionText).text.toString()
                val maxTry = dialogView.findViewById<EditText>(R.id.editQuestionMaxTry).text.toString().toIntOrNull() ?: 1

                addQuestion(questionText, maxTry)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun addQuestion(text: String, maxTry: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val testId = arguments?.getLong("test_id") ?: return@launch
                println("testId: ${testId}, text: $text")
                val response = RetrofitClient.questionApi.create(CreateQuestionRequest(testId, text, maxTry))
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Question added", Toast.LENGTH_SHORT).show()
                    loadQuestions(testId)
                } else {
                    Toast.makeText(requireContext(), "Failed to add question: "+ response.code(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error adding question", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadQuestions(testId: Long) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.questionApi.getAll(testId)
                if (response.isSuccessful) {
                    val questions = response.body()?.data ?: emptyList()
                    recyclerView.adapter = QuestionAdapter(questions) { question ->
                        val fragment = AnswerListFragment.newInstance(question.id)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load questions", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error loading questions", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        fun newInstance(id: Long, title: String, questions: Int, date: Long): TestDetailFragment {
            val fragment = TestDetailFragment()
            val args = Bundle().apply {
                putLong("test_id", id)
                putString("test_title", title)
                putInt("test_questions", questions)
                putLong("test_date", date)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
