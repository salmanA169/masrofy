package com.masrofy.core.backup

import com.masrofy.model.Account
import com.masrofy.model.Transaction

data class BackupDataModel(
    val account: Account,
    val transactions:List<Transaction>
)
