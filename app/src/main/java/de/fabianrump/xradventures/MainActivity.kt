package de.fabianrump.xradventures

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.xr.compose.platform.LocalHasXrSpatialFeature
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.EdgeOffset
import androidx.xr.compose.spatial.Orbiter
import androidx.xr.compose.spatial.OrbiterEdge
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.Volume
import androidx.xr.compose.subspace.layout.SpatialRoundedCornerShape
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.width
import androidx.xr.scenecore.GltfModel
import androidx.xr.scenecore.GltfModelEntity
import androidx.xr.scenecore.MovableComponent
import de.fabianrump.xradventures.ui.theme.XRAdventuresTheme
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var selectedFish by remember { mutableStateOf("") }

            XRAdventuresTheme {
                val session = LocalSession.current
                if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
                    Subspace {
                        MySpatialContent(
                            selectedFish = selectedFish,
                            onFishClicked = { updatedFish ->
                                selectedFish = updatedFish
                            },
                            onRequestHomeSpaceMode = { session?.spatialEnvironment?.requestHomeSpaceMode() })
                    }
                } else {
                    My2DContent(
                        onFishClicked = { updatedFish ->
                            selectedFish = updatedFish
                        },
                        onRequestFullSpaceMode = { session?.spatialEnvironment?.requestFullSpaceMode() })
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun MySpatialContent(
    selectedFish: String,
    onFishClicked: (String) -> Unit,
    onRequestHomeSpaceMode: () -> Unit
) {
    SpatialPanel(
        modifier = SubspaceModifier
            .width(1_280.dp)
            .height(height = 800.dp)
            .resizable()
            .movable()
    ) {
        Surface {
            MainContent(onFishClicked = onFishClicked)
        }
        Orbiter(
            position = OrbiterEdge.Top,
            offset = EdgeOffset.inner(offset = 20.dp),
            alignment = Alignment.End,
            shape = SpatialRoundedCornerShape(size = CornerSize(size = 28.dp))
        ) {
            HomeSpaceModeIconButton(
                onClick = onRequestHomeSpaceMode,
                modifier = Modifier.size(size = 56.dp)
            )
        }
        Orbiter(
            position = OrbiterEdge.Start,
            offset = EdgeOffset.inner(offset = 200.dp),
            alignment = Alignment.CenterVertically,
        ) {
            ObjectVolume(selectedFish = selectedFish)
        }
    }
}

@Composable
private fun ObjectVolume(selectedFish: String) {
    val session = requireNotNull(LocalSession.current)
    val scope = rememberCoroutineScope()
    Subspace {
        Volume {
            scope.launch {
                val model = GltfModel.create(session = session, name = selectedFish).await()
                val modelEntity = GltfModelEntity.create(session = session, model = model)
                val movableComponent = MovableComponent.create(session = session)
                modelEntity.addComponent(component = movableComponent)
                it.addChild(child = modelEntity)
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun My2DContent(onFishClicked: (String) -> Unit, onRequestFullSpaceMode: () -> Unit) {
    Surface {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (LocalHasXrSpatialFeature.current) {
                FullSpaceModeIconButton(
                    onClick = onRequestFullSpaceMode,
                    modifier = Modifier.padding(all = 32.dp)
                )
            }
            MainContent(onFishClicked = onFishClicked)
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainContent(onFishClicked: (String) -> Unit) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<FishItem>()
    val scope = rememberCoroutineScope()
    val selectedItem = scaffoldNavigator.currentDestination?.contentKey
    val categoryList = listOf(
        FishItem(
            id = 1,
            name = "Guppy",
            description = """
                The guppy (Poecilia reticulata) is a small, colorful freshwater fish native to South America. Known for its vibrant colors, flowing tails, and peaceful temperament, the guppy is one of the most popular aquarium fish in the world. Males are usually smaller and more colorful than females, displaying a wide variety of patterns and hues, including blues, reds, yellows, and greens.

                Guppies are livebearers, meaning they give birth to free-swimming young rather than laying eggs. They are hardy, adaptable, and easy to care for, making them ideal for beginner aquarists. Guppies thrive in community tanks and enjoy clean, well-oxygenated water with temperatures between 22–28°C (72–82°F).
            """.trimIndent(),
            gltfName = "guppy.glb",
        ),
        FishItem(
            id = 2,
            name = "Koi",
            description = """
                The koi fish (Cyprinus rubrofuscus), also known as nishikigoi, is a colorful variety of ornamental carp that originated in Japan. Koi are prized for their striking patterns and vibrant colors, which include combinations of white, black, red, orange, yellow, and blue. With their graceful swimming and symbolic meaning, koi are often associated with peace, strength, and good fortune in many cultures.

                Koi are large freshwater fish that can grow up to 36 inches (90 cm) in length and live for decades—sometimes over 50 years in well-maintained ponds. They thrive in outdoor water gardens and are known for their friendly nature, often eating from their keeper’s hand.
            """.trimIndent(),
            gltfName = "koi_orange_fish.glb",
        ),
        FishItem(
            id = 3,
            name = "Shark",
            description = """
                Sharks are a diverse group of cartilaginous fish belonging to the subclass Elasmobranchii. With over 500 known species, sharks range in size from the small dwarf lanternshark (just 8 inches long) to the massive whale shark, which can exceed 40 feet. These powerful predators have streamlined bodies, keen senses, and rows of replaceable teeth, making them highly efficient hunters.

                Sharks inhabit oceans all over the world—from shallow coastal waters to the deep sea. While they have a fearsome reputation, most sharks are not dangerous to humans. In fact, many species are shy and elusive. They play a vital role in marine ecosystems by keeping prey populations in balance and ensuring healthy oceans.
            """.trimIndent(),
            gltfName = "shark.glb",
        ),
        FishItem(
            id = 4,
            name = "Smoked Fish",
            description = """
                Smoked fish is fish that has been cured through the process of smoking, a traditional method used to preserve and flavor food. The fish is typically first salted—either through dry curing or brining—and then exposed to smoke from smoldering wood. This not only extends shelf life but also imparts a rich, savory, and often slightly sweet aroma and taste.

                Popular types of smoked fish include salmon, mackerel, trout, herring, and whitefish. Smoking can be done using cold smoking (which cures the fish without cooking it) or hot smoking (which cooks the fish during the process). The result is a flavorful delicacy enjoyed on its own, in salads, on bagels, or as part of gourmet dishes around the world.
            """.trimIndent(),
            gltfName = "smoked_fish.glb",
        ),
        FishItem(
            id = 5,
            name = "Tuna",
            description = """
                Tuna are large, fast-swimming saltwater fish belonging to the Thunnini tribe, part of the mackerel family. Known for their sleek, torpedo-shaped bodies and incredible speed (up to 75 km/h or 47 mph), tuna are powerful predators found in warm and temperate oceans worldwide.

                There are several species of tuna, including bluefin, yellowfin, albacore, and skipjack, each valued for their firm, flavorful flesh. Tuna is a staple in global cuisine—especially popular in sushi, sashimi, canned goods, and grilled dishes.

                Tuna are also known for their long-distance migrations and complex role in marine ecosystems. Some species, like the Atlantic bluefin, are considered overfished and are the focus of conservation efforts.
            """.trimIndent(),
            gltfName = "tuna_fish.glb",
        ),
        FishItem(
            id = 6,
            name = "Whale",
            description = """
                Whales are large, intelligent marine mammals belonging to the order Cetacea. They are warm-blooded, breathe air through lungs, and give birth to live young. Whales are found in oceans all around the world and are divided into two main groups: toothed whales (like orcas and sperm whales) and baleen whales (like blue whales and humpbacks), which filter small prey like krill through comb-like plates.

                The blue whale is the largest animal ever known to have lived, reaching lengths of over 30 meters (98 feet). Whales are known for their complex social behaviors, long migrations, and in some species, haunting vocalizations or “songs.”

                As keystone species, whales play a crucial role in the health of marine ecosystems—and have been central to human culture, myth, and science for centuries.
            """.trimIndent(),
            gltfName = "whale.glb",
        ),
    )

    ListDetailPaneScaffold(
        directive = PaneScaffoldDirective.Default,
        scaffoldState = scaffoldNavigator.scaffoldState,
        listPane = {
            AnimatedPane {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(categoryList) { item ->
                        val isSelected = item == selectedItem
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onFishClicked(item.gltfName)
                                    scope.launch {
                                        scaffoldNavigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            item
                                        )
                                    }
                                }
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.1f
                                    ) else Color.Transparent
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(width = 12.dp))
                            Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.weight(weight = 1f))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Details"
                            )
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                selectedItem?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(height = 16.dp))
                        Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        Text(text = it.description, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(height = 16.dp))
                    }
                } ?: Text(text = "Choose your fish", modifier = Modifier.padding(all = 24.dp))
            }
        },
    )
}

@Composable
fun FullSpaceModeIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_full_space_mode_switch),
            contentDescription = stringResource(R.string.switch_to_full_space_mode)
        )
    }
}

@Composable
fun HomeSpaceModeIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalIconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home_space_mode_switch),
            contentDescription = stringResource(R.string.switch_to_home_space_mode)
        )
    }
}

@PreviewLightDark
@Composable
fun My2dContentPreview() {
    XRAdventuresTheme {
        My2DContent(
            onRequestFullSpaceMode = {},
            onFishClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FullSpaceModeButtonPreview() {
    XRAdventuresTheme {
        FullSpaceModeIconButton(onClick = {})
    }
}

@PreviewLightDark
@Composable
fun HomeSpaceModeButtonPreview() {
    XRAdventuresTheme {
        HomeSpaceModeIconButton(onClick = {})
    }
}

data class FishItem(
    val id: Int,
    val name: String,
    val description: String,
    val gltfName: String,
)
