package me.alpha432.oyvey.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.events.ClientEvent;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.util.Timer;
import me.alpha432.oyvey.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class HUD extends Module {
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static HUD INSTANCE = new HUD();
    private final Map<Potion, Color> potionColorMap = new HashMap<>();
    public Map<Integer,  Integer> colorMap;
    private final Setting<Boolean> grayNess = register(new Setting("Gray", Boolean.valueOf(true)));
    public Setting<Boolean> textRadar = this.register(new Setting("TextRadar", false, "A TextRadar"));
    public Setting<Integer> textRadarUpdates = this.register(new Setting<Integer>("Updates", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), n -> this.textRadar.getValue()));
    private final Setting<Boolean> renderingUp = register(new Setting("RenderingUp", Boolean.valueOf(false), "Orientation of the HUD-Elements."));
    private final Setting<Boolean> waterMark = register(new Setting("Watermark", Boolean.valueOf(false)));
    private final Setting<Boolean> version = register(new Setting("Version", Boolean.valueOf(false), v -> this.waterMark.getValue().booleanValue()));
    private final Setting<Boolean> github = register(new Setting("Github", Boolean.valueOf(false), v -> this.version.getValue().booleanValue()));
    private final Setting<Integer> waterMarkY = register(new Setting("Watermark Y", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(20), v -> this.waterMark.getValue().booleanValue()));
    //private final Setting<Boolean> noVersion = register(new Setting("NoVersion", Boolean.valueOf(false), "displays no version watermark"));
    private final Setting<Boolean> futureBeta = register(new Setting("FutureBeta", Boolean.valueOf(false), "Future beta watermark"));
    public Setting<Integer> futureBetaY = register(new Setting("Future Beta Y", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(20), v -> this.futureBeta.getValue().booleanValue()));
    private final Setting<Boolean> FBwatermark = this.register(new Setting<Boolean>("CS:GO", Boolean.FALSE));
    private final Setting<Boolean> FBwhiterender = this.register(new Setting<Object>("WhiteText", Boolean.FALSE, object -> this.FBwatermark.getValue()));
    public Setting<Integer> fbred = this.register(new Setting<Integer>("Line Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), n -> this.FBwatermark.getValue()));
    public Setting<Integer> fbgreen = this.register(new Setting<Integer>("Line Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), n -> this.FBwatermark.getValue()));
    public Setting<Integer> fbblue = this.register(new Setting<Integer>("Line Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), n -> this.FBwatermark.getValue()));
    public Setting<Integer> fbalpha = this.register(new Setting<Integer>("Line Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), n -> this.FBwatermark.getValue()));
    private final Setting<Boolean> arrayList = register(new Setting("ActiveModules", Boolean.valueOf(false), "Lists the active modules."));
    private final Setting<Boolean> coords = register(new Setting("Coords", Boolean.valueOf(false), "Your current coordinates"));
    private final Setting<Boolean> directiom = register(new Setting("Direction", Boolean.valueOf(false),  n -> this.coords.getValue()));
    private final Setting<Boolean> test = register(new Setting("Short", Boolean.valueOf(false),  n -> this.directiom.getValue()));
    private final Setting<Boolean> pvpInfo = register(new Setting("1337 hvh hud", Boolean.valueOf(false), "hhhh"));
    private final Setting<Integer> pvpInfoY = this.register(new Setting<Object>("PvP Info Y", Integer.valueOf(60), Integer.valueOf(0), Integer.valueOf(500), object -> this.pvpInfo.getValue()));
    private final Setting<Boolean> pvpInfoSync = this.register(new Setting<Object>("PvP Info Sync", Boolean.valueOf(false), object -> this.pvpInfo.getValue()));
    public Setting<String> pvpInfoText = register(new Setting("PvP Info Text", "Guguhack", object -> this.pvpInfo.getValue()));
    public Setting<Compass> compass = this.register(new Setting<Compass>("Compass", Compass.NONE));
    public Setting<Integer> compassX = this.register(new Setting<Object>("CompX", Integer.valueOf(472), Integer.valueOf(0), Integer.valueOf(1000), v -> this.compass.getValue() != Compass.NONE));
    public Setting<Integer> compassY = this.register(new Setting<Object>("CompY", Integer.valueOf(424), Integer.valueOf(0), Integer.valueOf(1000), v -> this.compass.getValue() != Compass.NONE));
    public Setting<Integer> scale = this.register(new Setting<Object>("Scale", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(10), v -> this.compass.getValue() != Compass.NONE));
    private final Setting<Boolean> armor = register(new Setting("Armor", Boolean.valueOf(false), "ArmorHUD"));
    private final Setting<Boolean> totems = register(new Setting("Totems", Boolean.valueOf(false), "TotemHUD"));
    private final Setting<Boolean> greeter = register(new Setting("Greeter", Boolean.valueOf(false), "The time"));
    public Setting<String> greeterText1 = register(new Setting("Text", "Wagwan", v -> this.greeter.getValue().booleanValue()));
    public Setting<String> greeterText2 = register(new Setting("Text2", ":^)", v -> this.greeter.getValue().booleanValue()));
    private final Setting<Boolean> speed = register(new Setting("Speed", Boolean.valueOf(false), "Your Speed"));
    public final Setting<Boolean> potions = register(new Setting("Potions", Boolean.valueOf(false), "Your Speed"));
    private final Setting<Boolean> ping = register(new Setting("Ping", Boolean.valueOf(false), "Your response time to the server."));
    private final Setting<Boolean> tps = register(new Setting("TPS", Boolean.valueOf(false), "Ticks per second of the server."));
    //private final Setting<Boolean> currentTps = register(new Setting("CurrentTPS", Boolean.valueOf(false), n -> tps.getValue().booleanValue()));
    private final Setting<Boolean> fps = register(new Setting("FPS", Boolean.valueOf(false), "Your frames per second."));
    private final Setting<Boolean> lag = register(new Setting("Lag-O-Meter", Boolean.valueOf(false), "The time"));
    private final Timer timer = new Timer();
    private Map<String, Integer> players = new HashMap<>();
    //public Setting<Boolean> magenDavid = register(new Setting("pepsi", Boolean.valueOf(false), "draws magen david"));
    public Setting<Integer> animationHorizontalTime = register(new Setting("AnimationHTime", Integer.valueOf(500), Integer.valueOf(1), Integer.valueOf(1000), v -> this.arrayList.getValue().booleanValue()));
    public Setting<Integer> animationVerticalTime = register(new Setting("AnimationVTime", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(500), v -> this.arrayList.getValue().booleanValue()));
    public Setting<RenderingMode> renderingMode = register(new Setting("Ordering", RenderingMode.ABC));
    //public Setting<Integer> noVerY = register(new Setting("NoVersion Y", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(20), v -> this.noVersion.getValue().booleanValue()));
    public Setting<Boolean> time = register(new Setting("Time", Boolean.valueOf(false), "The time"));
    public Setting<Integer> lagTime = register(new Setting("LagTime", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(2000)));
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    private final String string1 = OyVey.MODNAME + " - " + OyVey.MODVER;
    private final String string2 = OyVey.MODNAME;
    Date date = new Date();
    private final String tetys = "\u00A75[\u00A7d\u00A7lTethys\u00A7r\u00A75]\u00A7r";



    private static final DecimalFormat df = new DecimalFormat("0.00");

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static HUD getInstance() {
        if (INSTANCE == null)
            INSTANCE = new HUD();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onUpdate() {
        if (this.shouldIncrement)
            this.hitMarkerTimer++;
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
        if (this.timer.passedMs(HUD.getInstance().textRadarUpdates.getValue())) {
            this.players = this.getTextRadarPlayers();
            this.timer.reset();
        }
    }

    private int getColor(String string, EntityPlayer entityPlayer) {
        int n = 65280;
        int n2 = 0xFF0000;
        switch (string) {
            case "Safe": {
                if (entityPlayer != null) {
                    int n3 = CombatUtil.isInHoleInt(entityPlayer);
                    if (n3 == 0) {
                        return 0xFF5252;
                    }
                    if (n3 == 1) {
                        return 0x4FFF4F;
                    }
                    if (n3 == 2) {
                        return 0xFFA336;
                    }
                }
                return 0xFF0000;
            }
        }
        if (string.startsWith("Ping:") && !mc.isSingleplayer()) {

            if(mc.getConnection() == null)
                return 0xFF5252;

            int n4 = (this.mc.getConnection() != null && this.mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID()) != null ? this.mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime() : 0);
            if (n4 >= 100) {
                return 0xFF5252;
            }
            return 0x4FFF4F;
        }
        if (string.startsWith("Tots:")) {
            int n5 = ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING);
            if (n5 > 0) {
                return 0x4FFF4F;
            }
        }
        return 0xFF5252;
    }

    public void onRender2D(Render2DEvent event) {
        if (this.compass.getValue() != Compass.NONE) {
            this.drawCompass();
        }
        if (fullNullCheck())
            return;
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA((ClickGui.getInstance()).red.getValue().intValue(), (ClickGui.getInstance()).green.getValue().intValue(), (ClickGui.getInstance()).blue.getValue().intValue());
        if (this.waterMark.getValue().booleanValue()) {
            String watermark = OyVey.MODNAME;
            String watermarkver = OyVey.MODNAME + " " + ChatFormatting.GRAY + OyVey.MODVER;
            String watermarkgit = OyVey.MODNAME + " " + ChatFormatting.GRAY + OyVey.MODVERGIT;
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string1, 2.0F, this.waterMarkY.getValue().intValue(), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = watermark.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0F + f, this.waterMarkY.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                if ((HUD.getInstance()).version.getValue().booleanValue()) {
                    if ((HUD.getInstance()).github.getValue().booleanValue()) {
                        this.renderer.drawString(watermarkgit, 2.0F, this.waterMarkY.getValue().intValue(), this.color, true);
                    } else {
                        this.renderer.drawString(watermarkver, 2.0F, this.waterMarkY.getValue().intValue(), this.color, true);
                    }
                }
            } this.renderer.drawString(watermark, 2.0F, this.waterMarkY.getValue().intValue(), this.color, true);
        }

        if (this.futureBeta.getValue().booleanValue()) {
            String string = "Future v2.14-beta+71.6874cfee82";
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0F, this.futureBetaY.getValue().intValue(), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = string.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0F + f, this.futureBetaY.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                this.renderer.drawString(string, 2.0F, this.futureBetaY.getValue().intValue(), this.color, true);
            }
        }

        if (this.pvpInfo.getValue().booleanValue()) {
            String[] stringArray = new String[]{"Tots: " + ItemUtil.getItemCount(Items.TOTEM_OF_UNDYING), "Ping: " + (this.mc.getConnection() != null && this.mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID()) != null ? Integer.valueOf(this.mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime()) : "0"), "Safe"};
            int n = 0;
            int n2 = 0;
            OyVey.textManager.drawString(this.pvpInfoText.getPlannedValue(), 2.0f, this.pvpInfoY.getValue() - 9, this.pvpInfoSync.getValue() != false ? OyVey.colorManager.getColorAsInt() : ColorUtil.rainbow(16).getRGB(), true);
            for (n = 0; n < stringArray.length; ++n) {
                String string = stringArray[n];
                OyVey.textManager.drawString(string, 2.0f, this.pvpInfoY.getValue() + n2, getColor(string, mc.player), true);
                n2 += 9;
            }
        }



//        if (this.noVersion.getValue().booleanValue()) {
//            String string = this.command.getPlannedValue();
//            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
//                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
//                    this.renderer.drawString(string, 2.0F, this.noVerY.getValue().intValue(), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
//                } else {
//                    int[] arrayOfInt = {1};
//                    char[] stringToCharArray = string.toCharArray();
//                    float f = 0.0F;
//                    for (char c : stringToCharArray) {
//                        this.renderer.drawString(String.valueOf(c), 2.0F + f, this.noVerY.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
//                        f += this.renderer.getStringWidth(String.valueOf(c));
//                        arrayOfInt[0] = arrayOfInt[0] + 1;
//                    }
//                }
//            } else {
//                this.renderer.drawString(string, 2.0F, this.noVerY.getValue().intValue(), this.color, true);
//            }
//        }
        if (this.textRadar.getValue()) {
            this.drawTextRadar(0);
        }
        int[] counter1 = {1};
        int j = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && !this.renderingUp.getValue().booleanValue()) ? 14 : 0;
        if (this.arrayList.getValue().booleanValue())
            if (this.renderingUp.getValue().booleanValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); k++) {
                        String str = OyVey.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (2 + j * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                        j++;
                        counter1[0] = counter1[0] + 1;
                    }
                } else {
                    for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); k++) {
                        Module module = OyVey.moduleManager.sortedModules.get(k);
                        String str = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (2 + j * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                        j++;
                        counter1[0] = counter1[0] + 1;
                    }
                }
            } else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < OyVey.moduleManager.sortedModulesABC.size(); k++) {
                    String str = OyVey.moduleManager.sortedModulesABC.get(k);
                    j += 10;
                    this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (height - j), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                for (int k = 0; k < OyVey.moduleManager.sortedModules.size(); k++) {
                    Module module = OyVey.moduleManager.sortedModules.get(k);
                    String str = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    j += 10;
                    this.renderer.drawString(str, (width - 2 - this.renderer.getStringWidth(str)), (height - j), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        String grayString = this.grayNess.getValue().booleanValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && this.renderingUp.getValue().booleanValue()) ? 13 : (this.renderingUp.getValue().booleanValue() ? -2 : 0);
        if (this.renderingUp.getValue().booleanValue()) {
            if (this.potions.getValue().booleanValue()) {
                List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = OyVey.potionManager.getColoredPotionString(potionEffect);
                    i += 9;
                    this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                String str = grayString + "Km/h: " + ChatFormatting.WHITE + df.format(OyVey.speedManager.getSpeedKpH());
                i += 9;
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                String str = grayString + "Time: " + ChatFormatting.WHITE + (new SimpleDateFormat("H:mm:ss")).format(new Date());
                i += 9;
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                DecimalFormat tpsFormatted = new DecimalFormat("0.00");
                String str = grayString + "Tps: " + ChatFormatting.WHITE + tpsFormatted.format(OyVey.serverManager.getTPS());

                i += 9;
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;

            }


            String fpsText = grayString + "Fps: " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping: " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    i += 9;
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    i += 9;
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    i += 9;
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    i += 9;
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            if (this.potions.getValue().booleanValue()) {
                List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = OyVey.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue().booleanValue()) {
                String str = grayString + "Speed: " + ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + "km/h";
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                String str = grayString + "Time: " + ChatFormatting.WHITE + (new SimpleDateFormat("H:mm:ss")).format(new Date());
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                DecimalFormat tpsFormatted = new DecimalFormat("0.00");
                String str = grayString + "Tps: " + ChatFormatting.WHITE + tpsFormatted.format(OyVey.serverManager.getTPS());
                this.renderer.drawString(str, (width - this.renderer.getStringWidth(str) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            String fpsText = grayString + "Fps: " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping: " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, (width - this.renderer.getStringWidth(fpsText) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString(str1, (width - this.renderer.getStringWidth(str1) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        if (this.FBwatermark.getValue()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm:ss");
            final String string14 = OyVey.MODNAME + " | " + Objects.requireNonNull(HUD.mc.getCurrentServerData()).serverIP + " | " + Minecraft.debugFPS + " FPS" + "";
            Gui.drawRect(2, 2, this.renderer.getStringWidth(string14) + 10, 16, -1879048192);
            Gui.drawRect(2, 2, this.renderer.getStringWidth(string14) + 10, 3, ColorUtil.toRGBA(this.fbred.getValue(), this.fbgreen.getValue(), this.fbblue.getValue(), this.fbalpha.getValue()));
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string14, 6.0f, 6.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] array4 = { 1 };
                    final char[] charArray2 = string14.toCharArray();
                    float n5 = 0.0f;
                    for (final char c2 : charArray2) {
                        this.renderer.drawString(String.valueOf(c2), 6.0f + n5, 6.0f, ColorUtil.rainbow(array4[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        n5 += this.renderer.getStringWidth(String.valueOf(c2));
                        ++array4[0];
                    }
                }
            }
            else {
                this.renderer.drawString(string14, 6.0f, 6.0f, this.color, true);
            }
            if (this.FBwhiterender.getValue()) {
                this.renderer.drawString(string14, 6.0f, 6.0f, -1, true);
            }
        }
        boolean inHell = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell");
        String posX = df.format(mc.player.posX);
        String posY = df.format(mc.player.posY);
        String posZ = df.format(mc.player.posZ);
        float nether = !inHell ? 0.125F : 8.0F;
        String hposX = df.format((mc.player.posX * nether));
        String hposZ = df.format((mc.player.posZ * nether));
        i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0;
        String shortd = ChatFormatting.GRAY + " ["  + ChatFormatting.WHITE + OyVey.rotationManager.getDirection4D(false) + ChatFormatting.GRAY + "]";
        String coordinates = ChatFormatting.RESET + (inHell ? ("XYZ " + ChatFormatting.WHITE + posX + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE  + posY + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + posZ  + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + hposZ + ChatFormatting.GRAY + "]" + ChatFormatting.RESET) : ("XYZ " + ChatFormatting.WHITE + posX + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE  + posY + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + posZ  + ChatFormatting.GRAY + " [" + ChatFormatting.WHITE + hposX + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + hposZ + ChatFormatting.GRAY + "]" + ChatFormatting.RESET));
        String coords = this.coords.getValue().booleanValue() ? coordinates : "";
        String direction = this.directiom.getValue().booleanValue() ? OyVey.rotationManager.getDirection4D2(false) + ChatFormatting.GRAY + " ["  + ChatFormatting.WHITE + OyVey.rotationManager.getDirection4D(false) + ChatFormatting.GRAY + "]" : "";
        i += 9;
        if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
            String rainbowCoords = this.coords.getValue().booleanValue() ? ((inHell ? (posX + ", " + posY + ", " + posZ  + " [" + hposX + ", " + hposZ + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + "]"))) : "";
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(rainbowCoords, 2.0F, (height - i), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
            } else {
                int[] counter2 = {1};
                float s = 0.0F;
                int[] counter3 = {1};
                char[] stringToCharArray2 = rainbowCoords.toCharArray();
                float u = 0.0F;
                for (char c : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c), 2.0F + u, (height - i), ColorUtil.rainbow(counter3[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c));
                    counter3[0] = counter3[0] + 1;
                }
            }
        } else {
            if (this.test.getValue().booleanValue()) {
                this.renderer.drawString(coords + shortd, 2.0F, (height - i), this.color, true);
            }
            else {
                this.renderer.drawString(direction, 2.0F, (height - i - 9), this.color, true);
                this.renderer.drawString(coords, 2.0F, (height - i), this.color, true);
            }
        }
        if (this.armor.getValue().booleanValue())
            renderArmorHUD(true);
        if (this.totems.getValue().booleanValue())
            renderTotemHUD();
        if (this.greeter.getValue().booleanValue())
            renderGreeter();
        if (this.lag.getValue().booleanValue())
            renderLag();
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderGreeter() {
        int width = this.renderer.scaledWidth;
        String text = this.greeterText1.getPlannedValue() + " ";
        String text2 = " " + this.greeterText2.getPlannedValue();
        if (this.greeter.getValue().booleanValue())
            text = text + mc.player.getDisplayNameString() + text2;
        if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
            } else {
                int[] counter1 = {1};
                char[] stringToCharArray = text.toCharArray();
                float i = 0.0F;
                for (char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F + i, 2.0F, ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    i += this.renderer.getStringWidth(String.valueOf(c));
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, this.color, true);
        }
    }

    public void renderLag() {
        int width = this.renderer.scaledWidth;
        if (OyVey.serverManager.isServerNotResponding()) {
            String text = ChatFormatting.RED + "[!] Server has not responded for " + MathUtil.round((float) OyVey.serverManager.serverRespondingTime() / 1000.0F, 1) + "s";
            this.renderer.drawString(text, width / 2.0F - this.renderer.getStringWidth(text) / 2.0F + 2.0F, 20.0F, this.color, true);
        }
    }

    public void renderTotemHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
            totems += mc.player.getHeldItemOffhand().getCount();
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int iteration = 0;
            int y = height - 55 - ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", (x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    public void renderArmorHUD(boolean bl) {
        int n = this.renderer.scaledWidth;
        int n2 = this.renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        int n3 = n / 2;
        int n4 = 0;
        int n5 = n2 - 55 - (Util.mc.player.isInWater() && Util.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
        for (ItemStack itemStack : Util.mc.player.inventory.armorInventory) {
            ++n4;
            if (itemStack.isEmpty()) continue;
            int n6 = n3 - 90 + (9 - n4) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, n6, n5);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(Util.mc.fontRenderer, itemStack, n6, n5, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String string = itemStack.getCount() > 1 ? itemStack.getCount() + "" : "";
            this.renderer.drawStringWithShadow(string, n6 + 19 - 2 - this.renderer.getStringWidth(string), n5 + 9, 0xFFFFFF);
            if (!bl) continue;
            float f = ((float)itemStack.getMaxDamage() - (float)itemStack.getItemDamage()) / (float)itemStack.getMaxDamage();
            float f2 = 1.0f - f;
            int n7 = 100 - (int)(f2 * 100.0f);
            this.renderer.drawStringWithShadow(n7 + "", n6 + 8 - this.renderer.getStringWidth(n7 + "") / 2, n5 - 11, ColorUtil.toRGBA((int)(f2 * 255.0f), (int)(f * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(AttackEntityEvent event) {
        this.shouldIncrement = true;
    }

    public void onLoad() {
        OyVey.commandManager.setClientMessage(getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 &&
                equals(event.getSetting().getFeature()))
            OyVey.commandManager.setClientMessage(getCommandMessage() + ChatFormatting.BOLD);
    }

    public String getCommandMessage() {
        return tetys;
    }

    public void drawTextRadar(final int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (final Map.Entry<String, Integer> player : this.players.entrySet()) {
                final String text = player.getKey() + " ";
                final int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, (float) y, this.color, true);
                y += textheight;
            }
        }
    }

    public void drawCompass() {
        ScaledResolution sr = new ScaledResolution(mc);
        if (this.compass.getValue() == Compass.LINE) {
            float playerYaw = HUD.mc.player.rotationYaw;
            float rotationYaw = MathUtil.wrap(playerYaw);
            RenderUtil.drawRect(this.compassX.getValue().intValue(), this.compassY.getValue().intValue(), this.compassX.getValue() + 100, this.compassY.getValue() + this.renderer.getFontHeight(), 1963986960);
            RenderUtil.glScissor(this.compassX.getValue().intValue(), this.compassY.getValue().intValue(), this.compassX.getValue() + 100, this.compassY.getValue() + this.renderer.getFontHeight(), sr);
            GL11.glEnable((int)3089);
            float zeroZeroYaw = MathUtil.wrap((float)(Math.atan2(0.0 - HUD.mc.player.posZ, 0.0 - HUD.mc.player.posX) * 180.0 / Math.PI) - 90.0f);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + zeroZeroYaw, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + zeroZeroYaw, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -61424);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 45.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 45.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 45.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 45.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 135.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 135.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 135.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 135.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            this.renderer.drawStringWithShadow("n", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 180.0f - (float)this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("n", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 180.0f - (float)this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("e", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 90.0f - (float)this.renderer.getStringWidth("e") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("s", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - (float)this.renderer.getStringWidth("s") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("w", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 90.0f - (float)this.renderer.getStringWidth("w") / 2.0f, this.compassY.getValue().intValue(), -1);
            RenderUtil.drawLine(this.compassX.getValue() + 50, this.compassY.getValue() + 1, this.compassX.getValue() + 50, this.compassY.getValue() + this.renderer.getFontHeight() - 1, 2.0f, -7303024);
            GL11.glDisable((int)3089);
        } else {
            double centerX = this.compassX.getValue().intValue();
            double centerY = this.compassY.getValue().intValue();
            for (Direction dir : Direction.values()) {
                double rad = HUD.getPosOnCompass(dir);
                this.renderer.drawStringWithShadow(dir.name(), (float)(centerX + this.getX(rad)), (float)(centerY + this.getY(rad)), dir == Direction.N ? -65536 : -1);
            }
        }
    }
    private double getX(double rad) {
        return Math.sin(rad) * (double)(this.scale.getValue() * 10);
    }

    private double getY(double rad) {
        double epicPitch = MathHelper.clamp((float)(HUD.mc.player.rotationPitch + 30.0f), (float)-90.0f, (float)90.0f);
        double pitchRadians = Math.toRadians(epicPitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (double)(this.scale.getValue() * 10);
    }
    private static double getPosOnCompass(Direction dir) {
        double yaw = Math.toRadians(MathHelper.wrapDegrees((float)HUD.mc.player.rotationYaw));
        int index = dir.ordinal();
        return yaw + (double)index * 1.5707963267948966;
    }

    public static enum Compass {
        NONE,
        CIRCLE,
        LINE;

    }

    private static enum Direction {
        N,
        W,
        S,
        E;

    }

    public enum RenderingMode {
        Length, ABC
    }
}
