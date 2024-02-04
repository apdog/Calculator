import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean isExit = false;
        System.out.println("Для рассчета используйте можно использовать только целые арабские или римские " +
                "цифры от 1 до 10 включительно. \nНапример '5 + 3' или V + III. \n***************************");
        Scanner in = new Scanner(System.in);

        while (!isExit) {
            try {
                System.out.println("Введите пример для решения: ");
                String task = in.nextLine().trim();
                if (task.equals("end")) {
                    isExit = true;
                    System.out.println("Программа завершена");
                } else {
                    System.out.println(taskSolution(task));
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static String taskSolution(String task) {
        char operator = 0;
        boolean operatorFound = false;

        char[] arrayOfOperands = task.toCharArray();
        StringBuilder operand1 = new StringBuilder();
        StringBuilder operand2 = new StringBuilder();

        for (char i : arrayOfOperands) {
            if (Character.isDigit(i) || i == 'I' || i == 'V' || i == 'X') {
                if (!operatorFound) {
                    operand1.append(i);
                } else {
                    operand2.append(i);
                }
            } else if (i == '+' || i == '-' || i == '*' || i == '/') {
                operator = i;
                operatorFound = true;
            } else if (i != ' ') {
                throw new IllegalArgumentException("Некорректный символ в выражении: " + i);
            }
        }

        if (operand1.length() == 0 || operand2.length() == 0) {
            throw new IllegalArgumentException("Не введены оба операнда");
        }

        int number1 = parseAndCheckNumber(operand1.toString());
        int number2 = parseAndCheckNumber(operand2.toString());

        int result = switch (operator) {
            case '+' -> number1 + number2;
            case '-' -> number1 - number2;
            case '*' -> number1 * number2;
            case '/' -> number1 / number2;
            default -> throw new IllegalArgumentException("Неправильный оператор действия: " + operator);
        };

        if (isRoman(operand1.toString()) && isRoman(operand2.toString())) {
            if (result <= 0) {
                throw new IllegalArgumentException("Результат работы с римскими числами должен быть положительным");
            }
            return toRoman(result);
        } else {
            return String.valueOf(result);
        }
    }

    private static int parseAndCheckNumber(String numberStr) {
        try {
            int number = Integer.parseInt(numberStr);
            if (number < 1 || number > 10) {
                throw new IllegalArgumentException("Число должно быть в диапазоне от 1 до 10");
            }
            return number;
        } catch (NumberFormatException e) {
            if (!isRoman(numberStr)) {
                throw new IllegalArgumentException("Неправильный формат числа: " + numberStr);
            }

            int result = fromRoman(numberStr);
            if (result < 1 || result > 10) {
                throw new IllegalArgumentException("Число должно быть в диапазоне от I до X");
            }
            return result;
        }
    }

    private static boolean isRoman(String str) {
        return str.matches("[IVX]+");
    }

    private static String toRoman(int number) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException("Число должно быть в диапазоне от 1 до 100");
        }
        StringBuilder result = new StringBuilder();
        RomanNumeral[] values = RomanNumeral.values();
        for (int i = values.length - 1; i >= 0; i--) {
            while (number >= values[i].getValue()) {
                result.append(values[i].name());
                number -= values[i].getValue();
            }
        }
        return result.toString();
    }

    private static int fromRoman(String roman) {
        int result = 0;
        RomanNumeral[] values = RomanNumeral.values();
        for (int i = 0; i < roman.length(); i++) {
            int currentNumeralValue = RomanNumeral.valueOf(String.valueOf(roman.charAt(i))).getValue();
            if (i < roman.length() - 1) {
                int nextNumeralValue = RomanNumeral.valueOf(String.valueOf(roman.charAt(i + 1))).getValue();
                if (currentNumeralValue < nextNumeralValue) {
                    result -= currentNumeralValue;
                } else {
                    result += currentNumeralValue;
                }
            } else {
                result += currentNumeralValue;
            }
        }
        return result;
    }
}
