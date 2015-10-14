package com.javarush.test.HH;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Vitaly Moskalik.
 *
 * Бесконечная последовательность.
 * Возьмём бесконечную цифровую последовательность, образованную склеиванием последовательных положительных чисел: S = 123456789101112131415...
 * Определите первое вхождение заданной последовательности A в бесконечной последовательности S (нумерация начинается с 1).
 *
 * Пример входных данных:
 * 6789
 * 111
 *
 * Пример выходных данных:
 * 6
 * 12
 */
public class Second {

    /**
     * Метод проверяет, является ли переданный символ цифрой.
     *
     * @param c символ, который необходимо проверить
     * @return если символ является цифрой - true, иначе - false
     */
    private static boolean isDigit(Character c){
        return (c >= '0' && c <= '9');
    }

    /**
     * Метод выводит в консоль искомый индекс.
     *
     * Последовательность вводится с клавиатуры.
     * Последовательность должна состоять из цифр [0-9]. Допускаются ведущие и завершающие пробелы.
     * В случае неверного формата последовательности, выводится соответствующее сообщение.
     *
     * @param args параметры командной строки
     */
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        long cur = 1;
        long index;

        try {
            System.out.print("Введите последовательность: ");
            String s = reader.readLine().trim();
            reader.close();

            long sLength = s.length();
            if (sLength == 0) {
                System.out.println("Введены неверные данные.");
                return;
            }
            for (int i = 0; i < sLength; i++) {
                if (!isDigit(s.charAt(i))){
                    System.out.println("Введены неверные данные.");
                    return;
                }
            }
            while (sb.toString().length() < sLength){
                sb.append(cur);
                cur++;
            }
            while ((index = sb.toString().indexOf(s)) == -1){
                sb.append(cur);
                cur++;
            }
            System.out.println(index + 1);
            //System.out.println(cur - 1); // последнее добавленное число
            //System.out.println(sb.toString()); // состояние последовательности
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
