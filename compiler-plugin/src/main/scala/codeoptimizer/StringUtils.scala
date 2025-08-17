package codeoptimizer

import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.util.Locale

object StringUtils:
  private val symbols = new DecimalFormatSymbols(Locale.UK)
  symbols.setGroupingSeparator('.') // use dot as thousands separator

  private val formatter = new DecimalFormat("#,###", symbols)

  def format(i: Long): String = formatter.format(i)
