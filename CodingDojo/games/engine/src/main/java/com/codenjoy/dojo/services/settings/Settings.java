package com.codenjoy.dojo.services.settings;

import java.util.List;

/**
 * Все то, что ты захардкодил бы в константы в своей игре ты можешь заврапить в параметр с помощью этого чуда.
 * Для каждой константы (отличаются они по имени) будет создан враппер, который ты сможешь
 * проинитить значением по-умолчанию и в дальнейшем пользоваться в коде игры, заместь
 * непосредственного захардкодженного значения. Магия в том, что они отображатся на адмике после выбора твоей игры.
 * @see Parameter
 */
public interface Settings {

    /**
     * @return список всех констант
     */
    List<Parameter<?>> getParameters();

    /**
     * @param name имя константы
     * @return враппер над константой
     */
    Parameter<?> addEditBox(String name);

    /**
     * @deprecated Пока не реализовано
     */
    Parameter<?> addSelect(String name, List<Object> strings);

    /**
     * @deprecated Пока не реализовано
     */
    Parameter<Boolean> addCheckBox(String name);


    Parameter<?> getParameter(String name);
}
