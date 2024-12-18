package ru.zako.questionapp.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.zako.questionapp.R


class ResultTestFragment : Fragment() {

    companion object {

        fun newInstance(): ResultTestFragment {
            val fragment = ResultTestFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.result_test, container, false)
    }
}