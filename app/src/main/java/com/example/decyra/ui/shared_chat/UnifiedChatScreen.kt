package com.example.decyra.ui.shared_chat

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink

fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.lines()

        lines.forEachIndexed { index, rawLine ->
            val line = rawLine.trimEnd()

            when {
                line.isBlank() -> append("\n")

                isHorizontalRule(line) -> {
                    withStyle(
                        SpanStyle(color = Color.Gray)
                    ) {
                        append("────────")
                    }
                    if (index != lines.lastIndex) append("\n")
                }

                line.startsWith("### ") -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        appendInlineMarkdown(line.removePrefix("### "))
                    }
                    if (index != lines.lastIndex) append("\n")
                }

                line.startsWith("## ") -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        appendInlineMarkdown(line.removePrefix("## "))
                    }
                    if (index != lines.lastIndex) append("\n")
                }

                line.startsWith("# ") -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        appendInlineMarkdown(line.removePrefix("# "))
                    }
                    if (index != lines.lastIndex) append("\n")
                }

                line.startsWith("- ") || line.startsWith("* ") -> {
                    append("• ")
                    appendInlineMarkdown(line.drop(2))
                    if (index != lines.lastIndex) append("\n")
                }

                else -> {
                    appendInlineMarkdown(line)
                    if (index != lines.lastIndex) append("\n")
                }
            }
        }
    }
}

private fun isHorizontalRule(line: String): Boolean {
    val trimmed = line.trim()
    return trimmed == "---" || trimmed == "***" || trimmed == "___"
}

private fun AnnotatedString.Builder.appendInlineMarkdown(text: String) {
    var i = 0

    while (i < text.length) {
        when {
            text.startsWith("**", i) -> {
                val end = text.indexOf("**", startIndex = i + 2)
                if (end != -1) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        appendInlineMarkdown(text.substring(i + 2, end))
                    }
                    i = end + 2
                } else {
                    append(text[i])
                    i++
                }
            }

            text.startsWith("`", i) -> {
                val end = text.indexOf("`", startIndex = i + 1)
                if (end != -1) {
                    withStyle(
                        SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            background = Color(0xFFEAEAEA)
                        )
                    ) {
                        append(text.substring(i + 1, end))
                    }
                    i = end + 1
                } else {
                    append(text[i])
                    i++
                }
            }

            text.startsWith("[", i) -> {
                val closeText = text.indexOf("]", startIndex = i + 1)
                val openUrl = if (closeText != -1) text.indexOf("(", startIndex = closeText) else -1
                val closeUrl = if (openUrl != -1) text.indexOf(")", startIndex = openUrl) else -1

                if (closeText != -1 && openUrl == closeText + 1 && closeUrl != -1) {
                    val label = text.substring(i + 1, closeText)
                    val url = text.substring(openUrl + 1, closeUrl)

                    pushStringAnnotation(tag = "URL", annotation = url)
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF1565C0),
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(label)
                    }
                    pop()

                    i = closeUrl + 1
                } else {
                    append(text[i])
                    i++
                }
            }

            text.startsWith("*", i) -> {
                val end = text.indexOf("*", startIndex = i + 1)
                if (end != -1) {
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(text.substring(i + 1, end))
                    }
                    i = end + 1
                } else {
                    append(text[i])
                    i++
                }
            }

            else -> {
                append(text[i])
                i++
            }
        }
    }
}

private fun AnnotatedString.Builder.appendInlineMarkdown(
    text: String,
    boldRegex: Regex,
    italicRegex: Regex,
    codeRegex: Regex,
    linkRegex: Regex
) {
    var currentIndex = 0

    val matches = buildList {
        addAll(boldRegex.findAll(text).map { MatchToken("bold", it.range.first, it.range.last + 1, it.groupValues[1], null) })
        addAll(italicRegex.findAll(text).map { MatchToken("italic", it.range.first, it.range.last + 1, it.groupValues[1], null) })
        addAll(codeRegex.findAll(text).map { MatchToken("code", it.range.first, it.range.last + 1, it.groupValues[1], null) })
        addAll(linkRegex.findAll(text).map { MatchToken("link", it.range.first, it.range.last + 1, it.groupValues[1], it.groupValues[2]) })
    }.sortedBy { it.start }

    for (match in matches) {
        if (match.start < currentIndex) continue

        append(text.substring(currentIndex, match.start))

        when (match.type) {
            "bold" -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(match.content)
            }
            "italic" -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                append(match.content)
            }
            "code" -> withStyle(
                SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    background = Color(0xFFEAEAEA)
                )
            ) {
                append(match.content)
            }
            "link" -> withLink(
                LinkAnnotation.Url(
                    match.extra.orEmpty(),
                    TextLinkStyles(
                        style = SpanStyle(
                            color = Color(0xFF1565C0)
                        )
                    )
                )
            ) {
                append(match.content)
            }
        }

        currentIndex = match.end
    }

    if (currentIndex < text.length) {
        append(text.substring(currentIndex))
    }
}

private data class MatchToken(
    val type: String,
    val start: Int,
    val end: Int,
    val content: String,
    val extra: String?
)

