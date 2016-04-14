package testapplicationclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class WarningWindow {

    private long myTimeMS;
    private long res;
    private String timeForOffensive;
    private final SimpleDateFormat formatFullDate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private Date d = new Date();
//    private long nowTimeMS;

    
    //Проверка наступления события
    public int checkTaskOffensive(ArrayList<TaskConstructor> taskList, int i)
            throws ParseException, InterruptedException {
        long nowTimeMS = d.getTime(); //текущее время
        nowTimeMS +=3000*(1+i);
        for (TaskConstructor tc : taskList) {
            timeForOffensive = tc.getDate() + " " + tc.getTime();
            Date docDate = formatFullDate.parse(timeForOffensive);
            myTimeMS = docDate.getTime();
            res = ((myTimeMS - nowTimeMS) - (3600 * 1000 * 7)) / 60000;
            if (res == 0) {
                JOptionPane.showMessageDialog(null, "У вас сработало событие!!!"
                        + "\nНазвание: " + tc.getName()
                        + "\nИнформация: " + tc.getInfo()
                        + "\nКонтакты: " + tc.getContact()
                );
                return 1;
            }
        }
        return 0;
    }
    
    //Вывод ошибки, если в полях jTable выбрали много полей
    public void showWarningFieldSelection(JButton deleteButton,
            JButton editButton, JLabel currentSelectionLabel, JTable jTasks) {
        int counter = 0;
        int[] selectedRows = jTasks.getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            counter++;
        }
        if (counter > 1) {
            currentSelectionLabel.setText("    Вы выбрали слишком много записей!  ");
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        }
        if (counter == 1) {
            currentSelectionLabel.setText("");
            deleteButton.setEnabled(true);
            editButton.setEnabled(true);
        }
    }

}
