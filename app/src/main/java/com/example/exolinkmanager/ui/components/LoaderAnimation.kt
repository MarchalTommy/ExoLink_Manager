package com.example.exolinkmanager.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.exolinkmanager.R

@Composable
fun Loader(isVisible: Boolean) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "LoaderAlphaAnimation"
    )
    Box(
        modifier = Modifier
            .alpha(alpha)
            .fillMaxSize()
            .background(color = colorResource(id = R.color.loader_background))
    ) {
        LottieAnimation(
            modifier = Modifier.align(Alignment.Center),
            composition = composition,
            progress = { progress }
        )
    }
}