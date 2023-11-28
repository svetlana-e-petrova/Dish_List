package com.example.dishlist.presentation.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dishlist.R
import com.example.dishlist.databinding.FragmentDishDetailsBinding

class DishDetailsFragment : Fragment() {

    private var _binding: FragmentDishDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DishDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDishDetailsBinding.inflate(layoutInflater, container, false)
        sharedElementReturnTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model = args.dish
        binding.dishDetailsImage.transitionName = model.id

        binding.dishDetailsName.text = model.name
        binding.dishDetailsDescription.text = model.description
        binding.dishDetailsPrice.text = requireContext().getString(R.string.price_placeholder, model.price)

        Glide.with(requireContext())
            .load(model.image)
            .into(binding.dishDetailsImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}