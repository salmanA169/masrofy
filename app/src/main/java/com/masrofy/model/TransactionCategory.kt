package com.masrofy.model

enum class TransactionCategory(val nameCategory: String, val type: TransactionType,val position:Int) {
    FOOD("Food", TransactionType.EXPENSE,1),
    SOCIAL_LIFE("Social Life", TransactionType.EXPENSE,2),
    SELF_DEVELOPMENT("Self Development", TransactionType.EXPENSE,3),
    TRANSPORTATION("Transportation", TransactionType.EXPENSE,4),
    CULTURE("Culture", TransactionType.EXPENSE,5),
    HOUSEHOLD("Household", TransactionType.EXPENSE,6),
    APPAREL("Apparel", TransactionType.EXPENSE,7),
    BEAUTY("Beauty", TransactionType.EXPENSE,8),
    HEALTH("Health", TransactionType.EXPENSE,9),
    EDUCATION("Education", TransactionType.EXPENSE,10),
    GIFT("Gift", TransactionType.EXPENSE,11),
    OTHER("Other", TransactionType.EXPENSE,12),
    COFFEE("Coffee", TransactionType.EXPENSE,13),
    ALLOWANCE("Allowance", TransactionType.INCOME,1),
    SALARY("Salary", TransactionType.INCOME,2),
    PETTY_CASH("Petty Cash", TransactionType.INCOME,3),
    BOUNCE("Bounce", TransactionType.INCOME,4),
    OTHER_INCOME("Other", TransactionType.INCOME,5)

}