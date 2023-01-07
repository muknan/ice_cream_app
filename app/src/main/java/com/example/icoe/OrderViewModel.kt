package com.example.icoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.icoe.data.Order
import com.example.icoe.data.OrderDao
import kotlinx.coroutines.launch

class OrderViewModel(private val orderDao: OrderDao) : ViewModel() {

    // Cache all courses form the database using LiveData.
    val allOrders: LiveData<List<Order>> = orderDao.getOrders().asLiveData()
    val topOrders: LiveData<List<Order>> = orderDao.getTop().asLiveData()
    val coneOrders: LiveData<List<Order>> = orderDao.getOrderCone().asLiveData()

    fun addNewOrder(orderQuantity: String, orderBase: String, orderTotal: String) {
        val newOrder = getNewOrderEntry(orderQuantity, orderBase, orderTotal)
        insertOrder(newOrder)
    }

    /**
     * Launching a new coroutine to insert an course in a non-blocking way
     */
    private fun insertOrder(order: Order) {
        viewModelScope.launch {
            orderDao.insert(order)
        }
    }

    /**
     * Launching a new coroutine to delete an course in a non-blocking way
     */
    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            orderDao.delete(order)
        }
    }

    /**
     * Retrieve an course from the repository.
     */
    fun retrieveOrder(id: Int): LiveData<Order> {
        return orderDao.getOrder(id).asLiveData()
    }

    fun updateOrder(
        orderId: Int,
        orderQuan: String,
        orderBase: String,
        orderTotal: String
    ) {
        val updatedOrder = getUpdatedOrderEntry(orderId, orderQuan, orderBase, orderTotal)
        updateOrder(updatedOrder)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderDao.update(order)
        }
    }

    fun isEntryValid(orderQuan: String, orderBase: String, orderTotal: String): Boolean {
        if (orderQuan.isBlank() || orderBase.isBlank() || orderTotal.isBlank()) {
            return false
        }
        return true
    }

    private fun getUpdatedOrderEntry(
        orderId: Int,
        orderQuan: String,
        orderBase: String,
        orderTotal: String
    ): Order {
        return Order(
            id = orderId,
            orderQuan = orderQuan,
            orderBase = orderBase,
            orderTotal = orderTotal
        )
    }

    private fun getNewOrderEntry(orderQuan: String, orderBase: String, orderTotal: String): Order {
        return Order(
            orderQuan = orderQuan,
            orderBase = orderBase,
            orderTotal = orderTotal
        )
    }
}

class OrderViewModelFactory(private val orderDao: OrderDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}