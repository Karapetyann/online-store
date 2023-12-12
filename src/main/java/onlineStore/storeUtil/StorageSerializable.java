package onlineStore.storeUtil;

import onlineStore.storeStorage.OrderStorage;
import onlineStore.storeStorage.ProductStorage;
import onlineStore.storeStorage.UserStorage;

import java.io.*;


public class StorageSerializable implements Serializable {
    public static final String USER_STORAGE_PATH = "C:\\Users\\Hakob\\IdeaProjects\\online-store\\src\\main\\java\\onlineStore\\storeData\\UserStorage";
    public static final String PRODUCT_STORAGE_PATH = "C:\\Users\\Hakob\\IdeaProjects\\online-store\\src\\main\\java\\onlineStore\\storeData\\ProductStorage";
    public static final String ORDER_STORAGE_PATH = "C:\\Users\\Hakob\\IdeaProjects\\online-store\\src\\main\\java\\onlineStore\\storeData\\OrderStorage";


    public static void serializeUserStorage(UserStorage userStorage) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(USER_STORAGE_PATH))) {
            outputStream.writeObject(userStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserStorage deSerializeUserStorage() {
        File file = new File(USER_STORAGE_PATH);
        if (!file.exists()) {
            return new UserStorage();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(USER_STORAGE_PATH))) {
            Object object = inputStream.readObject();
            if (object instanceof UserStorage userStorage) {
                return userStorage;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new UserStorage();
    }

    public static void serializeProductStorage(ProductStorage productStorage) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(PRODUCT_STORAGE_PATH))) {
            outputStream.writeObject(productStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ProductStorage deSerializeProductStorage() {
        File file = new File(PRODUCT_STORAGE_PATH);
        if (!file.exists()) {
            return new ProductStorage();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PRODUCT_STORAGE_PATH))) {
            Object object = inputStream.readObject();
            if (object instanceof ProductStorage productStorage) {
                return productStorage;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ProductStorage();
    }

    public static void serializeOrderStorage(OrderStorage orderStorage) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(ORDER_STORAGE_PATH))) {
            outputStream.writeObject(orderStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OrderStorage deSerializeOrderStorage() {
        File file = new File(ORDER_STORAGE_PATH);
        if (!file.exists()) {
            return new OrderStorage();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ORDER_STORAGE_PATH))) {
            Object object = inputStream.readObject();
            if (object instanceof OrderStorage orderStorage) {
                return orderStorage;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new OrderStorage();
    }
}
