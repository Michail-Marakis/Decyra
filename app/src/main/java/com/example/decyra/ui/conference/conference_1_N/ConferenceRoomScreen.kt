package com.example.decyra.ui.conference.conference_1_N

import android.graphics.Bitmap
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.example.decyra.ui.modeSelection.OrchidPrimary
import com.example.decyra.ui.modeSelection.ProfileAvatar
import com.example.decyra.ui.modeSelection.ProfileMenuDropdown
import com.example.decyra.ui.modeSelection.PureWhite
import com.example.decyra.ui.modeSelection.SoftPinkGlow
import kotlin.math.sin

private val InkDeep = Color(0xFF1E1B4B)
private val HeroIndigoEnd = Color(0xFF312E81)
private val OrchidPrimary = Color(0xFFD946EF)
private val SoftPinkGlow = Color(0xFFFFE4FF)
private val PureWhite = Color(0xFFFFFFFF)
private val InkBlack = Color(0xFF000000)

@Composable
fun ConferenceRoomScreen(
    userName: String,
    roomCode: String,
    permissionsGranted: Boolean,
    showPermissionDenied: Boolean,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    onBackClick: () -> Unit,
    onRequestPermissions: () -> Unit,
    onDismissPermissionWarning: () -> Unit,
    onFragmentHostReady: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
    ) {
        AnimatedMeshBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ConferenceTopBar(
                userName = userName,
                roomCode = roomCode,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onBackClick = onBackClick,
                menuExpanded = menuExpanded,
                onMenuExpandedChange = { menuExpanded = it },
                onProfileClick = onProfileClick,
                onChatClick = onChatClick,
                onConferenceClick = onConferenceClick,
                onNotesClick = onNotesClick,
                onLogoutClick = onLogoutClick
            )

            if (permissionsGranted) {
                ZegoConferenceFragmentHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    onReady = onFragmentHostReady
                )
            } else {
                PermissionGate(
                    roomCode = roomCode,
                    onRequestPermissions = onRequestPermissions
                )
            }
        }
    }

    if (showPermissionDenied) {
        AlertDialog(
            onDismissRequest = onDismissPermissionWarning,
            title = {
                Text("Permissions required", fontWeight = FontWeight.Bold, color = InkDeep)
            },
            text = {
                Text(
                    "Camera and microphone access are required to join the conference room.",
                    color = Color.DarkGray
                )
            },
            confirmButton = {
                TextButton(onClick = onRequestPermissions) {
                    Text("Grant", color = OrchidPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissPermissionWarning) {
                    Text("Later", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = PureWhite
        )
    }
}

@Composable
private fun ConferenceTopBar(
    userName: String,
    roomCode: String,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    onBackClick: () -> Unit,
    menuExpanded: Boolean,
    onMenuExpandedChange: (Boolean) -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, SoftPinkGlow, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = InkDeep)
            }

            Spacer(Modifier.size(12.dp))

            Column {
                Text(
                    text = "LIVE CONFERENCE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.6.sp,
                        color = InkDeep
                    )
                )
                Text(
                    text = "$userName · Room $roomCode",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Box {
            ProfileAvatar(
                imageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onClick = {
                    onMenuExpandedChange(true)
                }
            )

            ProfileMenuDropdown(
                expanded = menuExpanded,
                onDismiss = { onMenuExpandedChange(false) },
                onAccountClick = {
                    onMenuExpandedChange(false)
                    onProfileClick()
                },
                onChatClick = {
                    onMenuExpandedChange(false)
                    onChatClick()
                },
                onConferenceClick = {
                    onMenuExpandedChange(false)
                    onConferenceClick()
                },
                onNotesClick = {
                    onMenuExpandedChange(false)
                    onNotesClick()
                },
                onLogoutClick = {
                    onMenuExpandedChange(false)
                    onLogoutClick()
                }
            )
        }
    }
}

@Composable
private fun PermissionGate(
    roomCode: String,
    onRequestPermissions: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            shadowElevation = 12.dp,
            tonalElevation = 0.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, SoftPinkGlow)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ConferenceAura()
                Spacer(Modifier.height(18.dp))
                Text(
                    text = "Room $roomCode is ready",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = InkDeep
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Enable camera and microphone access to launch the conference experience.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PermissionChip(icon = Icons.Default.Videocam, text = "Camera")
                    PermissionChip(icon = Icons.Default.Mic, text = "Microphone")
                }
                Spacer(Modifier.height(22.dp))
                Button(
                    onClick = onRequestPermissions,
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = InkDeep),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        "ENABLE & JOIN",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.4.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier
            .background(SoftPinkGlow, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = OrchidPrimary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.size(6.dp))
        Text(text = text, color = InkDeep, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}

@Composable
private fun ZegoConferenceFragmentHost(
    modifier: Modifier = Modifier,
    onReady: (Int) -> Unit
) {
    val context = LocalContext.current
    var containerId by remember { mutableStateOf<Int?>(null) }

    AndroidView(
        modifier = modifier,
        factory = {
            FragmentContainerView(it).apply {
                id = ViewCompat.generateViewId()
                containerId = id
            }
        }
    )

    LaunchedEffect(containerId) {
        containerId?.let(onReady)
    }

    DisposableEffect(context) {
        onDispose {
            val activity = context as? FragmentActivity
            val id = containerId
            if (activity != null && id != null) {
                activity.supportFragmentManager.findFragmentById(id)?.let { fragment ->
                    activity.supportFragmentManager.beginTransaction()
                        .remove(fragment)
                        .commitAllowingStateLoss()
                }
            }
        }
    }
}

@Composable
private fun ConferenceAura() {
    Box(
        modifier = Modifier.size(96.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(OrchidPrimary.copy(alpha = 0.18f), CircleShape)
                .blur(18.dp)
        )
        Box(
            modifier = Modifier
                .size(74.dp)
                .background(
                    brush = Brush.linearGradient(listOf(InkDeep, HeroIndigoEnd)),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.VideoCall, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
        }
    }
}

@Composable
private fun AnimatedMeshBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "roomMesh")
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "roomWave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(OrchidPrimary.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(
                    size.width * (0.85f + (0.05f * sin(wave * 2 * Math.PI.toFloat()))),
                    size.height * 0.1f
                ),
                radius = 1100f
            )
        )
    }
}
