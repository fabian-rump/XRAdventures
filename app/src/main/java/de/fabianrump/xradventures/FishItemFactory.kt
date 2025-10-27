package de.fabianrump.xradventures

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.xr.runtime.math.Pose
import androidx.xr.runtime.math.Quaternion
import androidx.xr.runtime.math.Vector3

@Composable
fun createFishItems(): List<FishItem> = listOf(
    FishItem(
        name = "Pyjama Shark",
        description = """
            The pyjama shark or striped catshark (Poroderma africanum) is a species of catshark, and part of the family Scyliorhinidae, endemic to the coastal waters of South Africa.
            This abundant, bottom-dwelling species can be found from the intertidal zone to a depth of around 100 m (330 ft), particularly over rocky reefs and kelp beds.
            With a series of thick, parallel, dark stripes running along its stout body, the pyjama shark has an unmistakable appearance.
            It is additionally characterized by a short head and snout with a pair of slender barbels that do not reach the mouth, and two dorsal fins that are placed far back on the body.
            It can grow up to a length of 1.1 m (3.6 ft) long.
            """.trimIndent(),
        gltfName = "pyjama_shark.glb",
        scale = 0.55f,
        pose = Pose(
            translation = Vector3(x = 0.4f, y = -1f, z = -0.4f),
            rotation = Quaternion.fromEulerAngles(pitch = 0f, yaw = 30f, roll = 0f)
        ),
        picture = painterResource(id = R.drawable.pyjama_shark)
    ),
    FishItem(
        name = "Great White Shark",
        description = """
                The great white shark (Carcharodon carcharias), also known as the white shark, white pointer, or simply great white, is a species of large mackerel shark which can be found in the coastal surface waters of all the major oceans.
                It is the only known surviving species of its genus Carcharodon.
                The great white shark is notable for its size, with the largest preserved female specimen measuring 5.83 m (19.1 ft) in length and around 2,000 kg (4,400 lb) in weight at maturity.
                However, most are smaller; males measure 3.4 to 4.0 m (11 to 13 ft), and females measure 4.6 to 4.9 m (15 to 16 ft) on average.
                According to a 2014 study, the lifespan of great white sharks is estimated to be as long as 70 years or more, well above previous estimates, making it one of the longest lived cartilaginous fishes currently known.
                According to the same study, male great white sharks take 26 years to reach sexual maturity, while the females take 33 years to be ready to produce offspring.
                Great white sharks can swim at speeds of 25 km/h (16 mph) for short bursts and to depths of 1,200 m (3,900 ft).
            """.trimIndent(),
        gltfName = "great_white_shark.glb",
        scale = 0.8f,
        pose = Pose(
            translation = Vector3(x = 0.4f, y = -0.4f),
            rotation = Quaternion.fromEulerAngles(pitch = 0f, yaw = -60f, roll = 0f)
        ),
        picture = painterResource(id = R.drawable.great_white_shark)
    ),
    FishItem(
        name = "Tuna",
        description = """
                A tuna (pl.: tunas or tuna) is a saltwater fish that belongs to the tribe Thunnini, a subgrouping of the Scombridae (mackerel) family.
                The Thunnini comprise 15 species across five genera, the sizes of which vary greatly, ranging from the bullet tuna (max length: 50 cm or 1.6 ft, weight: 1.8 kg or 4 lb) up to the Atlantic bluefin tuna (max length: 4.6 m or 15 ft, weight: 684 kg or 1,508 lb), which averages 2 m (6.6 ft) and is believed to live up to 50 years.
            """.trimIndent(),
        gltfName = "tuna.glb",
        scale = 0.4f,
        pose = Pose(
            translation = Vector3(x = 0.6f),
            rotation = Quaternion.fromEulerAngles(pitch = 0f, yaw = 60f, roll = 0f)
        ),
        picture = painterResource(id = R.drawable.tuna)
    ),
    FishItem(
        name = "Mooneye",
        description = """
            Hiodontidae, commonly called mooneyes, is a family of ray-finned fish with a single included genus Hiodon.
            The genus comprise two extant species native to North America and three to five extinct species recorded from Paleocene to Eocene age fossils.
            They are large-eyed, fork-tailed fish that superficially resemble shads.
            The vernacular name comes from the metallic shine of their eyes.
            """.trimIndent(),
        gltfName = "mooneye.glb",
        scale = 0.6f,
        pose = Pose(
            translation = Vector3(x = 0.4f),
            rotation = Quaternion.fromEulerAngles(pitch = 0f, yaw = 45f, roll = 0f)
        ),
        picture = painterResource(id = R.drawable.mooneye)
    ),
    FishItem(
        name = "Smoked Fish",
        description = """
                Smoked fish is fish that has been cured through the process of smoking, a traditional method used to preserve and flavor food.
                The fish is typically first salted—either through dry curing or brining—and then exposed to smoke from smoldering wood.
                This not only extends shelf life but also imparts a rich, savory, and often slightly sweet aroma and taste.
                Popular types of smoked fish include salmon, mackerel, trout, herring, and whitefish.
                Smoking can be done using cold smoking (which cures the fish without cooking it) or hot smoking (which cooks the fish during the process).
                The result is a flavorful delicacy enjoyed on its own, in salads, on bagels, or as part of gourmet dishes around the world.
            """.trimIndent(),
        gltfName = "smoked_fish.glb",
        scale = 5f,
        pose = Pose(
            translation = Vector3(x = 0.4f),
            rotation = Quaternion.fromEulerAngles(pitch = 90f, yaw = -60f, roll = 0f)
        ),
        picture = painterResource(id = R.drawable.smoked_fish)
    ),
)