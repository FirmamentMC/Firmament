package moe.nea.firmament.mixins.render.entitytints;

import com.mojang.blaze3d.textures.GpuTextureView;
import moe.nea.firmament.events.EntityRenderTintEvent;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Replaces the overlay texture used by rendering with the override specified in {@link EntityRenderTintEvent#overlayOverride}
 */
@Mixin(RenderSetup.class)
public class ReplaceOverlayTexture {
	@ModifyVariable(
		method = {"prepareTextures"},
		at = @At("HEAD"),
		argsOnly = true,
		ordinal = 0)
	private GpuTextureView replaceOverlayTexture(GpuTextureView original) {
		if (EntityRenderTintEvent.overlayOverride != null)
			return EntityRenderTintEvent.overlayOverride.getTextureView();
		return original;
	}
}
