package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.event.Event;

public class Tests_SimpleDemo {
    public static void main(String[] args) {
        // Инициализация фреймворка D2D2
        // Root - это корневой контейнер, получаем его при инициализации фреймворка
        // в это же время на экране появляется наше окно
        Root root = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));


        // создаем спрайт с текстурой "satellite" - это встроенная текстура для тестов, но можно
        // загружать свои из так называемых атласов
        Sprite sprite = new Sprite("satellite");

        sprite.setXY(100, 100); // задаем позицию спрайта по X и Y
        sprite.setScale(2, 2); // задаем размеры, делаем спрайт в два раза больше
        sprite.setColor(Color.RED); // задаем цветc

        // Добавляем наш спрайт на корневой контейнер проекта
        root.add(sprite);

        // ригистрируем событие "каждый кадр"
        // лямбда event -> {} будет выполняться каждый кадр отрисовки
        sprite.addEventListener(Event.EACH_FRAME, event -> {
            sprite.moveX(1); // двигаем спрайт на 1 пиксель право по X
            sprite.rotate(1); // вращаем спрайт на 1 градус
        });

        // это запуск мейн-лупа, считай запуск движка, все остальное на событиях (например ввод с клавиатуры)
        D2D2.loop();

        // Эта секция деинициализации, этот участок кода выполнится после закрытия окна
    }
}
