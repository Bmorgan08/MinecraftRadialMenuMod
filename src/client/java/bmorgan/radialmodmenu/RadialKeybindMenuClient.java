package bmorgan.radialmodmenu;

import net.fabricmc.api.ClientModInitializer;

public class RadialKeybindMenuClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(new HudRenderCallbackImpl());
	}
}

int wheel = 1; // Default to Wheel 1 (1-3)
String[][] wheelBinds = new String[3][8]; // wheelBinds[0] = Wheel1, wheelBinds[1] = Wheel2, wheelBinds[2] = Wheel3
String[] wheelActivationKeys = new String[3]; // Keys that activate each wheel (e.g., "Key.Z", "Key.X", "Key.C")
int selectedSlotForEdit = -1; // -1 = none, 0-7 = slot index, 8-10 = wheel activation key (8=wheel1, 9=wheel2, 10=wheel3)

public class Renderer {
	public void render() {
		public void renderConfigMenu() {
			guiGraphics.Background.fill(0, 0, width, height, 0x88000000);
			
			// Render wheel buttons with activation keys
			int[] wheelYPositions = {0, 40, 80};
			String[] wheelLabels = {"Wheel 1", "Wheel 2", "Wheel 3"};
			
			for (int i = 0; i < 3; i++) {
				int yPos = wheelYPositions[i];
				int highlightColor = (wheel == i + 1) ? 0xFFFFFFFF : 0xFF000000;
				guiGraphics.fill(0, yPos, 80, yPos + 30, highlightColor);
				guiGraphics.drawString(font, wheelLabels[i], 10, yPos + 10, 0xFFFFFFFF);
				
				// Display activation key next to wheel button
				String keyDisplay = wheelActivationKeys[i] != null ? wheelActivationKeys[i] : "Not Set";
				int keyBoxColor = (selectedSlotForEdit == 8 + i) ? 0xFFFFAA00 : 0xFF333333;
				guiGraphics.fill(90, yPos, 170, yPos + 30, keyBoxColor);
				guiGraphics.drawString(font, keyDisplay, 100, yPos + 10, 0xFFFFFFFF);
			}
			
			// Render selected wheel's slots
			String[] currentWheelBinds = wheelBinds[wheel - 1];
			int[] slotIndices = {0, 2, 4, 6};
			int[] slotYPositions = {10, 50, 90, 130};
			
			for (int i = 0; i < slotIndices.length; i++) {
				int slotIndex = slotIndices[i];
				int yPos = slotYPositions[i];
				String displayText = currentWheelBinds[slotIndex] != null ? currentWheelBinds[slotIndex] : "Empty Slot";
				int slotBoxColor = (selectedSlotForEdit == slotIndex) ? 0xFFFFAA00 : 0xFF000000;
				renderSlot(yPos, displayText, slotBoxColor);
			}
			
			DrawPreview(wheel);
		}
		
		// TODO: Add mouse click detection to select wheel buttons and slots for editing
		// TODO: Add keyboard input capture when a slot/key is selected (ESC to cancel, any other key to bind)
		// TODO: Handle wheel button clicks to switch between wheels (1, 2, 3)
		// TODO: Handle slot clicks to enter keybind input mode
		// TODO: Add visual indicator showing "Press any key..." when waiting for input
		// TODO: Save/load configuration to file (JSON or similar)
		// TODO: Close menu with ESC key
		
		private void renderSlot(int yPos, String displayText, int boxColor) {
			guiGraphics.fill(180, yPos, 260, yPos + 30, boxColor);
			guiGraphics.drawString(font, displayText, 190, yPos + 5, 0xFFFFFFFF);
		}
			
			int centerX = 440;
			int centerY = 150;
			int radius = 100;
			
			String[] wheelBinds = getWheelBinds(wheelNumber);
			int segmentCount = getSegmentCount(wheelBinds);
			if (segmentCount == 0) segmentCount = 8;
			
			// Draw segments
			for (int i = 0; i < segmentCount; i++) {
				double angle = (360.0 / segmentCount) * i - 90;
				double nextAngle = (360.0 / segmentCount) * (i + 1) - 90;
				
				int color = wheelBinds[i] != null ? 0xFF4488FF : 0xFF222222;
				drawSegment(guiGraphics, centerX, centerY, radius, angle, nextAngle, color);
			}
			
			guiGraphics.fill(centerX - 15, centerY - 15, centerX + 15, centerY + 15, 0xFF6699FF);
		}
		
		private String[] getWheelBinds(int wheelNumber) {
			return wheelBinds[wheelNumber - 1];
		}
		
		private int getSegmentCount(String[] wheelBinds) {
			return (int) java.util.Arrays.stream(wheelBinds).filter(e -> e != null).count();
		}
		
		private void drawSegment(GuiGraphics g, int cx, int cy, int r, double a1, double a2, int color) {
			double rad1 = Math.toRadians(a1);
			double rad2 = Math.toRadians(a2);
			int x1 = cx + (int)(Math.cos(rad1) * r);
			int y1 = cy + (int)(Math.sin(rad1) * r);
			int x2 = cx + (int)(Math.cos(rad2) * r);
			int y2 = cy + (int)(Math.sin(rad2) * r);
			
			drawLine(g, cx, cy, x1, y1, color);
			drawLine(g, cx, cy, x2, y2, color);
			drawLine(g, x1, y1, x2, y2, color);
		}
		
		private void drawLine(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
			int dx = Math.abs(x2 - x1);
			int dy = Math.abs(y2 - y1);
			int sx = x1 < x2 ? 1 : -1;
			int sy = y1 < y2 ? 1 : -1;
			int err = dx - dy;
			int x = x1, y = y1;
			
			while (true) {
				g.fill(x, y, x + 1, y + 1, color);
				if (x == x2 && y == y2) break;
				int e2 = 2 * err;
				if (e2 > -dy) err -= dy;
				if (e2 < dx) err += dx;
				x += sx;
				y += sy;
				}
			}

			DrawPreview(wheel);
		}
	}
}