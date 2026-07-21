package bmorgan.radialmodmenu;

import net.minecraft.client.KeyMapping;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
//? if >=1.21.9 {
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
//?} else {
/*import net.minecraft.locale.Language;
*///?}
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;

public class RadialMenuConfigScreen extends Screen {

    private final RadialMenuHud hud;
    private int editingWheel;
    private int editingSlot = -1;

    private List<KeyMapping> allBindings;
    private int scrollOffset = 0;
    private EditBox nameBox;

    private static final int SLOT_W     = 160;
    private static final int SLOT_H     = 20;
    private static final int SLOT_PAD   = 6;
    private static final int NAME_H     = 16;
    private static final int LIST_W     = 180;
    private static final int LIST_ROW_H = 16;
    private static final int LIST_ROWS  = 14;

    private static final String[] SLOT_NAMES = { "Top-Right", "Bottom-Right", "Bottom-Left", "Top-Left" };

    public RadialMenuConfigScreen(RadialMenuHud hud) {
        super(Component.literal("Radial Menu Config"));
        this.hud = hud;
        this.editingWheel = hud.getActiveWheel();
    }

    @Override
    protected void init() {
        allBindings = Arrays.stream(minecraft.options.keyMappings)
                .sorted((a, b) -> {
                    int cat = categoryKey(a).compareTo(categoryKey(b));
                    return cat != 0 ? cat : a.getName().compareTo(b.getName());
                })
                .toList();

        int cx     = width / 2;
        int leftX  = cx - SLOT_W - 20;
        int startY = 40;

        // One shared EditBox, repositioned whenever editingSlot changes
        nameBox = new EditBox(font, leftX, startY, SLOT_W, NAME_H, Component.literal("Custom name"));
        nameBox.setMaxLength(32);
        nameBox.setResponder(text -> {
            if (editingSlot >= 0) {
                RadialMenuHud.config.wheelSlotNames[editingWheel][editingSlot] = text.isEmpty() ? null : text;
                RadialMenuHud.config.save();
            }
        });
        nameBox.visible = false;
        addWidget(nameBox);
    }

    // Category as a plain string key, regardless of whether KeyMapping.Category exists (1.21.9+) or not.
    private static String categoryKey(KeyMapping km) {
        //? if >=1.21.9 {
        return km.getCategory().id().toString();
        //?} else {
        /*return km.getCategory();
        *///?}
    }

    // Human-readable category label.
    private static String categoryLabel(KeyMapping km) {
        //? if >=1.21.9 {
        return km.getCategory().label().getString();
        //?} else {
        /*return Language.getInstance().getOrDefault(km.getCategory());
        *///?}
    }

    private void selectSlot(int slot) {
        editingSlot  = slot;
        scrollOffset = 0;
        int cx     = width / 2;
        int leftX  = cx - SLOT_W - 20;
        int startY = 40;

        if (slot >= 0) {
            // Position name box just below the selected slot row
            int slotRowH = SLOT_H + SLOT_PAD;
            int by = startY + slot * slotRowH + SLOT_H + 2;
            nameBox.setX(leftX);
            nameBox.setY(by);
            String existing = RadialMenuHud.config.wheelSlotNames[editingWheel][slot];
            nameBox.setValue(existing != null ? existing : "");
            nameBox.visible = true;
            setFocused(null); // don't auto-focus; user clicks it if they want to type
        } else {
            nameBox.visible = false;
            nameBox.setValue("");
        }
    }

    @Override
    public boolean isPauseScreen() { return false; }

