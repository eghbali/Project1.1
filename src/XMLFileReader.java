import MyException.RunException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class XMLFileReader {
    public XMLFileReader() {
        try {
            //parse xml file
            File fXmlFile = new File("input.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("deposit");
            ArrayList<Deposit> deposits = new ArrayList<Deposit>();
            String errorMessage;
            //sakhte object az hesab ha va mohasebe sood , zakhire dar file
            for (int i = 0; i < list.getLength(); i++) {

                Node n = list.item(i);
                //namayesh khata dar surati k noe hesab motabar nabashad
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) n;
                    String depositType = element.getElementsByTagName("depositType").item(0).getTextContent();
                    BigDecimal depositBalance = new BigDecimal(element.getElementsByTagName("depositBalance").item(0).getTextContent());
                    String customerNumber = element.getElementsByTagName("customerNumber").item(0).getTextContent();
                    long durationInDay = Long.parseLong(element.getElementsByTagName("durationInDay").item(0).getTextContent());

                    if (!depositType.equals("ShortTerm") && !depositType.equals("Qarz") && !depositType.equals("LongTerm")) {
                        errorMessage = "Deposit Type Exception";
                        throw new RunException(errorMessage);

                    }
                    //namayeshe khata dar surati ke tedad ruz ha manfi ya sefr bashad

                    if (durationInDay <= 0) {
                        errorMessage = "Duration Exception";
                        throw new RunException(errorMessage);
                    }
                    //namayesh khata dar surati ke mande mojudi manfi bashad

                    if (depositBalance.compareTo(BigDecimal.ZERO) == -1) {
                        errorMessage = "Deposit Balance Exception";
                        throw new RunException(errorMessage);
                    } else {

                        //sakht object ba tavajo be noe hesab (reflection)
                        DepositType dpTypeClass = (DepositType) Class.forName(depositType).newInstance();
                        Deposit deposit = new Deposit(depositBalance, customerNumber, durationInDay, dpTypeClass);
                        deposit.calculatePayedInterest();
                        deposits.add(deposit);
                        sortAndWriteInFile(deposits);
                    }

                }

            }
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    /**
     * @param deposits majmue hesab ha ke dar ayyaylist zakhire shode
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public void sortAndWriteInFile(ArrayList<Deposit> deposits) throws FileNotFoundException, UnsupportedEncodingException {
        // sort kardane hesab ha bar asase meghdare sud.
        Collections.sort(deposits);
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        //neveshtane etelaat hesab ha dar file.
        for (Deposit dp : deposits) {
            writer.println(dp.customerNumber + "#" + dp.payedInterest);
        }
        writer.close();
    }

}



