package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.util.OnTextChangedListener;
import wumf.com.sharedapps.view.CustomTopBar;
import wumf.com.sharedapps.view.TagsTextView;

/**
 * Created by max on 07.12.16.
 */

public class AttacheTagForMyProfileActivity extends Activity {

    private List<String> mockTags = new ArrayList<>();
    {
        mockTags.add("KievAndroidDevClub");
        mockTags.add("animals");
        mockTags.add("english");
        mockTags.add("some_tag");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_attache_tag_for_my_profile);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Attache tag for my profile").bind(this);
        findViewById(R.id.attache_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "test", Toast.LENGTH_LONG).show();
            }
        });

        final TagsTextView tagsTextView = (TagsTextView) findViewById(R.id.tags_text_view);
        tagsTextView.setTags(mockTags);

        EditText editText = (EditText) findViewById(R.id.edit_text);
        editText.addTextChangedListener(new OnTextChangedListener() {
            @Override
            public void onTextChanged(String oldText, String newText) {
                tagsTextView.autocomplete(newText);
            }
        });
//        ((ListView) findViewById(R.id.list_view)).setAdapter(new FollowUnfollowPeopleAdapter());
    }

}
