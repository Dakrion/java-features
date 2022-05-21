package app;

import operations.Operations;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {

    Operations operations;
    String operator;

    public void innerNum() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Введите первое число: ");
            Integer num1 = scanner.nextInt();
            System.out.println("Введите второе число: ");
            Integer num2 = scanner.nextInt();

            operations = new Operations(num1, num2);
        } catch (InputMismatchException ex) {
            System.out.println("Допустим ввод только чисел");
            innerNum();
        }
    }

    public void innerOperator() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите операцию: ");
        operator = scanner.next();
    }

    public void printResult() {
        System.out.println("Результат: " + operations.calculate(operator));
    }

    public void closeApp() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Для выхода из приложения нажмите N, для продолжения нажмите любую клавишу");
        if (scanner.next().equals("N")) {
            System.exit(0);
        }
    }
}