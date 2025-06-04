package com.ancevt.d2d2.scene.shader;

import com.ancevt.d2d2.D2D2;

public interface ShaderProgram {

    int getId();

    int getUniformLocation(String name);

    void setUniform(String name, float value);

    void setUniform(String name, int value);

    void setUniform(String name, float x, float y);

    void setUniform(String name, float x, float y, float z);

    void setUniform(String name, float x, float y, float z, float w);

    void destroy();

    static ShaderProgram createShaderProgram(String vertexShaderSource, String fragmentShaderSource){
        return D2D2.engine().createShaderProgram(vertexShaderSource, fragmentShaderSource);
    }
}
