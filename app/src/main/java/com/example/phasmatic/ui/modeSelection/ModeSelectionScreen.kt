package com.example.phasmatic.ui.modeSelection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.ImageView
import com.bumptech.glide.Glide

@Composable
fun ModeSelectionScreen(
    userId: String?,
    userFullName: String?,
    userEmail: String?,
    userPhone: String?,

    profileImageUrl: String?, // 🔥 NEW

    onModeSelected: (String) -> Unit,
    onForumClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E3A5F))
            .padding(24.dp)
    ) {

        // PROFILE TOP RIGHT (IMAGE + fallback)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Box {

                IconButton(onClick = { menuExpanded = true }) {

                    if (!profileImageUrl.isNullOrEmpty()) {

                        AndroidView(
                            factory = { context ->
                                ImageView(context).apply {
                                    Glide.with(context)
                                        .load(profileImageUrl)
                                        .placeholder(android.R.drawable.ic_menu_gallery)
                                        .into(this)
                                }
                            },
                            modifier = Modifier.size(40.dp)
                        )

                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }

                ProfileMenuDropdown(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onChatClick = onChatClick,
                    onConferenceClick = onConferenceClick,
                    onNotesClick = onNotesClick,
                    onLogoutClick = onLogoutClick,
                    onAccountClick = onProfileClick
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Explore Your Options", fontSize = 26.sp, color = Color.White)
        Text("Choose the type of studies", fontSize = 14.sp, color = Color.LightGray)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onModeSelected("erasmus") }) { Text("Erasmus") }
            Button(onClick = { onModeSelected("master") }) { Text("Master") }
            Button(onClick = { onModeSelected("career") }) { Text("Career") }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Video Here", color = Color.White)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text("Explore Forum", fontSize = 22.sp, color = Color.White)

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onForumClick) {
            Text("Go to Forum")
        }
    }
}

@Composable
fun ProfileMenuDropdown(
    expanded: Boolean,
    onDismiss: () -> Unit,

    onAccountClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {

        DropdownMenuItem(
            text = { Text("My account") },
            onClick = {
                onDismiss()
                onAccountClick()
            }
        )

        DropdownMenuItem(
            text = { Text("Messages") },
            onClick = {
                onDismiss()
                onChatClick()
            }
        )

        DropdownMenuItem(
            text = { Text("Conferences") },
            onClick = {
                onDismiss()
                onConferenceClick()
            }
        )

        DropdownMenuItem(
            text = { Text("Notes") },
            onClick = {
                onDismiss()
                onNotesClick()
            }
        )

        Divider()

        DropdownMenuItem(
            text = { Text("Log out") },
            onClick = {
                onDismiss()
                onLogoutClick()
            }
        )
    }
}