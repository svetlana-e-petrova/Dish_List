package com.example.dishlist.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.dishlist.R
import com.example.dishlist.data.Dish
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox

class DishItemView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    MaterialCardView(context, attrs, defStyleAttr) {

    // todo: use binding for get views
    private val dishCardView by lazy { findViewById<MaterialCardView>(R.id.dish_item_card_view) }
    private val dishItemName by lazy { findViewById<TextView>(R.id.dish_item_name) }
    private val dishItemPrice by lazy { findViewById<TextView>(R.id.dish_item_price) }
    private val dishImage by lazy { findViewById<ImageView>(R.id.dish_item_image) }
    private val checkBox by lazy { findViewById<MaterialCheckBox>(R.id.dish_item_check_box) }

    fun populate(
        model: Dish,
        checkDishAction: (String, Boolean) -> Unit,
        transitionAction: (Dish, View) -> Unit
    ) {
        dishImage.transitionName = model.id
        dishItemName.text = model.name
        dishItemPrice.text = context.getString(R.string.price_placeholder, model.price)
        checkBox.isChecked = model.isChecked
        checkBox.setOnClickListener { _ ->
            checkDishAction(model.id, checkBox.isChecked)
        }

        Glide.with(context)
            .load(model.image)
            .placeholder(R.drawable.alpha_dish_placeholder)
            .into(dishImage)

        dishCardView.setOnClickListener {
            transitionAction(model, dishImage)
        }
    }
}