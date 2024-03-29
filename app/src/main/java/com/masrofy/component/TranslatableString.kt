package com.masrofy.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class TranslatableString {
    class PlainString(val text: String) : TranslatableString()
    class ResString(@StringRes val id: Int, vararg val formatArgs: Any) : TranslatableString()

    @Composable
    fun getString(): String {
        return when (this) {
            is PlainString -> text
            is ResString -> stringResource(id, *formatArgs)
        }
    }
}

fun translatablePlain(text:String):TranslatableString{
    return TranslatableString.PlainString(text)
}
fun translatableRes(@StringRes  id: Int,  vararg formatArgs: Any):TranslatableString{
    return TranslatableString.ResString(id, formatArgs)
}
interface WithTranslatableTitle {
    val title: TranslatableString
}