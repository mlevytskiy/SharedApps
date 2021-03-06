package wumf.com.sharedapps.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.tiancaicc.springfloatingactionmenu.OnAppClickListener;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.OpenAllAppsListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.List;

import hugo.weaving.DebugLog;
import wumf.com.appsprovider.App;
import wumf.com.sharedapps.AllAppsActivity;
import wumf.com.sharedapps.CurrentUser;
import wumf.com.sharedapps.IHideShow;
import wumf.com.sharedapps.MainActivity;
import wumf.com.sharedapps.MainApplication;
import wumf.com.sharedapps.OnBackPressedListener;
import wumf.com.sharedapps.R;
import wumf.com.sharedapps.adapter.AppsAdapter;
import wumf.com.sharedapps.eventbus.ChangeAllFoldersAndAppsFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.ChangeTop6AppsEvent;
import wumf.com.sharedapps.eventbus.OnClickAppEvent;
import wumf.com.sharedapps.eventbus.RemoveAppEvent;
import wumf.com.sharedapps.firebase.FavouriteAppsFirebase;
import wumf.com.sharedapps.view.AppsRecycleView;
import wumf.com.sharedapps.view.TouchyRecyclerView;

/**
 * Created by max on 22.08.16.
 */
@DebugLog
public class SharedAppsFragment extends Fragment implements OnAppClickListener, IHideShow, OnBackPressedListener {

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
    private boolean isDisableOpenMenuListener = false;
    private AppsRecycleView appsRecycleView;

