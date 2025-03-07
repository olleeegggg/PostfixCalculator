import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static final Map<String, BigInteger> VARIABLES = new HashMap<>();

    static {
        VARIABLES.put("x1", BigInteger.ZERO);
        VARIABLES.put("x2", BigInteger.ZERO);
        VARIABLES.put("x3", BigInteger.ZERO);
        VARIABLES.put("x4", BigInteger.ZERO);
        VARIABLES.put("x5", BigInteger.ZERO);
    }

    public static void main(String[] args) {
        printIntroduction();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите своё выражение ниже:");
            String expression = scanner.nextLine();

            if ("quit".equals(expression)) {
                break;
            }

            if (expression.contains("=")) {
                processAssignment(expression);
            } else {
                BigInteger result = evaluatePostfix(expression.split("\\s+"));
                if (result != null) {
                    System.out.println("Результат: " + result);
                }
            }
        }

        scanner.close();
    }

    private static void processAssignment(String expression) {
        String[] parts = expression.split("=", 2);
        if (parts.length != 2) {
            System.out.println("Ошибка: некорректное присваивание");
            return;
        }

        String variable = parts[0].trim();
        String valueExpression = parts[1].trim();

        if (!VARIABLES.containsKey(variable)) {
            System.out.println("Ошибка: неизвестная переменная " + variable);
            return;
        }

        BigInteger value = evaluatePostfix(valueExpression.split("\\s+"));
        if (value != null) {
            VARIABLES.put(variable, value);
            System.out.println(variable + " = " + value);
        }
    }

    public static BigInteger evaluatePostfix(String[] splitExpression) {
        Stack<BigInteger> stack = new Stack<>();

        for (String element : splitExpression) {
            if (element.matches("\\d+")) {
                stack.push(new BigInteger(element));
            } else if (VARIABLES.containsKey(element)) {
                stack.push(VARIABLES.get(element));
            } else if (element.matches("[+\\-*/]")) {
                if (stack.size() < 2) {
                    System.out.println("Ошибка: не хватает операндов");
                    return null;
                }

                BigInteger b = stack.pop();
                BigInteger a = stack.pop();

                if ("/".equals(element) && BigInteger.ZERO.equals(b)) {
                    System.out.println("Ошибка: деление на ноль");
                    return null;
                }

                BigInteger result;
                switch (element) {
                    case "+":
                        result = a.add(b);
                        break;
                    case "-":
                        result = a.subtract(b);
                        break;
                    case "*":
                        result = a.multiply(b);
                        break;
                    case "/":
                        result = a.divide(b);
                        break;
                    default:
                        System.out.println("Ошибка: неизвестный оператор " + element);
                        return null;
                }

                stack.push(result);
            } else {
                System.out.println("Ошибка: недопустимый символ " + element);
                return null;
            }
        }

        if (stack.size() == 1) {
            return stack.pop();
        } else {
            System.out.println("Ошибка: слишком много операндов");
            return null;
        }
    }

    public static void printIntroduction() {
        System.out.println("\nДобро пожаловать в постфиксный калькулятор. Автор: Аравин Олег ББИ2410.\n");
        System.out.println("Программа поддерживает вычисления выражений в постфиксной записи над неотрицательными целыми числами.");
        System.out.println("Доступные операции: сложение(+), вычитание(-), умножение(*), целочисленное деление(/).");
        System.out.println("Можно использовать переменные: x1, x2, x3, x4, x5.");
        System.out.println("Чтобы выйти из программы, введите \"quit\".");
    }
}
