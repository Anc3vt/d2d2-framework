/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.StageEvent;
import com.ancevt.d2d2.scene.BasicGroup;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Sprite;
import com.ancevt.d2d2.scene.Stage;
import com.ancevt.d2d2.scene.shader.ShaderProgram;
import com.ancevt.d2d2.time.Timer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.ancevt.d2d2.D2D2.engine;
import static com.ancevt.d2d2.D2D2.stage;

public class StarletSpace extends BasicGroup {

    private static StarletSpace starletSpace;
    public static Sprite d2d2Title;
    private float velocityX = 30f;
    @Getter
    @Setter
    private int someVal = -110;

    private final List<Starlet> starlets = new ArrayList<>();

    public static int count = 100;

    public StarletSpace(int count) {
        for (int i = 0; i < count; i++) {
            Starlet starlet = new Starlet(this);
            starlet.setX((float) (Math.random() * stage().getWidth()));
            addChild(starlet);
            starlets.add(starlet);
        }

        stage().onTick(e -> tick()); // centralized tick
    }

    private void tick() {
        for (Starlet starlet : starlets) {
            starlet.tick();
        }
    }

    private static class Starlet extends BasicGroup {
        private final Sprite sprite;
        private float t;
        private final StarletSpace starletSpace;
        private final float scale;
        private final Color color;

        private float plumeDistanceAccumulator = 0f;

        public Starlet(StarletSpace starletSpace) {
            this.starletSpace = starletSpace;
            sprite = engine.nodeFactory().createSprite("d2d2-core-demo-tileset.png", 32, 144, 8, 8);
            sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
            addChild(sprite);

            color = new Color(0x88, 0x88, (int) (0x80 + (Math.random() * 0x80)));
            sprite.setColor(color);

            scale = (float) (Math.random() * 3);
            setAlpha((float) Math.random() * 0.3f);
            setScale(scale, scale);
        }

        public void tick() {
            setAlpha(getAlpha() + t);
            t -= 0.01f;
            if (getAlpha() <= 0.0f) t += 0.1f;

            float dx = -getScaleX() * starletSpace.velocityX / 75f;
            moveX(dx);

            // ✨ Копим пройденное расстояние
            plumeDistanceAccumulator += Math.abs(dx);

            // ✨ Каждые someVal пикселей — создаём свечу
            if (plumeDistanceAccumulator >= starletSpace.someVal) {
                createPlumeAt(getX(), getY());
                plumeDistanceAccumulator = 0f;
            }

            if (getX() < -16.0f) {
                setX(stage().getWidth() + 16);
                setY((float) (stage().getHeight() * Math.random()));
            }

            if (getX() > stage().getWidth() + 16.0f) {
                setX(-16.0f);
                setY((float) (stage().getHeight() * Math.random()));
            }

            if (getY() < -16.0f) {
                setY(stage().getHeight() + 16);
            }

            if (getY() > stage().getHeight() + 16.0f) {
                setY(-16.0f);
            }
        }


        private void createPlumeAt(float x, float y) {
            Sprite plume = sprite.cloneSprite();
            plume.setColor(Color.WHITE);
            plume.setAlpha(0.1f);
            plume.setPosition(x, y);
            plume.move((-sprite.getWidth() / 2) * getScaleX(), (-sprite.getHeight() / 2) * getScaleY());
            plume.setScale(getScaleX(), getScaleY());

            getParent().addChild(plume);

            stage().addEventListener(plume, StageEvent.Tick.class, e -> {
                plume.setAlpha(plume.getAlpha() - 0.01f);
                plume.moveY(0.05f);
                plume.rotate(1f);
                plume.scaleY(0.99f);
                if (plume.getAlpha() <= 0.025f) {
                    plume.removeFromParent();
                    stage().removeEventListener(plume, StageEvent.Tick.class);
                }
            });

            Timer.setTimeout(1000, timer -> plume.dispose());
        }
    }

    public static StarletSpace haveFun(boolean logo) {
        if (starletSpace != null) starletSpace.removeFromParent();

        ShaderProgram shaderProgram = ShaderProgram.createShaderProgram(
                """
                        #version 330 core
                                                
                        layout(location = 0) in vec2 aPosition;
                        layout(location = 1) in vec2 aUV;
                        layout(location = 2) in vec4 aColor;
                                                
                        uniform mat4 uProjection;
                                                
                        out vec2 vUV;
                        out vec4 vColor;
                                                
                        void main() {
                            vUV = aUV;
                            vColor = aColor;
                            gl_Position = uProjection * vec4(aPosition, 0.0, 1.0);
                        }
                                       
                        """,
                """
                        #version 330 core
                        in vec2 vUV;
                        in vec4 vColor;
                                                
                        out vec4 fragColor;
                                                
                        uniform sampler2D uTexture;
                        uniform float uTime;
                                                
                        void main() {
                            vec2 uv = vUV;
                            float offset = sin(uv.x * 20.0 + uTime * 5.0) * 0.015;
                            uv.y = clamp(vUV.y + offset, 0.001, 0.999); // 👈 и вниз и вверх в рамках текстуры
                            fragColor = texture(uTexture, uv) * vColor;
                        }
                                                
                        """);


        Stage stage = stage();
        stage.setBackgroundColor(Color.of(0x000510));
        d2d2Title = engine().nodeFactory().createSprite("d2d2-logo.png", 0, 0, 512, 128);
        d2d2Title.setColor(Color.LIGHT_GRAY);
        d2d2Title.setShaderProgram(shaderProgram);

        starletSpace = new StarletSpace(count);
        if (logo) starletSpace.addChild(d2d2Title, (stage.getWidth() - d2d2Title.getWidth()) / 2, 45);
        stage.addChild(starletSpace);

        stage.addEventListener(CommonEvent.Resize.class, e ->
                d2d2Title.setPosition((stage.getWidth() - d2d2Title.getWidth()) / 2, 45));

        stage.addEventListener(InputEvent.MouseMove.class, e -> {
            float center = stage.getWidth() / 2f;
            starletSpace.velocityX = center - e.getX();
        });

        return starletSpace;
    }

    public static StarletSpace haveFun() {
        return haveFun(true);
    }
}
