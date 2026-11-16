package moe.nea.firmament.events

import net.minecraft.client.renderer.SubmitNodeCollector
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.state.level.CameraRenderState

/**
 * This event is called after all world rendering is done, but before any GUI rendering (including hand) has been done.
 */
data class WorldRenderLastEvent(
	val matrices: PoseStack,
	val tickCounter: Int,
	val camera: CameraRenderState,
	val submitNodeCollector: SubmitNodeCollector,
) : FirmamentEvent() {
	companion object : FirmamentEventBus<WorldRenderLastEvent>()
}
