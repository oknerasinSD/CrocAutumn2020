import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Data {

    /** Директория "базы данных" */
    private File dataDirectory;

    /** Директория с заданиями */
    private File tasksDirectory;

    /** Директория с исполнителями */
    private File executorsDirectory;

    /** Классы, для которых в "базе" существуют отдельные директории */
    private Map<Class, File> directories = new HashMap<>();

    public Data() {
        dataDirectory = new File("data");
        dataDirectory.mkdir();
        tasksDirectory = new File (dataDirectory.getPath() + File.separator + "tasks");
        tasksDirectory.mkdir();
        executorsDirectory = new File(dataDirectory.getPath() + File.separator + "executors");
        executorsDirectory.mkdir();
        directories.put(Task.class, tasksDirectory);
        directories.put(Executor.class, executorsDirectory);
    }

    /**
     * Сериализация объекта.
     * @param object - объект.
     * @param name - имя файла, в которых он будет сохранен.
     */
    public void serializeObject(Object object, String name) {
        String path = buildPath(object.getClass(), name);
        try (FileOutputStream fileOutputStream =
                     new FileOutputStream(path)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Десериализация объекта.
     * @param directory - директория, в которой лежит файл, который необходимо десериализовать.
     * @param name - имя файла.
     * @return - объект, полученный после десериализации.
     */
    public Object deserializeObject(File directory, String name) {
        try {
            FileInputStream fileInputStream =
                    new FileInputStream(directory.getPath() + File.separator + name);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Построение пути для сохранения файла при сериализации.
     * @param classOfObject - класс объекта.
     * @param name - имя файла.
     * @return - строка = имя файла относительно корневой директории проекта.
     */
    private String buildPath(Class classOfObject, String name) {
        String path = "";
        if (directories.containsKey(classOfObject)) {
            path = directories.get(classOfObject) + File.separator;
        }
        path += name + ".bin";
        return path;
    }

    public File getDataDirectory() {
        return dataDirectory;
    }

    public File getTasksDirectory() {
        return tasksDirectory;
    }

    public File getExecutorsDirectory() {
        return executorsDirectory;
    }
}
