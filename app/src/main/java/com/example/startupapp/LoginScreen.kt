package com.example.startupapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.startupapp.ui.theme.StartupAppTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onCheckRegistration = viewModel::onCheckRegistration,
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onCheckRegistration: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Перевірка реєстрації",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Електронна пошта") },
            singleLine = true,
            isError = state.emailError != null,
            supportingText = state.emailError?.let { error ->
                { Text(text = error) }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Пароль") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onCheckRegistration,
            enabled = state.canCheckRegistration,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Перевірити реєстрацію")
        }

        state.result?.let { result ->
            Spacer(modifier = Modifier.height(20.dp))
            val isRegistered = result == CheckResult.REGISTERED
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isRegistered) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
                    contentColor = if (isRegistered) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isRegistered) {
                        "Користувач зареєстрований у системі"
                    } else {
                        "Користувача не зареєстровано (невірний логін або пароль)"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Тестові дані: user@mail.com / 1234",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginEmptyPreview() {
    StartupAppTheme {
        LoginContent(
            state = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onCheckRegistration = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginEmailErrorPreview() {
    StartupAppTheme {
        LoginContent(
            state = LoginUiState(
                email = "invalid-email",
                emailError = "Некоректний формат електронної пошти"
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onCheckRegistration = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginRegisteredPreview() {
    StartupAppTheme {
        LoginContent(
            state = LoginUiState(
                email = "user@mail.com",
                password = "1234",
                result = CheckResult.REGISTERED
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onCheckRegistration = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginNotFoundPreview() {
    StartupAppTheme {
        LoginContent(
            state = LoginUiState(
                email = "other@mail.com",
                password = "wrong",
                result = CheckResult.NOT_FOUND
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onCheckRegistration = {}
        )
    }
}
