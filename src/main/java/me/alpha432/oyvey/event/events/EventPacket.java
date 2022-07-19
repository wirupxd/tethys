package me.alpha432.oyvey.event.events;

import net.minecraft.network.Packet;

public class EventPacket extends MinecraftEvent
{
    private Packet<?>  _packet;

    public EventPacket(Packet<?>  packet, Stage stage)
    {
        super(stage);
        _packet = packet;
    }

    public Packet<?> getPacket()
    {
        return _packet;
    }
}
