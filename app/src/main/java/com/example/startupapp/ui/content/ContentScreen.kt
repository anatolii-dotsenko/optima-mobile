package com.example.startupapp.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.startupapp.ui.theme.StartupAppTheme

private data class StructureItem(
    val title: String,
    val description: String
)

private val androidStructureItems = listOf(
    StructureItem(
        title = "AndroidManifest.xml",
        description = "Описує точку входу застосунку (.StartupApp), активності, дозволи та налаштування платформи Android."
    ),
    StructureItem(
        title = "res/ (values, drawable, mipmap)",
        description = "Ресурси застосунку: рядки, кольори, теми, іконки запуску та конфігурації XML."
    ),
    StructureItem(
        title = "StartupApp (Application)",
        description = "Кореневий контейнер застосунку, що зберігає єдиний singleton-екземпляр SessionRepository."
    ),
    StructureItem(
        title = "data/SessionRepository",
        description = "Singleton-сервіс-репозиторій для спільного стану авторизації між сторінками через StateFlow."
    ),
    StructureItem(
        title = "navigation/AppShell",
        description = "Оболонка навігації з боковим меню (ModalNavigationDrawer), TopAppBar та NavHost."
    )
)

@Composable
fun ContentScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContentViewModel = viewModel(factory = ContentViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ContentBody(
        state = state,
        onLogout = viewModel::onLogout,
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier
    )
}

@Composable
fun ContentBody(
    state: ContentUiState,
    onLogout: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!state.isAuthorized) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Доступ обмежено",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Для перегляду контенту необхідно спочатку зареєструватися на головній сторінці.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Перейти на головну сторінку")
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Доступ відкрито",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Ви увійшли як: ${state.registeredEmail}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Text(
                text = "Структура Android-проєкту",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            androidStructureItems.forEach { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Вийти")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentUnauthorizedPreview() {
    StartupAppTheme {
        ContentBody(
            state = ContentUiState(registeredEmail = null),
            onLogout = {},
            onNavigateToLogin = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentAuthorizedPreview() {
    StartupAppTheme {
        ContentBody(
            state = ContentUiState(registeredEmail = "user@mail.com"),
            onLogout = {},
            onNavigateToLogin = {}
        )
    }
}
