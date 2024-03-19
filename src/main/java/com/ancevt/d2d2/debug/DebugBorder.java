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
package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.DisplayObject;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.Resizable;

public class DebugBorder extends Container implements Resizable, IColored {
	
	private final PlainRect l;
	private final PlainRect t;
	private final PlainRect r;
	private final PlainRect b;
	
	private float width;
	private float height;
	
	private static final Color COLOR_BLACK = Color.BLACK;
	private static final Color COLOR_WHITE = Color.WHITE;
	
	private byte timer;
	
	private DisplayObject assignTarget;
	
	public DebugBorder() {
		this(10, 10);
	}
	
	public DebugBorder(DisplayObject assignTo) {
		this(assignTo.getWidth(), assignTo.getHeight());
		assign(assignTo);
	}
	
	public DebugBorder(float width, float height) {
		l = new PlainRect();
		t = new PlainRect();
		r = new PlainRect();
		b = new PlainRect();
		
		add(l);
		add(t);
		add(r);
		add(b);
		
		setSize(width, height);

		addEventListener(Event.EXIT_FRAME, this::eachFrame);
	}

	private void rebuild() {
		t.setScaleX(width);
		l.setScaleY(height);
		
		b.setScaleX(width);
		r.setScaleY(height);
		
		r.setX(width);
		b.setY(height);
	}
	
	@Override
	public void setColor(Color color) {
		l.setColor(color);
		t.setColor(color);
		r.setColor(color);
		b.setColor(color);
	}

	@Override
	public void setColor(int rgb) {
		l.setColor(rgb);
		t.setColor(rgb);
		r.setColor(rgb);
		b.setColor(rgb);
	}

	@Override
	public Color getColor() {
		return l.getColor();
	}

	@Override
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		rebuild();
	}

	@Override
	public void setWidth(float value) {
		this.width = value;
		rebuild();
	}

	@Override
	public void setHeight(float value) {
		this.height = value;
		rebuild();
	}
	
	@Override
	public final float getWidth() {
		return width;
	}
	
	@Override
	public float getHeight() {
		return height;
	}

	public final void assign(final DisplayObject assignTo) {
		assignTarget = assignTo;
	}
	
	public void eachFrame(Event event) {
		
		if(assignTarget != null) {
			setSize(
				assignTarget.getWidth() * assignTarget.getAbsoluteScaleX(), 
				assignTarget.getHeight() * assignTarget.getAbsoluteScaleY()
			);
			
			setXY(assignTarget.getX(), assignTarget.getY());
			
			if(assignTarget.hasParent() && getParent() != assignTarget.getParent()) {
				if(hasParent()) getParent().remove(this);
				assignTarget.getParent().add(this);
			}
			
			timer ++;
			final Color color = timer % 20 < 10 ? COLOR_BLACK : COLOR_WHITE;
			if(timer >= 20) timer = 0;
			
			l.setColor(color);
			r.setColor(color);
			t.setColor(color);
			b.setColor(color);
		}
	}
	
	
}
