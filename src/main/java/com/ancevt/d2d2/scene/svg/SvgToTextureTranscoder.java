/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.scene.svg;

/*import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.scene.texture.Texture;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;*/

//TODO: uncomment, move to another artifact
public class SvgToTextureTranscoder {

    /*
    public static Texture transcodeSvgToTexture(InputStream svgInputStream, Map<Object, Object> svgTranscodingHints) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PNGTranscoder transcoder = new PNGTranscoder();

        svgTranscodingHints.forEach((key, value) -> transcoder.addTranscodingHint((TranscodingHints.Key) key, value));

        TranscoderInput input = new TranscoderInput(svgInputStream);
        TranscoderOutput output = new TranscoderOutput(byteArrayOutputStream);
        try {
            transcoder.transcode(input, output);
        } catch (TranscoderException e) {
            throw new IllegalArgumentException(e);
        }
        return D2D2.textureManager().loadTexture(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    public static Texture transcodeSvgToTexture(InputStream svgInputStream, float desiredWidth, float desiredHeight) {
        return transcodeSvgToTexture(svgInputStream, Map.of(
                PNGTranscoder.KEY_WIDTH, (int) desiredWidth,
                PNGTranscoder.KEY_HEIGHT, (int) desiredHeight
        ));
    }

    public static Texture transcodeSvgToTexture(InputStream svgInputStream) {
        return transcodeSvgToTexture(svgInputStream, Map.of());
    }

    public static Texture transcodeSvgToTexture(String assetPath, Map<Object, Object> svgTranscodingHints) {
        return transcodeSvgToTexture(Assets.getAsset(assetPath), svgTranscodingHints);
    }

    public static Texture transcodeSvgToTexture(String assetPath, float desiredWidth, float desiredHeight) {
        return transcodeSvgToTexture(Assets.getAsset(assetPath), desiredWidth, desiredHeight);
    }

    public static Texture transcodeSvgToTexture(String assetPath) {
        return transcodeSvgToTexture(Assets.getAsset(assetPath));
    }
     */
}

/*

        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-transcoder</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-codec</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-svggen</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-dom</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-parser</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-anim</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-awt-util</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-bridge</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-gvt</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-script</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-util</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-extension</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-xml</artifactId>
            <version>1.16</version>
        </dependency>
        <dependency>
            <groupId>org.apache.xmlgraphics</groupId>
            <artifactId>batik-swing</artifactId>
            <version>1.16</version>
        </dependency>
 */