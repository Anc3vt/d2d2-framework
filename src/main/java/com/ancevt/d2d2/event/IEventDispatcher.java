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
package com.ancevt.d2d2.event;

public interface IEventDispatcher {

    void addEventListener(String type, EventListener listener);

    void addEventListener(String type, EventListener listener, boolean reset);

    void addEventListener(Object owner, String type, EventListener listener);

    void addEventListener(Object owner, String type, EventListener listener, boolean reset);

    void removeEventListener(String type, EventListener listener);

    void removeEventListener(Object owner, String type);

    void dispatchEvent(Event event);

    void removeAllEventListeners(String type);

    void removeAllEventListeners();
}
