package com.utkarshr.callblocker

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    lateinit var editText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        editText = view.findViewById<EditText>(R.id.editTextPhone)
        setupEditText()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupKeyBoard(requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupEditText() {
        editText.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    //NOTE:wait few milliseconds before dismissing
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(200)
                        CoroutineScope(Dispatchers.Main).launch {
                            editText.clearFocus()
                        }
                    }
                }
                else -> {
                    Log.w("TAG", "another action id ${actionId}")
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyBoard(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                try {
                    val inputMethodManager: InputMethodManager = requireActivity().getSystemService(
                        Activity.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
                    editText.clearFocus()
                } catch (e: Exception) {

                }
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView = (view as ViewGroup).getChildAt(i)
                setupKeyBoard(innerView)
            }
        }
    }
}