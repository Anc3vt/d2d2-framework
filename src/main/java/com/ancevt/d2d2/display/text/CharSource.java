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
package com.ancevt.d2d2.display.text;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CharSource {

    public static final CharSource BASIC_LATIN          = new CharSource( "Basic Latin",          0x0000,  0x007f);
    public static final CharSource CYRILLIC             = new CharSource( "Cyrillic",             0x0400,  0x04FF);
    public static final CharSource CHINESE              = new CharSource( "Chinese",              0x4E00,  0x9FFF);
    public static final CharSource KOREAN               = new CharSource( "Korean",               0xAC00,  0xD7A3);
    public static final CharSource HINDI                = new CharSource( "Hindi",                0x0900,  0x097F);
    public static final CharSource HEBREW               = new CharSource( "Hebrew",               0x0590,  0x05FF);
    public static final CharSource ARABIC               = new CharSource( "Arabic",               0x0600,  0x06FF);
    public static final CharSource THAI                 = new CharSource( "Thai",                 0x0E00,  0x0E7F);
    public static final CharSource JAPANESE             = new CharSource( "Japanese",             0x4E00,  0x9FFF);
    public static final CharSource GREEK                = new CharSource( "Greek",                0x0370,  0x03FF);
    public static final CharSource CYRILLIC_SUPPLEMENT  = new CharSource( "Cyrillic Supplement",  0x0500,  0x052F);
    public static final CharSource GEORGIAN             = new CharSource( "Georgian",             0x10A0,  0x10FF);
    public static final CharSource ARMENIAN             = new CharSource( "Armenian",             0x0530,  0x058F);
    public static final CharSource BENGALI              = new CharSource( "Bengali",              0x0980,  0x09FF);
    public static final CharSource TAMIL                = new CharSource( "Tamil",                0x0B80,  0x0BFF);
    public static final CharSource TIBETAN              = new CharSource( "Tibetan",              0x0F00,  0x0FFF);
    public static final CharSource MALAYALAM            = new CharSource( "Malayalam",            0x0D00,  0x0D7F);
    public static final CharSource DEVANAGARI           = new CharSource( "Devanagari",           0x0900,  0x097F);
    public static final CharSource GURMUKHI             = new CharSource( "Gurmukhi",             0x0A00,  0x0A7F);
    public static final CharSource SINHALA              = new CharSource( "Sinhala",              0x0D80,  0x0DFF);
    public static final CharSource GUJARATI             = new CharSource( "Gujarati",             0x0A80,  0x0AFF);
    public static final CharSource LAO                  = new CharSource( "Lao",                  0x0E80,  0x0EFF);
    public static final CharSource KHMER                = new CharSource( "Khmer",                0x1780,  0x17FF);
    public static final CharSource TELUGU               = new CharSource( "Telugu",               0x0C00,  0x0C7F);
    public static final CharSource KANNADA              = new CharSource( "Kannada",              0x0C80,  0x0CFF);
    public static final CharSource ORIYA                = new CharSource( "Oriya",                0x0B00,  0x0B7F);
    public static final CharSource MYANMAR              = new CharSource( "Myanmar",              0x1000,  0x109F);
    public static final CharSource EMOTICONS            = new CharSource( "Emoticons",            0x1F600, 0x1F64F);
    public static final CharSource ARROWS               = new CharSource( "Arrows",               0x2190,  0x21FF);
    public static final CharSource MATH_SYMBOLS         = new CharSource( "Math Symbols",         0x2200,  0x22FF);
    public static final CharSource GEOMETRIC_SHAPES     = new CharSource( "Geometric Shapes",     0x25A0,  0x25FF);
    public static final CharSource BOX_DRAWING          = new CharSource( "Box Drawing",          0x2500,  0x257F);
    public static final CharSource CURRENCY_SYMBOLS     = new CharSource( "Currency Symbols",     0x20A0,  0x20CF);
    public static final CharSource DINGBATS             = new CharSource( "Dingbats",             0x2700,  0x27BF);
    public static final CharSource SUPPLEMENTAL_SYMBOLS = new CharSource( "Supplemental Symbols", 0x2B00,  0x2BFF);
    public static final CharSource ANCIENT_SYMBOLS      = new CharSource( "Ancient Symbols",      0x10190, 0x101CF);


    @Getter
    private final String name;
    @Getter
    private final int beginCharIndex;
    @Getter
    private final int endCharIndex;

    public static String generate(CharSource... charSources) {
        char[] controlCharacters = {
            '\n', // Перенос строки (Line Feed) - \n
            '\r', // Возврат каретки (Carriage Return) - \r
            '\t', // Горизонтальная табуляция (Horizontal Tab) - \t
            '\b', // Удаление (Backspace) - \b
            '\f'  // Форматирование (Form Feed) - \f
        };

        StringBuilder stringBuilder = new StringBuilder();
        for (CharSource charSource : charSources) {
            for (int i = charSource.beginCharIndex; i <= charSource.endCharIndex; i++) {
                char ch = (char) i;

                if (!contains(controlCharacters, ch)) {
                    stringBuilder.append(ch);
                }
            }
        }

        return stringBuilder.toString();
    }

    private static boolean contains(char[] charArray, char targetChar) {
        for (char ch : charArray) {
            if (ch == targetChar) {
                return true;
            }
        }
        return false;
    }
}
