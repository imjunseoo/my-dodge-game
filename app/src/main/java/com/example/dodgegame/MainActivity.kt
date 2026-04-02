package com.example.dodgegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DodgeGame()
            }
        }
    }
}

@Composable
fun DodgeGame() {
    var playerPosX by remember { mutableStateOf(500f) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    val enemies = remember { mutableStateListOf<Offset>() }

    LaunchedEffect(!isGameOver) {
        while (!isGameOver) {
            delay(16L)
            score++
            if (Random.nextInt(100) < 5) {
                enemies.add(Offset(Random.nextFloat() * 1000f, 0f))
            }
            val iterator = enemies.listIterator()
            while (iterator.hasNext()) {
                val enemy = iterator.next()
                val nextEnemy = enemy.copy(y = enemy.y + 15f + (score / 500f))
                if (nextEnemy.y > 2000f) {
                    iterator.remove()
                } else {
                    iterator.set(nextEnemy)
                }
                if (Math.abs(nextEnemy.x - playerPosX) < 60f && Math.abs(nextEnemy.y - 1600f) < 60f) {
                    isGameOver = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    playerPosX += dragAmount.x
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Blue,
                topLeft = Offset(playerPosX - 50f, 1600f),
                size = androidx.compose.ui.geometry.Size(100f, 100f)
            )
            enemies.forEach { enemy ->
                drawCircle(color = Color.Red, radius = 30f, center = enemy)
            }
        }
        Text(
            text = "Score: $score",
            modifier = Modifier.align(Alignment.TopCenter),
            fontSize = 30.sp,
            color = Color.Black
        )
        if (isGameOver) {
            Text(
                text = "GAME OVER\nScore: $score",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 40.sp,
                color = Color.Red
            )
        }
    }
}
