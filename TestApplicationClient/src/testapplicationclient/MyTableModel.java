package testapplicationclient;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

    private final ArrayList<TaskConstructor> tasks;

    MyTableModel(ArrayList<TaskConstructor> tasks) {
        super();
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int c) {
        String result = "";
        switch (c) {
            case 0:
                result = "Id";
                break;
            case 1:
                result = "Название";
                break;
            case 2:
                result = "Информация";
                break;
            case 3:
                result = "Число";
                break;
            case 4:
                result = "Время";
                break;
            case 5:
                result = "Контакты";
                break;
        }
        return result;
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return tasks.get(row).getId();
            case 1:
                return tasks.get(row).getName();
            case 2:
                return tasks.get(row).getInfo();
            case 3:
                return tasks.get(row).getDate();
            case 4:
                return tasks.get(row).getTime();
            case 5:
                return tasks.get(row).getContact();
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        switch (column) {
            case 1:
                tasks.get(row).setName((String) value);
                break;
            case 2:
                tasks.get(row).setInfo((String) value);
                break;
            case 3:
                tasks.get(row).setDate((String) value);
                break;
            case 4:
                tasks.get(row).setTime((String) value);
                break;
            case 5:
                tasks.get(row).setContact((String) value);
                break;
        }
        fireTableCellUpdated(row, column);

    }

    public void removeRow(int row) {
        tasks.remove(row);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

}
