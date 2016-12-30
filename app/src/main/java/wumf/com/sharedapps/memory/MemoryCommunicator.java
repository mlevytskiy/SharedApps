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

    private static final String KEY = "list";
    private static final String SEPARATOR = "|";
    private SharedPreferences sp;

    public MemoryCommunicator() {
        sp = MainApplication.instance.getSharedPreferences("oldContacts", MODE_PRIVATE);
    }

    public void saveList(List<String> list) {
        sp.edit().putString(KEY, TextUtils.join(SEPARATOR, list)).apply();
    }

    public List<String> loadList() {
        if (sp.contains(KEY)) {
            return Arrays.asList( TextUtils.split(sp.getString(KEY, ""), Pattern.quote(SEPARATOR)) );
        } else {
            return new ArrayList<>();
        }
    }

}
