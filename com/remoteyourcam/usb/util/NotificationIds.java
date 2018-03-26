package com.remoteyourcam.usb.util;

import java.util.HashMap;
import java.util.Map;

public class NotificationIds {
    private static NotificationIds instance = new NotificationIds();
    private int counter;
    private final Map<String, Integer> map = new HashMap();

    public static NotificationIds getInstance() {
        return instance;
    }

    public int getUniqueIdentifier(String str) {
        Integer num = (Integer) this.map.get(str);
        if (num != null) {
            return num.intValue();
        }
        this.counter++;
        this.map.put(str, Integer.valueOf(this.counter));
        return this.counter;
    }
}
