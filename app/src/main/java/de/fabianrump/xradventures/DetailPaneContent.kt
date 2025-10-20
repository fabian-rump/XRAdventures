package de.fabianrump.xradventures

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import de.fabianrump.xradventures.ui.theme.XRAdventuresTheme

@Composable
fun DetailPaneContent(fishItem: FishItem) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(height = 24.dp))
        Row {
            Text(
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(horizontal = 16.dp),
                text = fishItem.name,
                style = MaterialTheme.typography.headlineMedium
            )
            // Add small picture of fish here
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = fishItem.description,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(height = 24.dp))
    }
}

@PreviewLightDark
@Composable
private fun DetailPaneContentPreview() {
    XRAdventuresTheme {
        Surface {
            DetailPaneContent(
                fishItem = createFishItems().first()
            )
        }
    }
}