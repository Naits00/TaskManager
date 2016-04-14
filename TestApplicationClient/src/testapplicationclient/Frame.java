package testapplicationclient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Frame extends Thread implements Runnable {

    private final int SERVERPORT = 6666;
    private final String ADDRESS = "127.0.0.1";
    private Socket clientSocket = null;

    private final HandlerXML handlerXML = new HandlerXML();
    private final WarningWindow ww = new WarningWindow();

    private OutputStream sout = null;
    private DataOutputStream out = null;

    private final DateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy"); //формат даты
    private final DateFormat formatTime = new SimpleDateFormat("HH:mm");      //формат времени

    private int numberId;                                                     //Получает номер id под которым нужно создать новый таск
    private String nameFile;
    private ArrayList<TaskConstructor> taskList = new ArrayList<>();

    private JFrame mainFrame = null;
    private JFrame loginFrame = null;
    private JFrame createFrame = null;

    private JTable jTasks = null;
    private MyTableModel tModel = null;

    private JButton editButton = null;
    private JButton exitButton = null;
    private JButton loginButton = null;
    private JButton createButton = null;
    private JButton deleteButton = null;
    private JButton cancelButton = null;
    private JButton createButton2 = null;

    private JTextField listInfo = null;
    private JTextField listName = null;
    private JTextField listLogin = null;
    private JTextField listContact = null;
    private JFormattedTextField listDate = null;
    private JFormattedTextField listTime = null;

    private JLabel labelName = null;
    private JLabel labelInfo = null;
    private JLabel labelDate = null;
    private JLabel labelTime = null;
    private JLabel labelTitle = null;
    private JLabel labelContact = null;

    //Запуск стартовой формы
    @Override
    public void run() {
        try {
            int checker = 0;
            InetAddress ipAddress = InetAddress.getByName(ADDRESS); // создаем объект который отображает вышеописанный IP-адрес.
            clientSocket = new Socket(ipAddress, SERVERPORT); // создаем сокет используя IP-адрес и порт сервера.
            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
            sout = clientSocket.getOutputStream();
            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            out = new DataOutputStream(sout);
            getLoginPanel();
            while (nameFile == null) {
                Thread.sleep(500);
            }
            handlerXML.sendFile(clientSocket, nameFile);
            handlerXML.readXML(taskList, nameFile);
            numberId = handlerXML.getCounter();
            getMainFrame();

            for (int i = 0;; i++, Thread.sleep(3000)) {
                checker = ww.checkTaskOffensive(taskList, i);
                if (checker != 0) {
                    i += 25;
                    Thread.sleep(60000);
                }
            }

        } catch (InterruptedException |
                ParserConfigurationException | SAXException | UnknownHostException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Соединение с сервером не удалось!");
            System.exit(0);
        } catch (ParseException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Cоздание окна авторизации
    private JFrame getLoginPanel() {
        if (loginFrame == null) {
            loginFrame = new JFrame("Login Panel");
            loginFrame.setSize(400, 200);
            loginFrame.getContentPane().setBackground(new Color(240, 240, 240));
            loginFrame.setLayout(null);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel labelLogin = new JLabel("Назовите ваше имя ");
            labelLogin.setSize(200, 30);
            labelLogin.setLocation(130, 5);
            loginFrame.add(labelLogin);

            listLogin = new JTextField();
            listLogin.setSize(200, 30);
            listLogin.setLocation(100, 60);
            loginFrame.add(listLogin);

            loginFrame.add(getLoginButton(loginFrame));

            loginFrame.setVisible(true);
        }
        return loginFrame;
    }

    //Создания окна отображения событий
    private JFrame getMainFrame() {
        if (mainFrame == null) {
            mainFrame = new JFrame("Task Manager");
            mainFrame.setSize(700, 280);
            mainFrame.getContentPane().setBackground(new Color(240, 240, 240));
            mainFrame.getContentPane().setLayout(new FlowLayout());
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel labelTitle = new JLabel(
                    "<html><font size = +2> Ваши события </font></html>");
            mainFrame.add(labelTitle);

            //Добавление таблицы
            getMainTable();
            //Добавление кнопок
            mainFrame.add(getCreateMainFrameButton());
            mainFrame.add(getEditButton());
            mainFrame.add(getDeleteButton());
            mainFrame.add(getExitButton());

            mainFrame.setVisible(true);
        }
        return mainFrame;
    }

    //Создание таблицы и модели таблицы
    private JTable getMainTable() {
        if (jTasks == null) {
            //Cоздание скролла
            tModel = new MyTableModel(taskList);
            jTasks = new JTable((TableModel) tModel);
            jTasks.setPreferredScrollableViewportSize(new Dimension(620, 100));

            //Изменение размеров колонок
            jTasks.getColumnModel().getColumn(0).setMaxWidth(30);
            jTasks.getColumnModel().getColumn(1).setMaxWidth(225);
            jTasks.getColumnModel().getColumn(2).setMaxWidth(350);
            jTasks.getColumnModel().getColumn(3).setMaxWidth(70);
            jTasks.getColumnModel().getColumn(4).setMaxWidth(50);
            jTasks.getColumnModel().getColumn(5).setMaxWidth(275);

            mainFrame.add(jTasks);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            final JScrollPane scrollPane = new JScrollPane(jTasks);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            mainFrame.getContentPane().add(mainPanel);

            final JLabel currentSelectionLabel = new JLabel("");
            currentSelectionLabel.setAutoscrolls(true);
            mainFrame.add(currentSelectionLabel);

            final ListSelectionModel selModel = jTasks.getSelectionModel();
            selModel.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    ww.showWarningFieldSelection(deleteButton,
                            editButton, currentSelectionLabel, jTasks);
                }
            });
        }
        return jTasks;
    }

    //Метод отображения всех JLabel
    private void getAllLabel() {
        labelName = new JLabel("Введите название события: ");
        labelName.setSize(200, 30);
        labelName.setLocation(30, 35);
        createFrame.add(labelName);

        labelInfo = new JLabel("Введите описание события: ");
        labelInfo.setSize(200, 30);
        labelInfo.setLocation(30, 85);
        createFrame.add(labelInfo);

        labelDate = new JLabel("Введите дату оповещения(в формате dd.MM.yyyy): ");
        labelDate.setSize(300, 30);
        labelDate.setLocation(30, 135);
        createFrame.add(labelDate);

        labelTime = new JLabel("Введите время оповещения(в формате hh:mm): ");
        labelTime.setSize(300, 30);
        labelTime.setLocation(30, 185);
        createFrame.add(labelTime);

        labelContact = new JLabel("Введите контакты: ");
        labelContact.setSize(200, 30);
        labelContact.setLocation(30, 235);
        createFrame.add(labelContact);

    }

    //Метод отображения всех JTextField
    private void getAllTextField() {
        listName = new JTextField();
        listName.setSize(200, 30);
        listName.setLocation(30, 60);
        createFrame.add(listName);

        listInfo = new JTextField();
        listInfo.setSize(200, 30);
        listInfo.setLocation(30, 110);
        createFrame.add(listInfo);

        listDate = new JFormattedTextField(formatDate);
        listDate.setSize(120, 30);
        listDate.setLocation(30, 160);
        createFrame.add(listDate);

        listTime = new JFormattedTextField(formatTime);
        listTime.setSize(120, 30);
        listTime.setLocation(30, 210);
        createFrame.add(listTime);

        listContact = new JTextField();
        listContact.setSize(200, 30);
        listContact.setLocation(30, 260);
        createFrame.add(listContact);
    }

    //Слушатель для кнопки создания событий
    public void getCreateFrame() {

        System.out.println("Создайте событие");
        createFrame = new JFrame("Create Task");
        createFrame.setSize(400, 405);
        createFrame.getContentPane().setBackground(new Color(240, 240, 240));
        createFrame.setLayout(null);
        createFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        labelTitle = new JLabel(
                "<html><font size = +2> Создайте событие </font></html>");
        labelTitle.setSize(400, 30);
        labelTitle.setLocation(90, 10);
        createFrame.add(labelTitle);

        getAllLabel();
        getAllTextField();

        createFrame.add(getCreateButton2());
        createFrame.add(getCancelButton());

        mainFrame.setVisible(false);
        createFrame.setVisible(true);

    }

    //Кнопка авторизации
    private JButton getLoginButton(final JFrame frame) {
        if (loginButton == null) {
            loginButton = new JButton();
            loginButton.setText("Войти");
            loginButton.setSize(100, 30);
            loginButton.setForeground(new Color(21, 29, 48));
            loginButton.setLocation(140, 120);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        nameFile = listLogin.getText() + ".xml";
                        out.writeUTF(listLogin.getText());
                        frame.setVisible(false);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        return loginButton;
    }

    //Кнопка создания события в главном окне
    private JButton getCreateMainFrameButton() {
        if (createButton == null) {
            createButton = new JButton("Cоздать");
            createButton.setSize(100, 30);
            createButton.setForeground(new Color(21, 29, 48));

            //Слушатель для создания таска
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getCreateFrame();
                    tModel.fireTableDataChanged(); //Делает мгновенное обновление таблицы
                    deleteButton.setEnabled(false);
                    editButton.setEnabled(false);
                }
            });
        }
        return createButton;
    }

    //Кнопка подтверждения создания события
    private JButton getCreateButton2() {
        if (createButton2 == null) {
            createButton2 = new JButton("Cоздать");
            createButton2.setSize(100, 30);
            createButton2.setLocation(70, 310);
            createButton2.setForeground(new Color(21, 29, 48));

            createButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (listName.getText().isEmpty()
                            || listInfo.getText().isEmpty()
                            || listDate.getText().isEmpty()
                            || listTime.getText().isEmpty()
                            || listContact.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(createFrame, "Заполните все поля!");
                    } else {
                        taskList.add(new TaskConstructor(
                                numberId++,
                                listName.getText(),
                                listInfo.getText(),
                                listDate.getText(),
                                listTime.getText(),
                                listContact.getText()
                        ));

                        System.out.println("Событие создано!");
                        mainFrame.setVisible(true);
                        createFrame.setVisible(false);
                    }
                }
            });
        }
        return createButton2;
    }

    //Кнопка отмены в окне создания события
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton("Отмена");
            cancelButton.setSize(100, 30);
            cancelButton.setLocation(240, 310);
            cancelButton.setForeground(new Color(21, 29, 48));

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainFrame.setVisible(true);
                    createFrame.setVisible(false);
                }
            });
        }
        return cancelButton;
    }

    //Кнопка редактирования в окне просмотра события
    private JButton getEditButton() {
        if (editButton == null) {
            editButton = new JButton("Редактировать");
            editButton.setSize(130, 30);
            editButton.setForeground(new Color(21, 29, 48));
            editButton.setEnabled(false);

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = JOptionPane.showInputDialog("Введите новое название события: ");
                    String info = JOptionPane.showInputDialog("Введите новую информацию о событии: ");
                    String date = JOptionPane.showInputDialog("Введите новое число события, в формате(dd,MM,yyyy): ");
                    String time = JOptionPane.showInputDialog("Введите время события, в формате (hh:mm): ");
                    String contact = JOptionPane.showInputDialog("Введите контакты: ");

                    jTasks.getModel().setValueAt(name, jTasks.getSelectedRow(), 1);
                    jTasks.getModel().setValueAt(info, jTasks.getSelectedRow(), 2);
                    jTasks.getModel().setValueAt(date, jTasks.getSelectedRow(), 3);
                    jTasks.getModel().setValueAt(time, jTasks.getSelectedRow(), 4);
                    jTasks.getModel().setValueAt(contact, jTasks.getSelectedRow(), 5);
                }
            });

        }
        return editButton;
    }

    //Кнопка удаления в окне просмотра события
    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new JButton("Удалить");
            deleteButton.setSize(130, 30);
            deleteButton.setForeground(new Color(21, 29, 48));
            deleteButton.setEnabled(false);

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((MyTableModel) jTasks.getModel()).removeRow(jTasks.getSelectedRow());
                    //Мгновенное обновление данных таблицы
                    tModel.fireTableDataChanged();
                    deleteButton.setEnabled(false);
                    editButton.setEnabled(false);
                }
            });
        }
        return deleteButton;
    }

    //Кнопка выхода в окне просмотра события
    private JButton getExitButton() {
        if (exitButton == null) {
            exitButton = new JButton("Сохранить и Выйти");
            exitButton.setSize(200, 30);
            exitButton.setForeground(new Color(21, 29, 48));

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        for (TaskConstructor o : taskList) {
                            out.writeUTF(o.getName());
                            out.writeUTF(o.getInfo());
                            out.writeUTF(o.getDate());
                            out.writeUTF(o.getTime());
                            out.writeUTF(o.getContact());

                        }
                        handlerXML.deleteFile(nameFile);
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Данные на сервер отправлены!");
                    System.exit(0);
                }
            });
        }
        return exitButton;
    }

}
