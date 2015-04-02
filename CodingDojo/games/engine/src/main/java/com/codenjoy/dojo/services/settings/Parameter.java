package com.codenjoy.dojo.services.settings;

/**
 * Изменяемый параметр. Фишка его в том, чтобы на админке можно было в рантайме менять параметры игры,
 * которые иначе тебе пришлось бы тебе захардкодить в твоей игре.
 * @see Settings
 */
public interface Parameter<T> {

    /**
     * @return Значение параметра
     */
    T getValue();

    String getName();

    void update(T value);

    /**
     * Так ты указываешь значение по умолчанию. Обычно этого достаточно для ввода значения.
     * @param value значение
     * @return возвращается this
     */
    Parameter<T> def(T value);

    boolean itsMe(String name);

    <V> Parameter<V> type(Class<V> integerClass);

    void select(int index);
}
