package com.soaic.zero.designpatterns.flyweight;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.UUID;

public class flyweight {

    public HashMap<String, String> CACHE = new HashMap<>();

    public String getCodeByKey(String key) {
        String code = CACHE.get(key);
        if (TextUtils.isEmpty(key)) {
            code = UUID.randomUUID().toString();
            CACHE.put(key, code);
        }
        return code;
    }
}
