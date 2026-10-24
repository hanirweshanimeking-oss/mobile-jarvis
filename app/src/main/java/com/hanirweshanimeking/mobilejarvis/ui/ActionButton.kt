package com.hanirweshanimeking.mobilejarvis.ui

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChatMessage(
    val sender: String,
    val text: String,
)

data class AgentTask(
    val name: String,
    val state: String,
)

data class JarvisUiState(
    val status: String = "Awake and ready",
    val agentMode: String = "ACE",
    val isListening: Boolean = false,
    val messages: List<ChatMessage> = listOf(
        ChatMessage("MEGHA", "Welcome back. I can plan tasks, answer questions, and help with consent-based device actions."),
        ChatMessage("You", "Summarize my day and help me stay focused."),
        ChatMessage("MEGHA", "I have prioritized your reminders, healthy routine, and coding tasks. We are ready to proceed."),
    ),
    val tasks: List<AgentTask> = listOf(
        AgentTask("Planner Agent", "READY"),
        AgentTask("Research Agent", "WORKING"),
        AgentTask("Device Agent", "WAITING_FOR_APPROVAL"),
    )
)

class JarvisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JarvisUiState())
    val uiState: StateFlow<JarvisUiState> = _uiState.asStateFlow()

    fun toggleListening() {
        _uiState.update {
            it.copy(
                isListening = !it.isListening,
                status = if (!it.isListening) "Listening for your command" else "Awake and ready"
            )
        }
    }

    fun sendPrompt(prompt: String) {
        val trimmed = prompt.trim()
        if (trimmed.isEmpty()) return

        val response = when {
            trimmed.contains("plan", ignoreCase = true) -> "I have created a lightweight plan with priorities, checkpoints, and a review step."
            trimmed.contains("voice", ignoreCase = true) || trimmed.contains("talk", ignoreCase = true) -> "Voice mode is ready. I will listen and respond once the microphone is enabled."
            trimmed.contains("memory", ignoreCase = true) -> "I will keep your preferences and recent context in memory while staying transparent about what I store."
            trimmed.contains("automation", ignoreCase = true) || trimmed.contains("device", ignoreCase = true) -> "I can assist with approved device actions and verify results before proceeding."
            else -> "I am here to coordinate tasks, keep context, and help you act with clarity."
        }

        _uiState.update { current ->
            current.copy(
                messages = current.messages + ChatMessage("You", trimmed) + ChatMessage("MEGHA", response),
                status = "Working on your request"
            )
        }
    }
}
