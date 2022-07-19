package me.alpha432.oyvey.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public interface Minecraftable {
    Minecraft mc = Minecraft.getMinecraft();

    final static Minecraft mc2 = Minecraft.getMinecraft();
    public static Minecraft GetMC()
    {
        return mc2;
    }

    public static EntityPlayerSP GetPlayer()
    {
        return mc2.player;
    }
}
