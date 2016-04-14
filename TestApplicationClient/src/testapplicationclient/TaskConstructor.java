package testapplicationclient;

public class TaskConstructor {

    private int id;
    private String name;
    private String contact;
    private String date;
    private String time;
    private String info;

    public TaskConstructor(int id, String name, String info, 
            String date, String time, String contact) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.date = date;
        this.time = time;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "id: " + id + " "
                + "Имя: " + name + " "
                + "Информация: " + info + " "
                + "Число: " + date + " "
                + "Время: " + time + " "
                + "Контакты: " + contact;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getTime() {
        return time;
    }

    public String getInfo() {
        return info;
    }

}
