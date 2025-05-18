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

package com.ancevt.d2d2.scene.text;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import lombok.Getter;

import java.io.InputStream;
import java.nio.file.Path;

import static com.ancevt.d2d2.scene.text.CharSource.*;

public class FontBuilder {

    private static final int DEFAULT_WIDTH = 512;
    private static final int DEFAULT_HEIGHT = 512;
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final int DEFAULT_SPACING_X = 2;
    private static final int DEFAULT_SPACING_Y = 2;

    @Getter
    private int atlasWidth = DEFAULT_WIDTH;
    @Getter
    private int atlasHeight = DEFAULT_HEIGHT;
    @Getter
    private InputStream inputStream;
    @Getter
    private Path filePath;
    @Getter
    private String assetPath;
    @Getter
    private int fontSize = DEFAULT_FONT_SIZE;
    @Getter
    private boolean textAntialiasOn = true;
    @Getter
    private boolean textAntialiasGasp;
    @Getter
    private boolean textAntialiasLcdHrgb;
    @Getter
    private boolean textAntialiasLcdHbgr;
    @Getter
    private boolean textAntialiasLcdVrgb;
    @Getter
    private boolean textAntialiasLcdVbgr;
    @Getter
    private FractionalMetrics fractionalMetrics;
    @Getter
    private int spacingX = DEFAULT_SPACING_X;
    @Getter
    private int spacingY = DEFAULT_SPACING_Y;
    @Getter
    private boolean bold;
    @Getter
    private boolean italic;

    @Getter
    private int offsetX = -1;
    @Getter
    private int offsetY = -1;
    @Getter
    private String name;

    //@Getter
    //private String charSourceString = " !\"#№$%&'()*+,-./\\0123456789:;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]_{}АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя?^~`ҐґЇїЎў";
    //private String charSourceString = " !\"#$%&'()*+,-./\\0123456789:;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]_{}АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя?^~`ҐґЇїЎў¡¿ÑñÁÉÍÓÚÜáéíóúüßäÄöÖüÜçÇğĞıİşŞøæÆåÅøØæÆęĘłŁńŃśŚźŹżŻđĐďĎťŤŕŔůŮôÔâÂêÊûÛîÎôÔŷŶŵŴŝŜĉĈĥĤẅẄỳỲỵỴẉẈỹỸỷỶỻỺṃṂṗṖṙṘṣṢṭṬỡỠẃẂẁẀẘẘễỄẽẼỹỸỵỴḿḾṫṪȯȮḟḞġĠṅṄṇṆñÑóÓúÚìÌèÈàÀâÂêÊûÛîÎôÔäÄëËïÏöÖüÜçÇøØåÅæÆãÃõÕāĀēĒīĪōŌūŪǎǍěĚǐǏǒǑǔǕǖǗǘǙǚǛǜǺǻǼǽɛƐɔƆɪɨɒƝƉɖɗƐɛʒƷʃƨƱƲɣƔʔʡʕʢəƏʌʊʋʁʀŋɲɳɴɤƷƸɚɝɨɩɾɽʙʜɦɧɥʎʟʤʦʧʨɟʎɧʂʈʐɖɳɱɫɬɮɲɳɴɸɹɻɰɯɸɡɣɢɠʖɥɦɧɨɪɨɭɬɮɫɬɱɯɰɲɳɵɶɷɸɹɻɼɽɾɿʀʁʂʃʄʅʆʇʈʉʊʋʌʍʎʏʐʑʒʓʔʕʖʗʘʙʚʛʜʝʞʟʠʡʢʣʤʥʦʧʨʩʪʫʬʭʮʯʰʱʲʳʴʵʶʷʸʹʺʻʼʽʾʿˀˁ˂˃˄˅ˆˇˈˉˊˋˌˍˎˏːˑ˒˓˔˕˖˗˘˙˚˛˜˝˞˟ˠˡˢˣˤ˥˦˧˨˩˪˫ˬ˭ˮ" +
    //  "⎗⎘⎙⎚⎛⎜⎝⎞⎟⎠⎡⎢⎣⎤⎥⎦⎧⎨⎩⎪⎬⎭┌┍┎┏┐┑┒┓└┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄╅╆╇╈╉╊╋╌╍╎╏═║╒╓╔╕╖╗╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬▀▁▂▃▄▅▆▇█▉▊▋▌▍▎▏▐░▒▓▔▕▖▗▘▙▚▛▜▝▞▟▲△▴▵▶▷▸►▹▻▼▽▾▿◀◁◂◃◄◅◆◇◈◉◊○◌◍◎●◐◑◒◓◔◕◖◗◘◙◚◛◜◝◞◟◠◡◢◣◤◥◦◧◨◩◪◫◬◭◮◯◰◱◲◳◴◵◶◷◸◹◺◻◼◽◾◿";

    @Getter
    private String charSourceString = CharSource.generate(
            BASIC_LATIN
            , CYRILLIC
            , SUPPLEMENTAL_SYMBOLS
            , ANCIENT_SYMBOLS
            //, CHINESE
    );

    public FontBuilder charSourceString(String charSourceString) {
        this.charSourceString = charSourceString;
        return this;
    }

    public FontBuilder name(String name) {
        this.name = name;
        return this;
    }

    public FontBuilder bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public FontBuilder offsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public FontBuilder offsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public FontBuilder filePath(String filePath) {
        return filePath(Path.of(filePath));
    }

    public FontBuilder filePath(Path filePath) {
        this.filePath = filePath;
        return this;
    }

    public FontBuilder atlasWidth(int atlasWidth) {
        this.atlasWidth = atlasWidth;
        return this;
    }

    public FontBuilder atlasHeight(int atlasHeight) {
        this.atlasHeight = atlasHeight;
        return this;
    }

    public FontBuilder assetPath(String assetPath) {
        this.assetPath = assetPath;
        return inputStream(Assets.getAsset(assetPath).getInputStream());
    }

    public FontBuilder inputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public FontBuilder fontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public FontBuilder textAntialias(boolean textAntialiasOn) {
        this.textAntialiasOn = textAntialiasOn;
        return this;
    }

    public FontBuilder withHintTextAntialiasGasp() {
        this.textAntialiasGasp = true;
        return this;
    }

    public FontBuilder withHintTextAntialiasLcdHrgb() {
        this.textAntialiasLcdHrgb = true;
        return this;
    }

    public FontBuilder withHintTextAntialiasLcdHbgr() {
        this.textAntialiasLcdHbgr = true;
        return this;
    }

    public FontBuilder withHintTextAntialiasLcdVrgb() {
        this.textAntialiasLcdVrgb = true;
        return this;
    }

    public FontBuilder withHintTextAntialiasLcdVbgr() {
        this.textAntialiasLcdVbgr = true;
        return this;
    }

    public FontBuilder fractionalMetrics(FractionalMetrics fractionalMetrics) {
        this.fractionalMetrics = fractionalMetrics;
        return this;
    }

    public FractionalMetrics fractionalMetrics() {
        return fractionalMetrics;
    }

    public FontBuilder spacingX(int spacingX) {
        this.spacingX = spacingX;
        return this;
    }

    public FontBuilder spacingY(int spacingY) {
        this.spacingY = spacingY;
        return this;
    }

    public BitmapFont build() {
        if (filePath == null && assetPath == null && inputStream == null) {
            throw new IllegalStateException("filePath == null &&  assetPath == null && inputStream == null");
        }

        if (name == null) {
            if (assetPath != null) {
                name = assetPath;
            } else if (filePath != null) {
                name = filePath.toString();
            }
        }

        return D2D2.engine().generateBitmapFont(this);
    }

}
