package wumf.com.sharedapps.mockactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.R;

/**
 * Created by max on 11.12.16.
 */

public class TagCloudActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_tag_cloud);

        EditText editText = (EditText) findViewById(R.id.edit_text);

        TagCloudLinkView tagCloudLinkView = (TagCloudLinkView) findViewById(R.id.tag_cloud_link_view);
        tagCloudLinkView.enableAutocompleteMode(editText);

        List<String> tags = new ArrayList<>();
        tags.add("test1");
        tags.add("test2");
        tags.add("test3");
        tags.add("test4");
        tags.add("test5");
        tags.add("test6");
        tags.add("test7");
        tags.add("test8");
        tags.add("test9");
        tags.add("test10");
        tags.add("test11");

        tagCloudLinkView.setAll(tags);

        tagCloudLinkView.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(String tag, int position) {
                Toast.makeText(TagCloudActivity.this, tag + " deleted", Toast.LENGTH_LONG).show();
            }
        });

        tagCloudLinkView.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener() {
            @Override
            public void onTagSelected(String tag, int position) {
                Toast.makeText(TagCloudActivity.this, tag, Toast.LENGTH_LONG).show();
            }
        });

        tagCloudLinkView.setOnAddTagListener(new TagCloudLinkView.OnAddTagListener() {
            @Override
            public void onAddTag() {
                Toast.makeText(TagCloudActivity.this, "add", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Tag getTag(String str) {
        Tag tag = new Tag(str.hashCode(), str);
        return tag;
    }

}
