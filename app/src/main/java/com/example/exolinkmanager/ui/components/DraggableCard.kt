package com.example.exolinkmanager.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.exolinkmanager.R
import com.example.exolinkmanager.ui.models.CardModel

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
    val cardShape = remember {
        RoundedCornerShape(16.dp)
    }

    val offsetX by remember { mutableFloatStateOf(0f) }
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_TEST_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset - offsetX else -offsetX },
    )

    val secondaryCardColor = MaterialTheme.colorScheme.secondary
    val primaryCardColor = MaterialTheme.colorScheme.secondaryContainer
    val backgroundAnimation = remember {
        Animatable(primaryCardColor)
    }
    LaunchedEffect(key1 = isRevealed) {
        backgroundAnimation.animateTo(
            targetValue = if (isRevealed) secondaryCardColor else primaryCardColor,
            animationSpec = tween(durationMillis = ANIMATION_TEST_DURATION)
        )
    }

    val secondaryCardTitleColor = MaterialTheme.colorScheme.onSecondary
    val primaryCardTitleColor = MaterialTheme.colorScheme.onSecondaryContainer
    val cardTitleColorAnimation = remember {
        Animatable(primaryCardTitleColor)
    }
    LaunchedEffect(key1 = isRevealed) {
        cardTitleColorAnimation.animateTo(
            targetValue = if (isRevealed) secondaryCardTitleColor else primaryCardTitleColor,
            animationSpec = tween(durationMillis = ANIMATION_TEST_DURATION)
        )
    }

    val cardElevationAnimation = remember {
        Animatable(0f)
    }
    val primaryElevation = dimensionResource(id = R.dimen.margin_xsmall)
    val secondaryElevation = dimensionResource(id = R.dimen.margin_small)
    LaunchedEffect(key1 = isRevealed) {
        cardElevationAnimation.animateTo(
            targetValue = if (isRevealed) secondaryElevation.value else primaryElevation.value,
            animationSpec = tween(durationMillis = ANIMATION_TEST_DURATION)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.margin_large),
                vertical = dimensionResource(id = R.dimen.margin_small)
            )
            .height(cardHeight)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount >= MIN_DRAG_AMOUNT -> onExpand()
                        dragAmount < -MIN_DRAG_AMOUNT -> onCollapse()
                    }
                }
            }
            .graphicsLayer {
                this.translationX = offsetTransition
            },
        backgroundColor = backgroundAnimation.value,
        elevation = cardElevationAnimation.value.dp,
        shape = cardShape,
        onClick = { onClick() },
        content = {
            CardTitle(
                cardTitle = card.title,
                textColor = cardTitleColorAnimation.value
            )
        }
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