package de.fabianrump.xradventures

import androidx.compose.ui.graphics.painter.Painter
import androidx.xr.runtime.math.Pose

data class FishItem(
    val name: String,
    val description: String,
    val gltfName: String,
    val scale: Float,
    val pose: Pose,
    val picture: Painter
)