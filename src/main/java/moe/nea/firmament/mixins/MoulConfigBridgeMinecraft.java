package moe.nea.firmament.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MoulConfigBridgeMinecraft {
	@Shadow
	@Final
	public Gui gui;

	public void setScreen(@Nullable Screen screen) {
		this.gui.setScreen(screen);
	}
}
