package com.example.phasmatic.ui.modeSelection

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.ImageView
import com.bumptech.glide.Glide

@Composable
fun ModeSelectionScreen(
    userId: String?,
    userFullName: String?,
    userEmail: String?,
    userPhone: String?,
    profileImageUrl: String?,
    onModeSelected: (String) -> Unit,
    onForumClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    // Deep Cinematic Background
    val backgroundBrush = Brush.verticalGradient(
        0.0f to Color(0xFF040B14),
        0.5f to Color(0xFF0A192F),
        1.0f to Color(0xFF040B14)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            // Ambient Glows
            GlowOrb(Modifier.align(Alignment.TopStart).offset(x = (-50).dp, y = 100.dp), Color(0xFF64FFDA).copy(0.15f))
            GlowOrb(Modifier.align(Alignment.BottomEnd).offset(x = 50.dp, y = (-100).dp), Color(0xFF7C4DFF).copy(0.2f))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 60.dp, bottom = 40.dp)
            ) {
                // Header Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Welcome back,",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                userFullName?.split(" ")?.firstOrNull() ?: "Explorer",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        // Profile Avatar with Pulse Effect
                        Box {
                            ProfileAvatar(
                                imageUrl = profileImageUrl,
                                onClick = { menuExpanded = true }
                            )
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
                }

                item { Spacer(Modifier.height(40.dp)) }

                // Hero Section
                item {
                    Text(
                        "Explore Your Future",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-1).sp
                        ),
                        color = Color.White
                    )
                    Text(
                        "Select a path to begin your journey",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                item { Spacer(Modifier.height(32.dp)) }

                // Interactive Mode Cards
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ModeCard(
                            title = "Erasmus",
                            subtitle = "International Education",
                            icon = Icons.Default.Public,
                            mainColor = Color(0xFF00E5FF),
                            onClick = { onModeSelected("erasmus") }
                        )
                        ModeCard(
                            title = "Master",
                            subtitle = "Specialized Knowledge",
                            icon = Icons.Default.School,
                            mainColor = Color(0xFFFF4081),
                            onClick = { onModeSelected("master") }
                        )
                        ModeCard(
                            title = "Career",
                            subtitle = "Professional Growth",
                            icon = Icons.Default.Work,
                            mainColor = Color(0xFF7C4DFF),
                            onClick = { onModeSelected("career") }
                        )
                    }
                }

                item { Spacer(Modifier.height(32.dp)) }

                // Enhanced Video Preview / Feature Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.PlayCircleFilled, null, tint = Color.White, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Introductory Video", color = Color.White.copy(0.7f))
                        }
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }

                // Forum Action
                item {
                    Button(
                        onClick = onForumClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Icon(Icons.Default.Forum, null, tint = Color.Black)
                        Spacer(Modifier.width(12.dp))
                        Text("Open Community Forum", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ModeCard(title: String, subtitle: String, icon: ImageVector, mainColor: Color, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "scale")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .scale(scale)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(mainColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = mainColor, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(20.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                Text(subtitle, fontSize = 14.sp, color = Color.White.copy(alpha = 0.5f))
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = Color.White.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun ProfileAvatar(imageUrl: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.5.dp, Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF7C4DFF))), CircleShape)
            .clickable { onClick() }
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(context).load(imageUrl).circleCrop().into(this)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(Icons.Default.AccountCircle, null, tint = Color.White, modifier = Modifier.fillMaxSize().padding(8.dp))
        }
    }
}

@Composable
fun GlowOrb(modifier: Modifier, color: Color) {
    Box(
        modifier = modifier
            .size(300.dp)
            .background(color, CircleShape)
            .blur(100.dp)
    )
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
    MaterialTheme(
        colorScheme = darkColorScheme(surface = Color(0xFF161B22)),
        shapes = Shapes(extraSmall = RoundedCornerShape(16.dp))
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier.width(200.dp).border(0.5.dp, Color.White.copy(0.1f), RoundedCornerShape(16.dp))
        ) {
            DropdownMenuItem(
                text = { Text("Account") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                onClick = { onDismiss(); onAccountClick() }
            )
            DropdownMenuItem(
                text = { Text("Messages") },
                leadingIcon = { Icon(Icons.Default.Chat, null) },
                onClick = { onDismiss(); onChatClick() }
            )
            DropdownMenuItem(
                text = { Text("Conferences") },
                leadingIcon = { Icon(Icons.Default.VideoCall, null) },
                onClick = { onDismiss(); onConferenceClick() }
            )
            DropdownMenuItem(
                text = { Text("Notes") },
                leadingIcon = { Icon(Icons.Default.Description, null) },
                onClick = { onDismiss(); onNotesClick() }
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp), color = Color.White.copy(0.05f))
            DropdownMenuItem(
                text = { Text("Logout", color = Color(0xFFFF5252)) },
                leadingIcon = { Icon(Icons.Default.Logout, null, tint = Color(0xFFFF5252)) },
                onClick = { onDismiss(); onLogoutClick() }
            )
        }
    }
}