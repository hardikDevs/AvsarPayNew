package com.itcc.avasarpay.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 * Created By Sunny on 20-05-2024.
 */
class DecimalDigitsInputFilter : InputFilter {
    var mPattern: Pattern = Pattern.compile("[0-9]*+((\\.[0-9]?)?)||(\\.)?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher = mPattern.matcher(dest)
        if (!matcher.matches()) return ""
        return null
    }
}
