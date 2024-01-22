package com.joincoded.composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joincoded.composable.ui.theme.ComposableTheme

data class Question(val text: String, val isCorrect: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrueFalseGame()
                }
            }
        }
    }
}

@Composable
fun TrueFalseGame() {
    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentQuestionIndex = remember { mutableStateOf(0) }
    var AnsrCorrect by remember { mutableStateOf(false) }
    var showFeedback by remember { mutableStateOf(false) }
    var showAnswerOptionsRow by remember { mutableStateOf(true) }
    var userScore = remember { mutableStateOf(0) }
    var showResetButton by remember { mutableStateOf(false) }


    if (questions.isEmpty()) {
        questions = listOf(
            Question("can count till 10?", true),
            Question("the water has a blue color", false),
            Question("the sky is blue?", true),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step 1
        Text(
            text = questions[currentQuestionIndex.value].text,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (showResetButton) {
            Button(onClick = {
                // Show the final score and restart button
                currentQuestionIndex.value = 0
                userScore.value = 0
                showAnswerOptionsRow = true
                showResetButton = false
            }) {
                Text(text = "Reset Game")
            }
        }
//the condition of the correct answer
        if (AnsrCorrect && showFeedback ) {
            AnswerFeedback("Correct!", MaterialTheme.colorScheme.secondary)
            Text(text = "Score: ${userScore.value}")
            Button(
                onClick = {
                    showFeedback = false
                    showAnswerOptionsRow = true
                    // Move to the next question or show the final score
                    if (currentQuestionIndex.value == questions.size - 1) {
                        showResetButton = true
                        AnsrCorrect = false
                    }

                    if (currentQuestionIndex.value < questions.size - 1) {
                        currentQuestionIndex.value++

                    }


                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(10.dp),
            ) {
                Text(text = "Next Question")
            }
        } else if (!AnsrCorrect && showFeedback) {
            // Keep true and false buttons visible
            showFeedback = true
            // Show feedback for wrong answer
            AnswerFeedback("Wrong!", MaterialTheme.colorScheme.error)

        }

        if (showAnswerOptionsRow) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {

                TrueFalseButton("True") {
                    AnsrCorrect = true == questions[currentQuestionIndex.value].isCorrect
                    if (AnsrCorrect) {
                        userScore.value += 1
                        showAnswerOptionsRow = false
                    }
                    showFeedback = true

                }
                TrueFalseButton("False") {
                    AnsrCorrect = false == questions[currentQuestionIndex.value].isCorrect
                    if (AnsrCorrect) {
                        userScore.value += 1
                        showAnswerOptionsRow = false
                    }
                    showFeedback = true

                }
            }
        }

    }
}

//@Composable
//fun nextQuestionButton(){
//
//}
@Composable
fun TrueFalseButton(text: String, UserAnsr: () -> Unit) {
    Button(
        onClick = {
            UserAnsr()

        },
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)

    ) {
        Text(text = text)
    }
}

@Composable
fun AnswerFeedback(message: String, backgroundColor: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier

            .size(100.dp)
            .clip(CircleShape)
            .clip(MaterialTheme.shapes.large)
            .background(backgroundColor)
            .padding(16.dp),
        Alignment.Center
        // contentAlignment = Alignment.Center

    ) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrueFalseGamePreview() {
    TrueFalseGame()
}