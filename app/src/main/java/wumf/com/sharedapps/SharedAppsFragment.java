package wumf.com.sharedapps;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

/**
 * Created by max on 22.08.16.
 */
public class SharedAppsFragment extends Fragment implements View.OnClickListener, IHideShow {

    private int FRAME_DURATION = 20;

    private static int[] FRAME_ANIM_RES = new int[] {
            R.mipmap.compose_anim_1,
            R.mipmap.compose_anim_2,
            R.mipmap.compose_anim_3,
            R.mipmap.compose_anim_4,
            R.mipmap.compose_anim_5,
            R.mipmap.compose_anim_6,
            R.mipmap.compose_anim_7,
            R.mipmap.compose_anim_8,
            R.mipmap.compose_anim_9,
            R.mipmap.compose_anim_10,
            R.mipmap.compose_anim_11,
            R.mipmap.compose_anim_12,
            R.mipmap.compose_anim_13,
            R.mipmap.compose_anim_14,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_15,
            R.mipmap.compose_anim_16,
            R.mipmap.compose_anim_17,
            R.mipmap.compose_anim_18,
            R.mipmap.compose_anim_19
    };

    private FloatingActionButton fab;
    private AnimationDrawable frameAnim;
    private AnimationDrawable frameReverseAnim;

    private SpringFloatingActionMenu springFloatingActionMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_apps, container, false);

        createFabReverseFrameAnim();
        fab = createFloatingActionButton();

        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(getContext())
                .fab(fab)
                .addMenuItem(R.color.photo, R.mipmap.ic_new_folder, "", R.color.text_color, this)
                .addMenuItem(R.color.chat, R.mipmap.ic_messaging_posttype_chat, "", R.color.text_color,this)
                .addMenuItem(R.color.quote, R.mipmap.ic_messaging_posttype_quote, "", R.color.text_color,this)
                .addMenuItem(R.color.link, R.mipmap.ic_messaging_posttype_link, "", R.color.text_color,this)
                .addMenuItem(R.color.audio, R.mipmap.ic_messaging_posttype_audio, "", R.color.text_color,this)
                .addMenuItem(R.color.text, R.mipmap.ic_messaging_posttype_text, "", R.color.text_color,this)
                .addMenuItem(R.color.video, R.mipmap.ic_messaging_posttype_video, "", R.color.text_color,this)
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.colorPrimary2)
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fab.setImageDrawable(frameAnim);
                        frameReverseAnim.stop();
                        frameAnim.start();
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageDrawable(frameReverseAnim);
                        frameAnim.stop();
                        frameReverseAnim.start();
                    }
                })
                .build();

        return view;
    }

    private FloatingActionButton createFloatingActionButton() {
        frameAnim = createFabFrameAnim();
        final FloatingActionButton fab = new FloatingActionButton(getContext());
        fab.setType(FloatingActionButton.TYPE_NORMAL);
        fab.setImageDrawable(frameAnim);
        fab.setShadow(true);
        fab.setColorPressedResId(R.color.fab);
        fab.setColorNormalResId(R.color.fab);
        fab.setColorRippleResId(R.color.text_color);
        return fab;
    }

    private AnimationDrawable createFabFrameAnim() {
        AnimationDrawable frameAnim = new AnimationDrawable();
        frameAnim.setOneShot(true);
        Resources resources = getResources();
        for (int res : FRAME_ANIM_RES) {
            frameAnim.addFrame(resources.getDrawable(res), FRAME_DURATION);
        }
        return frameAnim;
    }

    private void createFabReverseFrameAnim() {
        frameReverseAnim = new AnimationDrawable();
        frameReverseAnim.setOneShot(true);
        Resources resources = getResources();
        for (int i = FRAME_ANIM_RES.length - 1; i >= 0; i--) {
            frameReverseAnim.addFrame(resources.getDrawable(FRAME_ANIM_RES[i]), FRAME_DURATION);
        }
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public void hide() {

        if (fab.getVisibility() == View.GONE) {
            return;
        }

        springFloatingActionMenu.hideFollowCircles();
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.collaps);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //do nothing
            }
        });
        fab.startAnimation(anim);
    }

    @Override
    public void show() {
        ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        anim.setFillAfter(true);
        fab.setImageDrawable(frameAnim);
        fab.setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                springFloatingActionMenu.showFollowCircles();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(anim);
    }

}
