package testapplicationclient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HandlerXML {

    private int counter = 1;

    //Получаем XML с сервера
    public void sendFile(Socket clientSocket, String nameFile) 
            throws IOException, InterruptedException {
        byte[] mybytearray = new byte[4096];
        InputStream is = clientSocket.getInputStream();
        FileOutputStream fos = new FileOutputStream(nameFile);

        try (BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            int bytesRead = is.read(mybytearray, 0, mybytearray.length);
            bos.write(mybytearray, 0, bytesRead);
        } finally {
            fos.close();
        }
    }

    public void deleteFile(String nameFile) {
        File myFile = new File(nameFile);
        myFile.delete();

    }
    
    public int getCounter() {
        return counter;
    }

    //Чтение XMl файла и добавление тасков во временный контейнер 
    public void readXML(ArrayList<TaskConstructor> taskList, String nameFile)
            throws ParserConfigurationException, IOException, SAXException {
        File tasks = new File(nameFile);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(tasks);
            doc.getDocumentElement().normalize();

            NodeList nodeLst = doc.getElementsByTagName("task");

            int containId;
            String containName;
            String containInfo;
            String containDate;
            String containTime;
            String containContact;

            for (int i = 0; i < nodeLst.getLength(); i++) {
                Node root = nodeLst.item(i);

                if (root.getNodeType() == Node.ELEMENT_NODE) {
                    Element task = (Element) root;

                    NodeList taskId = task.getElementsByTagName("id");
                    Element id = (Element) taskId.item(0);
                    NodeList nodeId = id.getChildNodes();

                    NodeList taskName = task.getElementsByTagName("name");
                    Element name = (Element) taskName.item(0);
                    NodeList nodeName = name.getChildNodes();

                    NodeList taskInfo = task.getElementsByTagName("info");
                    Element info = (Element) taskInfo.item(0);
                    NodeList nodeInfo = info.getChildNodes();

                    NodeList taskDate = task.getElementsByTagName("date");
                    Element date = (Element) taskDate.item(0);
                    NodeList nodeDate = date.getChildNodes();

                    NodeList taskTime = task.getElementsByTagName("time");
                    Element time = (Element) taskTime.item(0);
                    NodeList nodeTime = time.getChildNodes();

                    NodeList taskContact = task.getElementsByTagName("contact");
                    Element contact = (Element) taskContact.item(0);
                    NodeList nodeContact = contact.getChildNodes();

                    containId = Integer.parseInt(((Node) nodeId.item(0)).getNodeValue());
                    containName = ((Node) nodeName.item(0)).getNodeValue();
                    containInfo = ((Node) nodeInfo.item(0)).getNodeValue();
                    containDate = ((Node) nodeDate.item(0)).getNodeValue();
                    containTime = ((Node) nodeTime.item(0)).getNodeValue();
                    containContact = ((Node) nodeContact.item(0)).getNodeValue();

                    taskList.add(new TaskConstructor(
                            containId,
                            containName,
                            containInfo,
                            containDate,
                            containTime,
                            containContact
                    ));
                }
                counter++;
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | NumberFormatException e) {
        }

    }

}
