package com.tungtop.exampekotlinvalidation

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView

/**
 * Textvalidator
 * @property textView The first text.
 * @property textForCompare The second text
 */
open class TextValidator(val textView: TextView) : TextWatcher {
    protected lateinit var textForCompare: TextView

    constructor(textView: TextView, textForCompare: TextView) : this(textView) {
        this.textForCompare = textForCompare
    }

    open fun validate(): Boolean {
        if (textView.text.isEmpty()) {
            textView.error = "Field required"
            return false
        }
        return true
    }

    override fun afterTextChanged(s: Editable) {
        validate()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}

class TextValidatorEmail(textView: TextView) : TextValidator(textView) {

    override fun validate(): Boolean {
        val isValidEmail: Boolean = isValidEmail(textView.text)
        if (!isValidEmail) {
            textView.error = "Email is invalid"
            return false
        }
        return true
    }

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}

class TextValidatorNumber(textView: TextView) : TextValidator(textView) {
    override fun validate(): Boolean {
        val isValidNumber: Int? = textView.text.toString().toIntOrNull()
        if (isValidNumber !== null) {
            return true
        }
        textView.error = "Must be number"
        return false
    }
}

class ValidatorPasswordMatch(textView: TextView, textForCompare: TextView) :
    TextValidator(textView, textForCompare) {
    override fun validate(): Boolean {
        if (textView.text.toString() != textForCompare.text.toString()) {
            textForCompare.error = "Password mismatch"
            return false
        }
        return true
    }
}

class ValidatorForm(private val listValidator: Array<TextValidator>) {

    fun validate(): Boolean {
        var result = true
        for (validator: TextValidator in listValidator) {
            if (!validator.validate()) {
                result = false
            }
        }
        return result
    }
}
