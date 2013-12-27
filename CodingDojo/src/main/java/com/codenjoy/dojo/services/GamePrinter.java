package com.codenjoy.dojo.services;

/**
 * Принтер доски может прежставить любой ее элемент как {@see com.codenjoy.dojo.sample.model.Elements}
 */
public interface GamePrinter {

    /**
     * Вызывается перед прогоном по всему полю. Сделано для оптимизации и уменьшеня рассчетов в методе
     * @see GamePrinter#get(Point)
     */
    void init();

    /**
     * В процессе пробегания по всей доске для каждой ее клеточки дергаяется этот метод
     * @param pt Координата
     * @return Тип элемента
     */
    Enum get(Point pt);

}
