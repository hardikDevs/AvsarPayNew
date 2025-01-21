package com.itcc.stonna.utils

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itcc.avasarpay.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.regex.Pattern


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}
 fun hideViews(vararg views: View) {
    views.forEach { it.hide() }
}
fun EditText.getValue(): String {
    return this.text.toString().trim()
}

fun TextView.getValue(): String {
    return this.text.toString().trim()
}

fun EditText.isEmpty(): Boolean {
    return this.text.trim().isEmpty()
}

fun TextView.isEmpty(): Boolean {
    return this.text.isNullOrEmpty()
}
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

inline fun <reified T : java.io.Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}
inline fun <reified T> SharedPreferences.addItemToList(spListKey: String, item: T) {
    val savedList = getList<T>(spListKey).toMutableList()
    savedList.add(item)
    val listJson = Gson().toJson(savedList)
    edit { putString(spListKey, listJson) }
}

inline fun <reified T> SharedPreferences.removeItemFromList(spListKey: String, item: T) {
    val savedList = getList<T>(spListKey).toMutableList()
    savedList.remove(item)
    val listJson = Gson().toJson(savedList)
    edit {
        putString(spListKey, listJson)
    }
}

fun <T> SharedPreferences.putList(spListKey: String, list: List<T>) {
    val listJson = Gson().toJson(list)
    edit {
        putString(spListKey, listJson)
    }
}

inline fun <reified T> SharedPreferences.getList(spListKey: String): List<T> {
    val listJson = getString(spListKey, "")
    if (!listJson.isNullOrBlank()) {
        val type = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson(listJson, type)
    }
    return listOf()
}

fun View.showSnackBar(context: Context,msg: String, duration: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, msg, duration)
    val sbView = snack.view
    snack.setBackgroundTint(ContextCompat.getColor(context, R.color.white))
    val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.setTextColor(Color.BLACK)
    snack.show()
}

@RequiresApi(Build.VERSION_CODES.O)
fun progress(start: LocalDateTime,
             end: LocalDateTime,
             now: LocalDateTime = LocalDateTime.now()): Double = when {
    now <= start -> 0.0
    now >= end -> 1.0
    else -> start.until(now, ChronoUnit.SECONDS) /
            start.until(end, ChronoUnit.SECONDS).toDouble()
}


fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

/**
 * setDrawableColor
 * @param color
 * @return
 */
fun TextView.setDrawableColor(@ColorRes color: Int) {
    //  context.setDrawableTintColor(color)
    // this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock, 0, 0, 0)
}



fun Context.changeDrawableColor(drawable: Int, color: Int): Drawable {
    val coloredDrawable =
        AppCompatResources.getDrawable(this, drawable)
    val wrappedDrawable = DrawableCompat.wrap(coloredDrawable!!)
    DrawableCompat.setTint(
        wrappedDrawable,
        ContextCompat.getColor(this, color)
    )
    return coloredDrawable

}

/**
 * setDrawableTintColor
 * @param color
 * @return
 */
fun Context.setDrawableTintColor(@ColorRes color: Int, @DrawableRes icon: Int) {
    val unwrappedDrawable = AppCompatResources.getDrawable(this, icon)
    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
    DrawableCompat.setTint(
        wrappedDrawable, ContextCompat.getColor(
            this,
            color
        )
    )
}
fun Calendar.isSameDay(newDate: Calendar): Boolean =
    this.get(Calendar.DAY_OF_MONTH) == newDate.get(Calendar.DAY_OF_MONTH)

fun Calendar.isSameMonth(newDate: Calendar): Boolean =
    this.get(Calendar.MONTH) == newDate.get(Calendar.MONTH)



var dialog: AlertDialog? = null
fun AppCompatActivity.showAlert(msg: String, cancelable: Boolean = false) {
    dialog = AlertDialog.Builder(this)
        .setMessage(msg)
        .setCancelable(cancelable)
        .setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which -> dialog.dismiss() }
        .create()
    dialog?.show()
}

