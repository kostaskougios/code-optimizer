package codeoptimizer

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

extension (d: Double)
  def toPercentagex100: String           = s"${(d * 100).toInt} %"
  def toPercentage: String               = s"${d.toInt} %"
  def to2DigitDecimalPointString: String = if d.isNaN then "-" else f"$d%.2f"

extension (b: Boolean) def toYesNo: String = if b then "Yes" else "No"

object StringUtils:
  private val symbols = new DecimalFormatSymbols(Locale.UK)
  symbols.setGroupingSeparator('.') // use dot as thousands separator

  private val formatter = new DecimalFormat("#,###", symbols)

  def format(i: Long): String = formatter.format(i)