    private SpringFloatingActionMenu springFloatingActionMenu;

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (appsRecycleView.isEmpty()) {
            FavouriteAppsFirebase.listenFoldersAndAppsSingleTime();
        }
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ChangeTop6AppsEvent event) {
        fill(event.apps);
    }

    @Subscribe
    public void onEvent(ChangeAllFoldersAndAppsFromFirebaseEvent event) {
        appsRecycleView.updateSharedApps(event.apps, true);
    }

    private WeakReference<Snackbar> lastSnackBar = null;
    boolean isActionButtonHiddenFromSnackBar = false;

    @Subscribe
    public void onEvent(RemoveAppEvent event) {
        final AppsAdapter.Item item = appsRecycleView.findItemByPackageName(event.appPackage);
        FavouriteAppsFirebase.removeApp(CurrentUser.getUID(), event.appPackage);
        final Runnable showSnackBarRunnable = new Runnable() {
            @Override
            public void run() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String text = "remove ";
                        int firstIndex = text.length();
                        text = text + item.name;
                        int secondIndex = text.length();
                        text = text + " from favourites";
                        Spannable richText = new SpannableString(text);
                        int redColor = getResources().getColor(R.color.remove_snack_bar_text);
                        int redColor2 = getResources().getColor(R.color.remove_snack_bar_text_app_name);

                        richText.setSpan(new ForegroundColorSpan(redColor), 0, firstIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        richText.setSpan(new ForegroundColorSpan(redColor2), firstIndex, secondIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        richText.setSpan(new ForegroundColorSpan(redColor), secondIndex, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        Snackbar snackbar = Snackbar.make(getView(), richText, Snackbar.LENGTH_LONG);
                        snackbar.setAction("Undo", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                FavouriteAppsFirebase.addApp(CurrentUser.getUID(), item.appPackage, item.name, item.icon, item.time);
                            }
                        });
                        lastSnackBar = new WeakReference<Snackbar>(snackbar);
                        snackbar.show();
                        snackbar.setCallback(new Snackbar.Callback() {

                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                isActionButtonHiddenFromSnackBar = false;
                                show();
                            }

                        });
                    }
                };
                if (isActionButtonHiddenFromSnackBar) {
                    runnable.run();
                } else {
                    isActionButtonHiddenFromSnackBar = true;
                    hide(runnable);
                }
            }
        };

        boolean isShowedSnackBar = (lastSnackBar != null) && (lastSnackBar.get() != null) && lastSnackBar.get().isShown();

        if (isShowedSnackBar) {
            lastSnackBar.get().setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    showSnackBarRunnable.run();
                }
            });
        } else {
            showSnackBarRunnable.run();
        }
    }

    private void fill(List<App> apps) {
        for (int i = 0; i < apps.size(); i++) {
            springFloatingActionMenu.changeMenuItem(i+1, apps.get(i).appPackage, apps.get(i).icon);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shared_apps, container, false);

        appsRecycleView = (AppsRecycleView) view.findViewById(R.id.apps_recycle_view);
        appsRecycleView.setEmptyView(view.findViewById(R.id.empty_view_type_face_text_view));

        createFabReverseFrameAnim();
        fab = createFloatingActionButton();

        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(getContext())
                .fab(fab)
                .addMenuItem(R.color.photo, R.mipmap.ic_new_folder, "", R.color.text_color, this)
                .addMenuItem(R.color.chat, R.mipmap.ic_messaging_posttype_link, "", R.color.text_color,this)
                .addMenuItem(R.color.quote, R.mipmap.ic_messaging_posttype_link, "", R.color.text_color,this)
                .addMenuItem(R.color.link, R.mipmap.ic_messaging_posttype_link, "", R.color.text_color,this)
                .addMenuItem(R.color.audio, R.mipmap.ic_messaging_posttype_link, "all apps", R.color.text_color,this)
                .addMenuItem(R.color.text, R.mipmap.ic_messaging_posttype_link, "", R.color.text_color,this)
                .addMenuItem(R.color.video, R.mipmap.ic_messaging_posttype_link, "", R.color.text_color,this)
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.colorPrimary2)
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        if (isDisableOpenMenuListener) {
                            return;
                        }
                        fab.setImageDrawable(frameAnim);
                        frameReverseAnim.stop();
                        frameAnim.start();
                        setStatusBarColor(R.color.colorPrimary2);
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageDrawable(frameReverseAnim);
                        frameAnim.stop();
                        frameReverseAnim.start();
                        setStatusBarColor(R.color.colorAccent);
                    }
                })
                .build();

        List<App> apps;
        if ( (apps = ((MainApplication) getActivity().getApplication()).top6apps) != null ) {
            fill(apps);
        }

        springFloatingActionMenu.setOpenAllAppsListener(new OpenAllAppsListener() {
            @Override
            public void onClick() {
                getActivity().startActivityForResult(new Intent(getContext(), AllAppsActivity.class),
                        MainActivity.REQUEST_CODE_CHIOCE_APP);
//                getActivity().startActivity();
            }
        });

        appsRecycleView.setOnNoChildClickListener(new TouchyRecyclerView.OnNoChildClickListener() {
            @Override
            public void onNoChildClick() {
                if (appsRecycleView.isNeedCloseFlippedCards()) {
                    appsRecycleView.closeFlippedCards();
                } else {
                    //do nothing
                }
            }
        });

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

    private void setStatusBarColor(@ColorRes int color) {
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getActivity().getResources().getColor(color));
        }
    }

    private void hide(final Runnable afterHide) {

        springFloatingActionMenu.disableOpenMenuCapability();
        isDisableOpenMenuListener = true;

        springFloatingActionMenu.hideFollowCircles();
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.collaps);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.GONE);
                if (afterHide != null) {
                    afterHide.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //do nothing
            }
        });
        fab.startAnimation(anim);
    }

    @Override
    public void hide() {

        if (springFloatingActionMenu == null) {
            return;
        }

        if (springFloatingActionMenu.isDisableOpenMenuCapability()) {
            return;
        }

        hide(null);
    }

    @Override
    public void show() {

        if (springFloatingActionMenu == null) {
            return;
        }

        if (isActionButtonHiddenFromSnackBar) {
            return;
        }

        springFloatingActionMenu.enableOpenMenuCapability();
        isDisableOpenMenuListener = false;
        ScaleAnimation anim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(200);
        anim.setFillAfter(true);
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

    @Override
    public boolean doBack(int delay) {
        if (springFloatingActionMenu == null) {
            return false;
        }

        if (springFloatingActionMenu.isMenuOpen()) {
            if (delay == 0) {
                springFloatingActionMenu.hideMenu();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springFloatingActionMenu.hideMenu();
                    }
                }, delay);
            }
            return true;
        } else {
            if (appsRecycleView.isNeedCloseFlippedCards()) {
                boolean hasClosedFlippedCards = appsRecycleView.closeFlippedCards();
                return hasClosedFlippedCards;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onClick(final String appPackage) {
        springFloatingActionMenu.hideMenu();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( TextUtils.isEmpty(appPackage) ) {
                    Toast.makeText(getContext(), "This option don't work yet", Toast.LENGTH_LONG).show();
                    //TODO : new folder functionality
//                    final String uid = ((MainActivity) getActivity()).currentUser.getUid();
//                    FavouriteAppsFirebase.getNewFolderName(uid, new GetNewFolderNameCallback() {
//                        @Override
//                        public void newFolderName(String name) {
//                            FavouriteAppsFirebase.addFolder(uid, name);
//                        }
//                    });
                } else {
                    EventBus.getDefault().post(new OnClickAppEvent(appPackage, true));
                }
            }
        }, 450);
    }
}
