package codeoptimizer

import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.util.Locale

extension (d: Double) def to2DigitDecimalPointString: String = if d.isNaN then "-" else f"$d%.2f"

extension (b: Boolean) def toYesNo: String = if b then "Yes" else "No"

object StringUtils:
  private val symbols = new DecimalFormatSymbols(Locale.UK)
  symbols.setGroupingSeparator('.') // use dot as thousands separator

  private val formatter = new DecimalFormat("#,###", symbols)

  def format(i: Long): String = formatter.format(i)
