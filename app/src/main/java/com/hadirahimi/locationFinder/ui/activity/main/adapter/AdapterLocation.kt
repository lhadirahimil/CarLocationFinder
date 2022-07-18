package com.hadirahimi.locationFinder.ui.activity.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.databinding.ItemLocationBinding
import javax.inject.Inject

class AdapterLocation @Inject constructor() : RecyclerView.Adapter<AdapterLocation.MyViewHolder>()
{
    private lateinit var binding : ItemLocationBinding
    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : MyViewHolder
    {
        binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        
        return MyViewHolder(binding.root)
    }
    
    override fun onBindViewHolder(holder : MyViewHolder , position : Int)
    {
        holder.setData(differ.currentList[position])
        
    }
    
    override fun getItemCount() : Int = differ.currentList.size
    
    private val diffUtil = object : DiffUtil.ItemCallback<ModelDataBase>()
    {
        override fun areItemsTheSame(oldItem : ModelDataBase , newItem : ModelDataBase) : Boolean
        {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem : ModelDataBase , newItem : ModelDataBase) : Boolean
        {
            return oldItem == newItem
        }
        
    }
    val differ = AsyncListDiffer(this , diffUtil)
    
    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun setData(location : ModelDataBase)
        {
            with(location) {
                binding.apply {
                    tvAddress.text = address
                    tvDate.text = " تاریخ : ${date.substring(0,10)}"
                    tvTime.text = " زمان : ${date.substring(11,date.length)}"
                    tvAddress.isSelected = true
                }
    
                binding.ivGoToLocation.setOnClickListener {
                    onItemGoToLocationCLickListener?.let {
                        it(location)
                    }
                }
                
                binding.ivDelete.setOnClickListener {
                    onItemDeleteLocationCLickListener?.let {
                        it(location)
                    }
                }
                
                
            }
            
        }
    }
    
    private var onItemGoToLocationCLickListener : ((ModelDataBase) -> Unit?)? = null
    
    fun setOnGoToLocationCLickListener(listener : (ModelDataBase) -> Unit)
    {
        onItemGoToLocationCLickListener = listener
    }
    
    private var onItemDeleteLocationCLickListener : ((ModelDataBase) -> Unit?)? = null
    fun setOnDeleteLocationCLickListener(listener : (ModelDataBase) -> Unit)
    {
        onItemDeleteLocationCLickListener = listener
    }
    
}


