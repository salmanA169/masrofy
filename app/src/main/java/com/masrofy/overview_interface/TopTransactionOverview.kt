package com.masrofy.overview_interface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.masrofy.R
import com.masrofy.model.TopTransactions

class TopTransactionOverview(override val data: List<TopTransactions>):BaseOverView<List<TopTransactions>> {
    override fun getIcon(): Int {
        return R.string.top_transactions
    }

    override fun getLabel(): Int {
        return R.drawable.statistic_icon1
    }

    @Composable
    override fun GetContent(modifier: Modifier) {
        BaseOverViewScreen(modifier = modifier){

        }
    }


}

