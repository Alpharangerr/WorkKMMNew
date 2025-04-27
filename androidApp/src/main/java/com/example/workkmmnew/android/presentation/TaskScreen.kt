package com.example.workkmmnew.android.presentation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import com.example.workkmmnew.android.data.Task
import com.example.workkmmnew.android.viewmodel.TaskViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable



@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val context = LocalContext.current
    val taskList by viewModel.tasks.collectAsState()

    var newTaskTitle by remember { mutableStateOf("") }
    var editMode by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    val sharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    val isDarkThemeSaved = sharedPreferences.getBoolean("isDarkTheme", false)
    var isDarkTheme by remember { mutableStateOf(isDarkThemeSaved) }

    LaunchedEffect(isDarkTheme) {
        sharedPreferences.edit().putBoolean("isDarkTheme", isDarkTheme).apply()
    }

    val backgroundColor = if (isDarkTheme) Color(0xFF303030) else Color.White
    val inputTextColor = if (isDarkTheme) Color.White else Color.Black
    val borderColor = if (isDarkTheme) Color.White else Color.Black

    val pendingTasks = taskList.filter { !it.isDone }
    val completedTasks = taskList.filter { it.isDone }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Task Manager",
                fontSize = 24.sp,
                color = inputTextColor,
                fontWeight = FontWeight.Bold, // Make title bold
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Input for new or editing task
            OutlinedTextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = {
                    Text(
                        text = if (editMode) "Edit Task" else "New Task",
                        color = inputTextColor,
                        fontWeight = FontWeight.Bold // Make label bold
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                textStyle = LocalTextStyle.current.copy(color = inputTextColor, fontWeight = FontWeight.Bold), // Make input text bold
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = inputTextColor,
                    cursorColor = inputTextColor,
                    focusedBorderColor = borderColor,
                    unfocusedBorderColor = borderColor
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (newTaskTitle.isNotBlank()) {
                            if (editMode && editingTask != null) {
                                viewModel.updateTask(editingTask!!.copy(title = newTaskTitle))
                                editMode = false
                                editingTask = null
                            } else {
                                viewModel.addTask(Task(title = newTaskTitle, description = ""))
                            }
                            newTaskTitle = ""
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Show "Add new task" when no tasks are available
            if (taskList.isEmpty()) {
                Text(
                    text = "Add new task",
                    fontSize = 18.sp,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center) // Center the text on the screen
                )
            } else {
                // Pending Tasks Section
                if (pendingTasks.isNotEmpty()) {
                    TaskListSection(
                        title = "Pending Tasks",
                        tasks = pendingTasks,
                        onEdit = { task ->
                            editMode = true
                            editingTask = task
                            newTaskTitle = task.title
                        },
                        onDelete = { viewModel.deleteTask(it) },
                        onCheckedChange = { updatedTask -> viewModel.updateTask(updatedTask) }
                    )
                }

                // Completed Tasks Section
                if (completedTasks.isNotEmpty()) {
                    TaskListSection(
                        title = "Completed Tasks",
                        tasks = completedTasks,
                        onEdit = { task ->
                            editMode = true
                            editingTask = task
                            newTaskTitle = task.title
                        },
                        onDelete = { viewModel.deleteTask(it) },
                        onCheckedChange = { updatedTask -> viewModel.updateTask(updatedTask) }
                    )
                }
            }
        }

        // Theme toggle button
        FloatingActionButton(
            onClick = { isDarkTheme = !isDarkTheme },
            backgroundColor = if (isDarkTheme) Color.White else Color.Black,
            contentColor = if (isDarkTheme) Color.Black else Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = if (isDarkTheme) "🌞" else "🌙", fontSize = 20.sp)
        }
    }
}



@Composable
fun TaskListSection(
    title: String,
    tasks: List<Task>,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onCheckedChange: (Task) -> Unit
) {
    Card(
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color(0xFFdcdcdc)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            for (task in tasks) {
                TaskItem(
                    task = task,
                    onEdit = { onEdit(task) },
                    onDelete = { onDelete(task) },
                    onCheckedChange = { updatedTask -> onCheckedChange(updatedTask) }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onEdit: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onCheckedChange: (Task) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                if (task.isDone) Color(0xFFB2FF59) else Color(0xFFFFFF00), // Pending tasks in yellow
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Keep the space between items
        ) {
            // Task Title in bold
            Text(
                text = task.title,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold, // Set the text to bold
                modifier = Modifier.weight(1f) // Makes sure the task title gets the available space
            )

            // Row to hold the "Completed?" and "Again?" buttons and the edit/delete icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp) // Space only between "Completed?" and "Again?" button
            ) {
                // "Completed?" or "Again?" Button
                Button(
                    onClick = { onCheckedChange(task.copy(isDone = !task.isDone)) }, // Toggle completion
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (task.isDone) Color.Red else Color.Green,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (task.isDone) "Again?" else "Completed?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold // Set the button text to bold
                    )
                }

                // Edit Button
                IconButton(onClick = { onEdit(task) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                }

                // Add space between edit and delete buttons
                Spacer(modifier = Modifier.width(0.dp))

                // Delete Button
                IconButton(onClick = { onDelete(task) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                }
            }
        }
    }
}
