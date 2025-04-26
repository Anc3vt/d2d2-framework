/**
 * Copyright (C) 2025 the original author or authors.
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

package com.ancevt.d2d2.scene.shader;

public interface ShaderProgram {

    void attachShader(Shader shader);

    void detachShader(Shader shader);

    int getId();

    void link();

    void use();

    void stop();

    void delete();

    public int getAttribLocation(String name);

    void setAttrib1f(String name, float value);

    void setAttrib2f(String name, float value1, float value2);

    void setAttrib3f(String name, float value1, float value2, float value3);

    void setAttrib4f(String name, float value1, float value2, float value3, float value4);

    int getUniformLocation(String name);

    void setUniform1f(String name, float value);

    void setUniform2f(String name, float value1, float value2);

    void setUniform3f(String name, float value1, float value2, float value3);

    void setUniform4f(String name, float value1, float value2, float value3, float value4);
}
