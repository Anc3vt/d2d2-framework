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
package com.ancevt.d2d2.common;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.display.texture.TextureManager;

public class PlainRect extends Sprite {
	
	private static final String FILE_PATH = "d2d2-core-1x1.png";

	private static Texture texture;

	private static Texture get1x1Texture() {
		if(texture != null) return texture;
		
		final TextureManager textureManager = D2D2.getTextureManager();
		final TextureAtlas textureAtlas = textureManager.loadTextureAtlas(FILE_PATH);
		return texture = textureAtlas.createTexture();
	}
	
	public PlainRect() {
		super(get1x1Texture());
	}
	
	public PlainRect(float width, float height) {
		this();
		setSize(width, height);
	}
	
	public PlainRect(Color color) {
		this();
		setColor(color);
	}
	
	public PlainRect(float width, float height, Color color) {
		this();
		setSize(width, height);
		setColor(color);
	}
	
	public void setWidth(float width) {
		setScaleX(width);
	}
	
	public void setHeight(float height) {
		setScaleY(height);
	}
	
	public void setSize(float width, float height) {
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public float getWidth() {
		return getScaleX();
	}
	
	@Override
	public float getHeight() {
		return getScaleY();
	}

}
