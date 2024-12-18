package ru.zako.questionapp.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.zako.questionapp.R

class TestFragment : Fragment() {

    companion object {
        private const val ARG_TEST_ID = "test_id"

        fun newInstance(testId: Int): TestFragment {
            val fragment = TestFragment()
            val args = Bundle()
            args.putInt(ARG_TEST_ID, testId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.submitTestButton).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ResultTestFragment.newInstance())
                .addToBackStack(null)
                .commit()
            Toast.makeText(requireContext(), "++", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_test, container, false)
    }
}