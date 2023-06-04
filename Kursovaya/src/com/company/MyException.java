package com.company;
/**
 * Класс исключения. Запрет на запись в файл пустого списка
 */

class MyException extends Exception {
    public MyException(){
        super("Запись в файл невозможна! Список пуст");
    }
}
