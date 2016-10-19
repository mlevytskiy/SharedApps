package wumf.com.sharedapps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import wumf.com.sharedapps.eventbus.GetNewCountryEvent;
import wumf.com.sharedapps.eventbus.NewCountryCodeFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.NewPhoneNumberFromViber;
import wumf.com.sharedapps.eventbus.SignInFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.SignOutFromFirebaseEvent;
import wumf.com.sharedapps.eventbus.WeAlreadyGetCountryCodeFromSystemEvent;
import wumf.com.sharedapps.firebase.UsersFirebase;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public static final int REQUEST_CODE_CHIOCE_APP = 544;
    public static final int REQUEST_CODE_RC_SIGN_IN = 543;
    public static final String PACKAGE_NAME = "packageName";
    private static final String TAG = MainActivity.class.getSimpleName();

    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private int currentFragmentIndex = 0;
    public GoogleApiClient gac;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseUser currentUser;
    private boolean weAlreadyGetCountryCodeFromSystem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = (AppBarLayout) findViewById(R.id.tabanim_appbar);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragmentIndex = position;
                for (int i = 0; i < adapter.mFragmentList.size(); i++) {
                    IHideShow fr = (IHideShow) adapter.mFragmentList.get(i);
                    if (i == position) {
                        fr.show();
                    } else {
                        fr.hide();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        RealmRecyclerView realmRecyclerView = (RealmRecyclerView) findViewById(R.id.realm_recycler_view);

        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, ((MainApplication) getApplication()).gso)
                .addApi(LocationServices.API)
                .build();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                currentUser = user;
                if (user != null) {
                    UsersFirebase.listenPhoneNumber(currentUser.getUid());
                    UsersFirebase.listenCountryCode(currentUser.getUid());
                }
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        gac.connect();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        gac.disconnect();
        super.onStop();
        EventBus.getDefault().unregister(this);
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Subscribe
    public void onEvent(WeAlreadyGetCountryCodeFromSystemEvent event) {
        weAlreadyGetCountryCodeFromSystem = true;
    }

    public void onEvent(NewCountryCodeFromFirebaseEvent event) {
        if ( weAlreadyGetCountryCodeFromSystem && TextUtils.isEmpty(event.countryCode) ) {
            UsersFirebase.updateCountryCode(currentUser.getUid(), MainApplication.instance.country);
            EventBus.getDefault().post(new GetNewCountryEvent(MainApplication.instance.country));
        } else {
            //do nothing
        }
    }

    @Subscribe
    public void onEvent(NewPhoneNumberFromViber event) {
        UsersFirebase.updatePhoneNumber(currentUser.getUid(), event.phone);
    }

    @Subscribe
    public void onEvent(SignOutFromFirebaseEvent event) {
        currentUser = null;
        MainApplication.instance.phoneNumber = null;
        Auth.GoogleSignInApi.signOut(gac);
    }

    @Override
    public void onBackPressed() {
        boolean doBack = ((OnBackPressedListener) adapter.mFragmentList.get(currentFragmentIndex)).doBack();
        if (!doBack) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_CHIOCE_APP) {
            if (resultCode == RESULT_OK) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((OnBackPressedListener) adapter.mFragmentList.get(currentFragmentIndex)).doBack();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String packageName = data.getExtras().getString(PACKAGE_NAME);
                                Toast.makeText(MainActivity.this, packageName, Toast.LENGTH_LONG).show();
                            }
                        }, 300);
                    }
                }, 300);
            } else {
                //do nothing
            }
        } else if (requestCode == REQUEST_CODE_RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
            }
            Toast.makeText(this, "onActivityResult", Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());
                        EventBus.getDefault().post(new SignInFromFirebaseEvent());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SharedAppsFragment(), getTabTitle(R.drawable.ic_heart));
        adapter.addFrag(new PersonFragment(), getTabTitle(R.drawable.ic_person));
        adapter.addFrag(new SearchFragment(), getTabTitle(R.drawable.ic_search));
        viewPager.setAdapter(adapter);
    }

    private CharSequence getTabTitle(@DrawableRes int drawableResId) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(" ");
        Drawable dr = getResources().getDrawable(drawableResId);
//        int color = getResources().getColor(R.color.photo);
//        dr.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(dr, DynamicDrawableSpan.ALIGN_BOTTOM);
        stringBuilder.setSpan(imageSpan, 0, 1, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed=" + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Location location = LocationServices.FusedLocationApi.getLastLocation(gac);
                Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if ( !addresses.isEmpty() ) {
                        MainApplication.instance.country = addresses.get(0).getCountryCode();
                        FirebaseUser user = MainActivity.this.currentUser;
                        if (user == null) {
                            EventBus.getDefault().post(new WeAlreadyGetCountryCodeFromSystemEvent());
                        } else {
                            UsersFirebase.updateCountryCode(user.getUid(), MainApplication.instance.country);
                            EventBus.getDefault().post(new GetNewCountryEvent(MainApplication.instance.country));
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<CharSequence> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, CharSequence title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
