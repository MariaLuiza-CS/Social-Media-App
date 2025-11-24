package com.picpay.desafio.android.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.help.HelpScreen
import com.picpay.desafio.android.presentation.home.HomeScreen
import com.picpay.desafio.android.presentation.utils.ErrorScreen
import com.picpay.desafio.android.ui.monaSansFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val images = listOf(
        R.drawable.img_one_login,
        R.drawable.img_two_login,
        R.drawable.img_three_login,
        R.drawable.img_four_login,
        R.drawable.img_five_login
    )

    val pagerState = rememberPagerState(pageCount = { images.size })
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToError -> {
                    navHostController.navigate(
                        ErrorScreen(
                            messageError = context.getString(R.string.txt_error_server)
                        )
                    )
                }

                is LoginEffect.NavigateToHome -> {
                    navHostController.navigate(
                        HomeScreen
                    )
                }
            }
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val screenWidth = size.width
                            val tappedX = offset.x
                            coroutineScope.launch {
                                if (tappedX > screenWidth / 2) {
                                    val next =
                                        (pagerState.currentPage + 1).coerceAtMost(images.size - 1)
                                    pagerState.animateScrollToPage(next)
                                } else {
                                    val prev = (pagerState.currentPage - 1).coerceAtLeast(0)
                                    pagerState.animateScrollToPage(prev)
                                }
                            }
                        }
                    },
                userScrollEnabled = false
            ) { page ->
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = stringResource(R.string.ctd_login_photos),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val response =
                                    credentialManager.getCredential(
                                        context = context,
                                        request = request
                                    )
                                val googleCredential =
                                    GoogleIdTokenCredential.createFrom(response.credential.data)
                                val idToken = googleCredential.idToken

                                viewModel.onEvent(LoginEvent.SignInGoogle(idToken))
                            } catch (e: Exception) {
                                navHostController.navigate(
                                    ErrorScreen(
                                        messageError = e.message
                                    )
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.btn_login),
                        fontFamily = monaSansFont,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navHostController.navigate(HelpScreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(R.string.btn_help),
                        fontFamily = monaSansFont,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Serializable
object LoginScreen
