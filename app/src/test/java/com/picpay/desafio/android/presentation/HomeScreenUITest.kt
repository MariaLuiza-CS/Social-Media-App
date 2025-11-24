package com.picpay.desafio.android.presentation

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.home.PersonItem
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config

@Config(sdk = [33])
class HomeScreenUITest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `should render user name and profile image`() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.setContent {
            PersonItem(
                context = context,
                profilePicture = "https://fake.img/profile.png",
                fistName = "Maria",
                photo = "https://fake.img/photo.png"
            )
        }

        composeRule
            .onNodeWithText("Maria")
            .assertExists()


        composeRule
            .onNodeWithContentDescription(context.getString(R.string.ctd_image_profile))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.ctd_image_feed))
            .assertExists()
    }
}