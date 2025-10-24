@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSubspaceVolumeApi::class)

package de.fabianrump.xradventures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.ExperimentalSubspaceVolumeApi
import androidx.xr.compose.subspace.MovePolicy
import androidx.xr.compose.subspace.ResizePolicy
import androidx.xr.compose.subspace.SpatialBox
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.SpatialRow
import androidx.xr.compose.subspace.Volume
import androidx.xr.compose.subspace.layout.SpatialAlignment
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.width
import androidx.xr.runtime.Session
import androidx.xr.scenecore.Entity
import androidx.xr.scenecore.GltfModel
import androidx.xr.scenecore.GltfModelEntity
import androidx.xr.scenecore.MovableComponent
import de.fabianrump.xradventures.ui.theme.XRAdventuresTheme
import kotlinx.coroutines.launch
import java.nio.file.Paths

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            XRAdventuresTheme {

                val fishItems = createFishItems()

                if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
                    FullSpaceContent(fishItems = fishItems)
                } else {
                    HomeSpaceContent(fishItems = fishItems)
                }
            }
        }
    }
}

@Composable
private fun HomeSpaceContent(fishItems: List<FishItem>) {
    val scope = rememberCoroutineScope()

    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<FishItem>()
    val selectedItem =
        scaffoldNavigator.currentDestination?.contentKey ?: fishItems.first()

    MainActivityContent(
        fishItems = fishItems,
        selectedFishItem = selectedItem,
        scaffoldState = scaffoldNavigator.scaffoldState,
        onFishItemClicked = { item ->
            scope.launch {
                scaffoldNavigator.navigateTo(
                    pane = ListDetailPaneScaffoldRole.Detail,
                    contentKey = item
                )
            }
        }
    )
}

@Composable
private fun FullSpaceContent(
    fishItems: List<FishItem>,
) {
    val scope = rememberCoroutineScope()

    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<FishItem>()
    val selectedItem =
        scaffoldNavigator.currentDestination?.contentKey ?: fishItems.first()

    var volumeEntity by remember {
        mutableStateOf<Entity?>(value = null)
    }
    var loadedGltfModelEntities by remember {
        mutableStateOf<List<GltfModelEntity>>(value = listOf())
    }
    val session = LocalSession.current

    LaunchedEffect(key1 = volumeEntity) {
        if (session == null || volumeEntity == null) return@LaunchedEffect
        loadedGltfModelEntities.forEach { item ->
            item.dispose()
        }
        loadedGltfModelEntities = listOf()
        fishItems.forEach { item ->
            val loadedGltfModelEntity = loadGltfModelEntity(
                session = session,
                fishItem = item
            )
            loadedGltfModelEntities =
                loadedGltfModelEntities + loadedGltfModelEntity
            loadedGltfModelEntity.setEnabled(enabled = false)
            volumeEntity?.addChild(child = loadedGltfModelEntity)
        }
    }

    LaunchedEffect(key1 = selectedItem, key2 = loadedGltfModelEntities) {
        if (loadedGltfModelEntities.size != fishItems.size) return@LaunchedEffect
        try {
            loadedGltfModelEntities.forEach {
                it.setEnabled(enabled = false)
            }
            loadedGltfModelEntities.get(
                index = fishItems.indexOf(selectedItem)
            ).setEnabled(enabled = true)
        } catch (_: Exception) {
            // Don't do anything
        }
    }

    Subspace {
        SpatialRow {
            SpatialPanel(
                modifier = SubspaceModifier
                    .height(height = 600.dp)
                    .width(width = 1000.dp),
                dragPolicy = MovePolicy(),
                resizePolicy = ResizePolicy()
            ) {
                MainActivityContent(
                    fishItems = fishItems,
                    selectedFishItem = selectedItem,
                    scaffoldState = scaffoldNavigator.scaffoldState,
                    onFishItemClicked = { item ->
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = item
                            )
                        }
                    }
                )
            }
            SpatialBox(
                modifier = SubspaceModifier
                    .height(height = 600.dp)
                    .width(width = 600.dp),
                alignment = SpatialAlignment.Center
            ) {
                Volume { entity -> volumeEntity = entity }
                if (loadedGltfModelEntities.size != fishItems.size) {
                    SpatialPanel {
                        CircularProgressIndicator(
                            modifier = Modifier.size(size = 150.dp),
                            strokeWidth = 12.dp
                        )
                    }
                }
            }
        }
    }
}

private suspend fun loadGltfModelEntity(
    session: Session,
    fishItem: FishItem,
): GltfModelEntity {
    val model =
        GltfModel.create(session = session, path = Paths.get(fishItem.gltfName))
    val modelEntity = GltfModelEntity.create(session = session, model = model)
    modelEntity.setScale(fishItem.scale)
    modelEntity.setPose(fishItem.pose)
    val movableComponent = MovableComponent.createSystemMovable(session = session, scaleInZ = false)
    modelEntity.addComponent(component = movableComponent)
    return modelEntity
}