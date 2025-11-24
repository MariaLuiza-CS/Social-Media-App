package com.picpay.desafio.android.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.contact.ContactScreen
import com.picpay.desafio.android.presentation.utils.shimmerEffect
import com.picpay.desafio.android.ui.monaSansFont
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            ProfileEvent.LoadCurrentUser
        )
        viewModel.onEvent(
            ProfileEvent.LoadContactUserList
        )
        viewModel.onEvent(
            ProfileEvent.LoadPeopleWithPhotoList
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        if (uiState.isLoading == true) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                        .semantics {
                            contentDescription = context.getString(R.string.ctd_loading_data)
                        }
                )

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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
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

                    Spacer(modifier = Modifier.width(8.dp))

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
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiState.currentUser?.img)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.ctd_image_profile),
                modifier = Modifier
                    .size(100.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.currentUser?.name ?: stringResource(R.string.txt_error_name),
                fontFamily = monaSansFont,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.currentUser?.email ?: stringResource(R.string.txt_error_email),
                fontFamily = monaSansFont,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = uiState.peopleWithPhotosList.size.toString(),
                    fontFamily = monaSansFont,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.txt_followers),
                    fontFamily = monaSansFont,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.txt_bullet_char),
                    fontFamily = monaSansFont,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = uiState.contactUsersList.size.toString(),
                    fontFamily = monaSansFont,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = Modifier.clickable {
                        navHostController.navigate(ContactScreen)
                    },
                    text = stringResource(R.string.txt_following),
                    fontFamily = monaSansFont,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.onEvent(
                        ProfileEvent.SignOut
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.background,
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_sign_out),
                    fontFamily = monaSansFont,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.height(1.dp),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(100.dp))

            Icon(
                modifier = Modifier.size(80.dp),
                painter = painterResource(R.drawable.ic_photo),
                contentDescription = stringResource(R.string.ctd_no_feed_photos),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.txt_create_first_photo),
                fontFamily = monaSansFont,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )

        }
    }
}

@Serializable
object ProfileScreen
