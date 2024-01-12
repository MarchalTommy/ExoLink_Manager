package com.example.exolinkmanager.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.exolinkmanager.ui.models.CardModel
import kotlin.math.roundToInt

const val ANIMATION_TEST_DURATION = 500
const val MIN_DRAG_AMOUNT = 6

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DraggableCard(
    card: CardModel,
    cardHeight: Dp,
    isRevealed: Boolean,
    cardOffset: Float,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onClick: () -> Unit
) {
    val offsetX by remember { mutableFloatStateOf(0f) }
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val cardBgColor by transition.animateColor(
        label = "cardBgColorTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_TEST_DURATION) },
        targetValueByState = {
            if (isRevealed) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
        }
    )
    val cardTitleColor by transition.animateColor(
        label = "cardTitleColorTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_TEST_DURATION) },
        targetValueByState = {
            if (isRevealed) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSecondaryContainer
        }
    )
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_TEST_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset - offsetX else -offsetX },
    )
    val cardElevation by transition.animateDp(
        label = "cardElevation",
        transitionSpec = { tween(durationMillis = ANIMATION_TEST_DURATION) },
        targetValueByState = { if (isRevealed) 8.dp else 2.dp }
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(cardHeight)
            .offset { IntOffset(offsetTransition.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount >= MIN_DRAG_AMOUNT -> onExpand()
                        dragAmount < -MIN_DRAG_AMOUNT -> onCollapse()
                    }
                }
            },
        backgroundColor = cardBgColor,
        shape = remember {
            RoundedCornerShape(16.dp)
        },
        elevation = cardElevation,
        onClick = { onClick() },
        content = { CardTitle(cardTitle = card.title, textColor = cardTitleColor) }
    )
}

@Composable
fun CardTitle(cardTitle: String, textColor: Color) {
    Text(
        text = cardTitle,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier
            .wrapContentSize(Alignment.Center),
        textAlign = TextAlign.Center,
    )
}