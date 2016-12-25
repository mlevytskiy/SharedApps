package wumf.com.sharedapps.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 25.12.16.
 */

public class TagsBuilder {

    private static final String SEPARATOR = ":";
    List<String> tags = new ArrayList<>();

    public TagsBuilder add(String tag) {
        tags.add(tag);
        return this;
    }

    public String build() {
        return TextUtils.join(SEPARATOR, tags);
    }

}
