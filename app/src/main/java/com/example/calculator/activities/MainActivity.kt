package com.example.calculator.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.R
import com.example.calculator.utils.Calculator
import com.example.calculator.utils.CalculatorImpl
import com.example.calculator.utils.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Calculator {

    lateinit var calc: CalculatorImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calc = CalculatorImpl(this)

        btn_plus.setOnClickListener { calc.handleOperation(PLUS) }
        btn_minus.setOnClickListener { calc.handleOperation(MINUS) }
        btn_multiply.setOnClickListener { calc.handleOperation(MULTIPLY) }
        btn_divide.setOnClickListener { calc.handleOperation(DIVIDE) }

        btn_clear.setOnClickListener { calc.handleClear() }
        btn_clear.setOnLongClickListener { calc.handleReset();true }

        btn_equals.setOnClickListener { calc.handleEqual() }

        getButtonIds().forEach { it ->
            it.setOnClickListener {
                calc.numpadClicked(it.id)
            }
        }
    }

    private fun getButtonIds() =
        arrayOf(btn_decimal, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9)


    override fun showNewResult(value: String) {
        result.text = value
    }

    override fun showNewFormula(value: String) {
        formula.text = value
    }


}