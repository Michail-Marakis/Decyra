package com.example.decyra.frontend.Profile_Menu.account_settings

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.decyra.R
import kotlin.math.sin

val InkBlack = Color(0xFF000000)
val InkDeep = Color(0xFF1E1B4B)
val HeroIndigoEnd = Color(0xFF312E81)
val OrchidPrimary = Color(0xFFD946EF)
val OrchidLight = Color(0xFFFDF4FF)
val SoftPinkGlow = Color(0xFFFFE4FF)
val PureWhite = Color(0xFFFFFFFF)

@Composable
fun AccountScreen(
    userFullName: String,
    userEmail: String,
    userPhone: String,
    university: String,
    academicLevel: String,
    field: String,
    languages: String,
    gpa: String,
    budget: String,
    yearOfStudies: String,
    advisorType: String,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    isUploading: Boolean,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onEditAcademicClick: () -> Unit,
    onChangePhotoClick: () -> Unit
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            AccountTopBar(onBackClick = onBackClick)

            Spacer(Modifier.height(24.dp))

            AccountHeroCard(
                userFullName = userFullName,
                userEmail = userEmail,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                isUploading = isUploading,
                onChangePhotoClick = onChangePhotoClick
            )

            Spacer(Modifier.height(22.dp))

            DataCard(
                title = "PERSONAL INFORMATION",
                items = listOf(
                    "Full name" to userFullName,
                    "Email" to userEmail,
                    "Phone" to userPhone
                )
            )

            Spacer(Modifier.height(16.dp))

            DataCard(
                title = "ACADEMIC INFORMATION",
                items = listOf(
                    "University" to university,
                    "Level" to academicLevel,
                    "Field" to field,
                    "Languages" to languages,
                    "GPA" to gpa,
                    "Budget per year" to budget,
                    "Year of studies" to yearOfStudies,
                    "Advisor type" to advisorType
                )
            )

            Spacer(Modifier.height(20.dp))

            PremiumPrimaryButton("EDIT PROFILE", onEditProfileClick)
            Spacer(Modifier.height(12.dp))
            PremiumSecondaryButton("EDIT ACADEMIC", onEditAcademicClick)
        }
    }
}

@Composable
fun AccountTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SoftPinkGlow)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = InkDeep)
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

        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
fun AccountHeroCard(
    userFullName: String,
    userEmail: String,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    isUploading: Boolean,
    onChangePhotoClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(InkDeep, HeroIndigoEnd),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .padding(24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box {
                ProfileAvatar(
                    imageUrl = profileImageUrl,
                    profileBitmap = profileBitmap,
                    onClick = onChangePhotoClick,
                    size = 92.dp
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(OrchidPrimary)
                        .clickable { onChangePhotoClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = "Change photo",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = userFullName,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            )
            Text(
                text = userEmail,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 13.sp
            )

            AnimatedVisibility(visible = isUploading) {
                Text(
                    text = "Uploading new photo...",
                    color = OrchidPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileAvatar(
    imageUrl: String?,
    profileBitmap: Bitmap?,
    onClick: () -> Unit,
    size: Dp = 48.dp
) {
    Box(
        modifier = Modifier
            .size(size)
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
                            Glide.with(context)
                                .load(imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
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
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun DataCard(
    title: String,
    items: List<Pair<String, String>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.96f))
            .border(1.dp, Color(0xFFF1E8F7), RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        AnimatedShimmerTitle(title)
        Spacer(Modifier.height(16.dp))

        items.forEachIndexed { index, pair ->
            Column {
                Text(pair.first, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(pair.second, color = InkDeep, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                if (index != items.lastIndex) {
                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = SoftPinkGlow)
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
fun PremiumPrimaryButton(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "primaryScale"
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
        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(10.dp))
        Text(text = text, color = Color.White, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
    }
}

@Composable
fun PremiumSecondaryButton(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "secondaryScale"
    )

    OutlinedButton(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .scale(scale),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.4.dp, OrchidPrimary.copy(alpha = 0.35f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = OrchidLight,
            contentColor = InkDeep
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AnimatedShimmerTitle(text: String) {
    val shimmerColors = listOf(InkBlack, InkBlack, OrchidPrimary, InkBlack, InkBlack)
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
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