package com.example.decyra.extras

import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun ProfileMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAction: (ProfileAction) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {

        DropdownMenuItem(
            text = { Text("My account") },
            onClick = { onAction(ProfileAction.ACCOUNT) }
        )

        DropdownMenuItem(
            text = { Text("Messages") },
            onClick = { onAction(ProfileAction.CHAT) }
        )

        DropdownMenuItem(
            text = { Text("Conferences") },
            onClick = { onAction(ProfileAction.CONFERENCE) }
        )

        DropdownMenuItem(
            text = { Text("Notes") },
            onClick = { onAction(ProfileAction.NOTES) }
        )

        DropdownMenuItem(
            text = { Text("Log out") },
            onClick = { onAction(ProfileAction.LOGOUT) }
        )
    }
}

enum class ProfileAction {
    ACCOUNT,
    CHAT,
    CONFERENCE,
    NOTES,
    LOGOUT
}