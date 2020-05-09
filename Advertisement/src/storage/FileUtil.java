package storage;


import model.Item;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
    private static final String FILE_PATH_USER = "C:\\Users\\Arzuman\\Desktop\\Folder\\Advertisement\\Advertisement\\src\\storage\\file\\User.txt";
    private static final String FILE_PATH_ITEM = "C:\\Users\\Arzuman\\Desktop\\Folder\\Advertisement\\Advertisement\\src\\storage\\file\\Item.txt";

    public static void serializeUserMap(Map<String, User> userMap) {
        File userMapFile = new File(FILE_PATH_USER);
        try {
            if (!userMapFile.exists()) {
                userMapFile.createNewFile();
            }
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH_USER))) {
                objectOutputStream.writeObject(userMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Map<String,User> deserializeUserMap() {
        Map<String,User> result = new HashMap<>();
        File userMapFile = new File(FILE_PATH_USER);
        if (userMapFile.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH_USER))) {
                Object o = objectInputStream.readObject();
                return (Map<String,User>) o;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

    public static void serializeItem(List<Item> item) {
        File fileItems = new File(FILE_PATH_ITEM);
        try {
            if (!fileItems.exists()) {
                fileItems.createNewFile(); }
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH_ITEM))) {
                objectOutputStream.writeObject(item); }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Item> deserializeItem() {
        List<Item> result = new ArrayList<>();
        File fileItems = new File(FILE_PATH_ITEM);
        if (fileItems.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH_ITEM))) {
                Object o = objectInputStream.readObject();
                return (List<Item>) o;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
