package com.masrofy.model

import com.masrofy.data.entity.TransactionEntity
import java.math.BigDecimal
import java.time.LocalDateTime

data class Account(
    val id :Int,
    val name:String,
    val type: CategoryAccount,
    val totalAmount:Long,
    val createdAt:LocalDateTime,
    val transactions:List<TransactionEntity>
)
