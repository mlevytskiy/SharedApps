package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.ns.developer.tagview.entity.Tag;
import com.ns.developer.tagview.widget.TagCloudLinkView;

/**
 * Created by max on 11.12.16.
 */

public class TagCloudActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_tag_cloud);

        TagCloudLinkView tagCloudLinkView = (TagCloudLinkView) findViewById(R.id.tag_cloud_link_view);

        tagCloudLinkView.add(getTag("test1"));
        tagCloudLinkView.add(getTag("test2"));
        tagCloudLinkView.add(getTag("test3"));
        tagCloudLinkView.add(getTag("test4"));
        tagCloudLinkView.add(getTag("test5"));
        tagCloudLinkView.add(getTag("test6"));
        tagCloudLinkView.add(getTag("test7"));
        tagCloudLinkView.add(getTag("test8"));
        tagCloudLinkView.add(getTag("test9"));
        tagCloudLinkView.add(getTag("test10"));
        tagCloudLinkView.add(getTag("test11"));

        tagCloudLinkView.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int position) {
                Toast.makeText(TagCloudActivity.this, tag.getText() + " deleted", Toast.LENGTH_LONG).show();
            }
        });

        tagCloudLinkView.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener() {
            @Override
            public void onTagSelected(Tag tag, int position) {
                Toast.makeText(TagCloudActivity.this, tag.getText(), Toast.LENGTH_LONG).show();
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
