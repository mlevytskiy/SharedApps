package wumf.com.sharedapps.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by max on 25.12.16.
 */

public class AutofollowTextBuilder {

    private List<String> tags;

    public AutofollowTextBuilder setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public String build() {
        return "Autofollow my phone contacts"
                + (tags.isEmpty() ? "" : ", ")
                + TextUtils.join(", ", tags);
    }

}
