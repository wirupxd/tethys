package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.command.Command;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.init.SoundEvents;

import java.util.HashSet;
import java.util.Set;

public class MobNotifier
        extends Module {

    private static MobNotifier instance;
    private final Set<Entity> donkeyList = new HashSet<Entity>();
    private final Set<Entity> llamaList = new HashSet<Entity>();
    private final Set<Entity> muleList = new HashSet<Entity>();
    private final Set<Entity> ghastList = new HashSet<Entity>();
    private final Set<Entity> mushroomList = new HashSet<Entity>();
    public Setting<Boolean> Donkey = this.register(new Setting<Boolean>("Donkey", true));
    public Setting<Boolean> Llama = this.register(new Setting<Boolean>("Llama", true));
    public Setting<Boolean> Mule = this.register(new Setting<Boolean>("Mule", true));
    public Setting<Boolean> Ghast = this.register(new Setting<Boolean>("Ghast", true));
    public Setting<Boolean> Mushroom = this.register(new Setting<Boolean>("Mooshroom", true));
    public Setting<Boolean> Sound = this.register(new Setting<Boolean>("Sound",true));


    public MobNotifier() {
        super("MobNotify", "when entitiyy spawn = notify", Category.MISC, true, false, false);
        instance = this;
    }

    //Command.sendMessage("Donkey detected at: x:" + entity.getPosition().getX() + ", y:" + entity.getPosition().getY() + ", z:" + entity.getPosition().getZ());

    @Override
    public void onEnable() {
        this.donkeyList.clear();
        this.llamaList.clear();
        this.muleList.clear();
        this.ghastList.clear();
        this.mushroomList.clear();
        /* this.g.clear();
        this.m.clear(); */
    }

    @Override
    public void onUpdate() {
        for (Entity entity : MobNotifier.mc.world.loadedEntityList) {

            if(entity instanceof EntityDonkey && !donkeyList.contains(entity) && Donkey.getValue())
            {
                Command.sendMessage(ChatFormatting.WHITE + "Donkey detected at " + entity.getPosition().getX() + ", " + entity.getPosition().getY() + ", " + entity.getPosition().getZ());
                donkeyList.add(entity);

                if (this.Sound.getValue())
                    MobNotifier.mc.player.playSound(SoundEvents.ENTITY_DONKEY_DEATH, 1.0f, 1.0f);

            }

            if(entity instanceof EntityLlama && !llamaList.contains(entity) && Llama.getValue())
            {
                Command.sendMessage(ChatFormatting.WHITE + "Llama detected at " + entity.getPosition().getX() + ", " + entity.getPosition().getY() + ", " + entity.getPosition().getZ());
                llamaList.add(entity);

                if (this.Sound.getValue())
                    MobNotifier.mc.player.playSound(SoundEvents.ENTITY_LLAMA_DEATH, 1.0f, 1.0f);

            }

            if(entity instanceof EntityMule && !muleList.contains(entity) && Mule.getValue())
            {
                Command.sendMessage(ChatFormatting.WHITE + "Mule detected at " + entity.getPosition().getX() + ", " + entity.getPosition().getY() + ", " + entity.getPosition().getZ());
                muleList.add(entity);

                if (this.Sound.getValue())
                    MobNotifier.mc.player.playSound(SoundEvents.ENTITY_DONKEY_DEATH, 1.0f, 1.0f);

            }



            if(entity instanceof EntityGhast && !ghastList.contains(entity) && Ghast.getValue())
            {
                Command.sendMessage(ChatFormatting.WHITE + "Ghast detected at " + entity.getPosition().getX() + ", " + entity.getPosition().getY() + ", " + entity.getPosition().getZ());
                ghastList.add(entity);

                if (this.Sound.getValue())
                    MobNotifier.mc.player.playSound(SoundEvents.ENTITY_GHAST_DEATH, 1.0f, 1.0f);

            }

            if(entity instanceof EntityMooshroom && !mushroomList.contains(entity) && Mushroom.getValue())
            {
                Command.sendMessage(ChatFormatting.WHITE + "Mooshroom detected at " + entity.getPosition().getX() + ", " + entity.getPosition().getY() + ", " + entity.getPosition().getZ());
                mushroomList.add(entity);

                if (this.Sound.getValue())
                    MobNotifier.mc.player.playSound(SoundEvents.ENTITY_COW_DEATH, 1.0f, 1.0f);

            }

//            if (!(osel instanceof EntityDonkey) || this.d.contains((Object)osel)) continue;
//              if (this.Donkey.getValue().booleanValue()) {
//                Command.sendMessage(ChatFormatting.WHITE + "Donkey detected at: x:" + osel.getPosition().getX() + ", y:" + osel.getPosition().getY() + ", z:" + osel.getPosition().getZ());
//                if (!this.Sound.getValue().booleanValue()) continue;
//                MobNotifier.mc.player.playSound(SoundEvents.ENTITY_DONKEY_DEATH, 1.0f, 1.0f);
//                this.d.add(osel);
        /* for (Entity ghas : MobNotifier.mc.world.loadedEntityList) {
            if (!(ghas instanceof EntityGhast) || this.g.contains((Object)ghas)) continue;
            if (this.Ghast.getValue().booleanValue()) {
                Command.sendMessage(ChatFormatting.WHITE + "Ghast detected at: x:" + ghas.getPosition().getX() + ", y:" + ghas.getPosition().getY() + ", z:" + ghas.getPosition().getZ());
                if (!this.Sound.getValue().booleanValue()) continue;
                MobNotifier.mc.player.playSound(SoundEvents.ENTITY_GHAST_DEATH, 1.0f, 1.0f);
            }
            this.g.add(ghas);
        for (Entity houba : MobNotifier.mc.world.loadedEntityList) {
            if (!(houba instanceof EntityGhast) || this.m.contains((Object)houba)) continue;
            if (this.Mushroom.getValue().booleanValue()) {
                Command.sendMessage(ChatFormatting.WHITE + "Ghast detected at: x:" + houba.getPosition().getX() + ", y:" + houba.getPosition().getY() + ", z:" + houba.getPosition().getZ());
                if (!this.Sound.getValue().booleanValue()) continue;
                MobNotifier.mc.player.playSound(SoundEvents.ENTITY_COW_DEATH, 1.0f, 1.0f);
            }
            this.m.add(houba);

        }
    }
}
    } */

        }
    }
}

