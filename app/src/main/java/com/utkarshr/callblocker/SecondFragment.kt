package com.utkarshr.callblocker

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.utkarshr.callblocker.database.RegexType
import com.utkarshr.callblocker.database.SpamNumber
import com.utkarshr.callblocker.database.SpamNumberDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var saveButton: Button
    private lateinit var radioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        saveButton = view.findViewById<Button>(R.id.save_button)
        radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        editText = view.findViewById<EditText>(R.id.editTextPhone)
        setupEditText()
        setupSaveButton()

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

        editText.doOnTextChanged { text, start, before, count ->
            saveButton.isEnabled = !text.isNullOrEmpty()
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val phone: String = editText.text.toString()
            val regexType = when (radioGroup.checkedRadioButtonId) {
                R.id.start_radio -> RegexType.START
                R.id.end_radio -> RegexType.END
                else -> RegexType.MIDDLE
            }

            val regex = when (regexType) {
                RegexType.START -> Regex("$phone\\d")
                RegexType.MIDDLE -> Regex("\\d$phone\\d")
                RegexType.END -> Regex("\\d$phone")
            }

            val spamNumber = SpamNumber(phone, regexType, regex)

//            SpamNumberDatabase.getInstance(requireContext()).dao.insert(spamNumber)


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