fun RecyclerView.enableClickListener(){
    val gesture = object : GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            this@enableClickListener.performClick()
            return super.onSingleTapConfirmed(e)
        }
    }
    val detector = GestureDetector(this.context, gesture)
    this.setOnTouchListener { v, event -> detector.onTouchEvent(event) }
}


fun Fragment.showAlert(msg: String, cancelable: Boolean = false) {
    dismissAlertDialog()
    dialog = AlertDialog.Builder(context)
        .setTitle(context?.getString(R.string.app_name))
        .setMessage(msg)
        .setCancelable(cancelable)
        .setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which -> dialog.dismiss() }
        .create()
    dialog?.show()
}

fun dismissAlertDialog() {
    dialog?.dismiss()
}



fun String.toHtml(): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        return Html.fromHtml(this)
    }
}





fun isActivityForIntentAvailable(
    context: Context,
    intent: Intent?
): Boolean {
    val packageManager = context.packageManager
    val list: List<*> =
        packageManager.queryIntentActivities(intent!!, PackageManager.MATCH_DEFAULT_ONLY)
    return list.size > 0
}


fun convertIntoTowDigit(value: Int): String {
    var finalValue = value.toString()
    if (value < 10) {
        finalValue = "0" + value.toString()
    }

    return finalValue
}
fun isValidPasswordFormat(password: String): Boolean {
    val passwordREGEX = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$");
    return passwordREGEX.matcher(password).matches()
}

fun isValidEmail(target: CharSequence): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
fun createPartFromString(stringData: String): RequestBody {
    return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
}

inline val AppCompatActivity.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


 fun openFile(url: String, context: Context) {
    try {

        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW)
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword")
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf")
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel")
        } else if (url.toString().contains(".zip")) {
            // ZIP file
            intent.setDataAndType(uri, "application/zip")
        } else if (url.toString().contains(".rar")) {
            // RAR file
            intent.setDataAndType(uri, "application/x-rar-compressed")
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf")
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav")
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif")
        } else if (url.toString().contains(".jpg") || url.toString()
                .contains(".jpeg") || url.toString().contains(".png")
        ) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg")
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain")
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
            url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString()
                .contains(".mp4") || url.toString().contains(".avi")
        ) {
            // Video files
            intent.setDataAndType(uri, "video/*")
        } else {
            intent.setDataAndType(uri, "*/*")
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT)
            .show()
    }
}
/*fun openCustomChromeTab(context: Context, url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}*/
 fun Intent.getClipDataUris(): ArrayList<Uri> {
    val resultSet = LinkedHashSet<Uri>()
    data?.let { data ->
        resultSet.add(data)
    }
    val clipData = clipData
    if (clipData == null && resultSet.isEmpty()) {
        return ArrayList()
    } else if (clipData != null) {
        for (i in 0 until clipData.itemCount) {
            val uri = clipData.getItemAt(i).uri
            if (uri != null) {
                resultSet.add(uri)
            }
        }
    }
    return ArrayList(resultSet)
}

@Suppress("DEPRECATION")
inline fun <reified T: Parcelable>Intent.getParcelableExtraProvider(identifierParameter: String): T? {

    return if (Build.VERSION.SDK_INT >= 33) {
        this.getParcelableExtra(identifierParameter, T::class.java)
    } else {
        this.getParcelableExtra(identifierParameter)
    }

}

@Suppress("DEPRECATION")
inline fun <reified T: Parcelable>Intent.getParcelableArrayListExtraProvider(identifierParameter: String): java.util.ArrayList<T>? {

    return if (Build.VERSION.SDK_INT >= 33) {
        this.getParcelableArrayListExtra(identifierParameter, T::class.java)
    } else {
        this.getParcelableArrayListExtra(identifierParameter)
    }

}

@Suppress("DEPRECATION")
inline fun <reified T: java.io.Serializable>Intent.getSerializableExtraProvider(identifierParameter: String): T? {

    return if (Build.VERSION.SDK_INT >= 33) {
        this.getSerializableExtra(identifierParameter, T::class.java)
    } else {
        this.getSerializableExtra(identifierParameter) as T?
    }

}