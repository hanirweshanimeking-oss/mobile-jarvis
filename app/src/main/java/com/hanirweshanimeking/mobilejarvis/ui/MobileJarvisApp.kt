package com.hanirweshanimeking.mobilejarvis.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hanirweshanimeking.mobilejarvis.domain.AssistantIntent
import com.hanirweshanimeking.mobilejarvis.domain.AssistantDecision
import com.hanirweshanimeking.mobilejarvis.domain.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppTab(val label: String) {
    HOME("Home"),
    VOICE("Voice"),
    MEMORY("Memory"),
    AUTOMATION("Automation")
}

data class ChatMessage(
    val sender: String,
    val text: String,
)

data class AssistantMemory(
    val id: String,
    val title: String,
    val detail: String,
)

data class AgentTask(
    val id: String,
    val name: String,
    val status: TaskStatus,
)

data class JarvisUiState(
    val status: String = "Awake and ready",
    val agentMode: String = "ACE",
    val isListening: Boolean = false,
    val selectedTab: AppTab = AppTab.HOME,
    val input: String = "",
    val intent: AssistantIntent = AssistantIntent.GENERAL,
    val messages: List<ChatMessage> = listOf(
        ChatMessage("MEGHA", "Welcome back. I can plan tasks, answer questions, and help with consent-based device actions."),
        ChatMessage("You", "Summarize my day and help me stay focused."),
        ChatMessage("MEGHA", "I have prioritized your reminders, healthy routine, and coding tasks. We are ready to proceed."),
    ),
    val tasks: List<AgentTask> = listOf(
        AgentTask("planner", "Planner Agent", TaskStatus.RUNNING),
        AgentTask("research", "Research Agent", TaskStatus.READY),
        AgentTask("device", "Device Agent", TaskStatus.WAITING_FOR_APPROVAL),
    ),
    val memories: List<AssistantMemory> = listOf(
        AssistantMemory("pref-1", "User preference", "Keep interactions concise and practical."),
        AssistantMemory("pref-2", "Routine", "Prioritize deep work before meetings."),
    )
)

class JarvisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JarvisUiState())
    val uiState: StateFlow<JarvisUiState> = _uiState.asStateFlow()

    fun setSelectedTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun setInput(value: String) {
        _uiState.update { it.copy(input = value) }
    }

    fun toggleListening() {
        _uiState.update { current ->
            val newListening = !current.isListening
            current.copy(
                isListening = newListening,
                status = if (newListening) "Listening for your command" else "Awake and ready",
                agentMode = if (newListening) "VOICE" else "ACE"
            )
        }
    }

    fun addMemory(title: String, detail: String) {
        val trimmedTitle = title.trim()
        val trimmedDetail = detail.trim()
        if (trimmedTitle.isEmpty() || trimmedDetail.isEmpty()) return

        _uiState.update { current ->
            current.copy(
                memories = listOf(
                    AssistantMemory(
                        id = "mem-${System.currentTimeMillis()}",
                        title = trimmedTitle,
                        detail = trimmedDetail
                    )
                ) + current.memories
            )
        }
    }

    fun approveTask(taskId: String) {
        _uiState.update { current ->
            val updated = current.tasks.map { task ->
                if (task.id == taskId) task.copy(status = TaskStatus.SUCCEEDED) else task
            }
            current.copy(tasks = updated)
        }
    }

    fun sendPrompt(prompt: String) {
        val trimmed = prompt.trim()
        if (trimmed.isEmpty()) return

        val decision = buildDecision(trimmed)

        _uiState.update { current ->
            current.copy(
                input = "",
                intent = decision.intent,
                messages = current.messages + ChatMessage("You", trimmed) + ChatMessage("MEGHA", decision.message),
                status = when (decision.intent) {
                    AssistantIntent.PLANNING -> "Planning your next action"
                    AssistantIntent.VOICE -> "Voice assistant is active"
                    AssistantIntent.MEMORY -> "Refreshing memory context"
                    AssistantIntent.AUTOMATION -> "Checking automation safeguards"
                    AssistantIntent.UNSUPPORTED -> "I can help with supported requests only"
                    AssistantIntent.GENERAL -> "Working on your request"
                }
            )
        }
    }

    private fun buildDecision(prompt: String): AssistantDecision = when {
        prompt.contains("plan", ignoreCase = true) -> AssistantDecision(
            AssistantIntent.PLANNING,
            "I have created a focused plan with priorities, checkpoints, and a short review loop."
        )
        prompt.contains("voice", ignoreCase = true) || prompt.contains("talk", ignoreCase = true) -> AssistantDecision(
            AssistantIntent.VOICE,
            "Voice mode is ready. I can listen and respond once microphone access is granted."
        )
        prompt.contains("memory", ignoreCase = true) -> AssistantDecision(
            AssistantIntent.MEMORY,
            "I can store your preferences and retrieve them when relevant, while keeping the policy transparent."
        )
        prompt.contains("automation", ignoreCase = true) || prompt.contains("device", ignoreCase = true) -> AssistantDecision(
            AssistantIntent.AUTOMATION,
            "I can suggest or schedule an approved device action and verify its outcome before completion."
        )
        else -> AssistantDecision(
            AssistantIntent.GENERAL,
            "I am here to coordinate tasks, keep context, and help you act with clarity."
        )
    }
}

@Composable
fun rememberJarvisViewModel(): JarvisViewModel = viewModel()
