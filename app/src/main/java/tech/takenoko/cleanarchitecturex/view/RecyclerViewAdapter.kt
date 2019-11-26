package tech.takenoko.cleanarchitecturex.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tech.takenoko.cleanarchitecturex.databinding.RecyclerItemBinding

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.BindingHolder>() {
    private var list: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.binding.text = list[position]
    }

    fun setItem(newList: List<String>) {
        DiffUtil.calculateDiff(Callback(this.list, newList), true).also {
            this.list.clear()
            this.list.addAll(newList)
        }.dispatchUpdatesTo(this)
    }

    class BindingHolder(var binding: RecyclerItemBinding): RecyclerView.ViewHolder(binding.root)

    class Callback(private val old: List<String>, private val new: List<String>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = old.size
        override fun getNewListSize(): Int = new.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition] == new[newItemPosition]
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = old[oldItemPosition] == new[newItemPosition]
    }
}

