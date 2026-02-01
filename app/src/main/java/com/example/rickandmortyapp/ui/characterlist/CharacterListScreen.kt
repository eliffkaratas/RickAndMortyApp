package com.example.rickandmortyapp.ui.characterlist

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.model.CharacterResponse
import com.example.rickandmortyapp.network.repository.CharacterUiEffect
import com.example.rickandmortyapp.network.repository.CharacterUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.min

@Preview(
    name = "Character List – Success",
    showBackground = true,
    backgroundColor = 0xFF000000,
    device = Devices.PIXEL_7
)
@Composable
fun CharacterListScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    CharacterListScreen(
        uiState = CharacterUiState.Success(
            data = CharacterResponse(
                info = CharacterResponse.CharacterInfo(
                    count = 20,
                    pages = 1,
                    next = null,
                    prev = null
                ),
                results = listOf(
                    CharacterResponse.CharacterResult(
                        id = 1,
                        name = "Alien Morty",
                        status = "unknown",
                        species = "Alien",
                        gender = "Male",
                        image = "https://rickandmortyapi.com/api/character/avatar/14.jpeg",
                        created = "2017-11-04",
                        characterLocation = CharacterResponse.CharacterResult.CharacterLocation(
                            name = "Unknown",
                            url = ""
                        ),
                        characterOrigin = CharacterResponse.CharacterResult.CharacterOrigin(
                            name = "Unknown",
                            url = ""
                        ),
                        type = "Human",
                        episode = listOf("Episode 2", "Episode 3"),
                        url = ""
                    )
                )
            )
        ),
        onCharacterClick = {},
        snackbarHostState = snackbarHostState,
        onRetry = {}
    )
}

@Preview(name = "Loading", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun CharacterListLoadingPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    CharacterListScreen(
        uiState = CharacterUiState.Loading,
        onCharacterClick = {},
        snackbarHostState = snackbarHostState,
        onRetry = {}
    )
}

@Preview(name = "Error", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun CharacterListErrorPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    CharacterListScreen(
        uiState = CharacterUiState.Error(stringResource(R.string.network_error)),
        onCharacterClick = {},
        snackbarHostState = snackbarHostState,
        onRetry = {}
    )
}

@Composable
fun CharacterListRoute(
    viewModel: CharacterViewModel,
    onCharacterClick: (CharacterResponse.CharacterResult) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // one-shot effects (snackbar)
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CharacterUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    CharacterListScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onRetry = { viewModel.refresh() },
        onCharacterClick = onCharacterClick
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    uiState: CharacterUiState,
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit,
    onCharacterClick: (CharacterResponse.CharacterResult) -> Unit
) {
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rick_and_morty),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding: PaddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        ) {
            when (uiState) {
                is CharacterUiState.Idle -> Unit

                is CharacterUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val isPreview = LocalInspectionMode.current

                        if (isPreview) {
                            CircularProgressIndicator()
                        } else {
                            LottieLoading(modifier = Modifier.size(120.dp))
                        }

                        Spacer(Modifier.height(12.dp))
                        SkeletonPager()
                    }
                }

                is CharacterUiState.Error -> {
                    ErrorState(
                        message = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CharacterUiState.Success -> {
                    val list = uiState.data.results
                    if (list.isEmpty()) {
                        EmptyState(
                            onRetry = onRetry,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        return@Box
                    }

                    // Fake infinite pager
                    val startPage = remember(list.size) {
                        val middle = Int.MAX_VALUE / 2
                        middle - (middle % list.size)
                    }

                    val pagerState = rememberPagerState(
                        initialPage = startPage,
                        pageCount = { Int.MAX_VALUE }
                    )
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(list.size) {
                        snapshotFlow { pagerState.isScrollInProgress }
                            .collectLatest { }
                    }

                    LaunchedEffect(list.size) {
                        while (true) {
                            while (pagerState.isScrollInProgress) {
                                delay(200)
                            }

                            delay(3000)

                            if (pagerState.isScrollInProgress) continue

                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }

                    val realIndex = pagerState.currentPage % list.size

                    Box(
                        modifier = Modifier.wrapContentSize().padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            HorizontalPager(
                                state = pagerState,
                                contentPadding = PaddingValues(horizontal = 80.dp),
                                flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) { page ->
                                val item = list[page % list.size]

                                val pageOffset =
                                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                                val scale = 0.85f + (1f - min(1f, pageOffset)) * 0.15f

                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                    CharacterCard(character = item, scale = scale, onClick = { onCharacterClick(item) })
                                }
                            }

                            Spacer(Modifier.height(14.dp))

                            PagerIndicator(count = list.size, selectedIndex = realIndex)

                            Spacer(Modifier.height(18.dp))

                            Text(
                                text = stringResource(R.string.info),
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterCard(
    character: CharacterResponse.CharacterResult,
    scale: Float,
    onClick: () -> Unit
) {
    val accent = when {
        character.id != null && character.id % 3 == 0 -> Color(0xFFFFA500)
        character.id != null && character.id % 2 == 0 -> Color(0xFFFF1493)
        else -> Color(0xFFBADA55)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(178.dp)
                    .clip(CircleShape)
                    .border(width = 4.dp, color = accent, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image.orEmpty())
                        .crossfade(true)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .build(),
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = character.name.orEmpty(),
                color = accent,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.get_schwifty)),
                fontWeight = FontWeight.Thin,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 48.sp
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Chip(
                    text = character.status.orEmpty().ifBlank { stringResource(R.string.unknown) },
                    color = statusColor(character.status)
                )
                Spacer(Modifier.size(8.dp))
                Chip(
                    text = character.species.orEmpty().ifBlank { stringResource(R.string.unknown) },
                    color = Color(0xFF2A2A2A)
                )
            }
        }
    }
}

@Composable
private fun Chip(text: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private fun statusColor(status: String?): Color {
    return when (status?.lowercase()) {
        "alive" -> Color(0xFF1E7F3B)
        "dead" -> Color(0xFF8B1A1A)
        else -> Color(0xFF3A3A3A)
    }
}

@Composable
private fun PagerIndicator(
    count: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    if (count > 10) {
        Text(
            text = "${selectedIndex + 1} / $count",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 14.sp,
            modifier = modifier
        )
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { i ->
            val isSelected = i == selectedIndex
            Box(
                modifier = Modifier
                    .size(if (isSelected) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color(0xFFBADA55) else Color.White.copy(alpha = 0.35f)
                    )
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message.ifBlank { stringResource(R.string.error) },
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.try_again))
        }
    }
}

@Composable
private fun EmptyState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.not_found),
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.try_again))
        }
    }
}

@Composable
private fun SkeletonPager() {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 }
    )

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 80.dp),
        pageSpacing = 10.dp,
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
    ) { page ->
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
        val scale = 0.85f + (1f - min(1f, pageOffset)) * 0.15f

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .alpha(alpha),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2A2A2A))
                    )

                    Spacer(Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth(0.7f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF2A2A2A))
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(26.dp)
                                .fillMaxWidth(0.25f)
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFF2A2A2A))
                        )
                        Spacer(Modifier.size(8.dp))
                        Box(
                            modifier = Modifier
                                .height(26.dp)
                                .fillMaxWidth(0.25f)
                                .clip(RoundedCornerShape(999.dp))
                                .background(Color(0xFF2A2A2A))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LottieLoading(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_lottie))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}