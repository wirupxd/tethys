package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.modules.client.HUD;
import me.alpha432.oyvey.util.Timer;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayDeque;


public class ServerManager
        extends Feature {
    private final float[] tpsCounts = new float[10];
    private final DecimalFormat format = new DecimalFormat("0.000");
    private final Timer timer = new Timer();
    private float TPS = 20.00f;
    private float currentTps = 20.00f;
    private long lastUpdate = -1L;
    private String serverBrand = "";
    private long time;
    private final ArrayDeque<Float> queue = new ArrayDeque<>(20);

    public void onPacketReceived() {
        this.timer.reset();
    }

    public boolean isServerNotResponding() {
        return this.timer.passedMs(HUD.getInstance().lagTime.getValue().intValue());
    }

    public long serverRespondingTime() {
        return this.timer.getPassedTimeMs();
    }

    public void update() {
        time = System.currentTimeMillis();
        float tps;
        long currentTime = System.currentTimeMillis();
        if (this.lastUpdate == -1L) {
            this.lastUpdate = currentTime;
            return;
        }
        long timeDiff = currentTime - this.lastUpdate;
        float tickTime = (float) timeDiff / 20.0f;
        if (tickTime == 0.0f) {
            tickTime = 50.0f;
        }
        if ((tps = 1000.0f / tickTime) > 20.0f) {
            tps = 20.0f;
        }
        System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
        this.tpsCounts[0] = tps;
        double total = 0.0;
        for (float f : this.tpsCounts) {
            total += f;
        }
        if ((total /= this.tpsCounts.length) > 20.0) {
            total = 20.0;
        }
        this.TPS = Float.parseFloat(this.format.format(total));
        this.lastUpdate = currentTime;
        currentTps = Math.max(0.0f, Math.min(20.0f, 20.0f * (1000.0f / (System.currentTimeMillis() - time))));
        queue.add(currentTps);
        float factor = 0.0f;
        for (Float qTime : queue)
        {
            factor += Math.max(0.0f, Math.min(20.0f, qTime));
        }

        if (queue.size() > 0)
        {
            factor /= queue.size();
        }

    }


    @Override
    public void reset() {
        Arrays.fill(this.tpsCounts, 20.0f);
        this.TPS = 20.0f;
    }

    public float getTpsFactor() {
        return 20.0f / this.TPS;
    }

    public float getTPS() {
        return this.TPS;
    }

    public float getCurrentTps()
    {
        return this.currentTps;
    }


    public String getServerBrand() {
        return this.serverBrand;
    }

    public void setServerBrand(String brand) {
        this.serverBrand = brand;
    }

    public int getPing() {
        if (ServerManager.fullNullCheck()) {
            return 0;
        }
        try {
            return Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
        } catch (Exception e) {
            return 0;
        }
    }
}

