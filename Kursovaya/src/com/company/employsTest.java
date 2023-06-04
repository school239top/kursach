package com.company;

import org.junit.Test;

/**
 * Класс тестов JUnit
 * Здесь будем тестировать методы класса employs
 */
public class employsTest {
    /**
     * Метод для тестирования метода print
     */
    @Test
    public void print() {
        employs.print(null,null,null,null);
        //employs.print("dataEmploy.xml", "window/dataEmploy", "Cherry.jrxml", "otchet.html");
    }

}