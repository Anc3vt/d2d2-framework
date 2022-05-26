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

import com.ancevt.d2d2.D2D2;

public class ShaderProgram {

    private final String vertexShader;
    private final String fragmentShader;
    private final int id;
    private int vertexShaderHandle;
    private int fragmentShaderHandle;
    private String log;
    private boolean disposed;

    public ShaderProgram(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;

        id = D2D2.getBackend().prepareShaderProgram(this);

        if (id == -1) {
            throw new IllegalStateException(getLog());
        }
    }

    public int getId() {
        return id;
    }

    public String getFragmentShaderSource() {
        return fragmentShader;
    }

    public String getVertexShaderSource() {
        return vertexShader;
    }

    public void setHandles(int vertexShaderHandle, int fragmentShaderHandle) {
        this.vertexShaderHandle = vertexShaderHandle;
        this.fragmentShaderHandle = fragmentShaderHandle;
    }

    public int getFragmentShaderHandle() {
        return fragmentShaderHandle;
    }

    public int getVertexShaderHandle() {
        return vertexShaderHandle;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLog() {
        return log;
    }

    public void dispose () {
        D2D2.getBackend().disposeShaderProgram(this);
        disposed = true;
    }

    public boolean isDisposed() {
        return disposed;
    }
}
