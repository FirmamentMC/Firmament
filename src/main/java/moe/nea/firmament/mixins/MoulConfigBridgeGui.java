package moe.nea.firmament.mixins;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Gui.class)
public class MoulConfigBridgeGui {
	@Shadow
	@Final
	public Hud hud;

	public ChatComponent getChat() {
		return this.hud.getChat();
	}
}
