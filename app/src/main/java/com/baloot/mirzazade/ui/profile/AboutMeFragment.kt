package com.baloot.mirzazade.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.baloot.mirzazade.databinding.FragmentAboutMeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.coordinatorlayout.widget.CoordinatorLayout

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.widget.FrameLayout
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.FragmentComponent

@AndroidEntryPoint
class AboutMeFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAboutMeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (getView()?.parent as View?)?.setBackgroundColor(Color.TRANSPARENT)

        binding.wv.loadUrl("file:///android_asset/my_cv.html")
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState).apply {
            // window?.setDimAmount(0.2f) // Set dim amount here
            setOnShowListener {
                val bottomSheet = findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(R.color.transparent)
            }
        }
    }
    companion object {

    }

}