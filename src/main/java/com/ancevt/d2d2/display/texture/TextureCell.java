/**
 * Copyright (C) 2022 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.display.texture;

import com.ancevt.d2d2.display.Color;

public class TextureCell {
	private Color color;

	private boolean pixel;

	private int id;
	private int x;
	private int y;
	private int repeatX = 1;
	private int repeatY = 1;

	private float scaleX = 1.0f;
	private float scaleY = 1.0f;
	private float alpha = 1.0f;
	private float rotation = 0.0f;

	private Texture texture;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isPixel() {
		return pixel;
	}

	public void setPixel(boolean pixel) {
		this.pixel = pixel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRepeatX() {
		return repeatX;
	}

	public void setRepeatX(int repeatX) {
		this.repeatX = repeatX;
	}

	public int getRepeatY() {
		return repeatY;
	}

	public void setRepeatY(int repeatY) {
		this.repeatY = repeatY;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	public String toString() {
		return "TextureCell{" +
				"color=" + color +
				", pixel=" + pixel +
				", id=" + id +
				", x=" + x +
				", y=" + y +
				", repeatX=" + repeatX +
				", repeatY=" + repeatY +
				", scaleX=" + scaleX +
				", scaleY=" + scaleY +
				", alpha=" + alpha +
				", rotation=" + rotation +
				", texture=" + texture +
				'}';
	}
}
