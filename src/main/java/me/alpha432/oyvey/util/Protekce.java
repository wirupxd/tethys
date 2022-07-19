package me.alpha432.oyvey.util;

import me.alpha432.oyvey.OyVey;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;

public class Protekce {
    public static String getHWID() {
        try{
            String toEncrypt =  System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
    public static boolean sus() {
        try {
            URL url = new URL("https://sqlskid.xyz/sushp.php?hwid=" + getHWID());
            HttpsURLConnection asd = (HttpsURLConnection) url.openConnection();
            asd.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(asd.getInputStream()));

            String line = reader.readLine();
            if(line.contains("1"))
            {
                OyVey.username = line.split(" ")[1];
                return true;
            }
            else
            {
                return false;
            }
        } catch (Exception e) {
            while (true){

            }
        }

    }

}
