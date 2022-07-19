package me.alpha432.oyvey.event.events;

import net.minecraft.network.Packet;

public class EventClientPacket extends EventPacket
{
    public EventClientPacket(Packet<?> packet, Stage stage)
    {
        super(packet, stage);
    }
}
