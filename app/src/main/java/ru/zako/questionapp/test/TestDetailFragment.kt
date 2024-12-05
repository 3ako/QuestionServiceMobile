package ru.zako.questionapp.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.zako.questionapp.R
import java.util.Date

class TestDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = view.findViewById<TextView>(R.id.textDetailTitle)
        val questionsTextView = view.findViewById<TextView>(R.id.textDetailQuestions)
        val dateTextView = view.findViewById<TextView>(R.id.textDetailDate)

        if (titleTextView != null && questionsTextView != null && dateTextView != null) {
            val testTitle = arguments?.getString("test_title") ?: "Unknown"
            val testQuestions = arguments?.getInt("test_questions") ?: 0
            val testDate = Date(arguments?.getLong("test_date") ?: 0)

            titleTextView.text = testTitle
            questionsTextView.text = "Questions: $testQuestions"
            dateTextView.text = "Date: $testDate"
        } else {
            Log.e("TestDetailFragment", "TextView is null!")
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
