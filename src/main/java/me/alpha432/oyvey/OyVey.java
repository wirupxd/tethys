package me.alpha432.oyvey;

import me.alpha432.oyvey.api.IconUtil;
import me.alpha432.oyvey.manager.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.InputStream;
import java.nio.ByteBuffer;

@Mod(modid = "tethys", name = "Tethys", version = OyVey.MODVER)
public class OyVey {
    public static final String MODID = "tethys";
    public static final String MODNAME = "Tethys";
    public static final String MODVER = "2.3.0";
    public static final String MODVERGIT = OyVey.MODVER;
    public static final Logger LOGGER = LogManager.getLogger("Tethys");
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static TotemPopManager totemPopManager;
    @Mod.Instance
    public static OyVey INSTANCE;
    public static String username = "User";
    private static boolean unloaded;


    static {
        unloaded = false;
    }

    public static void load() {
        LOGGER.info("\n\nLoading Tethys");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }

        /* if(!Protekce.sus()){
            JFrame intruderAlert = new JFrame("You are not whitelisted!");
            intruderAlert.setLocationRelativeTo(null);
            intruderAlert.setSize(new Dimension(600, 600));
            intruderAlert.setPreferredSize(new Dimension(600, 600));
            intruderAlert.setMinimumSize(new Dimension(600, 600));
            intruderAlert.setMaximumSize(new Dimension(600, 600));
            intruderAlert.setResizable(false);
            try {
                intruderAlert.add(new JLabel(new ImageIcon(ImageIO.read(OyVey.class.getClassLoader().getResourceAsStream("intruder.jpg")))));
            } catch (IOException e) {
                while (true){

                }
            }
            intruderAlert.setVisible(true);
            intruderAlert.pack();
           while (true){

           }
        }*/

        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        totemPopManager = new TotemPopManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        LOGGER.info("Tethys successfully loaded!\n");
        //Display.setTitle("guguhack - " + MODVER + " (user: " + username + ")");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading Tethys");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : "?");
        }
        OyVey.onUnload();
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        totemPopManager = null;
        LOGGER.info("Tethys unloaded!\n");
    }

    public static void reload() {
        OyVey.unload(false);
        OyVey.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(OyVey.configManager.config.replaceFirst("Tethys/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Display.setTitle("Guguhack 1.12.2");
        LOGGER.info("hhh");
        //setWindowIcon();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        OyVey.load();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent fMLPostInitializationEvent) {
    }


    public static void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            try (InputStream inputStream = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/icon/icon16x.png");
                 InputStream inputStream2 = Minecraft.class.getResourceAsStream("/assets/minecraft/textures/icon/icon32x.png");){
                ByteBuffer[] byteBufferArray = new ByteBuffer[]{IconUtil.INSTANCE.readImageToBuffer(inputStream), IconUtil.INSTANCE.readImageToBuffer(inputStream2)};
                Display.setIcon(byteBufferArray);
            }
            catch (Exception exception) {
                LOGGER.error("Couldn't set Windows Icon", exception);
            }
        }
    }
}
