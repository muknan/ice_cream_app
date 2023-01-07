/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.icoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.icoe.data.Order
import com.example.icoe.databinding.FragmentOrderDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OrderDetailFragment : Fragment() {
    private val navigationArgs: OrderDetailFragmentArgs by navArgs()
    lateinit var order: Order

    private val viewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(
            (activity?.application as OrderApplication).database.OrderDao()
        )
    }

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(order: Order) {
        binding.apply {
            orderQuan.text = order.orderQuan
            orderBase.text = order.orderBase
            orderTotal.text = order.orderTotal
            deleteBtn.setOnClickListener { showConfirmationDialog() }
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteOrder()
            }
            .show()
    }

    private fun deleteOrder() {
        viewModel.deleteOrder(order)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeBtn.setOnClickListener(){val action = OrderDetailFragmentDirections.actionOrderDetailFragmentToHomeFragment("test")
            findNavController().navigate(action)}
        val id = navigationArgs.orderId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveOrder(id).observe(this.viewLifecycleOwner) { selectedItem ->
            order = selectedItem
            bind(order)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}