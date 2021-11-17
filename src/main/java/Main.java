import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    private final static String url = "jdbc:mysql://localhost:3306/data_currency";
    private final static String user = "root";
    public static  String dateF = "";
    public static  String currencyF = "";
    public static  String valueF = "";


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

                try{
                    ArrayList<String> dateList = new ArrayList<>();
                    ArrayList<String> currencyList = new ArrayList<>();
                    ArrayList<String> valueList = new ArrayList<>();
                    while(resultSet.next()){
                        dateList.add(resultSet.getString("DATE"));
                        currencyList.add(resultSet.getString("CURRENCY"));
                        valueList.add(resultSet.getString("VALUE"));
                   }

                    int find = 0;
                    int index = 0;
                    for(String date: dateList){
                        if (date.equals(inputDate)){
                            String cur = currencyList.get(index);
                            if (cur.equals(inputID)){
                                dateF = date;
                                currencyF = cur;
                                valueF = valueList.get(index);
                                find += 1;
                                String output = "1" + " " + currencyF + " = " + valueF + " " + "Российский рубль";
                                System.out.println(output);
                                break;
                            }
                            }else{
                            index++;
                        }

                        }
                    if (find == 0) {
                        throw new SQLException();

                    }

                } catch (Exception SQLException) {
                    Quotation.requestRates(inputDate, inputID);

                    ArrayList<String> req = Quotation.requestRates(inputDate, inputID);

                    String valueNew = req.get(1);

                    statement.executeUpdate("INSERT Quoters(DATE, CURRENCY, VALUE) " +
                            "VALUES ('" + inputDate + "', '" + inputID + "', '" + valueNew + "')");
                    System.out.println("\n\n");

                }
            }

        }
        catch (Exception ex) {
            System.out.println("Connection failed...");
        }
    }



    static void exit(String input) {
        if (input.equals("Exit")) {
            System.out.println("Выход из программы");
            System.exit(0);
        }
    }
    public static void updateBD(){

    }
}
