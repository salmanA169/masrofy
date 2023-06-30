package com.masrofy.model

enum class TransactionCategory(val nameCategory: String, val type: TransactionType) {
    FOOD("Food", TransactionType.EXPENSE),
    SOCIAL_LIFE("Social Life", TransactionType.EXPENSE),
    SELF_DEVELOPMENT("Self Development", TransactionType.EXPENSE),
    TRANSPORTATION("Transportation", TransactionType.EXPENSE),
    CULTURE("Culture", TransactionType.EXPENSE),
    HOUSEHOLD("Household", TransactionType.EXPENSE),
    APPAREL("Apparel", TransactionType.EXPENSE),
    BEAUTY("Beauty", TransactionType.EXPENSE),
    HEALTH("Health", TransactionType.EXPENSE),
    EDUCATION("Education", TransactionType.EXPENSE),
    GIFT("Gift", TransactionType.EXPENSE),
    OTHER("Other", TransactionType.EXPENSE),
    COFFEE("Coffee", TransactionType.EXPENSE),
    ALLOWANCE("Allowance", TransactionType.INCOME),
    SALARY("Salary", TransactionType.INCOME),
    PETTY_CASH("Petty Cash", TransactionType.INCOME),
    BOUNCE("Bounce", TransactionType.INCOME),
    OTHER_INCOME("Other", TransactionType.INCOME)

}