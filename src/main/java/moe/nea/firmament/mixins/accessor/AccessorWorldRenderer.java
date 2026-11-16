package moe.nea.firmament.mixins.accessor;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.BlockDestructionProgress;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.SortedSet;

@Mixin(ClientLevel.class)
public interface AccessorWorldRenderer {
	@Accessor("destructionProgress")
	@NotNull
	Long2ObjectMap<SortedSet<BlockDestructionProgress>> getBlockBreakingProgressions_firmament();
}
