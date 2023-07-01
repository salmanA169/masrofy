package com.masrofy.currencyVisual

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.masrofy.utils.formatAsDisplayNormalize
import java.math.BigDecimal
import kotlin.math.max

class CurrencyAmountInputVisualTransformation(
) : VisualTransformation {

    private fun String.formattedAmount(): BigDecimal {
        return try {
            ifBlank { "0" }
                .replace("\\D".toRegex(), "")
                .toBigDecimal()
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }





    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText = text.text.formattedAmount()

        val newText = AnnotatedString(
            text = formatAsDisplayNormalize(formattedText,true),
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )
        val offsetMapping =
            FixedCursorOffsetMapping(
                text.text.length,
                newText.text.length,
            )
        return TransformedText(newText, offsetMapping)
    }

    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }

    private class MovableCursorOffsetMapping(
        private val unmaskedText: String,
        private val maskedText: String,
        private val decimalDigits: Int
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    maskedText.length - (unmaskedText.length - offset)
                }
                else -> {
                    offset + offsetMaskCount(offset, maskedText)
                }
            }

        override fun transformedToOriginal(offset: Int): Int =
            when {
                unmaskedText.length <= decimalDigits -> {
                    max(unmaskedText.length - (maskedText.length - offset), 0)
                }
                else -> {
                    offset - maskedText.take(offset).count { !it.isDigit() }
                }
            }

        private fun offsetMaskCount(offset: Int, maskedText: String): Int {
            var maskOffsetCount = 0
            var dataCount = 0
            for (maskChar in maskedText) {
                if (!maskChar.isDigit()) {
                    maskOffsetCount++
                } else if (++dataCount > offset) {
                    break
                }
            }
            return maskOffsetCount
        }
    }
}