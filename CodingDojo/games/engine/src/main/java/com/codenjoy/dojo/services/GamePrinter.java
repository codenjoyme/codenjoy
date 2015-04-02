package com.codenjoy.dojo.services;

/**
 * Принтер доски может прежставить любой ее элемент как {@see Elements}
 */
public interface GamePrinter {

    /**
     * Вызывается перед прогоном по всему полю. Сделано для оптимизации и уменьшеня рассчетов в методе
     * @see GamePrinter#printAll(Filler)
     */
    void init();

    /**
     * Этим методом мы даем клиенту возможность отрисоваться на доске
     * @param filler любой, кто хочет дать возможность писать в себя должен реализовать этот интерфейс
     */
    void printAll(Filler filler);

    interface Filler {
        void set(int x, int y, char ch);
    }
}
