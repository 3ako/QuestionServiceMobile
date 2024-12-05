package ru.zako.questionapp.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.zako.questionapp.R
import ru.zako.questionapp.api.RetrofitClient
import ru.zako.questionapp.api.test.TestDTO
import java.util.Date

class TestListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private var testList = mutableListOf<TestDTO>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        lifecycleScope.launch {
            val response = RetrofitClient.testApi.getAll()
            if (response.code() == 200) {
                if (response.isSuccessful && response.body()?.data != null) {
                    response.body()?.data.let {
                        if (it != null) {
                            testList.clear()
                            testList.addAll(it)
                            updateList()
                        }
                    }
                }
            }
        }
        return inflater.inflate(R.layout.fragment_testlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewTests)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        updateList()
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