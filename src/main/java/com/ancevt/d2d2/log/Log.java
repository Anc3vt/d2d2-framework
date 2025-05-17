package com.ancevt.d2d2.log;

public interface Log {

    int NONE = 0;
    int ERROR = 1;
    int INFO = 2;
    int DEBUG = 3;

    void setLevel(int level);

    int getLevel();

    void setColorized(boolean colorized);

    boolean isColorized();

    void error(Object tag, Object msg);

    void error(Object tag, Object msg, Throwable throwable);

    void info(Object tag, Object msg);

    void debug(Object tag, Object msg);


}
