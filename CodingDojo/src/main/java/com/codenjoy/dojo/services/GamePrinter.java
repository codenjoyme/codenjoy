package com.codenjoy.dojo.services;

/**
 * Принтер доски может прежставить любой ее элемент как {@see com.codenjoy.dojo.sample.model.Elements}
 */
public interface GamePrinter {

    /**
     * Вызывается перед прогоном по всему полю. Сделано для оптимизации и уменьшеня рассчетов в методе
     * @see GamePrinter#get(Point)
     * @return true - если реализация иснована на методе GamePrinter#get()
     *         false - если на GamePrinter#printAll() чо предпочтительнее
     */
    boolean init();

    /**
     * В процессе пробегания по всей доске для каждой ее клеточки дергаяется этот метод
     * @deprecated TODO что очень не оптимально, а потому я хочу перейти на метод printAll
     * @param pt Координата
     * @return элемент
     */
    char get(Point pt);

    /**
     * Этим методом мы даем клиенту возможность отрисоваться на доске
     * @param filler любой, кто хочет дать возможность писать в себя должен реализовать этот интерфейс
     */
    void printAll(Filler filler);

    interface Filler {
        void set(int x, int y, char ch);
    }
}
