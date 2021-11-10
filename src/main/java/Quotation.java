import kong.unirest.Unirest;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Quotation {

    public static String id = "";
    public static String value = "";
    public static String name = "";


    public static void requestRates(String date, String currency) throws ParserConfigurationException, IOException, SAXException {

        String request = Unirest.get("https://www.cbr.ru/scripts/XML_daily.asp?date_req={date}"
            ).routeParam("date", date).asString().getBody();


        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        docBuilder = builderFactory.newDocumentBuilder();

        try {
            Document doc = docBuilder.parse(new InputSource(new StringReader(request)));
            String textError = doc.getDocumentElement().getTextContent();
            if (textError.equals("Error in parameters")) {
                throw new DateError();
            }

            NodeList chars = doc.getDocumentElement().getElementsByTagName("CharCode");
            id = getCurrency(chars, currency);

            if (id.equals("") | id.equals(" ")){
                throw new CurrencyIdError();
            }

            NodeList currencies = doc.getDocumentElement().getElementsByTagName("Valute");
            name = getParams(currencies).get(1);
            value = getParams(currencies).get(0);

            String output = "1" + " " +  name + " = " + value + " " + "Российский рубль";
            System.out.println(output);
        }
        catch (DateError | CurrencyIdError exception) {
            System.out.println(exception);
        }
    }


    private static String getCurrency(NodeList code, String currency){
        String id = "";
        int n = 0;
        while (n < code.getLength()) {
            Node charItem = code.item(n);
            String docCurrency = charItem.getTextContent();
            if (docCurrency.equals(currency)) {
                id = charItem.getParentNode().getAttributes().getNamedItem("ID").toString();
                break;
            }
            n += 1;
        }
        System.out.println(id);
        return id;
    }

    private static ArrayList<String> getParams(NodeList currencies){
        String valueGet = "";
        String nameGet = "";
        int element = 0;
        while (element < currencies.getLength()) {
            Node cur = currencies.item(element);
            String txtCurrency = cur.getAttributes().getNamedItem("ID").toString();
            if (txtCurrency.equals(id)) {
                NodeList listCur = cur.getChildNodes();
                int i = 0;
                while (i < listCur.getLength()) {
                    Node index = listCur.item(i);
                    if (index.getNodeName().equals("Value")) {
                        valueGet = index.getTextContent();
                    }
                    if (index.getNodeName().equals("Name")) {
                        nameGet = index.getTextContent();
                    }
                    i++;
                }
                break;
            }
            element++;
        }
        ArrayList<String> params = new ArrayList<String>();
        params.add(valueGet);
        params.add(nameGet);
        return params;
    }
}
