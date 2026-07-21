package bmorgan.radialmodmenu;

//? if >=1.21.6 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
//?}
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
//? if >=1.20.5 {
import net.minecraft.client.DeltaTracker;
//?}
import net.minecraft.client.Minecraft;

//? if >=1.21.6 {
public class RadialMenuHud implements HudElement {
//?} else {
/*public class RadialMenuHud {
*///?}

    private int activeWheel = 0;
    public static RadialMenuConfig config = RadialMenuConfig.load();

    private static final int RADIUS     = 80;
    private static final int SLOT_BOX_W = 60;
    private static final int SLOT_BOX_H = 16;

    //? if >=26.1 {
    @Override
    public void extractRenderState(GuiGraphicsExtractor gfx, DeltaTracker delta) {
        // All rendering is done by RadialMenuScreen while the wheel is open.
    }
    //?} else if >=1.21.6 {
    /*@Override
    public void render(GuiGraphics gfx, DeltaTracker delta) {
        // All rendering is done by RadialMenuScreen while the wheel is open.
    }
    *///?} else if >=1.20.5 {
    /*public void renderHudCallback(GuiGraphics gfx, DeltaTracker delta) {
        // All rendering is done by RadialMenuScreen while the wheel is open.
    }
    *///?} else {
    /*public void renderHudCallback(GuiGraphics gfx, float tickDelta) {
        // All rendering is done by RadialMenuScreen while the wheel is open.
    }
    *///?}

    // Slots: 0=top-right (dx>=0,dy<0), 1=bottom-right (dx>=0,dy>=0),
    //        2=bottom-left (dx<0,dy>=0), 3=top-left (dx<0,dy<0)
    public int getHoveredSlot(int mouseX, int mouseY, int cx, int cy) {
        int dx = mouseX - cx;
        int dy = mouseY - cy;
        if (dx * dx + dy * dy < 20 * 20) return -1;
        if (dx >= 0 && dy <  0) return 0;
        if (dx >= 0 && dy >= 0) return 1;
        if (dx <  0 && dy >= 0) return 2;
        return 3;
    }

    //? if >=26.1 {
    public void renderWheel(GuiGraphicsExtractor gfx, int mouseX, int mouseY) {
    //?} else {
    /*public void renderWheel(GuiGraphics gfx, int mouseX, int mouseY) {
    *///?}
        Minecraft mc = Minecraft.getInstance();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();
        int cx = screenW / 2;
        int cy = screenH / 2;

        int hovered = getHoveredSlot(mouseX, mouseY, cx, cy);
        String[] slots      = config.wheelBinds[activeWheel];
        String[] slotNames  = config.wheelSlotNames[activeWheel];

        drawCircleSlices(gfx, cx, cy, RADIUS, hovered);

        // Center dot
        gfx.fill(cx - 6, cy - 6, cx + 6, cy + 6, 0xFF8899FF);

        // Labels at quadrant centers
        int mid = RADIUS / 2;
        int[][] labelCenters = {
            { cx + mid, cy - mid }, // top-right
            { cx + mid, cy + mid }, // bottom-right
            { cx - mid, cy + mid }, // bottom-left
            { cx - mid, cy - mid }, // top-left
        };

        for (int i = 0; i < 4; i++) {
            boolean isHovered = (i == hovered);
            boolean hasBinding = slots[i] != null && !slots[i].isEmpty();
            int boxColor = isHovered ? 0xDD4499FF : (hasBinding ? 0xCC2255AA : 0xCC333333);
            int lx = labelCenters[i][0] - SLOT_BOX_W / 2;
            int ly = labelCenters[i][1] - SLOT_BOX_H / 2;
            gfx.fill(lx, ly, lx + SLOT_BOX_W, ly + SLOT_BOX_H, boxColor);
            String customName = slotNames != null ? slotNames[i] : null;
            String label = customName != null && !customName.isEmpty() ? customName
                         : hasBinding ? slots[i] : "Empty";
            //? if >=26.1 {
            gfx.text(mc.font, label, lx + 4, ly + 4, 0xFFFFFFFF);
            //?} else {
            /*gfx.drawString(mc.font, label, lx + 4, ly + 4, 0xFFFFFFFF);
            *///?}
        }

        // Wheel tabs
        for (int w = 0; w < 3; w++) {
            int tx = cx - 45 + w * 32;
            int ty = cy - RADIUS - 38;
            int tabColor = (w == activeWheel) ? 0xCC4477CC : 0xCC222222;
            gfx.fill(tx, ty, tx + 26, ty + 14, tabColor);
            //? if >=26.1 {
            gfx.text(mc.font, "W" + (w + 1), tx + 6, ty + 3, 0xFFFFFFFF);
            //?} else {
            /*gfx.drawString(mc.font, "W" + (w + 1), tx + 6, ty + 3, 0xFFFFFFFF);
            *///?}
        }

        //? if >=26.1 {
        gfx.text(mc.font, "[1/2/3] Wheel  [C] Config  [ESC] Close",
                cx - 90, cy + RADIUS + SLOT_BOX_H + 8, 0xFFAAAAAA);
        //?} else {
        /*gfx.drawString(mc.font, "[1/2/3] Wheel  [C] Config  [ESC] Close",
                cx - 90, cy + RADIUS + SLOT_BOX_H + 8, 0xFFAAAAAA);
        *///?}
    }

