package onlineStore.storeStorage;


import onlineStore.model.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class UserStorage implements Serializable {


    private Map<String, User> userMap = new HashMap<>();




    public void printUsers() {
        for (String user : userMap.keySet()) {
            System.out.println(user);
        }
    }


    public User getByUser(String email, String password) {
        for (User value : userMap.values()) {
            if (value.getEmail().equals(email) && value.getPassword().equals(password)) {
                return value;
            }
        }
        return null;
    }

    public boolean getByEmail(String email) {
        for (String s : userMap.keySet()) {
            if (s.equals(email)) {
                return true;
            }
        }
        return false;
    }

    public void userPut(String email, User user1) {
        userMap.put(email, user1);

    }
}
