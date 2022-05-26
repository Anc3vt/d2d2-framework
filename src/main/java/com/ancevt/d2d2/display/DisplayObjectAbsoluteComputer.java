/*
 *     Copyright 2015-2022 Ancevt (me@ancevt.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.display;


import org.jetbrains.annotations.NotNull;

class DisplayObjectAbsoluteComputer {

	private DisplayObjectAbsoluteComputer(){}

	static float getAbsoluteX(@NotNull DisplayObject displayObject) {
		float result = displayObject.getX();

		IDisplayObjectContainer parent = displayObject.getParent();

		while (parent != null && !(parent instanceof Stage)) {
			result *= parent.getScaleX();
			result += parent.getX();
			parent = parent.getParent();
		}

		return result;
	}

	static float getAbsoluteY(final @NotNull DisplayObject displayObject) {
		float result = displayObject.getY();

		IDisplayObjectContainer parent = displayObject.getParent();

		while (parent != null && !(parent instanceof Stage)) {
			result *= parent.getScaleY();
			result += parent.getY();
			parent = parent.getParent();
		}

		return result;
	}

	static float getAbsoluteScaleX(final @NotNull DisplayObject displayObject) {
		float result = displayObject.getScaleX();

		IDisplayObjectContainer parent = displayObject.getParent();

		while (parent != null && !(parent instanceof Stage)) {
			result *= parent.getScaleX();
			parent = parent.getParent();
		}

		return result;
	}

	static float getAbsoluteScaleY(final @NotNull DisplayObject displayObject) {
		float result = displayObject.getScaleY();

		IDisplayObjectContainer parent = displayObject.getParent();

		while (parent != null && !(parent instanceof Stage)) {
			result *= parent.getScaleY();
			parent = parent.getParent();
		}

		return result;
	}

	static float getAbsoluteAlpha(final @NotNull DisplayObject displayObject) {
		float result = displayObject.getAlpha();

		IDisplayObjectContainer parent = displayObject.getParent();

		while (parent != null && !(parent instanceof Stage)) {
			result *= parent.getAlpha();
			parent = parent.getParent();
		}

		return result;
	}

	static float getAbsoluteRotation(final @NotNull DisplayObject displayObject) {
		float result = displayObject.getRotation();

		IDisplayObjectContainer parent = displayObject.getParent();

		while (parent != null && !(parent instanceof Stage)) {
			result += parent.getRotation();
			parent = parent.getParent();
		}

		return result;
	}
}
