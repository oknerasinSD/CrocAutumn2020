import java.io.Serializable;
import java.util.UUID;

public class Executor implements Serializable, Entity {

    /** Версия объекта */
    private static final long serialVersionUID = 1L;

    /** Идентификатор исполнителя */
    private String id = UUID.randomUUID().toString();

    /** Имя исполнителя */
    private String firstName;

    /** Фамилия исполнителя */
    private String lastName;

    /** Должность исполнителя */
    private String position;

    public Executor(String name, String surname, String position) {
        this.firstName = name;
        this.lastName = surname;
        this.position = position;
    }

    @Override
    public String getShortName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Исполнитель:" + "\n" +
                "ID: " + id + '\n' +
                "Имя: " + firstName + '\n' +
                "Фамилия: " + lastName + '\n' +
                "Должность: " + position;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }
}
