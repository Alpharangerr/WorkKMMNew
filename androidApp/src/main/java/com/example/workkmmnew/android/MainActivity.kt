package com.example.workkmmnew.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workkmmnew.android.data.TaskDatabase
import com.example.workkmmnew.android.presentation.TaskScreen
import com.example.workkmmnew.android.presentation.SplashScreen
import com.example.workkmmnew.android.viewmodel.TaskViewModel
import com.example.workkmmnew.android.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            TaskDatabase.getDatabase(context = this).taskDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Setup Navigation
            AppNavigation(viewModel)
        }
    }
}

@Composable
fun AppNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("task_screen") {
            TaskScreen(viewModel)
        }
    }
}
