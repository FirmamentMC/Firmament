package moe.nea.firmament.util.mc

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.Optional
import org.jetbrains.annotations.TestOnly
import net.minecraft.client.gui.components.PlayerTabOverlay
import net.minecraft.nbt.NbtOps
import net.minecraft.world.scores.PlayerTeam
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import moe.nea.firmament.annotations.Subscribe
import moe.nea.firmament.commands.thenExecute
import moe.nea.firmament.commands.thenLiteral
import moe.nea.firmament.events.CommandEvent
import moe.nea.firmament.events.TickEvent
import moe.nea.firmament.features.debug.DeveloperFeatures
import moe.nea.firmament.features.debug.ExportedTestConstantMeta
import moe.nea.firmament.mixins.accessor.AccessorPlayerListHud
import moe.nea.firmament.util.ClipboardUtils
import moe.nea.firmament.util.MC
import moe.nea.firmament.util.intoOptional
import moe.nea.firmament.util.mc.SNbtFormatter.Companion.toPrettyString

object MCTabListAPI {

	fun PlayerTabOverlay.cast() = this as AccessorPlayerListHud

	@Subscribe
	fun onTick(event: TickEvent) {
		_currentTabList = null
	}

	@Subscribe
	fun devCommand(event: CommandEvent.SubCommand) {
		event.subcommand(DeveloperFeatures.DEVELOPER_SUBCOMMAND) {
			thenLiteral("copytablist") {
				thenExecute {
					currentTabList.body.forEach {
						MC.sendChat(Component.literal(ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, it).orThrow.toString()))
					}
					var compound = CurrentTabList.CODEC.encodeStart(NbtOps.INSTANCE, currentTabList).orThrow
					compound = ExportedTestConstantMeta.SOURCE_CODEC.encode(
						ExportedTestConstantMeta.current,
						NbtOps.INSTANCE,
						compound
					).orThrow
					ClipboardUtils.setTextContent(
						compound.toPrettyString()
					)
				}
			}
		}
	}

	@get:TestOnly
	@set:TestOnly
	var _currentTabList: CurrentTabList? = null

	val currentTabList get() = _currentTabList ?: getTabListNow().also { _currentTabList = it }

	data class CurrentTabList(
		val header: Optional<Component>,
		val footer: Optional<Component>,
		val body: List<Component>,
	) {
		companion object {
			val CODEC: Codec<CurrentTabList> = RecordCodecBuilder.create {
				it.group(
					ComponentSerialization.CODEC.optionalFieldOf("header").forGetter(CurrentTabList::header),
					ComponentSerialization.CODEC.optionalFieldOf("footer").forGetter(CurrentTabList::footer),
					ComponentSerialization.CODEC.listOf().fieldOf("body").forGetter(CurrentTabList::body),
				).apply(it, ::CurrentTabList)
			}
		}
	}

	private fun getTabListNow(): CurrentTabList {
		// This is a precondition for PlayerListHud.collectEntries to be valid
		MC.networkHandler ?: return CurrentTabList(Optional.empty(), Optional.empty(), emptyList())
		val hud = MC.inGameHud.hud.tabList.cast()
		val entries = hud.collectPlayerEntries_firmament()
			.map {
				it.tabListDisplayName ?: run {
					val team = it.team
					val name = it.profile.name
					PlayerTeam.formatNameForTeam(team, Component.literal(name))
				}
			}
		return CurrentTabList(
			header = hud.header_firmament.intoOptional(),
			footer = hud.footer_firmament.intoOptional(),
			body = entries,
		)
	}
}
