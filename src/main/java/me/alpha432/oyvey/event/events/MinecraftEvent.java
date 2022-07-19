package me.alpha432.oyvey.event.events;

import me.alpha432.oyvey.api.Minecraftable;

public class MinecraftEvent extends Cancellable
{
    private Stage _stage = Stage.Pre;
    private final float partialTicks;

    public MinecraftEvent()
    {
        partialTicks = Minecraftable.GetMC().getRenderPartialTicks();
    }

    public MinecraftEvent(Stage stage)
    {
        this();
        _stage = stage;
    }

    public Stage getStage()
    {
        return _stage;
    }

    public void setEra(Stage stage)
    {
        this.setCancelled(false);
        _stage = stage;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    public enum Stage
    {
        Pre,
        Post,
    }
}
