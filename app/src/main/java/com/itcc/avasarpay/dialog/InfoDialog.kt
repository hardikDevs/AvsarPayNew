package com.itcc.avasarpay.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.itcc.avasarpay.R
import com.itcc.avasarpay.databinding.DialogInfoBinding

import com.itcc.avasarpay.utils.AppConstant



class InfoDialog : DialogFragment(), LifecycleOwner {

    private var _binding: DialogInfoBinding? = null

    private val binding get() = _binding!!

    companion object {
      var listener: OnItemClick?=null
        fun newInstance(
            listeners: OnItemClick
        ): InfoDialog {
            this.listener = listeners
            return InfoDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomAlertDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)

        _binding?.imgClose?.setOnClickListener {
            listener?.onItemCLicked()
            dismissAllowingStateLoss()
        }


    }

    private fun populateData() {
        val bundle = arguments
        if (bundle != null) {
            val title = bundle.getString(AppConstant.TITLE)
            val msg = bundle.getString(AppConstant.DATA)
            _binding?.tvTitle?.text = title
            _binding?.tvDesc?.text = msg

        }
    }

    interface OnItemClick {
        fun onItemCLicked()
    }



    override fun onDestroyView() {
        listener =null
        _binding=null
        super.onDestroyView()

    }
}








