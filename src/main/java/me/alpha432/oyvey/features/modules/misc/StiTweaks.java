package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.mixin.mixins.AccessorCPacketChatMessage;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StiTweaks extends Module {

    public StiTweaks() {
        super("StiTweaks", "enhance your stianarchy experience", Module.Category.MISC, true, false, false);
    }



    public Setting<Modes> Mode = register(new Setting<Modes>("Mode", Modes.RANDOM));
    /*public Setting<Boolean> stiColor = register(new Setting<Boolean>("Blue", Boolean.valueOf(true)));
    public Setting<Boolean> stiRandomColor = register(new Setting<Boolean>("RandomColor", Boolean.valueOf(false)));*/


    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            AccessorCPacketChatMessage packet = (AccessorCPacketChatMessage) event.getPacket();
            if (((CPacketChatMessage) event.getPacket()).getMessage().startsWith("/")) return;
            switch(Mode.getValue()){
                case RANDOM:
                    int min = 1;
                    int max = 18;
                    int colorValue = (int)Math.floor(Math.random()*(max-min+1)+min);
                    if (colorValue == 1)  {packet.setMessage("!b" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 2)  {packet.setMessage("!d" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 3)  {packet.setMessage("!c" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 4)  {packet.setMessage("!g" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 5)  {packet.setMessage("!f" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 6)  {packet.setMessage("!6" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 7)  {packet.setMessage("!7" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 8)  {packet.setMessage("!9" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 9)  {packet.setMessage("!5" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 10)  {packet.setMessage("!3" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 11)  {packet.setMessage("!2" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 12)  {packet.setMessage("!8" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 13)  {packet.setMessage("!4" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 14)  {packet.setMessage("!1" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 15)  {packet.setMessage("!j" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 16)  {packet.setMessage("!k" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 17)  {packet.setMessage("!i" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 18)  {packet.setMessage("!h" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    break;
                case BLACK:
                    packet.setMessage("!9" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case BLUE:
                    packet.setMessage("!d" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case GREEN:
                    packet.setMessage("!a" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case AQUA:
                    packet.setMessage("!e" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case RED:
                    packet.setMessage("!b" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case PURPLE:
                    packet.setMessage("!f" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case GOLD:
                    packet.setMessage("!6" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case LIGHT_GRAY:
                    packet.setMessage("!8" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case GRAY:
                    packet.setMessage("!c" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case LIGHT_BLUE:
                    packet.setMessage("!3" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case LIGHT_GREEN:
                    packet.setMessage("!2" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case LIGHT_AQUA:
                    packet.setMessage("!5" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case LIGHT_RED:
                    packet.setMessage("!4" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case LIGHT_PURPLE:
                    packet.setMessage("!g" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case YELLOW:
                    packet.setMessage("!7" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case WHITE:
                    packet.setMessage("!1" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case BOLD:
                    packet.setMessage("!j" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case STRIKE:
                    packet.setMessage("!k" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case UNDERLINE:
                    packet.setMessage("!i" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
                case ITALIC:
                    packet.setMessage("!h" + ((CPacketChatMessage) event.getPacket()).getMessage());
                    break;
            }
            /*if (stiColor.getValue() == true) {
                packet.setMessage("!3" + ((CPacketChatMessage) event.getPacket()).getMessage());
            } else {
                if (stiRandomColor.getValue() == true) {
                    int min = 1;
                    int max = 14;


                    int colorValue = (int)Math.floor(Math.random()*(max-min+1)+min);
                    if (colorValue == 1)  {packet.setMessage("!b" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 2)  {packet.setMessage("!d" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 3)  {packet.setMessage("!c" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 4)  {packet.setMessage("!g" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 5)  {packet.setMessage("!f" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 6)  {packet.setMessage("!6" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 7)  {packet.setMessage("!7" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 8)  {packet.setMessage("!9" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 9)  {packet.setMessage("!5" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 10)  {packet.setMessage("!3" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 11)  {packet.setMessage("!2" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 12)  {packet.setMessage("!8" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 13)  {packet.setMessage("!4" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    if (colorValue == 14)  {packet.setMessage("!1" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                    else {packet.setMessage("!j" + ((CPacketChatMessage) event.getPacket()).getMessage());}
                }
            }*/

        }
    }
}

enum Modes {
    RANDOM,
    BLACK,
    BLUE,
    GREEN,
    AQUA,
    RED,
    PURPLE,
    GOLD,
    LIGHT_GRAY,
    GRAY,
    LIGHT_BLUE,
    LIGHT_GREEN,
    LIGHT_AQUA,
    LIGHT_RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE,
    BOLD,
    STRIKE,
    UNDERLINE,
    ITALIC
}


