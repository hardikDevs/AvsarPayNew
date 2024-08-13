package com.itcc.avasarpay.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.itcc.avasarpay.R

import com.itcc.avasarpay.dialog.CustomProgressDialog
import com.itcc.stonna.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by Hardik
 */
@AndroidEntryPoint
open class BaseFragment : Fragment() {
    var mContext: Context? = null
    lateinit var session: SessionManager
    var progressDialog: CustomProgressDialog? = null
    var snackbar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        session = SessionManager(context)

    }

    fun showProgressbar() {
        showProgressbar(null)
    }

    fun showProgressbar(message: String? = getString(R.string.please_wait)) {
        hideProgressbar()
        progressDialog = mContext?.let { CustomProgressDialog(it, message) }
        progressDialog?.show()
    }

    fun hideProgressbar() {
        if (progressDialog != null && progressDialog?.isShowing!!) progressDialog!!.dismiss()
        progressDialog=null
    }

    fun showSoftKeyboard(view: EditText) {
        view.requestFocus(view.text.length)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            return false
        }
    }


    fun showSnackbar(view: View?, msg: String, bgColor:Int, textColor:Int) {
        if (view == null) return
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        val sbView = snackbar?.view
        sbView?.setBackgroundColor(ContextCompat.getColor(mContext!!, bgColor))
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(mContext!!, textColor))
        snackbar?.show()
    }


    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        progressDialog =null
        mContext=null
        snackbar=null

    }

    override fun onDestroy() {
        snackbar?.dismiss()
        hideProgressbar()
        super.onDestroy()
    }
}