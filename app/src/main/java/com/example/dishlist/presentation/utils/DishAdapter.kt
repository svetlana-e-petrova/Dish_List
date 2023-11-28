package com.example.dishlist.presentation.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dishlist.R
import com.example.dishlist.data.Dish
import com.example.dishlist.presentation.view.DishItemView

class DishAdapter(
    private val checkDishAction: (String, Boolean) -> Unit,
    private val transitionAction: (Dish, View) -> Unit
) : ListAdapter<Dish, DishAdapter.DishViewHolder>(DishDiffUtilCallback()) {

    private var items = ArrayList<Dish>()

    class DishViewHolder(private val view: DishItemView) : RecyclerView.ViewHolder(view) {

        fun bind(
            item: Dish,
            checkDishAction: (String, Boolean) -> Unit,
            transitionAction: (Dish, View) -> Unit
        ) {
            adapterPosition
            view.populate(item, checkDishAction, transitionAction)
        }
    }

    fun populateData(list: List<Dish>) {
        items = ArrayList(list)
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.dish_item, parent, false) as DishItemView
        return DishViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.bind(items[position], checkDishAction, transitionAction)
    }
}

class DishDiffUtilCallback : DiffUtil.ItemCallback<Dish>() {

    override fun areItemsTheSame(oldItem: Dish, newItem: Dish) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Dish, newItem: Dish) = oldItem == newItem

    override fun getChangePayload(oldItem: Dish, newItem: Dish): Any? {
        return true.takeIf { oldItem.isChecked != newItem.isChecked }
    }
}