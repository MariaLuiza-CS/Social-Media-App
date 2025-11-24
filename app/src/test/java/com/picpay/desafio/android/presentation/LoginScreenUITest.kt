package com.picpay.desafio.android.presentation

import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.navigation.compose.rememberNavController
import com.picpay.desafio.android.presentation.login.LoginScreen
import org.junit.Rule
import org.junit.Test
import org.robolectric.annotation.Config

@Config(sdk = [33])
class LoginScreenUITest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun should_render_login_screen_buttons() {
        composeRule.setContent {
            val nav = rememberNavController()
            LoginScreen(navHostController = nav)
        }

        composeRule.onNodeWithText("Entrar com Google").assertExists()
        composeRule.onNodeWithText("Precisa de ajuda?").assertExists()
    }

    @Test
    fun should_click_next_image_on_tap_right_side() {
        composeRule.setContent {
            val nav = rememberNavController()
            LoginScreen(navHostController = nav)
        }

        composeRule.onRoot().performTouchInput {
            click(centerRight)
        }

        composeRule.onNodeWithContentDescription("Fotos do login")
            .assertExists()
    }

    @Test
    fun should_click_help_button() {
        composeRule.setContent {
            val nav = rememberNavController()
            LoginScreen(navHostController = nav)
        }

        composeRule.onNodeWithText("Precisa de ajuda?").performClick()
    }
}
