package com.javatrainee.tanks;

public class Tanks {

    private Field field;

    //Тут мы подумали, что getField() нам понадобится. но один из нас
    //отстаивает позицию, что нужно вернуть не весь обьект field,
    // а лишь интерфейс взаимодействия с ним... чето запах наш код:) (с) Фаулер
    public Field getField() {
        return field;
    }

    public Tanks(int fieldSize) {
        field = new Field(fieldSize);
    }

    public String drawField() {
         return field.getFieldAsLine();
        //Оп. Запара. Неясно, чья ответственность выводить поле...
        //получается проброс - поле возвращает свое строковое представление,
        //а сам движок проделывает то же самое, только через метод drawField()...

    }
}
