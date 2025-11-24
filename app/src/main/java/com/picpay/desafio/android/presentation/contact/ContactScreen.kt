package com.picpay.desafio.android.presentation.contact

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.picpay.desafio.android.presentation.utils.ErrorScreen
import com.picpay.desafio.android.presentation.utils.shimmerEffect
import com.picpay.desafio.android.ui.monaSansFont
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    navHostController: NavHostController,
    viewModel: ContactViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (uiState.users.isEmpty()) {
            viewModel.onEvent(ContactEvent.LoadUsersList)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ContactEffect.NavigateToError -> {
                    navHostController.navigate(
                        ErrorScreen(
                            messageError = context.getString(R.string.txt_error_server)
                        )
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        stringResource(R.string.txt_followers),
                        fontFamily = monaSansFont,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(24.dp),
                        onClick = { navHostController.popBackStack() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_left),
                            contentDescription = stringResource(R.string.ctd_followers)
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(Constant.NUMBER_EIGHT) {
                        UserItemLoading(context)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(uiState.users) { user ->
                        UserItem(
                            context,
                            user.name,
                            user.username,
                            user.img
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    context: Context,
    name: String?,
    username: String?,
    img: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(img)
                .diskCachePolicy(CachePolicy.ENABLED)
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
                text = name ?: stringResource(R.string.txt_error_name),
                fontFamily = monaSansFont,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

            Text(
                text = username ?: stringResource(R.string.txt_error_nickname),
                fontFamily = monaSansFont,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun UserItemLoading(context: Context) {
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

            Spacer(modifier = Modifier.height(4.dp))

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
}

@Serializable
object ContactScreen
