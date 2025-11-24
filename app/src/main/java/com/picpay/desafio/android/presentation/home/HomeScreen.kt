package com.picpay.desafio.android.presentation.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.picpay.desafio.android.R
import com.picpay.desafio.android.domain.util.Constant
import com.picpay.desafio.android.presentation.utils.shimmerEffect
import com.picpay.desafio.android.ui.monaSansFont
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        if (uiState.isLoading) {
            LazyColumn(
                state = listState
            ) {
                items(Constant.NUMBER_EIGHT) {
                    PersonItemLoading(context)
                }
            }
            return@Box
        }

        LazyColumn(
            state = listState
        ) {
            items(
                uiState.peopleList,
                key = { people -> people?.id ?: "" }
            ) { people ->
                PersonItem(
                    context,
                    people?.profilePicture,
                    people?.fistName,
                    people?.photos?.getOrNull(0)?.url
                )
            }
        }
    }
}

@Composable
fun PersonItem(
    context: Context,
    profilePicture: String?,
    fistName: String?,
    photo: String?
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profilePicture)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.ctd_image_profile),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    loading = {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .shimmerEffect()
                        )
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .semantics {
                                    contentDescription =
                                        context.getString(R.string.ctd_error_loading_data)
                                }
                        )
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = fistName ?: stringResource(R.string.txt_error_name),
                        fontFamily = monaSansFont,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    )
                }
            }

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .crossfade(false)
                    .build(),
                contentDescription = stringResource(R.string.ctd_image_feed),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.FillWidth,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 180.dp)
                            .shimmerEffect()
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 180.dp),
                        contentAlignment = Alignment.Center,

                        ) {
                        Text(
                            text = stringResource(R.string.txt_error_download_image),
                            fontFamily = monaSansFont,
                            fontWeight = FontWeight.Thin,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun PersonItemLoading(context: Context) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                        .semantics {
                            contentDescription = context.getString(R.string.ctd_loading_data)
                        }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                            .semantics {
                                contentDescription = context.getString(R.string.ctd_loading_data)
                            }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
                    .semantics {
                        contentDescription = context.getString(R.string.ctd_loading_data)
                    }
            )
        }
    }
}

@Serializable
object HomeScreen
