@file:OptIn(
    ExperimentalMaterial3XrApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class
)

package de.fabianrump.xradventures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.xr.compose.material3.EnableXrComponentOverrides
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.MovePolicy
import androidx.xr.compose.subspace.ResizePolicy
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.SpatialRow
import de.fabianrump.xradventures.ui.theme.XRAdventuresTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            XRAdventuresTheme {

                val fishItems = createFishItems()

                if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
                    Subspace {
                        SpatialRow {
                            SpatialPanel(
                                dragPolicy = MovePolicy(),
                                resizePolicy = ResizePolicy()
                            ) {
                                Content(fishItems = fishItems)
                            }
                            // Add 3D model here
                        }
                    }
                } else {
                    Content(fishItems = fishItems)
                }
            }
        }
    }
}

@Composable
private fun Content(
    fishItems: List<FishItem>,
) {
    Column {
        AppBar()
        Surface {
            val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<FishItem>()
            val scope = rememberCoroutineScope()
            val selectedItem = scaffoldNavigator.currentDestination?.contentKey ?: fishItems.first()

            // Doesn't work with the current libraries yet (for the top app bar it does)
            //EnableXrComponentOverrides {
            ListDetailPaneScaffold(
                directive = PaneScaffoldDirective.Default,
                scaffoldState = scaffoldNavigator.scaffoldState,
                listPane = {
                    AnimatedPane {
                        ListPaneContent(
                            categoryList = fishItems,
                            selectedItem = selectedItem,
                            onFishClicked = { item ->
                                    scope.launch {
                                        scaffoldNavigator.navigateTo(
                                            pane = ListDetailPaneScaffoldRole.Detail,
                                            contentKey = item
                                        )
                                    }
                            },
                        )
                    }
                },
                detailPane = {
                    AnimatedPane {
                        DetailPaneContent(fishItem = selectedItem)
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
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = stringResource(id = R.string.fish_viewer)
                )
            },
            actions = {
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
                    if (capabilities.isSpatialUiEnabled) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home_space_mode_switch),
                            contentDescription = stringResource(id = R.string.switch_to_home_space_mode)
                        )
                    } else {
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

//@OptIn(ExperimentalSubspaceVolumeApi::class)
//@Composable
//private fun ObjectVolume(selectedFish: String, scale: Float) {
//    val session = requireNotNull(LocalSession.current)
//    val scope = rememberCoroutineScope()
//    Subspace {
//        Volume {
//            scope.launch {
//                val model = GltfModel.create(session = session, name = selectedFish).await()
//                val modelEntity = GltfModelEntity.create(session = session, model = model)
//                modelEntity.setScale(scale)
//                modelEntity.setPose(Pose(translation = Vector3(x = 1f, y = 0f, z = 0f), rotation = Quaternion.Identity))
//                val movableComponent = MovableComponent.create(session = session, scaleInZ = false)
//                modelEntity.addComponent(component = movableComponent)
//                it.addChild(child = modelEntity)
//            }
//        }
//    }
//}