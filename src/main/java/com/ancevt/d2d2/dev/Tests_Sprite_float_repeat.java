package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugGrid;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;

public class Tests_Sprite_float_repeat {
    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        DebugGrid debugGrid = new DebugGrid();
        stage.add(debugGrid);
        debugGrid.setScale(1f, 1f);
        debugGrid.setAlpha(0.25f);

        Sprite sprite = new Sprite("test16x16");

        sprite.setScale(4f, 4f);

        DebugPanel.setEnabled(true);
        DebugPanel.show("repeatxf", "").ifPresent(debugPanel -> {

            debugPanel.addEventListener(Event.EACH_FRAME, event -> {
                debugPanel.setText("repeatX: " + sprite.getRepeatX() + "\n" +
                        "repeatY: " + sprite.getRepeatY());
            });

            debugPanel.addButton("<", () -> sprite.setRepeatX(sprite.getRepeatX() - 0.25f));
            debugPanel.addButton(">", () -> sprite.setRepeatX(sprite.getRepeatX() + 0.25f));
            debugPanel.addButton("^", () -> sprite.setRepeatY(sprite.getRepeatY() - 0.25f));
            debugPanel.addButton("v", () -> sprite.setRepeatY(sprite.getRepeatY() + 0.25f));

            debugPanel.addButton("vbf-", () -> sprite.setVertexBleedingFix(sprite.getVertexBleedingFix() - 0.25));
            debugPanel.addButton("vbf+", () -> sprite.setVertexBleedingFix(sprite.getVertexBleedingFix() + 0.25));

            debugPanel.addButton("tbf-", () -> sprite.setTextureBleedingFix(sprite.getTextureBleedingFix() - 0.25));
            debugPanel.addButton("tbf+", () -> sprite.setTextureBleedingFix(sprite.getTextureBleedingFix() + 0.25));
        });

        stage.add(sprite, 128, 256);

        stage.add(new FpsMeter());
        D2D2.loop();
        DebugPanel.saveAll();
    }
}
