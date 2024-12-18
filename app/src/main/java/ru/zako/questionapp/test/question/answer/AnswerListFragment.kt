package ru.zako.questionapp.test.question.answer

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.zako.questionapp.R
import ru.zako.questionapp.api.RetrofitClient
import ru.zako.questionapp.api.test.question.answer.AnswerDTO
import ru.zako.questionapp.api.test.question.answer.CreateAnswerRequest

class AnswerListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var answerList = mutableListOf<AnswerDTO>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_answer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewAnswers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AnswerAdapter(answerList)

        val questionId = arguments?.getLong("questionId") ?: return
        loadAnswers(questionId)

        val createQuestionButton: Button = view.findViewById(R.id.buttonCreateAnswer)
        createQuestionButton.setOnClickListener {
            showAddAnswerDialog()
        }
    }

    private fun showAddAnswerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_answer, null)
        AlertDialog.Builder(requireContext())
            .setTitle("Добавить ответ")
            .setView(dialogView)
            .setPositiveButton("Создать") { _, _ ->
                val text = dialogView.findViewById<EditText>(R.id.editAnswerText).text.toString()
                val correct = dialogView.findViewById<Switch>(R.id.correctAnswer).isChecked

                createAnswer(text, correct)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun createAnswer(text: String, correct: Boolean) {
        lifecycleScope.launch {
            try {
                arguments?.getLong("questionId")?.let { it ->
                    val response = RetrofitClient.answerApi.create(CreateAnswerRequest(it, text, correct))
                    if (response.isSuccessful) {
                        response.body()?.data?.let { answer ->
                            answerList.add(answer)
                            recyclerView.adapter?.notifyDataSetChanged()
                            Toast.makeText(requireContext(), "Answer created " + response.code(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Failed to create answer: " + response.code(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error create answer", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun loadAnswers(questionId: Long) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.answerApi.getAll(questionId)
                if (response.isSuccessful) {
                    val answers: Collection<AnswerDTO> = response.body()?.data ?: emptyList()
                    if (answers.isEmpty()) {
                        Toast.makeText(requireContext(), "No answers available", Toast.LENGTH_SHORT).show()
                    } else {
                        answerList.clear()
                        answerList.addAll(answers)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load answers: " + response.code(), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error loading answers", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        fun newInstance(questionId: Long): AnswerListFragment {
            val fragment = AnswerListFragment()
            val args = Bundle().apply {
                putLong("questionId", questionId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
