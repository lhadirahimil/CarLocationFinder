package com.hadirahimi.locationFinder.ui.Dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.hadirahimi.locationFinder.databinding.DialogConfirmDeleteBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DialogConfirmDelete @Inject constructor(@ActivityContext context : Context) : Dialog(context)
{
    private lateinit var binding : DialogConfirmDeleteBinding
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        
        binding = DialogConfirmDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initStyle()
        clickListener()
    }
    
    private fun clickListener()
    {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnDelete.setOnClickListener {
            onDeleteClickListener.let {
                it?.let { it1 -> it1() }
            }
        }
    }
    
    private var onDeleteClickListener : (() -> Unit?)? = null
    fun setOnDeleteClickListener(listener : () -> Unit)
    {
        onDeleteClickListener = listener
    }
    
    private fun initStyle()
    {
        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        window?.setLayout(width , ConstraintLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    
    
}