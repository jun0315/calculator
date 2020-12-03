package com.example.calculator.utils

import com.example.calculator.R

class CalculatorImpl(calculator: Calculator) {
    //回调
    private var callback: Calculator? = calculator

    //需要展示的结果
    private var inputDisplayFormula = "0"

    private val operations = listOf("+", "-", "*", "/", "^", "%", "√")

    /**
     *小数字绑定
     */
    fun numpadClicked(id: Int) {
        when (id) {
            R.id.btn_decimal -> decimalClicked()
            R.id.btn_0 -> zeroClicked()
            R.id.btn_1 -> addDigit(1)
            R.id.btn_2 -> addDigit(2)
            R.id.btn_3 -> addDigit(3)
            R.id.btn_4 -> addDigit(4)
            R.id.btn_5 -> addDigit(5)
            R.id.btn_6 -> addDigit(6)
            R.id.btn_7 -> addDigit(7)
            R.id.btn_8 -> addDigit(8)
            R.id.btn_9 -> addDigit(9)

        }

    }

    //增加数字
    private fun addDigit(number: Int) {
        if (inputDisplayFormula == "0")
            inputDisplayFormula = ""
        inputDisplayFormula += number
        callback!!.showNewResult(inputDisplayFormula)
    }

    //获取可能存在的最后一个数字
    private fun getLastValue(): String {
        val checkVal: String = if (inputDisplayFormula.startsWith('-')) {
            inputDisplayFormula.substring(1)
        } else {
            inputDisplayFormula
        }
        return checkVal.substring(checkVal.indexOfAny(operations) + 1)

    }

    private fun zeroClicked() {
        val value = getLastValue()
        if (value != "0" || value.contains("."))
            addDigit(0)
    }

    private fun decimalClicked() {
        val value = getLastValue()
        if (!value.contains(".")) {
            inputDisplayFormula += when (value) {
                "0" -> "."
                "" -> "0."
                else -> "."
            }
        }
        callback!!.showNewResult(inputDisplayFormula)
    }

    fun handleClear() {
        inputDisplayFormula = inputDisplayFormula.dropLast(1)
        if (inputDisplayFormula == "")
            inputDisplayFormula = "0"
        callback!!.showNewResult(inputDisplayFormula)
    }

    //reset操作
    fun handleReset() {
        resetValues()
        callback!!.showNewResult(inputDisplayFormula)
        callback!!.showNewFormula("")
    }

    private fun resetValues() {
        inputDisplayFormula = "0"
    }

    //尝试计算当前结果
    private fun tryToCalculate() {
        if (inputDisplayFormula == "")
            inputDisplayFormula = "0"

        val checkVal: String = if (inputDisplayFormula.startsWith('-')) {
            inputDisplayFormula.substring(1)
        } else {
            inputDisplayFormula
        }
        //表达式中已经有相关运算，需要计算
        val index = checkVal.indexOfAny(operations)
        if (index != -1 && index != checkVal.length - 1) {
            callback!!.showNewFormula(inputDisplayFormula)
            calculate()
        }
    }

    fun handleEqual() {
        tryToCalculate()
        callback!!.showNewResult(inputDisplayFormula)
    }

    fun handleOperation(operation: String) {
        tryToCalculate()

        if (inputDisplayFormula.last().toString() in operations) {
            inputDisplayFormula = inputDisplayFormula.dropLast(1)
        }
        inputDisplayFormula += getSigns(operation)
        callback!!.showNewResult(inputDisplayFormula)

    }

    private fun getSigns(operation: String) = when (operation) {
        MINUS -> '-'
        MULTIPLY -> '*'
        DIVIDE -> '/'
        else -> "+"
    }

    private fun calculate() {
        var firstValueString = ""
        var secondValueString: String
        val tmpString: String
        if (inputDisplayFormula.startsWith('-')) {
            tmpString = inputDisplayFormula.substring(1)
            firstValueString += "-"
        } else {
            tmpString = inputDisplayFormula
        }
        firstValueString += tmpString.substring(0, tmpString.indexOfAny(operations))
        secondValueString = tmpString.substring(tmpString.indexOfAny(operations) + 1)
        val operation = tmpString[tmpString.indexOfAny(operations)]
        val firstValue: Double = firstValueString.toDouble()
        val secondValue: Double = secondValueString.toDouble()
        inputDisplayFormula = when (operation) {
            '+' -> (firstValue + secondValue).toString()
            '-' -> (firstValue - secondValue).toString()
            '*' -> (firstValue * secondValue).toString()
            '/' -> (firstValue / secondValue).toString()
            else -> ""
        }
        //将小数点后面可能为0的情况删除
        if (inputDisplayFormula.toDouble().toInt().toDouble() == inputDisplayFormula.toDouble()) {
            inputDisplayFormula = inputDisplayFormula.toDouble().toInt().toString()
        }

    }

}