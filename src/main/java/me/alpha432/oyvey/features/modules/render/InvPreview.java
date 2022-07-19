package me.alpha432.oyvey.features.modules.render;


import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.setting.Setting;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

public class InvPreview
        extends Module {
    private final Setting<Integer> posX = new Setting<Integer>("X", 20, 0, 1000);
    private final Setting<Integer> posY = new Setting<Integer>("Y", 20, 0, 1000);
    private final RenderItem itemRender = this.mc.getRenderItem();

    public InvPreview() {
        super("InvPreview", "self explaintory", Category.RENDER, true, false, false);
        this.register(posX);
        this.register(posY);
    }

    @Override
    public void onRender2D(Render2DEvent render2DEvent) {
        GlStateManager.enableBlend();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        int n = this.mc.player.inventory.mainInventory.size() - 9;
        for (int i = 0; i < n; ++i) {
            int n2 = this.posX.getValue() + (i % 9 << 4) + 11;
            int n3 = this.posY.getValue() + (i / 9 << 4) - 11 + 8;
            ItemStack itemStack = (ItemStack)this.mc.player.inventory.mainInventory.get(i + 9);
            this.itemRender.renderItemAndEffectIntoGUI(itemStack, n2, n3);
            this.itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, itemStack, n2, n3, null);
        }
        RenderHelper.disableStandardItemLighting();
        this.itemRender.zLevel = 0.0f;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    private static void drawTexturedRect(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(n, n2 + n6, n7).tex((float)n3 * 0.00390625f, (float)(n4 + n6) * 0.00390625f).endVertex();
        bufferBuilder.pos(n + n5, n2 + n6, n7).tex((float)(n3 + n5) * 0.00390625f, (float)(n4 + n6) * 0.00390625f).endVertex();
        bufferBuilder.pos(n + n5, n2, n7).tex((float)(n3 + n5) * 0.00390625f, (float)n4 * 0.00390625f).endVertex();
        bufferBuilder.pos(n, n2, n7).tex((float)n3 * 0.00390625f, (float)n4 * 0.00390625f).endVertex();
        tessellator.draw();
    }
}