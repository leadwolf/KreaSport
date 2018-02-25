package com.ccaroni.kreasport.legacy.view.activities.entry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.utils.CustomViewPager;
import com.ccaroni.kreasport.utils.PermissionsManager;
import com.ccaroni.kreasport.utils.PreferenceManager;

public class WelcomeActivity extends AppCompatActivity {

    private final static String PREF_NAME = "intro_slider-welcome";

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    private CustomViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] screenLayouts;
    private Button btnSkip, btnNext;

    private PreferenceManager prefManager;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PreferenceManager(this, PREF_NAME);
        permissionsManager = new PermissionsManager(this);

        checkSkip();

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);
        setup();

        skipIntro();
    }

    /**
     * Skip this activity if not first time and the permissions are already granted
     */
    private void checkSkip() {
        boolean hasWriteAccess = permissionsManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean hasLocationAccess = permissionsManager.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION);
        if (!prefManager.isFirstTimeLaunch() && hasWriteAccess && hasLocationAccess) {
            launchHomeScreen();
        }
    }

    /**
     * Initialises the view fields, viewpager and buttons
     */
    private void setup() {
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        // add few more screenLayouts if you want
        screenLayouts = new int[]{
                R.layout.slide_screen1,
                R.layout.slide_screen2,
                R.layout.slide_screen3,
                R.layout.slide_screen4,
                R.layout.slide_screen5,
                R.layout.slide_screen6};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        setupViewPager();
        initButtons();
    }

    /**
     * Sets up the viewpager and pageChangeListener to change the buttons according to granted permissions
     */
    private void setupViewPager() {
        viewPager.setAdapter(new MyViewPagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

                if (position == screenLayouts.length - 1) {
                    btnNext.setText(getString(R.string.start));
                } else if (position == screenLayouts.length - 2) {
                    btnSkip.setVisibility(View.GONE);
                    toggleProgressionPossible(permissionsManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                } else if (position == screenLayouts.length - 3) {
                    btnSkip.setVisibility(View.GONE);
                    toggleProgressionPossible(permissionsManager.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION));
                } else {
                    // still pages are left
                    btnNext.setText(getString(R.string.next));
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * Initialies the "skip" and "next" buttons
     */
    private void initButtons() {
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                launchHomeScreen();
//                Go to first permission request screen
                viewPager.setCurrentItem(screenLayouts.length - 3);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if second to last or last, will ask for permission
                int currentPage = viewPager.getCurrentItem();
                if (currentPage < screenLayouts.length - 3) {
                    // move to next screen
                    viewPager.setCurrentItem(currentPage + 1);
                } else if (currentPage == screenLayouts.length - 3) {
                    requestOrSelectNext(Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionsManager.CODE_WRITE_EXTERNAL_STORAGE, currentPage + 1);
                } else if (currentPage == screenLayouts.length - 2) {
                    requestOrSelectNext(Manifest.permission.ACCESS_FINE_LOCATION, PermissionsManager.CODE_ACCESS_FINE_LOCATION, currentPage + 1);
                } else if (currentPage == screenLayouts.length - 1) {
                    launchHomeScreen();
                }
            }
        });
    }

    /**
     * Skips the first 3 pages if this is not the first launch
     */
    private void skipIntro() {
        if (!prefManager.isFirstTimeLaunch()) {
            if (!permissionsManager.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                viewPager.setCurrentItem(screenLayouts.length - 3);
            } else {
                viewPager.setCurrentItem(screenLayouts.length - 2);
            }
        }
    }

    /**
     * Requests permission or sets {@link #viewPager} to index
     *
     * @param permission  the permission to check/request
     * @param requestCode the code to use for the permission request
     * @param index       the page to set to if the permission is granted
     */
    private void requestOrSelectNext(String permission, int requestCode, int index) {
        if (!permissionsManager.isPermissionGranted(permission))
            permissionsManager.requestPermission(requestCode);
        else
            viewPager.setCurrentItem(index);
    }

    /**
     * Adds bottom dots to the page and currentPage index
     *
     * @param currentPage the index to add the dots to
     */
    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[screenLayouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionsManager.CODE_ACCESS_FINE_LOCATION:
            case PermissionsManager.CODE_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toggleProgressionPossible(true);
                }
            default:
                Log.d(TAG, "onRequestPermissionsResult: receded permission request result for unknown permission");
                break;
        }
    }

    /**
     * Toggles the swiping according to enable and set the "next" button text to either "next" or "grant"
     *
     * @param enable if swiping should be enabled
     */
    private void toggleProgressionPossible(boolean enable) {
        if (enable) {
            viewPager.setPagingEnabled(true);
            btnNext.setText(getString(R.string.next));
        } else {
            viewPager.setPagingEnabled(true);
            btnNext.setText(getString(R.string.grant));
        }
    }

    /**
     * View pager adapter that uses our custom screen layouts: {@link #screenLayouts}
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(screenLayouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return screenLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
