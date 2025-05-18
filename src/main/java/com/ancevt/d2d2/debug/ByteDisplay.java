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


package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.MouseButton;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Group;
import com.ancevt.d2d2.scene.interactive.InteractiveGroup;
import com.ancevt.d2d2.scene.shape.BorderedRectangle;
import com.ancevt.d2d2.scene.text.BitmapText;
import com.ancevt.d2d2.util.InputStreamFork;
import lombok.Getter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ByteDisplay extends InteractiveGroup {

    private static final float DEFAULT_WIDTH = 580.0f;
    private static final float DEFAULT_HEIGHT = 400.0f;
    private static final float PADDING = 5.0f;


    public static final Color DEFAULT_BG_COLOR = Color.DARK_BLUE;
    private static final Color DEFAULT_BORDER_COLOR = Color.YELLOW;
    private static final int PAGE_SIZE = 64;

    private final BorderedRectangle bgRect;
    @Getter
    private final BitmapText bitmapText;

    private int oldX;
    private int oldY;
    private int mouseButton;
    private byte[] bytes;

    private int position;
    private boolean isHex = true;
    private boolean isUnsigned = true;
    private String gotoIndexString = "";
    private String utfString;
    private int inline, line;

    public ByteDisplay() {
        super();
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        bgRect = new BorderedRectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_BG_COLOR, DEFAULT_BORDER_COLOR);
        addChild(bgRect);

        bitmapText = new BitmapText();
        bitmapText.setText("#<FFFF00>ready");
        bitmapText.setMulticolor(true);
        bitmapText.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        addChild(bitmapText, PADDING, 2);

        addEventListener(this, InputEvent.MouseDown.class, this::mouseDown);
        addEventListener(this, InputEvent.MouseDrag.class, this::mouseDrag);
        addEventListener(this, InputEvent.KeyDown.class, this::keyDown);
        addEventListener(this, InputEvent.KeyRepeat.class, this::keyDown);
        addEventListener(this, InputEvent.MouseWheel.class, this::wheel);

    }

    private void wheel(InputEvent.MouseWheel e) {
        scroll(-e.getDelta() * inline * (e.getDelta() > 0 ? 10 : 1));
    }

    private void keyDown(InputEvent e) {
        int keyCode = 0;

        if (e instanceof InputEvent.KeyDown kde) {
            keyCode = kde.getKeyCode();
        } else if (e instanceof InputEvent.KeyRepeat kdr) {
            keyCode = kdr.getKeyCode();
        } else {
            return;
        }

        switch (keyCode) {
            case KeyCode.A, KeyCode.LEFT -> {
                scroll(-1);
            }
            case KeyCode.D, KeyCode.RIGHT -> {
                scroll(1);
            }
            case KeyCode.S, KeyCode.DOWN -> {
                scroll(inline);
            }
            case KeyCode.W, KeyCode.UP -> {
                scroll(-inline);
            }
            case KeyCode.F, KeyCode.PAGE_DOWN -> {
                scroll(PAGE_SIZE);
            }
            case KeyCode.R, KeyCode.PAGE_UP -> {
                scroll(-PAGE_SIZE);
            }
            case KeyCode.END -> {
                setPosition(bytes.length);
            }
            case KeyCode.HOME -> {
                setPosition(0);
            }
            case KeyCode.H -> {
                setHex(!isHex());
            }
            case KeyCode.U -> {
                setUnsigned(!isUnsigned());
            }
            case KeyCode.BACKSPACE -> {
                if (!gotoIndexString.isEmpty()) {
                    gotoIndexString = gotoIndexString.substring(0, gotoIndexString.length() - 1);
                    render();
                }
            }
            case KeyCode.ENTER -> {
                if (!gotoIndexString.isEmpty()) {
                    int targetPosition = Integer.parseInt(gotoIndexString);
                    setPosition(targetPosition);
                    gotoIndexString = "";
                    render();
                }
            }
        }

        if ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 320 && keyCode <= 329)) {
            int num = keyCode < 100 ? keyCode - 48 : keyCode - 320;
            gotoIndexString += "" + num;
            render();
        }

    }

    private void scroll(int i) {
        setPosition(getPosition() + i);
    }

    public void setPosition(int position) {
        if (bytes == null) return;

        this.position = position;

        if (this.position < 0) this.position = 0;

        if (this.position > bytes.length - 5) {
            this.position = bytes.length - 5;
        }

        render();
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void postFrame() {
        super.postFrame();
        focus();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        bgRect.setWidth(width);
        bitmapText.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        bgRect.setHeight(height);
        bitmapText.setHeight(height);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        bgRect.setSize(width, height);
        bitmapText.setSize(width, height);
    }

    private void mouseDown(InputEvent.MouseDown e) {
        mouseButton = e.getButton();

        oldX = (int) (e.getX() + getX());
        oldY = (int) (e.getY() + getY());

        Group parent = getParent();
        parent.removeChild(this);
        parent.addChild(this);

        focus();
    }

    private void mouseDrag(InputEvent.MouseDrag e) {
        if (mouseButton == MouseButton.RIGHT) {
            setSize(e.getX() / getScaleX() + 1, e.getY() / getScaleY() + 1);
            if (getWidth() < 320f) setWidth(320f);
            if (getHeight() < 100f) setHeight(100f);
            render();
            return;
        }

        final int tx = (int) (e.getX() + getX());
        final int ty = (int) (e.getY() + getY());

        move(tx - oldX, ty - oldY);

        oldX = tx;
        oldY = ty;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        while (this.bytes.length <= position) {
            position -= 15;
        }
        setPosition(position);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setHex(boolean hex) {
        isHex = hex;
        if (isHex) isUnsigned = true;
        render();
    }

    public boolean isHex() {
        return isHex;
    }

    public void setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
        render();
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    private void render() {
        StringBuilder s = new StringBuilder();
        s.append("#<CCCCCC>");
        renderStatusLine(s);
        renderDecorLine(s);
        renderData(s);

        int count = getHeightInChars() - line - 6;
        if (count < 0) count = 0;

        s.append("\n".repeat(count));

        renderDecorLine(s);
        renderTextRepresentation(s);

        s.append(utfString);

        bitmapText.setText(s.toString());
    }

    private void renderTextRepresentation(StringBuilder s) {

    }

    private void renderData(StringBuilder s) {
        ByteInput byteInput = ByteInput.newInstance(bytes);
        ByteInput byteInputUtf = ByteInput.newInstance(bytes);
        byteInput.skipBytes(position);
        byteInputUtf.skipBytes(position);

        ByteOutput byteOutputUtf = ByteOutput.newInstance();

        final int heightInChars = getHeightInChars();

        int inlineCounter = 0;
        inline = 0;
        line = 0;

        int p = 0;
        for (int i = position, j = 0; i < bytes.length; i++, j++) {
            String color = inlineCounter % 4 == 0 ? "<FFFFFF>" : "<999999>";

            byteOutputUtf.writeByte(byteInputUtf.readByte());

            int b = isUnsigned ? byteInput.readUnsignedByte() : byteInput.readByte();

            if (b == 0) s.append("<000000>");
            else s.append(color);

            String string = indent(isHex ? toHex(b < 0 ? b + 256 : b) : b);
            s.append(string);

            p += string.length();
            inlineCounter++;

            if (inlineCounter > inline) inline = inlineCounter;

            if (p >= getWidthInChars() - 4 && p % 4 == 0) {
                s.append("\n");
                p = 0;
                inlineCounter = 0;
                line++;
            }
            if (i == bytes.length - 1) {
                s.append(" <FF0000>X");
            }

            if (line >= heightInChars - 6) break;
        }

        s.append("\n");

        utfString = new String(byteOutputUtf.toArray(), StandardCharsets.UTF_8);
    }

    private int getWidthInChars() {
        return (int) ((getWidth() / bitmapText.getCharWidth()) - 4);
    }

    private int getHeightInChars() {
        return (int) (getHeight() / bitmapText.getCharHeight());
    }

    private static String indent(Object o) {
        StringBuilder s = new StringBuilder();

        if (o instanceof Integer i && i < 0) {

        } else {
            s.append(" ");
        }

        s.append(o);

        int l = s.length() - 1;

        s.append(" ".repeat(3 - l));

        return s.toString();
    }

    private static String toHex(int b) {
        String result = Integer.toHexString(b);
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result.toUpperCase();
    }

    private void renderStatusLine(StringBuilder s) {
        String totalSize = bytes != null ? bytes.length + "" : "-";

        s
                .append("position: ")
                .append(position)
                .append(" of ")
                .append(totalSize)
                .append("; ");

        if (isHex) {
            s.append("HEXADECIMAL ");
        } else {
            s.append("DECIMAL ");
        }

        if (isUnsigned) {
            s.append("<00FF00>UNSIGNED ");
        } else {
            s.append("<CCCCCC>SIGNED ");
        }

        if (gotoIndexString != null) {
            s
                    .append("<CCCCCC>")
                    .append(gotoIndexString);
        }

        s.append("\n");
    }

    private void renderDecorLine(StringBuilder s) {
        float cw = bitmapText.getCharWidth();
        int charCount = (int) ((getWidth() - PADDING * 2) / cw);
        String line = "<FFFFFF>%s".formatted("-".repeat(charCount));
        s.append(line).append("\n");
    }

    public static ByteDisplay show(byte[] bytes) {
        ByteDisplay result = new ByteDisplay();
        result.setBytes(bytes);
        D2D2.stage().addChild(result, 100 + new Random().nextInt(100), 100 + new Random().nextInt(100));
        return result;
    }

    public static ByteDisplay show(InputStream inputStream) {
        InputStreamFork fork = InputStreamFork.fork(inputStream);
        try {
            byte[] bytes = fork.left().readAllBytes();
            return show(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ByteOutput {

        private final ByteArrayOutputStream byteArrayOutputStream;
        private final DataOutputStream dataOutputStream;

        private ByteOutput(int initialSize) {
            byteArrayOutputStream = new ByteArrayOutputStream(initialSize);
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        }

        private ByteOutput() {
            byteArrayOutputStream = new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        }

        private void handleIOException(IOException ex) {
            throw new IllegalStateException(ex);
        }

        public byte[] toArray() {
            return byteArrayOutputStream.toByteArray();
        }

        public boolean hasData() {
            return byteArrayOutputStream.size() > 0;
        }

        public ByteOutput writeUtf(Class<?> lengthType, String string) {
            try {
                byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);

                if(lengthType == byte.class) {
                    dataOutputStream.writeByte(stringBytes.length);
                } else if (lengthType == short.class) {
                    dataOutputStream.writeShort(stringBytes.length);
                } else if (lengthType == int.class) {
                    dataOutputStream.writeInt(stringBytes.length);
                }
                dataOutputStream.write(stringBytes);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput write(int b) {
            try {
                dataOutputStream.write(b);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput write(byte[] b) {
            try {
                dataOutputStream.write(b);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput write(byte[] b, int off, int len) {
            try {
                dataOutputStream.write(b, off, len);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeBoolean(boolean v) {
            try {
                dataOutputStream.writeBoolean(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeByte(int v) {
            try {
                dataOutputStream.writeByte(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeShort(int v) {
            try {
                dataOutputStream.writeShort(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeChar(int v) {
            try {
                dataOutputStream.writeChar(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeInt(int v) {
            try {
                dataOutputStream.writeInt(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeLong(long v) {
            try {
                dataOutputStream.writeLong(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeFloat(float v) {
            try {
                dataOutputStream.writeFloat(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeDouble(double v) {
            try {
                dataOutputStream.writeDouble(v);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeBytes(String s) {
            try {
                dataOutputStream.writeBytes(s);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeChars(String s) {
            try {
                dataOutputStream.writeChars(s);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public ByteOutput writeUtf(String s) {
            try {
                dataOutputStream.writeUTF(s);
                return this;
            } catch (IOException e) {
                handleIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public static ByteOutput newInstance(int length) {
            return new ByteOutput(length);
        }

        public static ByteOutput newInstance() {
            return new ByteOutput();
        }
    }

    public static class ByteInput implements DataInput {

        private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
        private static final int DEFAULT_BUFFER_SIZE = 8192;

        private final DataInputStream dataInputStream;
        private final ByteArrayInputStream byteArrayInputStream;

        private final byte[] bytes;

        private ByteInput(byte[] bytes) {
            this.bytes = bytes;
            dataInputStream = new DataInputStream(
                byteArrayInputStream = new ByteArrayInputStream(bytes)
            );
        }

        public byte[] getBytes() {
            return bytes;
        }

        private void handleIfIOException(IOException ex) {
            throw new IllegalStateException(ex);
        }

        public boolean hasNextData() {
            return byteArrayInputStream.available() > 0;
        }

        public String readUtf(Class<?> lengthType) {
            int len = 0;
            if (lengthType == byte.class) {
                len = readUnsignedByte();
            } else if (lengthType == short.class) {
                len = readUnsignedShort();
            } else if (lengthType == int.class) {
                len = readInt();
            }
            return readUtf(len);
        }

        public byte[] readBytes(int length) {
            try {
                return readNBytes(dataInputStream, length);
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        private byte[] readNBytes(InputStream is, int len) throws IOException {
            if (len < 0) {
                throw new IllegalArgumentException("len < 0");
            }

            List<byte[]> bufs = null;
            byte[] result = null;
            int total = 0;
            int remaining = len;
            int n;
            do {
                byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
                int nread = 0;

                // read to EOF which may read more or less than buffer size
                while ((n = is.read(buf, nread,
                    Math.min(buf.length - nread, remaining))) > 0) {
                    nread += n;
                    remaining -= n;
                }

                if (nread > 0) {
                    if (MAX_BUFFER_SIZE - total < nread) {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    total += nread;
                    if (result == null) {
                        result = buf;
                    } else {
                        if (bufs == null) {
                            bufs = new ArrayList<>();
                            bufs.add(result);
                        }
                        bufs.add(buf);
                    }
                }
                // if the last call to read returned -1 or the number of bytes
                // requested have been read then break
            } while (n >= 0 && remaining > 0);

            if (bufs == null) {
                if (result == null) {
                    return new byte[0];
                }
                return result.length == total ?
                    result : Arrays.copyOf(result, total);
            }

            result = new byte[total];
            int offset = 0;
            remaining = total;
            for (byte[] b : bufs) {
                int count = Math.min(b.length, remaining);
                System.arraycopy(b, 0, result, offset, count);
                offset += count;
                remaining -= count;
            }

            return result;
        }


        public String readUtf(int length) {
            byte[] b = new byte[length];
            readFully(b);
            return new String(b, StandardCharsets.UTF_8);
        }

        @Override
        public void readFully(byte[] b) {
            try {
                dataInputStream.readFully(b);
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void readFully(byte[] b, int off, int len) {
            try {
                dataInputStream.readFully(b, off, len);
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public int skipBytes(int n) {
            try {
                return dataInputStream.skipBytes(n);
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public boolean readBoolean() {
            try {
                return dataInputStream.readBoolean();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public byte readByte() {
            try {
                return dataInputStream.readByte();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public int readUnsignedByte() {
            try {
                return dataInputStream.readUnsignedByte();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public short readShort() {
            try {
                return dataInputStream.readShort();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public int readUnsignedShort() {
            try {
                return dataInputStream.readUnsignedShort();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public char readChar() {
            try {
                return dataInputStream.readChar();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public int readInt() {
            try {
                return dataInputStream.readInt();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public long readLong() {
            try {
                return dataInputStream.readLong();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public float readFloat() {
            try {
                return dataInputStream.readFloat();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        public double readDouble() {
            try {
                return dataInputStream.readDouble();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        @Override
        @Deprecated
        public String readLine() {
            throw new IllegalStateException("not implemented");
        }

        @Override
        public String readUTF() {
            try {
                return dataInputStream.readUTF();
            } catch (IOException e) {
                handleIfIOException(e);
                throw new IllegalStateException(e);
            }
        }

        public static ByteInput newInstance(byte[] bytes) {
            return new ByteInput(bytes);
        }
    }
}
