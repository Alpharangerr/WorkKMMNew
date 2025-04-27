package com.example.workkmmnew.android.presentation

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workkmmnew.android.R // Assuming your image is inside the res/drawable folder

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        // Navigate to the TaskScreen after 4 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate("task_screen") {
                popUpTo("splash") { inclusive = true }
            }
        }, 4000) // 4 seconds delay
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.splash_image), // Replace with your image resource name
            contentDescription = "Splash Screen Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Loading spinner centered over the image (Black color)
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = Color.Black // Set loading spinner color to black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
