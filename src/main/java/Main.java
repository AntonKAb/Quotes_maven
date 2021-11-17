import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    private final static String url = "jdbc:mysql://localhost:3306/data_currency";
    private final static String user = "root";

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, SQLException {

        try {

            Connection connection = DriverManager.getConnection(url, user, "12124576");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Quoters");

            while (true) {
                System.out.println("Введите дату в формате dd/MM/yyyy: ");
                System.out.println("(для выхода из программы введите 'Exit')");
                Scanner input = new Scanner(System.in);
                String inputDate = input.next();
                exit(inputDate);

                System.out.println("Введите идентификатор валюты: ");
                String inputID = input.next();
                exit(inputID);

                try {

                    String date = resultSet.getString(inputDate);
                    String id = resultSet.getString(inputID);
                    String value = resultSet.getString(5);
                    System.out.printf(" 1 %s = %s Российский рубль", id, value);
                    Quotation.requestRates(inputDate, inputID);
                    System.out.println("\n\n");

                } catch (Exception ex) {
                    Quotation.requestRates(inputDate, inputID);

                    ArrayList<String> req = Quotation.requestRates(inputDate, inputID);

                    String valueNew = req.get(1);

                    statement.executeUpdate("INSERT Quoters(DATE, CURRENCY, VALUE) " +
                            "VALUES ('" + inputDate + "', '" + inputID + "', '" + valueNew + "')");
                    System.out.println("\n\n");

                }
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            //System.out.println(ex);
        }
    }

        static void exit(String input) {
        if (input.equals("Exit")) {
            System.out.println("Выход из программы");
            System.exit(0);
        }
    }
}
