package com.example.decyra.frontend.Forum.personal_review

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.example.decyra.backend.domain.ReviewComment
import com.example.decyra.frontend.modeSelection.OrchidPrimary
import com.example.decyra.frontend.modeSelection.ProfileAvatar
import com.example.decyra.frontend.modeSelection.ProfileMenuDropdown
import com.example.decyra.frontend.modeSelection.PureWhite
import com.example.decyra.frontend.modeSelection.SoftPinkGlow
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)
val StarGold = Color(0xFFFBBF24)

@Composable
fun ReviewDetailScreen(
    type: String?,
    university: String?,
    country: String?,
    userName: String?,
    rating: Float,
    text: String?,
    likes: Int,
    comments: List<ReviewComment>,
    userNameMap: Map<String, String>,
    userAcademicMap: Map<String, String>,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    commentText: String,
    isSendingComment: Boolean,
    onCommentTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSendCommentClick: () -> Unit,
    onCommentUserClick: (ReviewComment) -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PureWhite
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedMeshBackground()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 40.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = InkDeep
                            )
                        }

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

                item { Spacer(Modifier.height(20.dp)) }

                item {
                    ReviewHeaderCard(
                        type = type,
                        university = university,
                        country = country,
                        userName = userName,
                        rating = rating,
                        text = text,
                        likes = likes
                    )
                }

                item { Spacer(Modifier.height(28.dp)) }

                item {
                    AnimatedShimmerTitle("COMMENTS (${comments.size})")
                    Spacer(Modifier.height(18.dp))
                }

                if (comments.isEmpty()) {
                    item {
                        EmptyCommentsState()
                    }
                } else {
                    items(comments, key = { it.id ?: it.hashCode() }) { comment ->
                        CommentCard(
                            comment = comment,
                            userNameMap = userNameMap,
                            userAcademicMap = userAcademicMap,
                            onClick = {

                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onCommentUserClick(comment)
                            },
                            onAvatarClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onCommentUserClick(comment)
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }

            CommentInputBar(
                commentText = commentText,
                isSendingComment = isSendingComment,
                onCommentTextChange = onCommentTextChange,
                onSendClick = {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    onSendCommentClick()
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun ReviewHeaderCard(
    type: String?,
    university: String?,
    country: String?,
    userName: String?,
    rating: Float,
    text: String?,
    likes: Int
) {
    val title = when {
        type?.equals("erasmus", ignoreCase = true) == true -> "Erasmus · $university"
        type?.equals("master", ignoreCase = true) == true -> "Master · $university"
        else -> university ?: "Review"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1E8F7)),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = InkDeep
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "by ${userName ?: "Anonymous"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                RatingStars(rating = rating)
            }

            if (!country.isNullOrBlank() && !university.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = OrchidPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "$country · $university",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = text ?: "",
                fontSize = 15.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Likes",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "$likes likes",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun CommentCard(
    comment: ReviewComment,
    userNameMap: Map<String, String>,
    userAcademicMap: Map<String, String>,
    onClick: () -> Unit,
    onAvatarClick: () -> Unit
) {
    val name = userNameMap[comment.user_id] ?: comment.user_name ?: "User"
    val academic = userAcademicMap[comment.user_id] ?: comment.academic_profile ?: ""

    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CommentUserAvatar(
                userId = comment.user_id,
                onClick = onAvatarClick
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = InkDeep
                )

                if (academic.isNotBlank()) {
                    Text(
                        text = academic,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = comment.comment_text ?: "",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun CommentUserAvatar(
    userId: String?,
    onClick: () -> Unit
) {
    var avatarUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        if (!userId.isNullOrBlank()) {
            val db = com.google.firebase.database.FirebaseDatabase.getInstance(
                "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
            )
            db.getReference("users").child(userId).get()
                .addOnSuccessListener { snapshot ->
                    val url = snapshot.child("profileImageUrl").getValue(String::class.java)
                    if (!url.isNullOrBlank()) {
                        avatarUrl = "$url?t=${System.currentTimeMillis()}"
                    }
                }
        }
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(SoftPinkGlow)
            .border(1.5.dp, OrchidPrimary, CircleShape)
            .clickable { onClick() }
    ) {
        if (!avatarUrl.isNullOrEmpty()) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        Glide.with(context).load(avatarUrl).circleCrop().into(this)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = OrchidPrimary,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        }
    }
}
@Composable
fun EmptyCommentsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.ChatBubbleOutline,
            contentDescription = null,
            tint = OrchidPrimary.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "No comments yet",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = InkDeep
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Be the first to comment",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun CommentInputBar(
    commentText: String,
    isSendingComment: Boolean,
    onCommentTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PureWhite,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = commentText,
                onValueChange = onCommentTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Write a comment...") },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrchidPrimary,
                    unfocusedBorderColor = Color(0xFFE9D5F5),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                maxLines = 3
            )

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.9f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "sendScale"
            )

            IconButton(
                onClick = onSendClick,
                enabled = commentText.isNotBlank() && !isSendingComment,
                modifier = Modifier
                    .size(48.dp)
                    .scale(scale)
                    .background(
                        if (commentText.isNotBlank()) OrchidPrimary else Color.LightGray,
                        CircleShape
                    )
            ) {
                Icon(
                    if (isSendingComment) Icons.Default.HourglassEmpty else Icons.Default.Send,
                    contentDescription = "Send comment",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun RatingStars(rating: Float) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating.toInt()) Icons.Default.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = StarGold,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun AnimatedShimmerTitle(text: String) {
    val shimmerColors = listOf(InkBlack, InkBlack, OrchidPrimary, InkBlack, InkBlack)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(3000, easing = LinearEasing)
        ),
        label = "shimmerMove"
    )

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(translateAnim - 500f, translateAnim - 500f),
                end = Offset(translateAnim, translateAnim)
            )
        )
    )
}

@Composable
fun AnimatedMeshBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(10000, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(OrchidPrimary.copy(alpha = 0.12f), Color.Transparent),
                center = Offset(
                    size.width * (0.85f + (0.05f * sin(wave * 2 * Math.PI.toFloat()))),
                    size.height * 0.12f
                ),
                radius = 1100f
            )
        )
    }
}

@Composable
fun ProfileAvatar(
    imageUrl: String?,
    bitmap: Bitmap?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(SoftPinkGlow)
            .border(2.dp, OrchidPrimary, CircleShape)
            .clickable { onClick() }
    ) {
        when {
            !imageUrl.isNullOrEmpty() -> {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            Glide.with(context).load(imageUrl).circleCrop().into(this)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            bitmap != null -> {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            setImageBitmap(bitmap)
                        }
                    },
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