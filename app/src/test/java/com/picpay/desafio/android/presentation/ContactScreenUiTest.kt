package com.picpay.desafio.android.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.picpay.desafio.android.presentation.contact.ContactScreen
import org.junit.Rule
import org.robolectric.annotation.Config
import kotlin.test.Test

@Config(sdk = [33])
class ContactScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun topBar_isDisplayed() {
        composeRule.setContent {
            ContactScreen(rememberNavController())
        }


        composeRule.onNodeWithText("Followers").assertIsDisplayed()
    }
}
