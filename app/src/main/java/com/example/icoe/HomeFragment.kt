package com.example.icoe

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.icoe.data.Order
import com.example.icoe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val viewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory(
            (activity?.application as OrderApplication).database
                .OrderDao()
        )
    }

    var counter = 0

    private val navigationArgs: OrderDetailFragmentArgs by navArgs()

    lateinit var order: Order

    lateinit var orderBase: RadioButton

    var base : String = ""

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun base(){
        if(binding.cone.isChecked) {
            orderBase = binding.cone
            base = "cone"
        }
        else if(binding.cup.isChecked){
            orderBase = binding.cup
            base = "cup"
        }
    }

    private fun test(){

        base()

        binding.cone.setOnClickListener()
        {
            binding.iceCreamPic.setImageResource(R.drawable.cone)
        }

        binding.cup.setOnClickListener()
        {
            binding.iceCreamPic.setImageResource(R.drawable.cup)
        }

        binding.addOrder.setOnClickListener()
        {
            if(TextUtils.isEmpty(binding.orderQuan.text.toString()))
            {
                val temp = ++counter
                binding.orderQuan.text = Editable.Factory.getInstance().newEditable(temp.toString())
            }
            else
            {
                val x: Int = binding.orderQuan.text.toString().toInt()
                counter = x
                val temp = ++counter
                binding.orderQuan.text = Editable.Factory.getInstance().newEditable(temp.toString())
            }
        }

        binding.removeOrder.setOnClickListener()
        {
            if(TextUtils.isEmpty(binding.orderQuan.text.toString()))
            {
                val temp = --counter
                if (temp < 1) {
                    counter = 1
                    Toast.makeText(activity, "Order value cannot be zero!", Toast.LENGTH_SHORT).show()
                    binding.orderQuan.text = Editable.Factory.getInstance().newEditable(counter.toString())
                } else {
                    binding.orderQuan.text = Editable.Factory.getInstance().newEditable(temp.toString())
                }
            }
            else
            {
                val y: Int = binding.orderQuan.text.toString().toInt()
                counter = y
                val temp = --counter
                if (temp < 1) {
                    counter = 1
                    Toast.makeText(activity, "Order value cannot be zero!", Toast.LENGTH_SHORT)
                        .show()
                    binding.orderQuan.text = Editable.Factory.getInstance().newEditable(counter.toString())
                } else {
                    binding.orderQuan.text = Editable.Factory.getInstance().newEditable(temp.toString())
                }
            }
        }

        binding.calculate.setOnClickListener()
        {

            if (TextUtils.isEmpty(binding.orderQuan.text.toString()))
            {
                Toast.makeText(activity, "Please enter a value!", Toast.LENGTH_SHORT)
                    .show()
            }
            else
            {
                counter = binding.orderQuan.text.toString().toInt()
            }
            if(counter < 1)
            {
                Toast.makeText(activity, "Has to be more than 0!", Toast.LENGTH_SHORT)
                    .show()
            }
            if (binding.cup.isChecked && counter > 0)
            {
                val cupt = 3.49 * counter
                val t2 = String.format("%.2f", cupt)
                binding.orderTotal.text = "$ ${t2.toString()}"
                addNewOrder()
            }
            else if (binding.cone.isChecked && counter > 0)
            {
                val conet = 3.99 * counter
                val t1 = String.format("%.2f", conet)
                binding.orderTotal.text = "$ ${t1.toString()}"
                addNewOrder()
            }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.orderQuan.text.toString(),
            orderBase.text.toString(),
            binding.orderTotal.text.toString(),
        )
    }

    private fun bind(order: Order) {
        binding.apply {
            orderQuan.setText(order.orderQuan, TextView.BufferType.SPANNABLE)
            orderBase.setText(order.orderBase, TextView.BufferType.SPANNABLE)
            orderTotal.setText(order.orderTotal, TextView.BufferType.SPANNABLE)

            orderView.setOnClickListener { println("hello")
                updateOrder() }
        }
    }

    private fun addNewOrder() {
        test()
        if (isEntryValid()) {
            viewModel.addNewOrder(
                binding.orderQuan.text.toString(),
                orderBase.text.toString(),
                binding.orderTotal.text.toString()
            )
        }
    }

    private fun updateOrder() {
        base()
        if (isEntryValid()) {
            viewModel.updateOrder(
                this.navigationArgs.orderId,
                this.binding.orderQuan.text.toString(),
                orderBase.text.toString(),
                this.binding.orderTotal.text.toString()
            )
            val action = HomeFragmentDirections.actionHomeFragmentToOrderListFragment()
            findNavController().navigate(action)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderView.setOnClickListener { println("hello")
            updateOrder() }
        /*val id = navigationArgs.orderId
        if (id > 0) {
            viewModel.retrieveOrder(id).observe(this.viewLifecycleOwner) { selectedOrder ->
                order = selectedOrder
                bind(order)
            }
        }*/
        test()
            //binding.calculate.setOnClickListener {
               // test()
        //}
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}