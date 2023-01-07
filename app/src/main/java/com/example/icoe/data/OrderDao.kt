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
package com.example.icoe.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface OrderDao {

    @Query("SELECT * from `Order` ORDER BY id ASC")
    fun getOrders(): Flow<List<Order>>

    @Query("SELECT * from `Order` ORDER BY order_total ASC LIMIT 10")
    fun getTop(): Flow<List<Order>>

    @Query("SELECT * from `Order` WHERE order_base = 'Cup' ORDER BY id ASC")
    fun getOrderCup(): Flow<List<Order>>

    @Query("SELECT * from `Order` WHERE order_base = 'Cone' ORDER BY id ASC")
    fun getOrderCone(): Flow<List<Order>>

    @Query("SELECT * from `Order` ORDER BY order_quantity ASC")
    fun getOrderQuantity(): Flow<List<Order>>

    @Query("SELECT * from `Order` WHERE id = :id")
    fun getOrder(id: Int): Flow<Order>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(order: Order)

    @Update
    suspend fun update(order: Order)

    @Delete
    suspend fun delete(order: Order)
}
