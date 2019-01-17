package com.tungtop.exampekotlinvalidation

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var validatatorForm: ValidatorForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val validatorNickname = TextValidator(text_name)
        text_name.addTextChangedListener(validatorNickname)

        val validatorEmail = TextValidatorEmail(text_email)
        text_email.addTextChangedListener(validatorEmail)

        val validatorNumber = TextValidatorNumber(text_number)
        text_number.addTextChangedListener(validatorNumber)

        val validatorPasswordMatch = ValidatorPasswordMatch(text_password, text_verify_pass)
        text_verify_pass.addTextChangedListener(validatorPasswordMatch)

        val listValidator: Array<TextValidator> = arrayOf(
            validatorNickname,
            validatorEmail,
            validatorPasswordMatch
        )
        validatatorForm = ValidatorForm(listValidator)

        setupHideKeyBoard(findViewById(R.id.parent))
    }

    fun doSubmit(view: View) {
        if (validatatorForm!!.validate()) {
            showMessage("OK", "Successful", "all good")
        } else {
            showMessage("error", "Fail", "Some invalid values")
        }
    }

    fun showMessage(msgType: String, msgTitle: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(msgTitle)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }

        if (msgType == "error") {
            builder.setIcon(android.R.drawable.ic_dialog_alert)
        } else {
            builder.setIcon(android.R.drawable.ic_dialog_info)
        }
        builder.show()
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken, 0
        )
    }

    fun setupHideKeyBoard(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this@MainActivity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupHideKeyBoard(innerView)
            }
        }
    }
}
