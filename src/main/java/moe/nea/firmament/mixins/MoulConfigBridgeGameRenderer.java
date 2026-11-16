package moe.nea.firmament.mixins;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public abstract class MoulConfigBridgeGameRenderer {
	@Shadow
	public abstract GameRenderState gameRenderState();

	public GameRenderState getGameRenderState() {
		return this.gameRenderState();
	}
}
