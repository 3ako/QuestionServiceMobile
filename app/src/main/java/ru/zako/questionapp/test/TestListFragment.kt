package ru.zako.questionapp.test

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.zako.questionapp.R
import ru.zako.questionapp.api.RetrofitClient
import ru.zako.questionapp.api.test.CreateTestRequest
import ru.zako.questionapp.api.test.TestDTO

class TestListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private var testList = mutableListOf<TestDTO>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val tests = RetrofitClient.testApi.getAll().body()?.data.orEmpty()
                testList.apply {
                    clear()
                    addAll(tests)
                }
                updateList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return inflater.inflate(R.layout.fragment_testlist, container, false)
    }


    private fun showCreateTestDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_test, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Создать тест")
            .setView(dialogView)
            .setPositiveButton("Создать") { _, _ ->
                val titleInput = dialogView.findViewById<EditText>(R.id.editTestTitle)
                val questionsInput = dialogView.findViewById<EditText>(R.id.editTestQuestions)

                val title = titleInput.text.toString()
                val questions = questionsInput.text.toString().toIntOrNull() ?: 0

                createTest(title, questions)
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    private fun startTest() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_start_test, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Создать тест")
            .setView(dialogView)
            .setPositiveButton("Начать") { _, _ ->
                val titleInput = dialogView.findViewById<EditText>(R.id.testId)

                val id = titleInput.text.toString().toInt()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, TestFragment.newInstance(id))
                    .addToBackStack(null)
                    .commit()
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    private fun createTest(title: String, questions: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.testApi.create(CreateTestRequest(title, questions))
                if (response.isSuccessful) {
                    response.body()?.data?.let { createdTest ->
                        testList.add(createdTest)
                        updateList()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to create test", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val createTestButton: Button = view.findViewById(R.id.buttonCreateTest)
        createTestButton.setOnClickListener {
            showCreateTestDialog()
        }
        recyclerView = view.findViewById(R.id.recyclerViewTests)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val startId = view.findViewById<Button>(R.id.start)
        startId.setOnClickListener {
            startTest()
        }
    }

    private fun updateList() {
        recyclerView.adapter = TestAdapter(this.testList) { test ->
            val fragment = TestDetailFragment.newInstance(
                test.id,
                test.title,
                test.amountQuestions,
                test.createDate.time
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}