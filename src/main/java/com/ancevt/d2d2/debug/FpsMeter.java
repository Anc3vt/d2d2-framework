/**
 * Copyright (C) 2023 the original author or authors.
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

import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;

public class FpsMeter extends BitmapText {

	private long time = System.currentTimeMillis();
	private int frameCounter;
	private int actualFramesPerSeconds;

	public FpsMeter(BitmapFont font) {
		super(font);
		setName(getClass().getSimpleName() + displayObjectId());
		addEventListener(Event.EACH_FRAME, this::eachFrame);

	}

	public FpsMeter() {
		super();
		addEventListener(Event.EACH_FRAME, this::eachFrame);
	}
	
	public final int getFramesPerSecond() {
		return actualFramesPerSeconds;
	}

	public void eachFrame(Event event) {
		frameCounter++;
		final long time2 = System.currentTimeMillis();

		if (time2 - time >= 1000) {
			time = System.currentTimeMillis();

			setText("FPS: " + frameCounter);
			actualFramesPerSeconds = frameCounter;

			if (frameCounter > 40)
				setColor(Color.GREEN);
			else if (frameCounter >= 30 && frameCounter < 40)
				setColor(Color.YELLOW);
			else if (frameCounter < 30)
				setColor(Color.RED);

			frameCounter = 0;
		}
	}

	@Override
	public String toString() {
		return "FpsMeter{" +
				"time=" + time +
				", frameCounter=" + frameCounter +
				", actualFramesPerSeconds=" + actualFramesPerSeconds +
				'}';
	}
}

