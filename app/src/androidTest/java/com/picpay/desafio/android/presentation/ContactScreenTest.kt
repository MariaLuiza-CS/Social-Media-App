package com.picpay.desafio.android.presentation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.picpay.desafio.android.presentation.contact.UserItem
import com.picpay.desafio.android.presentation.contact.UserItemLoading
import org.junit.Rule
import org.junit.Test

class ContactScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun userItem_displaysCorrectNameAndUsername() {
        val fakeName = "Ada Lovelace"
        val fakeUsername = "@ada"

        composeRule.setContent {
            UserItem(
                name = fakeName,
                username = fakeUsername,
                img = null
            )
        }

        composeRule.onNodeWithText(fakeName).assertExists()
        composeRule.onNodeWithText(fakeUsername).assertExists()
    }

    @Test
    fun userItem_showsFallbackTextsWhenNull() {
        composeRule.setContent {
            UserItem(
                name = null,
                username = null,
                img = null
            )
        }

        composeRule.onNodeWithText("Erro ao carregar nome").assertExists()
        composeRule.onNodeWithText("Erro ao carregar nickname").assertExists()
    }

    @Test
    fun userItemLoading_showsThreeLoadingBlocks() {
        composeRule.setContent {
            UserItemLoading(context = LocalContext.current)
        }

        composeRule
            .onAllNodesWithContentDescription("Carregando dados")
            .assertCountEquals(3)
    }
}