package com.example.mywebview.ui.screens.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mywebview.App
import com.example.mywebview.ui.MainActivity
import com.example.mywebview.R


class PolicyFragment : Fragment() {

    lateinit var btnYes: Button
    lateinit var btnNo: Button
    lateinit var mainActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnYes = view.findViewById(R.id.btnYes)
        btnNo = view.findViewById(R.id.btnNo)

        btnYes.setOnClickListener {

            App.preferencesManager.saveUserData(true)

            var isInternet = mainActivity.checkForInternet(requireContext())
            mainActivity.checkInternetAndRealisationNavFrag(isInternet)

        }
        btnNo.setOnClickListener {
            mainActivity.finish()
        }
    }
}