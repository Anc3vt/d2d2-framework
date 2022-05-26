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

public interface IDisplayObjectContainer extends IDisplayObject {

	void add(@NotNull IDisplayObject child);

	void add(@NotNull IDisplayObject child, int index);
	
	void add(@NotNull IDisplayObject child, float x, float y);
	
	void add(@NotNull IDisplayObject child, int index, float x, float y);
	
	void remove(@NotNull IDisplayObject child);
	
	int indexOf(@NotNull IDisplayObject child);

	int getChildCount();

	@NotNull IDisplayObject getChild(int index);
	
	boolean contains(@NotNull IDisplayObject child);

	void removeAllChildren();
}
