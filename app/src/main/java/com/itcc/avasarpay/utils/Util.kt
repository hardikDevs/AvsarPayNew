package com.itcc.avasarpay.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textfield.TextInputLayout
import com.itcc.avasarpay.utils.AppConstant.DATE_FORMAT
import com.itcc.avasarpay.utils.AppConstant.DATE_FORMAT_DD_MM_YY
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


object Util {
     fun hideError(textInputLayout: TextInputLayout){
        textInputLayout.error = null
        if (textInputLayout.childCount == 2) textInputLayout.getChildAt(1).visibility = View.GONE;
    }
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

 /*   fun generateCountryListList(context: Context): List<CountryModal> {
        val response = context.assets.readAssetsFile("Country.json")
        return GsonBuilder().create().fromJson(response, Array<CountryModal>::class.java).asList()

    }*/

    /**
     * Method to prevent multiple clicks
     */
    fun preventMultipleClicks(): Boolean {
        // Preventing multiple clicks, using threshold of MULTIPLE_CLICK_THRESHOLD  second
        if (SystemClock.elapsedRealtime() - AppConstant.LAST_CLICK_ITEM < AppConstant.MULTIPLE_CLICK_THRESHOLD) {
            return false
        }
        AppConstant.LAST_CLICK_ITEM = SystemClock.elapsedRealtime()
        return true
    }


    /**
     * Method to set same OnClickListener on multiple views
     *
     * @param listener
     * @param views
     */
    fun setOnClickListener(listener: View.OnClickListener?, vararg views: View) {
        for (view in views) {
            view.setOnClickListener(listener)
        }
    }

    fun underLineDraw(textView: AppCompatTextView){
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }
    fun jsonData(json: String): String {
        var error = "null"
        try {
            val obj = JSONObject(json)
            val keys = obj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = obj[key]
                error = if (value is JSONArray)
                    obj.getJSONArray(key).join(",").replace("\"".toRegex(), "")
                else

                    obj.getString(key)

            }
        } catch (e: JSONException) {
            return "Request failed with error code: 400 Bad Request"
        }

        return error


    }
    fun getLocalDate(date: String): String {
        var convertDate = ""
        try {
            val f: DateFormat = SimpleDateFormat(AppConstant.DATE_FORMAT)
            val d: Date = f.parse(date)
            val date: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            convertDate = date.format(d).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convertDate

    }

    fun hours24To12(time:String):String{
        var hours =""
       /* try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(time)
            println(dateObj)
            println(SimpleDateFormat("K a").format(dateObj))
            hours=  SimpleDateFormat("K a").format(dateObj)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return  hours*/
        try {
            val sdf = SimpleDateFormat("HH:mm")
            val dateObj = sdf.parse(time)
            hours = SimpleDateFormat("hh:mm aa").format(dateObj)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return hours
    }
    fun getLocalDateAMPM(date: String): String {
        var convertDate = ""
        try {
            val f: DateFormat = SimpleDateFormat(AppConstant.DATE_FORMAT)
            val d: Date = f.parse(date)
            val date: DateFormat = SimpleDateFormat("dd/MM/yyyy hh a")
            convertDate = date.format(d).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convertDate

    }

    fun convertLocalDateToServerDate(date: String): String {
        var convertDate = ""
        try {
            val f: DateFormat = SimpleDateFormat(AppConstant.DATE_FORMAT_DD_MM_YY)
            val d: Date = f.parse(date)
            val date: DateFormat = SimpleDateFormat(DATE_FORMAT)
            convertDate = date.format(d).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convertDate

    }
    fun Uri.getMimeType(mContext: Context): String? {
        val cR: ContentResolver = mContext.contentResolver
        val mime = MimeTypeMap.getSingleton()
        val extension = mime.getExtensionFromMimeType(cR.getType(this))
        return mime.getMimeTypeFromExtension(extension)
    }

    fun millisToFormat(millis: Long): String {
        val timeZone = TimeZone.getDefault()
        return millisToFormat(millis, DATE_FORMAT_DD_MM_YY, timeZone)
    }
    fun millisToHHmmss(millis: Long): String {
        val timeZone = TimeZone.getDefault()
        return millisToFormat(millis, "HH:mm:ss", timeZone)
    }
    fun millisToFormat(milis: Long, format: String, tz: TimeZone): String {
        var millis = milis
        if (millis < 1000000000000L) {
            millis *= 1000
        }
        val cal = Calendar.getInstance(tz)
        cal.timeInMillis = millis
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        sdf.timeZone = tz
        return sdf.format(cal.time)
    }
    fun formatToMilliSeconds(value: String, format: String, timeZone: TimeZone): Long {
        try {
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            sdf.timeZone = timeZone
            val mDate = sdf.parse(value)
            return TimeUnit.MILLISECONDS.toSeconds(mDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0
    }
    fun formatToMilliSecond(value: String, format: String): Long {
        val timeZone = TimeZone.getDefault()
        return formatToMilliSeconds(value, format, timeZone)
    }
    /*fun showInfoAlert(activity: Activity, msg: String, isSuccess:Boolean) {
        val builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)
            .create()

        val view = activity.layoutInflater.inflate(R.layout.dialog_info, null)
        val button = view.findViewById<LinearLayout>(R.id.linConfirm)
        val img = view.findViewById<ImageView>(R.id.img)
        if (isSuccess)  img.setImageResource(R.drawable.ic_tick)
        else img.setImageResource(R.drawable.ic_red_cross)

        val txt = view.findViewById<TextView>(R.id.tvMessage)
        txt.text = msg
        builder.setView(view)
        button.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }*/
    @RequiresApi(Build.VERSION_CODES.O)
    fun printTimeSlots(startTime: LocalTime, endTime: LocalTime?, slotSizeInMinutes: Int) {
        var time = startTime
        var nextTime: LocalTime
        while (time.isBefore(endTime)) {
            nextTime = time.plusMinutes(slotSizeInMinutes.toLong())
            if (nextTime.isAfter(endTime)) break // time slot crosses end time

            println("$time-$nextTime")
            time = nextTime
        }
    }
    fun amountDecimal(number: Double): String? {
        val df = DecimalFormat("#.##")
        return df.format(number).toDouble().toString()
    }

    fun mimeTypeList(): ArrayList<String>{
        val mimeTypeList = ArrayList<String>()

        mimeTypeList.add("application/pdf")
        mimeTypeList.add("application/msword")
        mimeTypeList.add("application/vnd.ms-excel")
        mimeTypeList.add("application/vnd.google-apps.document")
        mimeTypeList.add("application/vnd.google-apps.kix")
        mimeTypeList.add("application/vnd.google-apps.spreadsheet")
        mimeTypeList.add("application/vnd.google-apps.presentation")
        mimeTypeList.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        mimeTypeList.add("application/mspowerpoint")
        mimeTypeList.add("text/plain")
        return  mimeTypeList
    }
}