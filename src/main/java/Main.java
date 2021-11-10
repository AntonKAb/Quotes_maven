import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Введите дату в формате dd/MM/yyyy: ");
            System.out.println("(для выхода из программы введите 'Exit')");
            Scanner input = new Scanner(System.in);
            String inputDate = input.next();
            exit(inputDate);

            System.out.println("Введите идентификатор валюты: ");
            String inputID = input.next();
            exit(inputID);
            
            Quotation.requestRates(inputDate, inputID);
            System.out.println("\n\n");
        }
    }

    static void exit(String input) {
        if (input.equals("Exit")) {
            System.out.println("Выход из программы");
            System.exit(0);
        }
    }
}
