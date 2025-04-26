package com.ancevt.d2d2.engine;

import com.ancevt.d2d2.scene.shader.Shader;
import com.ancevt.d2d2.scene.shader.ShaderProgram;

public interface ShaderFactory {

    Shader createFragmentShader(String source);

    Shader createVertexShader(String source);

    ShaderProgram createShaderProgram();
}
