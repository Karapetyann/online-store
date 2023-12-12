package onlineStore.storeUtil;

import java.util.UUID;

public class StoreIdGenerate {
    public static String idGenerate() {
        String uuid = UUID.randomUUID().toString();
        String[] uuids = uuid.split("-");
        return uuids[0];
    }
}
