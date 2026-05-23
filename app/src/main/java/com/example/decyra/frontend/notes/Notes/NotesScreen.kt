package com.example.decyra.frontend.notes

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.decyra.R
import com.example.decyra.backend.domain.Note
import com.example.decyra.frontend.modeSelection.ProfileAvatar
import com.example.decyra.frontend.modeSelection.ProfileMenuDropdown
import java.text.DateFormat
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)

@Composable
fun NotesScreen(
    notes: List<Note>,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    onBackClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onPinNote: (Note) -> Unit,
    onUnpinNote: (Note) -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
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
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            NotesTopBar(
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onBackClick = onBackClick,
                onProfileClick = onProfileClick,
                onChatClick = onChatClick,
                onConferenceClick = onConferenceClick,
                onNotesClick = onNotesClick,
                onLogoutClick = onLogoutClick
            )

            Spacer(Modifier.height(24.dp))

            Spacer(Modifier.height(18.dp))

            AddNoteButton(onAddNoteClick)

            Spacer(Modifier.height(18.dp))

            if (notes.isEmpty()) {
                EmptyNotesCard()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 30.dp)
                ) {
                    items(notes, key = { it.id ?: it.createdTime }) { note ->
                        NoteCard(
                            note = note,
                            onClick = { onNoteClick(note) },
                            onDelete = { onDeleteNote(note) },
                            onPinToggle = {
                                if (note.isPinned) onUnpinNote(note) else onPinNote(note)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotesTopBar(
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    var menuExpanded by remember { mutableStateOf(false) }
    // Καλούμε το Haptic Feedback
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SoftPinkGlow)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = InkDeep
            )
        }

        Text(
            text = "DECYRA",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                letterSpacing = 8.sp,
                brush = Brush.linearGradient(colors = listOf(InkDeep, OrchidPrimary))
            )
        )

        Box {
            ProfileAvatar(
                imageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    menuExpanded = true
                }
            )

            ProfileMenuDropdown(
                expanded = menuExpanded,
                onDismiss = { menuExpanded = false },
                onChatClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onChatClick()
                },
                onConferenceClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onConferenceClick()
                },
                onNotesClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onNotesClick()
                },
                onLogoutClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLogoutClick()
                },
                onAccountClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onProfileClick()
                }
            )
        }
    }
}

@Composable
fun AddNoteButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "addScale"
    )

    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(containerColor = InkDeep)
    ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(10.dp))
        Text(
            text = "ADD NEW NOTE",
            color = Color.White,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.4.sp
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onPinToggle: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "noteScale"
    )

    Box {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.97f)
            ),
            border = BorderStroke(1.dp, Color(0xFFF1E8F7)),
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                    onLongClick = { menuExpanded = true },
                    onLongClickLabel = "Show note actions"
                )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(OrchidLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            tint = OrchidPrimary
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = note.title,
                            color = InkDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        val formatted = try {
                            DateFormat.getDateTimeInstance().format(note.createdTime)
                        } catch (_: Exception) {
                            "-"
                        }

                        Text(
                            text = formatted,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }

                    if (note.isPinned) {
                        Icon(
                            Icons.Default.PushPin,
                            contentDescription = "Pinned",
                            tint = OrchidPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = note.description ?: "",
                    color = Color(0xFF4B5563),
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(if (note.isPinned) "UNPIN" else "PIN") },
                leadingIcon = {
                    Icon(
                        Icons.Default.PushPin,
                        contentDescription = null,
                        tint = OrchidPrimary
                    )
                },
                onClick = {
                    menuExpanded = false
                    onPinToggle()
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        "DELETE",
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color(0xFFEF4444)
                    )
                },
                onClick = {
                    menuExpanded = false
                    onDelete()
                }
            )
        }
    }
}

@Composable
fun EmptyNotesCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.dp, Color(0xFFF1E8F7), RoundedCornerShape(28.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Description,
            contentDescription = null,
            tint = OrchidPrimary,
            modifier = Modifier.size(42.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No notes yet",
            color = InkDeep,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Create your first note to start organizing ideas.",
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ProfileAvatar(
    imageUrl: String?,
    profileBitmap: Bitmap?,
    size: Dp = 48.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(SoftPinkGlow)
            .border(2.dp, OrchidPrimary, CircleShape)
    ) {
        when {
            !imageUrl.isNullOrEmpty() -> {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            Glide.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.baseline_face_24)
                                .error(R.drawable.baseline_face_24)
                                .circleCrop()
                                .into(this)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            profileBitmap != null -> {
                Image(
                    bitmap = profileBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = OrchidPrimary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun AnimatedMeshBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave"
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

@Composable
fun NeuralPrismAura() {
    val infiniteTransition = rememberInfiniteTransition(label = "prism")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing)
        ),
        label = "rot"
    )

    Box(
        modifier = Modifier
            .size(82.dp)
            .rotate(rotation)
            .drawBehind {
                drawCircle(
                    brush = Brush.sweepGradient(
                        listOf(OrchidPrimary, Color.Transparent, OrchidPrimary)
                    ),
                    style = Stroke(width = 6f, cap = StrokeCap.Round)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(OrchidPrimary.copy(alpha = 0.3f), Color.Transparent)
                    ),
                    radius = size.width / 1.5f
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Description,
            contentDescription = null,
            tint = OrchidPrimary,
            modifier = Modifier.size(28.dp)
        )
    }
}