@Composable
fun UnifiedChatScreen(
    title: String,
    subtitle: String,
    userFullName: String?,
    profileImageUrl: String?,
    profileBitmap: Bitmap?,
    inputText: String,
    messages: List<String>,
    isSending: Boolean,
    placeholder: String = "How can I help you?",
    onInputChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onSaveClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit,
    onConferenceClick: () -> Unit,
    onNotesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val animatedFinishedMessages = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        if (inputText.isNotBlank() && messages.isEmpty()) {
            onSendClick()
        }
    }

    LaunchedEffect(messages.size, isSending) {
        if (messages.isNotEmpty() || isSending) {
            listState.animateScrollToItem(if (isSending) messages.size else messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PureWhite,
        topBar = {
            PremiumTopBar(
                title = title,
                subtitle = subtitle,
                profileImageUrl = profileImageUrl,
                profileBitmap = profileBitmap,
                onBackClick = onBackClick,
                onAvatarClick = { menuExpanded = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedMeshBackground()

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
                ) {
                    itemsIndexed(
                        items = messages,
                        key = { index, message -> "$index-${message.hashCode()}" }
                    ) { index, message ->
                        val isAssistant = message.startsWith("Assistant:")
                        val isError = message.startsWith("Error:")
                        val cleanText = when {
                            isAssistant -> message.substringAfter("Assistant:\n")
                            isError -> message.substringAfter("Error: ")
                            else -> message.substringAfter("You: ")
                        }

                        ChatBubble(
                            message = cleanText,
                            isUser = !isAssistant && !isError,
                            isError = isError,
                            isLatestAssistant = index == messages.size - 1 && isAssistant,
                            alreadyAnimated = animatedFinishedMessages.contains(cleanText),
                            onAnimationFinished = {
                                if (!animatedFinishedMessages.contains(cleanText)) {
                                    animatedFinishedMessages.add(cleanText)
                                }
                            },
                            onCharTyped = {
                                scope.launch {
                                    listState.scrollToItem(messages.size - 1)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (isSending) {
                        item(key = "loading-indicator") {
                            ChatBubble(
                                message = "",
                                isUser = false,
                                isError = false,
                                isLatestAssistant = true,
                                alreadyAnimated = false,
                                onAnimationFinished = {},
                                onCharTyped = {}
                            )
                        }
                    }
                }

                ChatInputArea(
                    inputText = inputText,
                    isSending = isSending,
                    onInputChange = onInputChange,
                    onSendClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSendClick()
                    },
                    onVoiceClick = onVoiceClick,
                    onSaveClick = onSaveClick,
                    placeholder = placeholder
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 8.dp)
            ) {
                ProfileMenuDropdown(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onAccountClick = onProfileClick,

                    onChatClick = onChatClick,
                    onConferenceClick = onConferenceClick,
                    onNotesClick = onNotesClick,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: String,
    isUser: Boolean,
    isError: Boolean,
    isLatestAssistant: Boolean,
    alreadyAnimated: Boolean,
    onAnimationFinished: () -> Unit,
    onCharTyped: () -> Unit
) {
    var skipAnimation by remember(message, alreadyAnimated) {
        mutableStateOf(alreadyAnimated || !isLatestAssistant)
    }

    val haptic = LocalHapticFeedback.current

    val alignment = if (isUser) Alignment.End else Alignment.Start
    val textColor = when {
        isUser -> Color.White
        isError -> Color(0xFFB91C1C)
        else -> InkDeep
    }

    val shape = if (isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    val bubbleBrush = when {
        isUser -> Brush.linearGradient(listOf(InkDeep, HeroIndigoEnd))
        isError -> Brush.linearGradient(listOf(Color(0xFFFFE4E6), Color(0xFFFFF1F2)))
        else -> Brush.linearGradient(listOf(AssistantBubbleBg, Color.White))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser && !isError) {
                NeuralPrismAura(isSpeaking = isLatestAssistant && !skipAnimation)
                Spacer(Modifier.width(10.dp))
            }

            Surface(
                modifier = Modifier
                    .widthIn(max = 290.dp)
                    .clip(shape)
                    .clickable {
                        if (isLatestAssistant && !skipAnimation && message.isNotEmpty()) {
                            skipAnimation = true
                            onAnimationFinished()
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    },
                color = Color.Transparent,
                border = if (!isUser && !isError) BorderStroke(1.dp, Color(0xFFF1E8F7)) else null,
                shadowElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .background(bubbleBrush)
                        .padding(14.dp)
                ) {
                    Column {
                        if (isLatestAssistant && !skipAnimation && !isError) {
                            if (message.isEmpty()) {
                                ThinkingShimmerText()
                            } else {
                                TypewriterText(
                                    text = message,
                                    color = textColor,
                                    onCharTyped = onCharTyped,
                                    onFinish = {
                                        skipAnimation = true
                                        onAnimationFinished()
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    }
                                )
                                Spacer(Modifier.height(10.dp))
                                FastForwardBadge()
                            }
                        } else {
                            Text(
                                text = parseMarkdown(message),
                                color = textColor,
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}