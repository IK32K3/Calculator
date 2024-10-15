package com.example.calculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var currentInput: String = ""
    private var firstValue: Double = 0.0
    private var secondValue: Double = 0.0
    private var operator: String = ""
    private var resultDisplayed: Boolean = false

    private lateinit var textViewCalculation: TextView
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.linear)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textViewResult = findViewById(R.id.textViewResult)
        textViewCalculation = findViewById(R.id.textViewCalculation)

        setNumberClickListeners()
        setOperatorClickListeners()

    }

    // Hàm xử lý sự kiện cho các nút số
    private fun setNumberClickListeners() {
        val numberButtons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener { view ->
                val button = view as Button
                if (resultDisplayed) {
                    // Nếu vừa hiển thị kết quả, nhập giá trị mới sẽ thay thế kết quả
                    currentInput = ""
                    resultDisplayed = false
                }
                currentInput += button.text
                textViewResult.text = currentInput
            }
        }
    }

    private fun setOperatorClickListeners() {
        val operatorButtons = listOf(
            R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply, R.id.buttonDivide
        )

        for (id in operatorButtons) {
            findViewById<Button>(id).setOnClickListener { view ->
                val button = view as Button
                if (currentInput.isNotEmpty()) {
                    if (resultDisplayed) {
                        // Nếu vừa hiển thị kết quả, tiếp tục với kết quả làm toán tử thứ nhất
                        operator = button.text.toString()
                        currentInput = ""
                    } else {
                        firstValue = currentInput.toDouble()
                        operator = button.text.toString()
                        currentInput = ""
                    }

                    textViewCalculation.text = "${firstValue.toInt()} $operator"
                }
            }
        }

        // Nút tính toán kết quả
        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                secondValue = currentInput.toDouble()
                textViewCalculation.text = "${firstValue.toInt()} $operator ${secondValue.toInt()} ="
                calculateResult()
                resultDisplayed = true
            }


        }

        // Nút xóa (C)
        findViewById<Button>(R.id.buttonC).setOnClickListener {
            resetCalculator()
        }

        // Nút backspace (BS)
        findViewById<Button>(R.id.buttonBS).setOnClickListener {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                textViewResult.text = currentInput
            }
        }

        // Nút CE (Clear Entry)
        findViewById<Button>(R.id.buttonCE).setOnClickListener {
            currentInput = ""
            textViewResult.text = "0"
        }

        // Nút +/- (đổi dấu)
        findViewById<Button>(R.id.buttonPlusMinus).setOnClickListener {
            if (currentInput.isNotEmpty() && currentInput != "0") {
                currentInput = if (currentInput.startsWith("-")) {
                    currentInput.drop(1) // Xóa dấu âm
                } else {
                    "-$currentInput" // Thêm dấu âm
                }
                textViewResult.text = currentInput
            }
        }

        // Nút . (số thập phân)
        findViewById<Button>(R.id.buttonDots).setOnClickListener {
            if (!currentInput.contains(".")) {
                currentInput += "."
                textViewResult.text = currentInput
            }
        }
    }

    // Hàm tính toán kết quả
    private fun calculateResult() {
        val result = when (operator) {
            "+" -> firstValue + secondValue
            "-" -> firstValue - secondValue
            "x" -> firstValue * secondValue
            "/" -> {
                if (secondValue != 0.0) {
                    firstValue / secondValue
                } else {
                    "Error" // Không thể chia cho 0
                }
            }
            else -> "Error"
        }

        // Xử lý kết quả hiển thị
        if (result is Double) {
            // Kiểm tra xem kết quả có thể là số nguyên hay không
            if (result % 1.0 == 0.0) {
                // Nếu là số nguyên, chỉ hiển thị phần nguyên
                textViewResult.text = result.toInt().toString()
            } else {
                // Nếu là số thập phân, hiển thị đầy đủ
                textViewResult.text = result.toString()
            }

            // Cập nhật lại giá trị của `firstValue` bằng kết quả
            firstValue = result
        }
        // Reset `currentInput` để tiếp tục nhập liệu mới
        currentInput = result.toString()
    }

    // Hàm reset máy tính
    private fun resetCalculator() {
        currentInput = ""
        firstValue = 0.0
        secondValue = 0.0
        operator = ""
        textViewResult.text = "0"
        textViewCalculation.text =""
    }

}