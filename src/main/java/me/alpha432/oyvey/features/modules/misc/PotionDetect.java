package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class PotionDetect
        extends Module {
    private Set<EntityPlayer> str = Collections.newSetFromMap(new WeakHashMap());

    public PotionDetect() {
        super("PotionDetect", "Detects the potions someone has", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onToggle() {
        this.str = Collections.newSetFromMap(new WeakHashMap());
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityEffect) {
            SPacketEntityEffect sPacketEntityEffect = (SPacketEntityEffect)event.getPacket();
            System.out.println("Potion ID: " + sPacketEntityEffect.getEntityId());
        }
    }

    @Override
    public void onUpdate() {
        for (EntityPlayer player : PotionDetect.mc.world.playerEntities) {
            if (player.equals(PotionDetect.mc.player)) continue;
            if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
                Command.sendMessage(OyVey.friendManager.isFriend(player.getName()) ? ChatFormatting.AQUA + "\u00A7l" + player.getName() + ChatFormatting.RESET + " has (drank) strength" : ChatFormatting.RED + "\u00A7l" + player.getName() + ChatFormatting.RESET + " has (drank) strength");
                this.str.add(player);
            }
            if (!this.str.contains(player) || player.isPotionActive(MobEffects.STRENGTH)) continue;
            Command.sendMessage(OyVey.friendManager.isFriend(player.getName()) ? ChatFormatting.AQUA + "\u00A7l" + player.getName() + ChatFormatting.RESET + " no longer has strength" : ChatFormatting.RED + "\u00A7l" + player.getName() + ChatFormatting.RESET + " no longer has strength");
            this.str.remove(player);
        }
    }
}
