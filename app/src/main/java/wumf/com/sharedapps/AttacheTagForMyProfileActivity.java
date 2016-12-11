package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import wumf.com.sharedapps.firebase.TagsFirebase;
import wumf.com.sharedapps.util.OnTextChangedListener;
import wumf.com.sharedapps.view.CustomTopBar;
import wumf.com.sharedapps.view.OnClickTag;
import wumf.com.sharedapps.view.TagsTextView;

/**
 * Created by max on 07.12.16.
 */

public class AttacheTagForMyProfileActivity extends Activity {

    public static final String KEY_USER_UID = "uid";

    private String uid;
    private TagsTextView tagsTextView;
    private EditText editText;
    private View attacheTag;
    private List<String> mockTags = new ArrayList<>();
    {
        mockTags.add("KievAndroidDevClub");
        mockTags.add("animals");
        mockTags.add("english");
        mockTags.add("some_tag");
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        uid = getIntent().getExtras().getString(KEY_USER_UID);
        setContentView(R.layout.activity_attache_tag_for_my_profile);
        ((CustomTopBar) findViewById(R.id.top_bar)).setText("Attache tag for my profile").bind(this);
        attacheTag = findViewById(R.id.attache_tag);
        attacheTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                hideKeyboard();
                String tag = editText.getText().toString();
                if (TextUtils.isEmpty(tag)) {
                    TastyToast.makeText(view.getContext(), "You need enter a tag", TastyToast.LENGTH_LONG,
                            TastyToast.INFO);
                    return;
                }
                TagsFirebase.attachTag(uid, tag);
                TastyToast.makeText(view.getContext(), "Loading", TastyToast.LENGTH_LONG,
                        TastyToast.DEFAULT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TastyToast.makeText(view.getContext(), "Successful!", TastyToast.LENGTH_LONG,
                                TastyToast.SUCCESS);
                    }
                }, 2000);

            }
        });

        tagsTextView = (TagsTextView) findViewById(R.id.tags_text_view);
        tagsTextView.setTags(mockTags);

        editText = (EditText) findViewById(R.id.edit_text);
        editText.addTextChangedListener(new OnTextChangedListener() {
            @Override
            public void onTextChanged(String oldText, String newText) {
                tagsTextView.autocomplete(newText);
            }
        });
//        ((ListView) findViewById(R.id.list_view)).setAdapter(new FollowUnfollowPeopleAdapter());
    }

    public void onStart() {
        super.onStart();
        tagsTextView.setOnTagListener(new OnClickTag() {
            @Override
            public void onClick(String tag) {
                editText.getText().clear();
                editText.append(tag);
            }
        });
    }

    public void onStop() {
        super.onStop();
        tagsTextView.setOnTagListener(null);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
