package testapplicationServer;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SingleThread implements Runnable {

    private Socket clientSocket = null;
    File myFile = null;
    String nameFile = null;
    private final HandlerXML hxml = new HandlerXML();
    private ArrayList<TaskConstructor> tasks = new ArrayList<>();

    private InputStream sin = null;
    private DataInputStream in = null;

    public SingleThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту. 
            sin = clientSocket.getInputStream();
            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            in = new DataInputStream(sin);

            String namePerson = in.readUTF();
            System.out.println("Пользователь - " + namePerson + " залогинился!");
            nameFile = namePerson + ".xml";

            myFile = new File(nameFile);
            hxml.checkAvailabilityXMLFile(nameFile);
            hxml.sendFile(myFile, clientSocket);

            while (true) {
                tasks.add(new TaskConstructor(
                        in.readUTF(),
                        in.readUTF(),
                        in.readUTF(),
                        in.readUTF(),
                        in.readUTF())
                );
            }

        } catch (IOException x) {
            System.out.println("Клиент отключился! ");
        } catch (ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(SingleThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tasks = getTasks();
        if (!tasks.isEmpty()) {
            createXML(nameFile);
        } else {
            myFile.delete();
        }
    }

    public ArrayList<TaskConstructor> getTasks() {
        return tasks;
    }

    //Создание XML файла c помощью dom парсера
    public void createXML(String nameFile) {
        try {
            tasks = getTasks();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("tasks");
            doc.appendChild(rootElement);
            int id = 0;
            for (TaskConstructor tc : tasks) {

                Element task = doc.createElement("task");
                rootElement.appendChild(task);

                Element taskId = doc.createElement("id");
                taskId.appendChild(doc.createTextNode(Integer.toString(++id)));
                task.appendChild(taskId);

                Element taskName = doc.createElement("name");
                taskName.appendChild(doc.createTextNode(tc.getName()));
                task.appendChild(taskName);

                Element taskInfo = doc.createElement("info");
                taskInfo.appendChild(doc.createTextNode(tc.getInfo()));
                task.appendChild(taskInfo);

                Element taskDate = doc.createElement("date");
                taskDate.appendChild(doc.createTextNode(tc.getDate()));
                task.appendChild(taskDate);

                Element taskTime = doc.createElement("time");
                taskTime.appendChild(doc.createTextNode(tc.getTime()));
                task.appendChild(taskTime);

                Element taskContact = doc.createElement("contact");
                taskContact.appendChild(doc.createTextNode(tc.getContact()));
                task.appendChild(taskContact);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(nameFile));
                transformer.transform(source, result);

            }
        } catch (ParserConfigurationException | DOMException | TransformerException e) {
            System.out.println("Ошибка XML! Смотреть метод createXML()!!");
        }
    }

}
