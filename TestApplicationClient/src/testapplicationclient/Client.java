package testapplicationclient;

import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Client {

    public static void main(String[] ar) throws
            ParserConfigurationException, SAXException,
            ParseException, InterruptedException {
        Frame startFrame = new Frame();
        startFrame.start();
    }

}
