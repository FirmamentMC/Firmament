
package moe.nea.firmament.util.render

import org.joml.Matrix4f
import util.render.CustomRenderLayers
import net.minecraft.client.gui.Font
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.core.BlockPos
import moe.nea.firmament.util.center
import net.minecraft.util.LightCoordsUtil
import moe.nea.firmament.util.FirmFormatters
import moe.nea.firmament.util.MC
import moe.nea.firmament.util.assertTrueOr

@RenderContextDSL
class FacingThePlayerContext(val worldContext: RenderInWorldContext) {
    val matrixStack by worldContext::matrixStack
    fun waypoint(position: BlockPos, label: Component) {
        text(
            label,
            Component.literal("§e${FirmFormatters.formatDistance(MC.player?.position?.distanceTo(position.center) ?: 42069.0)}")
        )
    }

    fun text(
		vararg texts: Component,
		verticalAlign: RenderInWorldContext.VerticalAlign = RenderInWorldContext.VerticalAlign.CENTER,
		background: Int = 0x70808080,
    ) {
        assertTrueOr(texts.isNotEmpty()) { return@text }
        for ((index, text) in texts.withIndex()) {
            worldContext.matrixStack.pushPose()
            val width = MC.font.width(text)
            worldContext.matrixStack.translate(-width / 2F, verticalAlign.align(index, texts.size), 0F)
            worldContext.submitNodeCollector.submitCustomGeometry(
                worldContext.matrixStack,
                RenderTypes.textBackgroundSeeThrough()
            ) { pose, vertexConsumer ->
                val matrix4f = pose.pose()
                vertexConsumer.addVertex(matrix4f, -1.0f, -1.0f, 0.0f).setColor(background)
                    .setLight(LightCoordsUtil.FULL_BRIGHT)
                vertexConsumer.addVertex(matrix4f, -1.0f, MC.font.lineHeight.toFloat(), 0.0f).setColor(background)
                    .setLight(LightCoordsUtil.FULL_BRIGHT)
                vertexConsumer.addVertex(matrix4f, width.toFloat(), MC.font.lineHeight.toFloat(), 0.0f)
                    .setColor(background)
                    .setLight(LightCoordsUtil.FULL_BRIGHT)
                vertexConsumer.addVertex(matrix4f, width.toFloat(), -1.0f, 0.0f).setColor(background)
                    .setLight(LightCoordsUtil.FULL_BRIGHT)
            }
            worldContext.matrixStack.translate(0F, 0F, 0.01F)

            worldContext.submitNodeCollector.submitText(
                worldContext.matrixStack,
                0F,
                0F,
                text.visualOrderText,
                false,
                Font.DisplayMode.SEE_THROUGH,
                LightCoordsUtil.FULL_BRIGHT,
                -1,
                0,
                0
            )
            worldContext.matrixStack.popPose()
        }
    }


    fun texture(
		texture: Identifier, width: Int, height: Int,
		u1: Float, v1: Float,
		u2: Float, v2: Float,
    ) {
        val hw = width / 2F
        val hh = height / 2F
        worldContext.submitNodeCollector.submitCustomGeometry(
            worldContext.matrixStack,
            CustomRenderLayers.GUI_TEXTURED_NO_DEPTH_TRIS.apply(texture) // TODO: this is strictly an incorrect render layer
        ) { pose, buf ->
            val matrix4f: Matrix4f = pose.pose()
            buf.addVertex(matrix4f, -hw, -hh, 0F)
                .setColor(-1)
                .setUv(u1, v1)
            buf.addVertex(matrix4f, -hw, +hh, 0F)
                .setColor(-1)
                .setUv(u1, v2)
            buf.addVertex(matrix4f, +hw, +hh, 0F)
                .setColor(-1)
                .setUv(u2, v2)
            buf.addVertex(matrix4f, +hw, -hh, 0F)
                .setColor(-1)
                .setUv(u2, v1)
        }
    }

}
