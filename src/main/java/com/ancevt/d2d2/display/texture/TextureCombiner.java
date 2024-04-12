/**
 * Copyright (C) 2024 the original author or authors.
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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.text.BitmapText;

import java.util.ArrayList;
import java.util.List;

public class TextureCombiner {
	private final List<TextureCell> cells;
	private final int width;
	private final int height;
	private int cellIdCounter;
	
	public TextureCombiner(final int width, final int height) {
		this.width = width;
		this.height = height;
	
		cells = new ArrayList<>();
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public final int append(
			final Texture texture,
			final int x,
			final int y,
			final Color color,
			final float scaleX,
			final float scaleY,
			final float alpha,
			final float rotation,
			final float repeatX,
			final float repeatY) {
		
		final TextureCell cell = new TextureCell();
		cell.setX(x);
		cell.setY(y);
		cell.setColor(color);
		cell.setTexture(texture);
		cell.setScaleX(scaleX);
		cell.setScaleY(scaleY);
		cell.setAlpha(alpha);
		cell.setRotation(rotation);
		cell.setRepeatX(repeatX);
		cell.setRepeatY(repeatY);
		cell.setId(cellIdCounter++);
		cells.add(cell);
		
		return cell.getId();
	}
	
	public final int append(int x, int y, Color color, float alpha) {
		final TextureCell cell = new TextureCell();
		cell.setAlpha(alpha);
		cell.setPixel(true);
		cell.setColor(color);
		cell.setX(x);
		cell.setY(y);
		cell.setId(cellIdCounter++);
		cells.add(cell);
		
		return cell.getId();
	}
	
	public final int append(int x, int y, Color color) {
		final TextureCell cell = new TextureCell();
		cell.setPixel(true);
		cell.setColor(color);
		cell.setX(x);
		cell.setY(y);
		cell.setId(cellIdCounter++);
		cells.add(cell);
		return cell.getId();
	}
	
	public final int append(
			final Texture texture,
			final int x,
			final int y,
			final float scaleX,
			final float scaleY) {
		final TextureCell cell = new TextureCell();
		cell.setX(x);
		cell.setY(y);
		cell.setTexture(texture);
		cell.setScaleX(scaleX);
		cell.setScaleY(scaleY);
		cell.setId(cellIdCounter++);
		cells.add(cell);
		return cell.getId();
	}
	
	public final int append(
			final Texture texture,
			final int x,
			final int y,
			final int repeatX,
			final int repeatY) {
		final TextureCell cell = new TextureCell();
		cell.setX(x);
		cell.setY(y);
		cell.setTexture(texture);
		cell.setRepeatX(repeatX);
		cell.setRepeatY(repeatY);
		cell.setId(cellIdCounter++);
		cells.add(cell);
		
		return cell.getId();
	}

	public final int append(
			final Texture texture,
			final int x,
			final int y) {
		final TextureCell cell = new TextureCell();
		cell.setX(x);
		cell.setY(y);
		cell.setTexture(texture);
		cell.setId(cellIdCounter++);
		cells.add(cell);
		
		return cell.getId();
	}
	
	public final void remove(final int cellId) {
		final int count = cells.size();
		for(int i = 0; i < count; i ++) {
			final TextureCell cell = cells.get(i);
			if(cell.getId() == cellId) {
				cells.remove(cell);
				return;
			}
		}
	}
	
	public final TextureAtlas createTextureAtlas() {
		return D2D2.textureManager().getTextureEngine().
			createTextureAtlas(width, height, cells.toArray(new TextureCell[] {}));
	}
	
	public static TextureAtlas bitmapTextToTextureAtlas(final BitmapText bitmapText) {
		return D2D2.textureManager().bitmapTextToTextureAtlas(bitmapText);
	}
	
	public static Texture bitmapTextToTexture(final BitmapText bitmapText) {
		final TextureAtlas textureAtlas = bitmapTextToTextureAtlas(bitmapText);
		if(textureAtlas != null) {
			return textureAtlas.createTexture();
		}
		return null;
	}
}




























