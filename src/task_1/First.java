package com.javarush.test.HH;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Vitaly Moskalik.
 *
 * Полином.
 * Дано выражение, содержащее скобки, операции сложения, вычитания, умножения, возведения в константную степень и одну переменную, например: (x - 5)(2x^3 + x(x^2 - 9)).
 *
 * Представьте это выражение в развёрнутом виде, например: 3x^4 - 15x^3 - 9x^2 + 45x
 */
public class First {

    /**
     * Метод проверяет, является ли переданный символ цифрой.
     *
     * @param c символ, который необходимо проверить
     * @return если символ является цифрой - true, иначе - false
     */
    private static boolean isDigit(char c){
        return (c >= '0' && c <= '9');
    }

    /**
     * Метод проверяет, является ли переданный символ оператором.
     *
     * @param c символ, который необходимо проверить
     * @return (true или false)
     */
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '^';
    }

    /**
     * Метод проверяет, является ли переданная строка оператором.
     *
     * @param s строка, которую необходимо проверить
     * @return (true или false)
     */
    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("^");
    }

    /**
     * Метод проверяет, является ли переданный символ переменной.
     *
     * @param c символ, который необходимо проверить
     * @return если символ - буква латинского алфавита в нижнем регистре - true, иначе - false
     */
    private static boolean isVariable(char c){
        return (c >= 'a' && c <= 'z');
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Метод возвращает приоритет оператора.
     *
     * @param op оператор, приоритет которого необходимо вернуть
     * @return приоритет оператора (1, 2, или 3), если оператор не найден, возвращает (-1)
     */
    private static int priority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Метод возвращает переданное выражение, представленное в виде постфиксной нотациии.
     *
     * @param s выражение, которое необходимо представить в виде постфиксной нотациии
     * @return постфиксная нотациия в виде LinkedList
     */
    private static LinkedList<String> opn(String s) {
        try {
            LinkedList<String> result = new LinkedList<>();
            LinkedList<Character> op = new LinkedList<>(); // опрераторы в порядке поступления
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(')
                    op.add('(');
                else if (c == ')') {
                    while (op.getLast() != '(')
                        result.add(op.removeLast().toString());
                    op.removeLast();
                } else if (isOperator(c)) {
                    while (!op.isEmpty() && priority(op.getLast()) >= priority(c))
                        result.add(op.removeLast().toString());
                    op.add(c);
                } else if (Character.isDigit(c)){
                    String operand = "";
                    while (i < s.length() && Character.isDigit(s.charAt(i)))
                        operand += s.charAt(i++);
                    --i;
                    result.add(operand);
                } else { //переменная
                    result.add(String.valueOf(c));
                }
            }
            while (!op.isEmpty())
                result.add(op.removeLast().toString());

            return result;
        } catch (NoSuchElementException e){
            System.out.println(fail);
            //e.printStackTrace();
            return null;
        }
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Метод выполняет операцию сложения для двух верхних элементов стэка.
     * Эти элементы удаляются, на верх стэка кладется результат операции.
     *
     * @param st стэк, содержащий операнды
     */
    private static void procVarPlus(LinkedList<String> st) {
        String s1 = st.removeLast();
        String s2 = st.removeLast();
        if (!s1.contains("x") && !s2.contains("x")) {
            long f1 = Long.parseLong(s1);
            long f2 = Long.parseLong(s2);
            st.add(String.valueOf(f2 + f1));
        } else {
            st.add(s2 + "+" + s1);
        }
    }

    /**
     * Метод выполняет операцию вычитания для двух верхних элементов стэка (верхний вычитается из предыдущего).
     * Эти элементы удаляются, на верх стэка кладется результат операции.
     *
     * @param st стэк, содержащий операнды
     */
    private static void procVarMinus(LinkedList<String> st) {
        String s1 = st.removeLast();
        String s2 = st.removeLast();
        if (!s1.contains("x") && !s2.contains("x")) {
            long f1 = Long.parseLong(s1);
            long f2 = Long.parseLong(s2);
            st.add(String.valueOf(f2 - f1));
        } else {
            s1 = s1.replace('-', '@');
            s1 = s1.replace('+', '-');
            s1 = s1.replace('@', '+');
            if (s1.charAt(0) == '+'){
                st.add(s2 + s1);
            } else {
                st.add(s2 + "-" + s1);
            }
        }
    }

    /**
     * Метод выполняет операцию умножения для переданных выражений
     *
     * @param s1 первый множитель
     * @param s2 второй множитель
     * @return результат операции, представленный в виде строки
     */
    private static String procVarMult(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> list1 = varSplit(s1);
        ArrayList<String> list2 = varSplit(s2);

        for (int i = 0; i < list1.size() ; i++) {
            for (int j = 0; j < list2.size(); j++) {
                String t1 = list1.get(i);
                String t2 = list2.get(j);
                String temp = "";
                String m1 = "";
                String m2 = "";

                if (t1.charAt(0) == '-'){
                    m1 = "-";
                    t1 = t1.substring(1);
                }
                if (t2.charAt(0) == '-'){
                    m2 = "-";
                    t2 = t2.substring(1);
                }

                if (!t1.contains("x") && !t2.contains("x")) { // переменных нет
                    long f1 = Long.parseLong(t1);
                    long f2 = Long.parseLong(t2);
                    temp = String.valueOf(f2 * f1);
                } else {
                    if ((t1.contains("x") && !t2.contains("x")) || (!t1.contains("x") && t2.contains("x"))){ // переменная есть только в ондом множителе
                        String withVar = t1.contains("x") ? t1 : t2;
                        String woVar = !t1.contains("x") ? t1 : t2;

                        if (withVar.contains("*")){
                            int index = withVar.indexOf("*");
                            temp = Long.parseLong(withVar.substring(0, index)) * Long.parseLong(woVar) + "*" + withVar.substring(index + 1);
                        } else {
                            temp = woVar + "*" + withVar;
                        }
                    } else { // переменная есть в обоих множителях
                        long[] coeft1 = findCoef(t1);
                        long[] coeft2 = findCoef(t2);
                        temp = coeft1[0] * coeft2[0] + "*" + "x^" + (coeft1[1] + coeft2[1]);
                    }
                }
                if ((m1.equals("-") && m2.equals("-")) || (!m1.equals("-") && !m2.equals("-"))){ // произведение положительное
                    if (sb.length() == 0)
                        sb.append(temp);
                    else {
                        sb.append("+");
                        sb.append(temp);
                    }
                } else { // произведение отрицательное
                    sb.append("-");
                    sb.append(temp);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Метод выполняет операцию возведения в степень для переданных выражений (первое - показатель степени)
     *
     * @param s1 первое выражение
     * @param s2 второе выражение
     * @return результат операции, представленный в виде строки
     */
    private static String procVarPow(String s1, String s2) {
        int pow = Integer.parseInt(s1);
        if (pow == 0){
            if (s2.equals("0")){
                System.out.println("Возведение нуля в нулевую степень.");
                throw new NumberFormatException("Возведение нуля в нулевую степень.");
            } else return "1";
        }
        if (pow < 0){
            System.out.println("Возведение в отрицательную степень.");
            throw new NumberFormatException("Возведение в отрицательную степень.");
        }
        String result = s2;

        for (int i = pow; i > 1 ; i--) {
            result = procVarMult(result, s2);
        }
        return result;
    }

    /**
     * Метод находит коэффициенты выражения с переменной (множитель и показатель степени).
     *
     * @param s выражени с переменной
     * @return найденные коэффициенты в виде массива(0 - множитель, 1 - показатель степени)
     */
    private static long[] findCoef(String s){
        long[] result = new long[2];
        if (s.contains("^") && s.contains("*")){
            result[1] = Long.parseLong(s.substring(s.indexOf('^') + 1));
            result[0] = Long.parseLong(s.substring(0, s.indexOf('*')));
        } else if (s.contains("*")){
            result[1] = 1;
            result[0] = Long.parseLong(s.substring(0, s.indexOf('*')));
        } else if (s.contains("^")){
            result[1] = Long.parseLong(s.substring(s.indexOf('^') + 1));
            result[0] = s.charAt(0) == '-' ? -1 : 1;
        } else if (s.contains("x")){
            result[1] = 1;
            result[0] = s.charAt(0) == '-' ? -1 : 1;
        } else {
            result[1] = 0;
            result[0] = Long.parseLong(s);
        }
        return result;
    }

    /**
     * Метод разбивает переданное выражение на слагаемые
     *
     * @param s выражение, которое необходимо разбить на слагаемые
     * @return список ArrayList, каждый элемент которого является отдельным слагаемым переданного выражения
     */
    private static ArrayList<String> varSplit(String s){
        ArrayList<String> result = new ArrayList<>();
        if (!s.contains("-") && ! s.contains("+")){
            result.add(s);
            return result;
        } else if (!(s.charAt(0) == '-')){
            s = "+" + s;
        }
        String temp;
        while (s.contains("+") || s.contains("-")){
            String first = String.valueOf(s.charAt(0));
            s = s.substring(1);
            int m = s.indexOf("-");
            int p = s.indexOf("+");

            if (m == -1 && p == -1){
                temp = s;
            } else if (m == -1){
                temp = s.substring(0, p);
                s = s.substring(p);
            } else if (p == -1){
                temp = s.substring(0, m);
                s = s.substring(m);
            } else if (p < m){
                temp = s.substring(0, p);
                s = s.substring(p);
            } else {
                temp = s.substring(0, m);
                s = s.substring(m);
            }

            if (first.equals("-"))
                result.add(first + temp);
            else result.add(temp);
        }
        return result;
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Метод выполняет вычисление переданного выражения, представленного в виде постфиксной нотации.
     *
     * @param llist выражение, представленное в виде постфиксной нотации
     * @return результат вычисления в виде "ДНФ"
     */
    private static String varEval(LinkedList<String> llist) {
        LinkedList<String> result = new LinkedList<>(); // цифры в порядке поступления
        for (int i = 0; i < llist.size(); i++) {
            String s = llist.get(i);
            if (isOperator(s)) {
                try {
                    switch (s){
                        case "+":
                            procVarPlus(result);
                            break;
                        case "-":
                            procVarMinus(result);
                            break;
                        case "*":
                            result.add(procVarMult(result.removeLast(), result.removeLast()));
                            break;
                        case "^":
                            result.add(procVarPow(result.removeLast(), result.removeLast()));
                    }
                } catch (NoSuchElementException | NullPointerException | NumberFormatException e){
                    System.out.println(fail);
                    //e.printStackTrace();
                    return null;
                }
            } else {
                result.add(s);
            }
        }
        if (result.size() == 0){
            System.out.println(fail);
            return null;
        } else return result.get(0); // строка, в которой не приведены коэффициенты
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Метод выполняет приведение коэффициентов переданного выражения, представленного в виде "ДНФ"
     *
     * @param string выражение, представленное в виде "ДНФ"
     * @return результат приведения коэффициентов в виде "ДНФ". Слогаемые следуют в порядке уменьшения степени переменной.
     */
    private static String varReduction(String string) {
        ArrayList<String> list = varSplit(string);
        Map<Long, Long> map = new TreeMap<>(Collections.reverseOrder());
        long[] coef;

        for (String s : list){
            coef = findCoef(s);
            if (!map.containsKey(coef[1]))
                map.put(coef[1], coef[0]);
            else map.put(coef[1], map.get(coef[1]) + coef[0]);
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Long> pair : map.entrySet()){
            long key = pair.getKey();
            long val = pair.getValue();
            String temp;

            if (val == 0){
                continue;
            } else if (key == 0){
                temp = "" + val;
            } else if (key == 1){
                if (val == 1) temp = "x";
                else if (val == -1) temp = "-x";
                else temp = val + "*x";
            } else if (val == 1){
                temp = "x^" + key;
            } else if (val == -1){
                temp = "-x^" + key;
            } else {
                temp = val + "*x^" + key;
            }

            if (temp.charAt(0) == '-'){
                sb.append("- ");
                sb.append(temp.substring(1));
                sb.append(" ");
            } else {
                sb.append("+ ");
                sb.append(temp);
                sb.append(" ");
            }
        }
        if (sb.length() == 0)
            return "0";
        if (sb.charAt(0) == '+')
            return sb.toString().substring(2);
        else return sb.toString();
    }
    //----------------------------------------------------------------------------------------------------
    private static char globVar = '@';

    /**
     * Метод проверяет корректность введенной строки и приводит выражение к правильному виду для дальнейших вычислений.
     *
     * Проставляются все недостающие знаки умножения. Перед унарным оператором '-' добавляется '0'.
     *
     * @param s переданное выражение
     * @return выражение, представленное в "правильном" виде.
     *         null, если:
     *         - выражение содержит запрещенные символы,
     *         - количество открывающих и закрывающих скобок разное,
     *         - выражение начинается с символа '*' или '^' или ')' или '+'.
     */
    private static String getString(String s){
        StringBuilder sb = new StringBuilder();
        if (s.charAt(0) == '-')
            s = "0" + s;
        int lbracket = 0;
        int rbracket = 0;

        for (int i = 0; i < s.length(); i++) {
            char temp = s.charAt(i);
            if (!isDigit(temp) && !isOperator(temp) && !isVariable(temp) && temp != ')' && temp != '('){
                System.out.println(fail);
                return null;
            }

            if (temp == '(')
                lbracket++;
            if (temp == ')')
                rbracket++;
            if (isVariable(temp)){
                if (globVar == '@'){
                    globVar = temp;
                    temp = 'x';
                } else if (temp != globVar){
                    System.out.println(fail);
                    return null;
                } else temp = 'x';
            }

            if (i > 0){
                char last = sb.charAt(sb.length() -1);
                if (temp == '('){
                    if (isDigit(last) || isVariable(last) || last == ')'){
                        sb.append('*');
                        sb.append(temp);
                    } else sb.append(temp);
                } else if (temp == '-'){
                    if (last == '('){
                        sb.append('0');
                        sb.append(temp);
                    } else sb.append(temp);
                } else if (isVariable(temp)){
                    if (isDigit(last) || last == ')'){
                        sb.append('*');
                        sb.append(temp);
                    } else sb.append(temp);
                } else if (isDigit(temp)){
                    if (last == ')' || isVariable(last)){
                        sb.append('*');
                        sb.append(temp);
                    } else sb.append(temp);
                } else sb.append(temp);
            } else if (temp == '*' || temp == '^' || temp == ')' || temp == '+'){
                System.out.println(fail);
                return null;
            } else sb.append(temp);
        }

        if (lbracket != rbracket){
            System.out.println(fail);
            return null;
        }
        return sb.toString();
    }
    //----------------------------------------------------------------------------------------------------
    private static final String fail = "Введены неверные данные.";

    /**
     * Метод преобразовывает выражение к нужному виду (раскрывает скобки).
     *
     * Выражение вводится с клавиатуры.
     * Выражение может содержать скобки, операции сложения, вычитания, умножения, возведения в степень и одну переменную.
     * В качестве переменной может быть использована только маленькая буква латинского алфавита. Знаки умножения могут быть опущены или присутствовать.
     * Пробелы не учитываются (т.е. их может быть сколько и где угодно). При возведении в степень показатель может быть только целым неотрицательным числом.
     * В случае неверного формата выражения, выводится соответствующее сообщение.
     *
     * @param args параметры командной строки
     */
    public static void main(String[] args)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s = null;
        try {
            System.out.print("Введите выражение: ");
            s = reader.readLine().replace(" ", "");
            reader.close();
            if (s.length() == 0)
                return;
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            s = varReduction(varEval(opn(getString(s))));
            s = s.replace("*", "");
            s = s.replace("x", String.valueOf(globVar));
            System.out.println(s);
        } catch (NullPointerException e){
            //e.printStackTrace();
        }
    }
}
