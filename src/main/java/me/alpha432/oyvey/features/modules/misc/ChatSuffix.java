package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.mixin.mixins.AccessorCPacketChatMessage;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix extends Module {
    Setting<Boolean> unicod = this.register(new Setting<Boolean>("UnicodeBypass", false));
    Setting<Boolean> barva = this.register(new Setting<Boolean>("StiColor", false, n -> this.unicod.getValue()));
    public ChatSuffix() {
        super("ChatSuffix", "what the chat doin", Module.Category.MISC, true, false, false);
    }
    String tethys = "\u269D \uFF34\uFF45\uFF54\uFF48\uFF59\uFF53";
    String unicode = "* Tethys";
    String sti = "* !g!jTethys";
//packet.setMessage(((CPacketChatMessage) event.getPacket()).getMessage() + " " + tethys);
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
                AccessorCPacketChatMessage packet = (AccessorCPacketChatMessage) event.getPacket();
                if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/")) return;
                if (this.unicod.getValue()){
                    if (this.barva.getValue()){
                        packet.setMessage(((CPacketChatMessage) event.getPacket()).getMessage() + " " + sti);
                    } else {
                        packet.setMessage(((CPacketChatMessage) event.getPacket()).getMessage() + " " + unicode);
                    }
                } else {
                    packet.setMessage(((CPacketChatMessage) event.getPacket()).getMessage() + " " + tethys);
                        }
                    }
                }
            }