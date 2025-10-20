package de.fabianrump.xradventures

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import de.fabianrump.xradventures.ui.theme.XRAdventuresTheme

@Composable
fun ListPaneContent(
    categoryList: List<FishItem>,
    selectedItem: FishItem?,
    onFishClicked: (FishItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .padding(horizontal = 16.dp),
                text = stringResource(id = R.string.choose_your_fish),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(items = categoryList) { item ->
            val isSelected = item == selectedItem
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFishClicked(item)
                    },
                headlineContent = {
                    Text(text = item.name)
                },
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(id = R.string.get_details)
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant
                    else Color.Unspecified
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ListPaneContentPreview() {
    XRAdventuresTheme {
        val fishItems = createFishItems()

        Surface {
            ListPaneContent(
                categoryList = fishItems,
                selectedItem = fishItems.first(),
                onFishClicked = {}
            )
        }
    }
}