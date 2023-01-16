package com.masrofy.di

import com.masrofy.repository.AccountRepository
import com.masrofy.repository.AccountRepositoryImpl
import com.masrofy.repository.TransactionRepository
import com.masrofy.repository.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTransactionRepository(transactionRepository: TransactionRepositoryImpl): TransactionRepository

    @Binds
    abstract fun bindAccountRepository(accountRepository: AccountRepositoryImpl):AccountRepository
}