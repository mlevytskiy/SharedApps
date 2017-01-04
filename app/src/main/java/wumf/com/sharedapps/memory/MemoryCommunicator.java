package wumf.com.sharedapps.memory;

import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import wumf.com.sharedapps.MainApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by max on 30.12.16.
 * Only for small list (less 1000 items)
 */

public class MemoryCommunicator {

    private static final String STORAGE_NAME = "memory";
    private static final String SEPARATOR = "|";
    private SharedPreferences sp;

    public MemoryCommunicator() {
        sp = MainApplication.instance.getSharedPreferences(STORAGE_NAME, MODE_PRIVATE);
    }

    public void saveList(List<String> list, Key key) {
        if (list == null || list.isEmpty()) {
            sp.edit().remove(key.name()).apply();
            return;
        }
        sp.edit().putString(key.name(), TextUtils.join(SEPARATOR, list)).apply();
    }

    public List<String> loadList(Key key) {
        if (sp.contains(key.name())) {
            return Arrays.asList( TextUtils.split(sp.getString(key.name(), ""), Pattern.quote(SEPARATOR)) );
        } else {
            return new ArrayList<>();
        }
    }

    public void saveStr(String value, Key key) {
        sp.edit().putString(key.name(), value).apply();
    }

    public String loadStr(Key key) {
        return sp.getString(key.name(), "");
    }

}
