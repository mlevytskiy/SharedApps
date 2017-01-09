package wumf.com.sharedapps;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ns.developer.tagview.widget.TagCloudLinkView;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import wumf.com.sharedapps.firebase.TagsFirebase;
import wumf.com.sharedapps.firebase.TransactionResultListener;
import wumf.com.sharedapps.view.CustomTopBar;

import static com.sdsmdg.tastytoast.TastyToast.makeText;

/**
 * Created by max on 07.12.16.
 */

public class AttacheTagForMyProfileActivity extends Activity {

    public static final String KEY_USER_UID = "uid";

    private String uid;
    private TagCloudLinkView tagsTextView;
    private EditText editText;
    private View attacheTag;

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
                    makeText(view.getContext(), "You need enter a tag", TastyToast.LENGTH_LONG,
                            TastyToast.INFO);
                    return;
                }
                final Toast loadingToast = TastyToast.makeText(view.getContext(), "Loading", TastyToast.LENGTH_LONG,
                        TastyToast.DEFAULT);
                TagsFirebase.attachTag(uid, tag, new TransactionResultListener() {
                    @Override
                    public void onSuccess() {
                        loadingToast.cancel();
                        makeText(view.getContext(), "Successful!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        finish();
                    }

                    @Override
                    public void onError() {
                        loadingToast.cancel();
                        makeText(view.getContext(), "Check you internet connection", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        finish();
                    }
                });

            }
        });

        tagsTextView = (TagCloudLinkView) findViewById(R.id.tags_text_view);
        editText = (EditText) findViewById(R.id.edit_text);
        tagsTextView.enableAutocompleteMode(editText);
        TagsFirebase.listenAllTags(new OnChangeAllTagsListener() {
            @Override
            public void onChange(List<String> tags) {
                tagsTextView.setAll(tags);
            }
        });
        tagsTextView.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener() {
            @Override
            public void onTagSelected(String tag, int position) {
                editText.getText().clear();
                editText.append(tag);
            }
        });
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
