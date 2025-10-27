@file:OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3XrApi::class,
    ExperimentalMaterial3Api::class
)

package de.fabianrump.xradventures

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldState
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.xr.compose.material3.EnableXrComponentOverrides
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration
import de.fabianrump.xradventures.ui.theme.XRAdventuresTheme

@Composable
fun MainActivityContent(
    fishItems: List<FishItem>,
    selectedFishItem: FishItem,
    scaffoldState: ThreePaneScaffoldState,
    onFishItemClicked: (FishItem) -> Unit,
) {
    Column {
        AppBar()
        Surface {
            // Doesn't work because of an version incompatibility
            //EnableXrComponentOverrides {
            ListDetailPaneScaffold(
                directive = PaneScaffoldDirective.Default,
                scaffoldState = scaffoldState,
                listPane = {
                    AnimatedPane {
                        ListPaneContent(
                            fishItems = fishItems,
                            selectedFishItem = selectedFishItem,
                            onFishItemClicked = onFishItemClicked,
                        )
                    }
                },
                detailPane = {
                    AnimatedPane {
                        DetailPaneContent(fishItem = selectedFishItem)
                    }
                },
            )
            //}
        }
    }
}

@Composable
private fun AppBar() {
    val configuration = LocalSpatialConfiguration.current
    val capabilities = LocalSpatialCapabilities.current

    EnableXrComponentOverrides {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = stringResource(id = R.string.fish_viewer)
                )
            },
            actions = {
                if (capabilities.isSpatialUiEnabled) {
                    IconButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            if (capabilities.isSpatialUiEnabled) {
                                configuration.requestHomeSpaceMode()
                            } else {
                                configuration.requestFullSpaceMode()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home_space_mode_switch),
                            contentDescription = stringResource(id = R.string.switch_to_home_space_mode)
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            if (capabilities.isSpatialUiEnabled) {
                                configuration.requestHomeSpaceMode()
                            } else {
                                configuration.requestFullSpaceMode()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_full_space_mode_switch),
                            contentDescription = stringResource(id = R.string.switch_to_full_space_mode)
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun ListPaneContent(
    fishItems: List<FishItem>,
    selectedFishItem: FishItem?,
    onFishItemClicked: (FishItem) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = fishItems) { item ->
            val isSelected = item == selectedFishItem
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFishItemClicked(item)
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

@Composable
fun DetailPaneContent(fishItem: FishItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(height = 24.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            text = fishItem.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(size = 256.dp),
            painter = fishItem.picture,
            contentDescription = null,
        )
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
private fun MainActivityContentPreview() {
    XRAdventuresTheme {
        val fishItems = createFishItems()

        MainActivityContent(
            fishItems = fishItems,
            selectedFishItem = fishItems.first(),
            scaffoldState = rememberListDetailPaneScaffoldNavigator<FishItem>().scaffoldState,
            onFishItemClicked = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun ListPaneContentPreview() {
    XRAdventuresTheme {
        val fishItems = createFishItems()

        Surface {
            ListPaneContent(
                fishItems = fishItems,
                selectedFishItem = fishItems.first(),
                onFishItemClicked = {}
            )
        }
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
