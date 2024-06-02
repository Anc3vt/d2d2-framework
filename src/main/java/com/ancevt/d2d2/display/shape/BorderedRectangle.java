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
package com.ancevt.d2d2.display.shape;

import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.SimpleContainer;

public class BorderedRectangle extends SimpleContainer {
	private static final Color DEFAULT_FILL_COLOR = Color.WHITE;
	private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

	private static final float DEFAULT_WIDTH = 16;
	private static final float DEFAULT_HEIGHT = 16;

	private final RectangleShape borderLeft;
	private final RectangleShape borderRight;
	private final RectangleShape borderTop;
	private final RectangleShape borderBottom;
	private final RectangleShape fillRect;
	
	private float borderWidth = 1;
	
	public BorderedRectangle() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FILL_COLOR, DEFAULT_BORDER_COLOR);
	}

	public BorderedRectangle(float width, float height) {
		this(width, height, DEFAULT_FILL_COLOR, DEFAULT_BORDER_COLOR);
	}
	
	public BorderedRectangle(Color fillColor) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fillColor, DEFAULT_BORDER_COLOR);
	}

	public BorderedRectangle(Color fillColor, Color borderColor) {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fillColor, borderColor);
	}
	
	public BorderedRectangle(float width, float height, Color fillColor) {
		this(width, height, fillColor, DEFAULT_BORDER_COLOR);
	}

	public BorderedRectangle(float width, float height, Color fillColor, Color borderColor) {
		borderLeft   = new RectangleShape();
		borderRight  = new RectangleShape();
		borderTop    = new RectangleShape();
		borderBottom = new RectangleShape();
		fillRect     = new RectangleShape();

		borderLeft.setSize(1, 1);
		borderRight.setSize(1, 1);
		borderTop.setSize(1, 1);
		borderBottom.setSize(1, 1);
		fillRect.setSize(1, 1);
		
		addChild(fillRect);
		addChild(borderLeft);
		addChild(borderRight);
		addChild(borderTop);
		addChild(borderBottom);
		

		setBorderColor(borderColor);
		setFillColor(fillColor);
		
		setSize(width, height);
	}
	
	public void setWidth(float width) {
		fillRect.setWidth(width);
		borderTop.setWidth(width);
		borderBottom.setWidth(width);
		borderRight.setX(width - borderWidth);
	}
	
	public void setHeight(float height) {
		fillRect.setHeight(height);
		borderLeft.setHeight(height);
		borderRight.setHeight(height);
		borderBottom.setY(height - borderWidth);
	}
	
	public void setSize(float width, float height) {
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public float getWidth() {
		return fillRect.getWidth();
	}
	
	@Override
	public float getHeight() {
		return fillRect.getHeight();
	}
	
	public void setFillColor(Color color) {
		if(color == null) {
			if(fillRect.getParent() != null)
				fillRect.removeFromParent();
		} else {
			if(fillRect.getParent() == null)
				addChild(fillRect, 0);
		}
		
		fillRect.setColor(color);
	}
	
	public Color getFillColor() {
		return fillRect.getColor();
	}
	
	public void setBorderColor(Color color) {
		if(color == null) {
			if(borderLeft.getParent() != null) {
				borderLeft.removeFromParent();
				borderRight.removeFromParent();
				borderTop.removeFromParent();
				borderBottom.removeFromParent();
			}
		} else {
			if(borderLeft.getParent() == null) {
				addChild(borderLeft);
				addChild(borderRight);
				addChild(borderBottom);
				addChild(borderTop);
			}
		}
		
		borderLeft.setColor(color);
		borderRight.setColor(color);
		borderTop.setColor(color);
		borderBottom.setColor(color);
	}
	
	public Color getBorderColor() {
		return borderLeft.getColor();
	}
	
	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		borderLeft.setWidth(borderWidth);
		borderRight.setWidth(borderWidth);
		borderTop.setHeight(borderWidth);
		borderBottom.setHeight(borderWidth);
		setSize(getWidth(), getHeight());
	}
	
	public float getBorderWidth() {
		return borderWidth;
	}
}