    //? if >=26.1 {
    @Override
    public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float delta) {
        gfx.fill(0, 0, width, height, 0xCC000000);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float delta) {
        extractBackground(gfx, mouseX, mouseY, delta);

        int cx     = width / 2;
        int leftX  = cx - SLOT_W - 20;
        int rightX = cx + 20;
        int startY = 40;

        gfx.text(font, "Wheel " + (editingWheel + 1) + " — click a slot to assign", leftX, startY - 20, 0xFFFFFFFF);

        for (int w = 0; w < 3; w++) {
            int tx = leftX + w * 56;
            int ty = startY - 36;
            gfx.fill(tx, ty, tx + 50, ty + 14, w == editingWheel ? 0xCC4477CC : 0xCC222233);
            gfx.text(font, "Wheel " + (w + 1), tx + 5, ty + 3, 0xFFFFFFFF);
        }

        String[] slots = RadialMenuHud.config.wheelBinds[editingWheel];
        int slotRowH = SLOT_H + SLOT_PAD;

        for (int i = 0; i < 4; i++) {
            // When a slot above is selected, push lower slots down to make room for the name box
            int extraOffset = (editingSlot >= 0 && i > editingSlot) ? NAME_H + 2 : 0;
            int by = startY + i * slotRowH + extraOffset;

            boolean selected = editingSlot == i;
            boolean hovered  = mouseX >= leftX && mouseX <= leftX + SLOT_W
                            && mouseY >= by    && mouseY <= by + SLOT_H;
            gfx.fill(leftX, by, leftX + SLOT_W, by + SLOT_H, selected ? 0xCCAA6600 : hovered ? 0xCC334466 : 0xCC1A1A2E);

            String bind    = slots[i];
            String display = SLOT_NAMES[i] + ": ";
            if (bind != null && !bind.isEmpty()) {
                KeyMapping km = RadialMenuFire.findByName(bind);
                display += km != null ? km.getName() : bind;
            } else {
                display += "— unset —";
            }
            gfx.text(font, display, leftX + 5, by + 6, 0xFFFFFFFF);

            // Show custom name as a subtitle on the slot row
            if (selected) {
                gfx.text(font, "Name:", leftX, by + SLOT_H + 4, 0xFFCCCCCC);
            }
        }

        if (nameBox.visible) nameBox.extractRenderState(gfx, mouseX, mouseY, delta);

        gfx.text(font, "[Del] Clear bind  [ESC] Back", leftX,
                startY + 4 * slotRowH + (editingSlot >= 0 ? NAME_H + 2 : 0) + 4, 0xFFAAAAAA);

        // Right panel: keybind picker
        if (editingSlot >= 0) {
            gfx.text(font, "Pick bind for " + SLOT_NAMES[editingSlot] + ":", rightX, startY - 20, 0xFFFFFF55);

            int listH = LIST_ROWS * LIST_ROW_H;
            gfx.fill(rightX - 2, startY - 2, rightX + LIST_W + 2, startY + listH + 2, 0xFF334455);
            gfx.fill(rightX, startY, rightX + LIST_W, startY + listH, 0xFF111122);

            String lastCategory = null;
            int visibleRow = 0;
            for (int i = scrollOffset; i < allBindings.size() && visibleRow < LIST_ROWS; i++) {
                KeyMapping km  = allBindings.get(i);
                String cat = categoryKey(km);
                if (!cat.equals(lastCategory)) {
                    int ry = startY + visibleRow * LIST_ROW_H;
                    gfx.fill(rightX, ry, rightX + LIST_W, ry + LIST_ROW_H, 0xFF223344);
                    gfx.text(font, categoryLabel(km), rightX + 3, ry + 4, 0xFFAAAAFF);
                    lastCategory = cat;
                    visibleRow++;
                    if (visibleRow >= LIST_ROWS) break;
                }
                int ry = startY + visibleRow * LIST_ROW_H;
                boolean rowHovered = mouseX >= rightX && mouseX <= rightX + LIST_W
                                  && mouseY >= ry    && mouseY < ry + LIST_ROW_H;
                if (rowHovered) gfx.fill(rightX, ry, rightX + LIST_W, ry + LIST_ROW_H, 0xFF334466);
                String label = "  " + km.getName() + " [" + km.getTranslatedKeyMessage().getString() + "]";
                gfx.text(font, label, rightX + 3, ry + 4, 0xFFFFFFFF);
                visibleRow++;
            }

            gfx.text(font, "[Scroll] to browse", rightX, startY + listH + 4, 0xFFAAAAAA);
        }
    }
    //?} else {
    /*@Override
    public void renderBackground(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        gfx.fill(0, 0, width, height, 0xCC000000);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        renderBackground(gfx, mouseX, mouseY, delta);

        int cx     = width / 2;
        int leftX  = cx - SLOT_W - 20;
        int rightX = cx + 20;
        int startY = 40;

        gfx.drawString(font, "Wheel " + (editingWheel + 1) + " — click a slot to assign", leftX, startY - 20, 0xFFFFFFFF);

        for (int w = 0; w < 3; w++) {
            int tx = leftX + w * 56;
            int ty = startY - 36;
            gfx.fill(tx, ty, tx + 50, ty + 14, w == editingWheel ? 0xCC4477CC : 0xCC222233);
            gfx.drawString(font, "Wheel " + (w + 1), tx + 5, ty + 3, 0xFFFFFFFF);
        }

        String[] slots = RadialMenuHud.config.wheelBinds[editingWheel];
        int slotRowH = SLOT_H + SLOT_PAD;

        for (int i = 0; i < 4; i++) {
            // When a slot above is selected, push lower slots down to make room for the name box
            int extraOffset = (editingSlot >= 0 && i > editingSlot) ? NAME_H + 2 : 0;
            int by = startY + i * slotRowH + extraOffset;

            boolean selected = editingSlot == i;
            boolean hovered  = mouseX >= leftX && mouseX <= leftX + SLOT_W
                            && mouseY >= by    && mouseY <= by + SLOT_H;
            gfx.fill(leftX, by, leftX + SLOT_W, by + SLOT_H, selected ? 0xCCAA6600 : hovered ? 0xCC334466 : 0xCC1A1A2E);

            String bind    = slots[i];
            String display = SLOT_NAMES[i] + ": ";
            if (bind != null && !bind.isEmpty()) {
                KeyMapping km = RadialMenuFire.findByName(bind);
                display += km != null ? km.getName() : bind;
            } else {
                display += "— unset —";
            }
            gfx.drawString(font, display, leftX + 5, by + 6, 0xFFFFFFFF);

            // Show custom name as a subtitle on the slot row
            if (selected) {
                gfx.drawString(font, "Name:", leftX, by + SLOT_H + 4, 0xFFCCCCCC);
            }
        }

        if (nameBox.visible) nameBox.renderWidget(gfx, mouseX, mouseY, delta);

        gfx.drawString(font, "[Del] Clear bind  [ESC] Back", leftX,
                startY + 4 * slotRowH + (editingSlot >= 0 ? NAME_H + 2 : 0) + 4, 0xFFAAAAAA);

        // Right panel: keybind picker
        if (editingSlot >= 0) {
            gfx.drawString(font, "Pick bind for " + SLOT_NAMES[editingSlot] + ":", rightX, startY - 20, 0xFFFFFF55);

            int listH = LIST_ROWS * LIST_ROW_H;
            gfx.fill(rightX - 2, startY - 2, rightX + LIST_W + 2, startY + listH + 2, 0xFF334455);
            gfx.fill(rightX, startY, rightX + LIST_W, startY + listH, 0xFF111122);

            String lastCategory = null;
            int visibleRow = 0;
            for (int i = scrollOffset; i < allBindings.size() && visibleRow < LIST_ROWS; i++) {
                KeyMapping km  = allBindings.get(i);
                String cat = categoryKey(km);
                if (!cat.equals(lastCategory)) {
                    int ry = startY + visibleRow * LIST_ROW_H;
                    gfx.fill(rightX, ry, rightX + LIST_W, ry + LIST_ROW_H, 0xFF223344);
                    gfx.drawString(font, categoryLabel(km), rightX + 3, ry + 4, 0xFFAAAAFF);
                    lastCategory = cat;
                    visibleRow++;
                    if (visibleRow >= LIST_ROWS) break;
                }
                int ry = startY + visibleRow * LIST_ROW_H;
                boolean rowHovered = mouseX >= rightX && mouseX <= rightX + LIST_W
                                  && mouseY >= ry    && mouseY < ry + LIST_ROW_H;
                if (rowHovered) gfx.fill(rightX, ry, rightX + LIST_W, ry + LIST_ROW_H, 0xFF334466);
                String label = "  " + km.getName() + " [" + km.getTranslatedKeyMessage().getString() + "]";
                gfx.drawString(font, label, rightX + 3, ry + 4, 0xFFFFFFFF);
                visibleRow++;
            }

            gfx.drawString(font, "[Scroll] to browse", rightX, startY + listH + 4, 0xFFAAAAAA);
        }
    }
    *///?}

    //? if >=1.21.9 {
    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean handled) {
        int mouseX = (int) event.x();
        int mouseY = (int) event.y();
        boolean nameBoxHandled = nameBox.visible && nameBox.mouseClicked(event, false);
        if (nameBoxHandled) { setFocused(nameBox); return true; }
        if (handleMouseClicked(mouseX, mouseY)) return true;
        return super.mouseClicked(event, handled);
    }
    //?} else {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean nameBoxHandled = nameBox.visible && nameBox.mouseClicked(mouseX, mouseY, button);
        if (nameBoxHandled) { setFocused(nameBox); return true; }
        if (handleMouseClicked((int) mouseX, (int) mouseY)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }
    *///?}

    // Returns true if the click was consumed by wheel tabs, slot rows, or the keybind list.
    private boolean handleMouseClicked(int mouseX, int mouseY) {
        int cx     = width / 2;
        int leftX  = cx - SLOT_W - 20;
        int rightX = cx + 20;
        int startY = 40;
        int slotRowH = SLOT_H + SLOT_PAD;

        // Wheel tabs
        for (int w = 0; w < 3; w++) {
            int tx = leftX + w * 56, ty = startY - 36;
            if (mouseX >= tx && mouseX <= tx + 50 && mouseY >= ty && mouseY <= ty + 14) {
                editingWheel = w;
                selectSlot(-1);
                return true;
            }
        }

        // Slot rows (account for push-down offset)
        for (int i = 0; i < 4; i++) {
            int extraOffset = (editingSlot >= 0 && i > editingSlot) ? NAME_H + 2 : 0;
            int by = startY + i * slotRowH + extraOffset;
            if (mouseX >= leftX && mouseX <= leftX + SLOT_W && mouseY >= by && mouseY <= by + SLOT_H) {
                selectSlot(editingSlot == i ? -1 : i);
                return true;
            }
        }

        // Keybind list
        if (editingSlot >= 0) {
            int listH = LIST_ROWS * LIST_ROW_H;
            if (mouseX >= rightX && mouseX <= rightX + LIST_W
                    && mouseY >= startY && mouseY <= startY + listH) {
                String lastCategory = null;
                int visibleRow = 0;
                for (int i = scrollOffset; i < allBindings.size() && visibleRow < LIST_ROWS; i++) {
                    KeyMapping km  = allBindings.get(i);
                    String cat = categoryKey(km);
                    if (!cat.equals(lastCategory)) {
                        lastCategory = cat;
                        visibleRow++;
                        if (visibleRow > LIST_ROWS) break;
                    }
                    int ry = startY + visibleRow * LIST_ROW_H;
                    if (mouseY >= ry && mouseY < ry + LIST_ROW_H) {
                        RadialMenuHud.config.wheelBinds[editingWheel][editingSlot] = km.getName();
                        RadialMenuHud.config.save();
                        // Keep slot selected so name can still be edited
                        return true;
                    }
                    visibleRow++;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (editingSlot >= 0) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - (int) scrollY, allBindings.size() - 1));
            return true;
        }
        return false;
    }

    //? if >=1.21.9 {
    @Override
    public boolean keyPressed(KeyEvent event) {
        // Let the name box consume keystrokes when focused
        if (nameBox.visible && nameBox.isFocused()) {
            if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
                setFocused(null);
                return true;
            }
            return nameBox.keyPressed(event);
        }
        return handleKeyPressed(event.key());
    }
    //?} else {
    /*@Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        // Let the name box consume keystrokes when focused
        if (nameBox.visible && nameBox.isFocused()) {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                setFocused(null);
                return true;
            }
            return nameBox.keyPressed(key, scanCode, modifiers);
        }
        return handleKeyPressed(key);
    }
    *///?}

    private boolean handleKeyPressed(int key) {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            if (editingSlot >= 0) { selectSlot(-1); return true; }
            onClose();
            return true;
        }
        if (editingSlot >= 0 && (key == GLFW.GLFW_KEY_DELETE || key == GLFW.GLFW_KEY_BACKSPACE)) {
            RadialMenuHud.config.wheelBinds[editingWheel][editingSlot] = null;
            RadialMenuHud.config.save();
            selectSlot(-1);
            return true;
        }
        return false;
    }

    //? if >=1.21.9 {
    @Override
    public boolean charTyped(CharacterEvent event) {
        if (nameBox.visible && nameBox.isFocused()) {
            return nameBox.charTyped(event);
        }
        return false;
    }
    //?} else {
    /*@Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (nameBox.visible && nameBox.isFocused()) {
            return nameBox.charTyped(codePoint, modifiers);
        }
        return false;
    }
    *///?}

    @Override
    public void onClose() {
        RadialMenuFire.openScreen(minecraft, new RadialMenuScreen(hud));
    }
}
