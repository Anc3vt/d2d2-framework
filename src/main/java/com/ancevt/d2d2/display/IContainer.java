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
package com.ancevt.d2d2.display;


import java.util.Collection;

public interface IContainer extends IDisplayObject {

	void add( IDisplayObject child);

	void add( IDisplayObject child, int index);
	
	void add( IDisplayObject child, float x, float y);
	
	void add( IDisplayObject child, int index, float x, float y);

	default void add(Collection<IDisplayObject> children) {
		children.forEach(this::add);
	}

	default void remove(Collection<IDisplayObject> children){
		children.forEach(this::remove);
	}

	void remove( IDisplayObject child);
	
	int indexOf( IDisplayObject child);

	int getNumberOfChildren();

	 IDisplayObject getChild(int index);

	 IDisplayObject getChild(String name);

	boolean contains( IDisplayObject child);

	void removeAllChildren();
}
