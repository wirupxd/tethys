// Decompiled with: CFR 0.152
// Class Version: 8
package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class Capes
        extends Module {
    public static final ResourceLocation gugucape = new ResourceLocation("textures/gugucape.png");
    public static Map<String, String[]> UUIDs = new HashMap<String, String[]>();
    private static Capes instance;

    public Capes() {
        super("Capes", "Renders the client's capes", Module.Category.CLIENT, false, false, false);
        UUIDs.put("popbobik", new String[]{"5822acb0-5ff3-4e0d-86cf-6d5d96ddeeaf", "76f617ae-3714-4d5f-b31b-710812ce413a", "689d8ec1-e28c-49dc-b08c-6f84c5643eb4"});
        UUIDs.put("xello", new String[]{"860ea469-9468-4296-be1a-fb8121a8e57d", "7545184e-42e3-4844-bd7b-264942190fd9", "7ad34e8b-682c-4db9-854d-afa0a465f277"});
        UUIDs.put("hekt", new String[]{"d3078a12-1e33-4064-8ae1-f72696af4562"});
        UUIDs.put("cutjo", new String[]{"41406900-eeed-4222-b987-8d600932ed68"});
        UUIDs.put("adam", new String[]{"e5f0cb0a-4fcd-4f6a-954f-fa404ec5db06"});
        UUIDs.put("wasab", new String[]{"e541a8f4-3103-4f6f-846e-7b558390cf58"});
        UUIDs.put("josef", new String[]{"fa054918-7088-4308-baca-c6e84244bdc9"});
        UUIDs.put("drift", new String[]{"689f502a-8d28-4968-94ba-a9ac1e0f1dba", "fbcb73b0-0c14-430b-9aa0-0ffe6d313724"});
        instance = this;
    }

    public static Capes getInstance() {
        if (instance == null) {
            instance = new Capes();
        }
        return instance;
    }

    public static ResourceLocation getCapeResource(AbstractClientPlayer abstractClientPlayer) {
        for (String string : UUIDs.keySet()) {
            for (String string2 : UUIDs.get(string)) {
                if (string.equalsIgnoreCase("popbobik") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (string.equalsIgnoreCase("xello") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (string.equalsIgnoreCase("hekt") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (string.equalsIgnoreCase("cutjo") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (string.equalsIgnoreCase("adam") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (string.equalsIgnoreCase("wasab") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (string.equalsIgnoreCase("josef") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (string.equalsIgnoreCase("drift") && abstractClientPlayer.getUniqueID().toString().equals(string2)) {
                    return gugucape;
                }
                if (!string.equalsIgnoreCase("drift") || !abstractClientPlayer.getUniqueID().toString().equals(string2)) continue;
                return gugucape;
            }
        }
        return null;
    }

    public static boolean hasCape(UUID uUID) {
        Iterator<String> iterator = UUIDs.keySet().iterator();
        if (iterator.hasNext()) {
            String string = iterator.next();
            return Arrays.asList((Object[])UUIDs.get(string)).contains(uUID.toString());
        }
        return false;
    }

}