    //? if >=26.1 {
    private void drawCircleSlices(GuiGraphicsExtractor gfx, int cx, int cy, int r, int hoveredSlot) {
    //?} else {
    /*private void drawCircleSlices(GuiGraphics gfx, int cx, int cy, int r, int hoveredSlot) {
    *///?}
        // Slots: 0=top-right, 1=bottom-right, 2=bottom-left, 3=top-left
        // Split is a plain plus: left vs right by sign of dx, top vs bottom by sign of dy.
        int deadR2   = 20 * 20;
        int baseCol  = 0xAA111133;
        int hovCol   = 0xAA3366BB;
        int deadCol  = 0xAA222244;

        for (int dy = -r; dy <= r; dy++) {
            int halfW = (int) Math.sqrt((double)(r * r - dy * dy));
            if (halfW == 0) continue;
            int y         = cy + dy;
            int deadHalfW = (int) Math.sqrt(Math.max(0.0, deadR2 - (double)(dy * dy)));

            // Slot for this row's halves
            int slotRight = dy < 0 ? 0 : 1; // top-right or bottom-right
            int slotLeft  = dy < 0 ? 3 : 2; // top-left  or bottom-left

            // Right half: cx to cx+halfW, dead zone cx to cx+deadHalfW
            int rDeadEnd = cx + Math.min(deadHalfW, halfW);
            if (cx < rDeadEnd)
                gfx.fill(cx, y, rDeadEnd, y + 1, deadCol);
            if (rDeadEnd < cx + halfW)
                gfx.fill(rDeadEnd, y, cx + halfW, y + 1,
                         slotRight == hoveredSlot ? hovCol : baseCol);

            // Left half: cx-halfW to cx, dead zone cx-deadHalfW to cx
            int lDeadStart = cx - Math.min(deadHalfW, halfW);
            if (lDeadStart < cx)
                gfx.fill(lDeadStart, y, cx, y + 1, deadCol);
            if (cx - halfW < lDeadStart)
                gfx.fill(cx - halfW, y, lDeadStart, y + 1,
                         slotLeft == hoveredSlot ? hovCol : baseCol);
        }

        // Plus dividers
        int lineColor = 0xFF445566;
        gfx.fill(cx,     cy - r, cx + 1, cy + r, lineColor); // vertical
        gfx.fill(cx - r, cy,     cx + r, cy + 1, lineColor); // horizontal
    }

    public void switchWheel(int index) {
        if (index >= 0 && index < 3) activeWheel = index;
    }

    public int getActiveWheel() { return activeWheel; }
}
