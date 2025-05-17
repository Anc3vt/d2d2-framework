package com.ancevt.d2d2.util;


import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Args {

    private final String source;

    private final String[] elements;

    private int index;

    private Throwable problem;

    private String lastContainsCheckedKey;

    public Args(String source) {
        this.source = source;
        elements = ArgsSplitter.split(source, '\0');
    }

    public Args(String source, String delimiterChar) {
        this.source = source;
        elements = ArgsSplitter.split(source, delimiterChar);
    }

    public Args(String source, char delimiterChar) {
        this.source = source;
        elements = ArgsSplitter.split(source, delimiterChar);
    }

    public Args(String[] args) {
        this.source = collectSource(args);
        elements = args;
    }

    @Override
    public String toString() {
        return source;
    }

    private String collectSource(String[] args) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String a : args) {
            a = a.replace("\"", "\\\\\"");
            stringBuilder.append('"').append(a).append('"').append(' ');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public String[] getElements() {
        return elements;
    }

    public boolean contains(String... keys) {
        for (final String e : elements) {
            for (final String k : keys) {
                if (e.equals(k)) {
                    lastContainsCheckedKey = k;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasNext() {
        return index < elements.length;
    }

    public void skip() {
        next();
    }

    public void skip(int count) {
        for (int i = 0; i < count; i++) {
            next();
        }
    }

    public String next() {
        return next(String.class);
    }

    public <T> T next(Class<T> type) {
        if (index >= elements.length) {
            throw new ArgsException(format("next: Index out of bounds, index: %d, elements: %d", index, elements.length));
        }

        T result = get(type, index);

        if (result == null) {
            throw new ArgsException(String.format("Args exception no such element at index %d, type: %s", index, type));
        }

        index++;
        return result;
    }

    public <T> T next(Class<T> type, T defaultValue) {
        if (index >= elements.length) {
            throw new ArgsException(format("next: Index out of bounds, index: %d, elements: %d", index, elements.length));
        }

        T result = get(type, index, defaultValue);
        index++;
        return result;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if (index >= elements.length) {
            throw new ArgsException(format("Index out of bounds, index: %d, elements: %d", index, elements.length));
        }

        this.index = index;
    }

    public void resetIndex() {
        index = 0;
    }

    public int size() {
        return elements.length;
    }

    public <T> T get(Class<T> type) {
        return get(type, lastContainsCheckedKey);
    }

    public <T> T get(Class<T> type, int index, T defaultValue) {
        if (index < 0 || index >= elements.length) return defaultValue;

        try {
            return convertToType(elements[index], type);
        } catch (Exception e) {
            problem = e;
            return defaultValue;
        }
    }

    public <T> T get(Class<T> type, int index) {
        return get(type, index, null);
    }

    public <T> T get(Class<T> type, String key, T defaultValue) {
        for (int i = 0; i < elements.length - 1; i++) {
            final String currentArgs = elements[i];

            if (currentArgs.equals(key)) {
                return convertToType(elements[i + 1], type);
            }
        }

        return defaultValue;
    }

    public <T> T get(Class<T> type, String[] keys, T defaultValue) {
        for (final String key : keys) {
            for (int i = 0; i < elements.length - 1; i++) {
                final String currentArgs = elements[i];

                if (currentArgs.equals(key)) {
                    return convertToType(elements[i + 1], type);
                }
            }
        }

        return defaultValue;
    }

    public <T> T get(Class<T> type, String key) {
        return get(type, key, null);
    }

    public <T> T get(Class<T> type, String[] keys) {
        return get(type, keys, null);
    }

    public String get(String key, String defaultValue) {
        return get(String.class, key, defaultValue);
    }

    public String get(String[] keys, String defaultValue) {
        return get(String.class, keys, defaultValue);
    }

    public String get(String key) {
        return get(String.class, key);
    }

    public String get(String[] keys) {
        return get(String.class, keys);
    }

    private <T> T convertToType(String element, Class<T> type) {

        if (type == String.class) {
            return (T) element;
        } else if (type == boolean.class || type == Boolean.class) {
            return (T) (element.equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE);
        } else if (type == int.class || type == Integer.class) {
            return (T) Integer.valueOf(element);
        } else if (type == long.class || type == Long.class) {
            return (T) Long.valueOf(element);
        } else if (type == float.class || type == Float.class) {
            return (T) Float.valueOf(element);
        } else if (type == short.class || type == Short.class) {
            return (T) Short.valueOf(element);
        } else if (type == double.class || type == Double.class) {
            return (T) Double.valueOf(element);
        } else if (type == byte.class || type == Byte.class) {
            return (T) Byte.valueOf(element);
        } else {
            throw new ArgsException("Type " + type + " not supported");
        }
    }

    public String getSource() {
        return source;
    }

    public boolean isEmpty() {
        return elements == null || elements.length == 0;
    }

    public boolean hasProblem() {
        return problem != null;
    }

    public Throwable getProblem() {
        return problem;
    }

    public static Args of(String source) {
        return new Args(source);
    }

    public static Args of(String[] args) {
        return new Args(args);
    }

    public static Args of(String source, String delimiterChar) {
        return new Args(source, delimiterChar);
    }

    public static Args of(String source, char delimiterChar) {
        return new Args(source, delimiterChar);
    }

    public static class ArgsException extends RuntimeException {

        public ArgsException() {
        }

        public ArgsException(String message) {
            super(message);
        }

        public ArgsException(String message, Throwable cause) {
            super(message, cause);
        }

        public ArgsException(Throwable cause) {
            super(cause);
        }

        public ArgsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


    static class ArgsSplitter {

        private static final String SPACE_CHARS = "\n\t\r\b ";

        static String[] split(final String source, char delimiterChar) {
            final List<String> list = new ArrayList<>();
            final StringBuilder stringBuilder = new StringBuilder();
            int str = -1;
            int i = 0;
            final int len = source.length();
            while (i < len) {
                final char c = source.charAt(i++);
                if (c == '\\') {
                    if (i == len) {
                        break;
                    }
                    final char c2 = source.charAt(i++);
                    stringBuilder.append(c2);
                    continue;
                }
                if (str == -1) {
                    if (c == '"' || c == '\'') {
                        str = c;
                        continue;
                    }

                    if (delimiterChar == '\0') {
                        if (SPACE_CHARS.indexOf(c) != -1) {
                            if (stringBuilder.length() != 0) {
                                list.add(stringBuilder.toString());
                                stringBuilder.setLength(0);
                            }
                            continue;
                        }
                    } else {
                        if (c == delimiterChar) {
                            if (stringBuilder.length() != 0) {
                                list.add(stringBuilder.toString());
                                stringBuilder.setLength(0);
                            }
                            continue;
                        }
                    }
                } else {
                    if (c == str) {
                        str = -1;
                        continue;
                    }
                }
                stringBuilder.append(c);
            }
            if (stringBuilder.length() != 0) {
                list.add(stringBuilder.toString());
            }

            return list.toArray(new String[]{});
        }

        public static String[] split(String source, String delimiterChar) {
            if (delimiterChar.length() != 1) {
                throw new ArgsException("delimiter string must contain one character");
            }
            return split(source, delimiterChar.charAt(0));
        }
    }
}

