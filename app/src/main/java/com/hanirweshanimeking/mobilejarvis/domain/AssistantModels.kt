package com.hanirweshanimeking.mobilejarvis.domain

enum class AssistantIntent {
    GENERAL,
    PLANNING,
    VOICE,
    MEMORY,
    AUTOMATION,
    UNSUPPORTED
}

enum class TaskStatus(val label: String) {
    CREATED("Created"),
    PLANNING("Planning"),
    WAITING_FOR_APPROVAL("Waiting for approval"),
    READY("Ready"),
    RUNNING("Running"),
    VERIFYING("Verifying"),
    SUCCEEDED("Succeeded"),
    FAILED("Failed"),
    CANCELLED("Cancelled")
}

data class AssistantDecision(
    val intent: AssistantIntent,
    val message: String
)
