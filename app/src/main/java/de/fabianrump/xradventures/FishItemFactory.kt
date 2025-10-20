package de.fabianrump.xradventures

import androidx.compose.runtime.Composable

@Composable
fun createFishItems(): List<FishItem> = listOf(
    FishItem(
        id = 1,
        name = "Guppy",
        description = """
                The guppy (Poecilia reticulata) is a small, colorful freshwater fish native to South America. Known for its vibrant colors, flowing tails, and peaceful temperament, the guppy is one of the most popular aquarium fish in the world. Males are usually smaller and more colorful than females, displaying a wide variety of patterns and hues, including blues, reds, yellows, and greens.

                Guppies are livebearers, meaning they give birth to free-swimming young rather than laying eggs. They are hardy, adaptable, and easy to care for, making them ideal for beginner aquarists. Guppies thrive in community tanks and enjoy clean, well-oxygenated water with temperatures between 22–28°C (72–82°F).
            """.trimIndent(),
        gltfName = "guppy.glb",
        scale = 25f,
    ),
    FishItem(
        id = 2,
        name = "Koi",
        description = """
                The koi fish (Cyprinus rubrofuscus), also known as nishikigoi, is a colorful variety of ornamental carp that originated in Japan. Koi are prized for their striking patterns and vibrant colors, which include combinations of white, black, red, orange, yellow, and blue. With their graceful swimming and symbolic meaning, koi are often associated with peace, strength, and good fortune in many cultures.

                Koi are large freshwater fish that can grow up to 36 inches (90 cm) in length and live for decades—sometimes over 50 years in well-maintained ponds. They thrive in outdoor water gardens and are known for their friendly nature, often eating from their keeper’s hand.
            """.trimIndent(),
        gltfName = "koi_orange_fish.glb",
        scale = 0.01f,
    ),
    FishItem(
        id = 3,
        name = "Shark",
        description = """
                Sharks are a diverse group of cartilaginous fish belonging to the subclass Elasmobranchii. With over 500 known species, sharks range in size from the small dwarf lanternshark (just 8 inches long) to the massive whale shark, which can exceed 40 feet. These powerful predators have streamlined bodies, keen senses, and rows of replaceable teeth, making them highly efficient hunters.

                Sharks inhabit oceans all over the world—from shallow coastal waters to the deep sea. While they have a fearsome reputation, most sharks are not dangerous to humans. In fact, many species are shy and elusive. They play a vital role in marine ecosystems by keeping prey populations in balance and ensuring healthy oceans.
            """.trimIndent(),
        gltfName = "shark.glb",
        scale = 1f,
    ),
    FishItem(
        id = 4,
        name = "Smoked Fish",
        description = """
                Smoked fish is fish that has been cured through the process of smoking, a traditional method used to preserve and flavor food. The fish is typically first salted—either through dry curing or brining—and then exposed to smoke from smoldering wood. This not only extends shelf life but also imparts a rich, savory, and often slightly sweet aroma and taste.

                Popular types of smoked fish include salmon, mackerel, trout, herring, and whitefish. Smoking can be done using cold smoking (which cures the fish without cooking it) or hot smoking (which cooks the fish during the process). The result is a flavorful delicacy enjoyed on its own, in salads, on bagels, or as part of gourmet dishes around the world.
            """.trimIndent(),
        gltfName = "smoked_fish.glb",
        scale = 10f,
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
        scale = 0.3f,
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
        scale = 3f,
    ),
)