package com.example.dishlist.presentation.utils

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addDishItemDecoration(drawable: Drawable?) {
    val verticalDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    val horizontalDecorator = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)

    drawable?.let {
        verticalDecorator.setDrawable(it)
        horizontalDecorator.setDrawable(it)
    }

    addItemDecoration(verticalDecorator)
    addItemDecoration(horizontalDecorator)
}