package com.hanirweshanimeking.mobilejarvis.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class JarvisViewModelTest {
    @Test
    fun toggleListening_updatesState() {
        val viewModel = JarvisViewModel()

        assertFalse(viewModel.uiState.value.isListening)

        viewModel.toggleListening()

        assertTrue(viewModel.uiState.value.isListening)
        assertEquals("Listening for your command", viewModel.uiState.value.status)
        assertEquals("VOICE", viewModel.uiState.value.agentMode)
    }

    @Test
    fun addMemory_appendsMemory() {
        val viewModel = JarvisViewModel()

        viewModel.addMemory("Focus", "Prioritize deep work in the morning")

        assertEquals(3, viewModel.uiState.value.memories.size)
        assertEquals("Focus", viewModel.uiState.value.memories.first().title)
    }

    @Test
    fun approveTask_marksTaskApproved() {
        val viewModel = JarvisViewModel()

        viewModel.approveTask("device")

        assertEquals("APPROVED", viewModel.uiState.value.tasks.first { it.id == "device" }.state)
    }

    @Test
    fun sendPrompt_appendsUserAndAssistantMessages() {
        val viewModel = JarvisViewModel()

        viewModel.sendPrompt("Plan my day")

        assertEquals("", viewModel.uiState.value.input)
        assertEquals(5, viewModel.uiState.value.messages.size)
        assertEquals("You", viewModel.uiState.value.messages[3].sender)
        assertEquals("Plan my day", viewModel.uiState.value.messages[3].text)
    }
}

