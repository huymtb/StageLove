package jp.stage.stagelovemaker.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.utils.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by congn on 7/11/2017.
 */

public class CommonActivity extends AppCompatActivity {
    
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void add(final BaseFragment fragment, final String tag, final boolean backstack, final boolean animation) {

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override

            public void run() {

                if (!isFinishing() && !isDestroyed()) {

                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {

                            try {

                                if (!isFinishing() && !isDestroyed()) {

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                    if (animation)

                                        transaction.setCustomAnimations(R.anim.slide_in_right,

                                                R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                                    transaction.add(R.id.flContainer, fragment, tag);

                                    if (backstack)

                                        transaction.addToBackStack(tag);

                                    transaction.commitAllowingStateLoss();

                                }

                            } catch (Exception e) {


                            }

                        }

                    });

                }

            }

        }, 200);


    }


    public void replace(final BaseFragment fragment, final String tag, final boolean backstack, final boolean animation) {

        Utils.hideSoftKeyboard(this);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override

            public void run() {

                if (!isFinishing() && !isDestroyed()) {

                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {


                            try {

                                if (!isFinishing() && !isDestroyed()) {

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                    if (animation)

                                        transaction.setCustomAnimations(R.anim.slide_in_right,

                                                R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);


                                    transaction.replace(R.id.flContainer, fragment, tag);

                                    if (backstack)

                                        transaction.addToBackStack(tag);

                                    transaction.commitAllowingStateLoss();

                                }

                            } catch (Exception e) {


                            }

                        }

                    });

                }

            }

        }, 200);

    }


    public void add(final BaseFragment fragment, final String tag, final boolean backstack, final boolean animation, final int containViewId) {

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override

            public void run() {

                if (!isFinishing() && !isDestroyed()) {

                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {

                            try {

                                if (!isFinishing() && !isDestroyed()) {

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                                    if (animation)

                                        transaction.setCustomAnimations(R.anim.slide_in_right,

                                                R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                                    transaction.add(containViewId, fragment, tag);

                                    if (backstack)

                                        transaction.addToBackStack(tag);

                                    transaction.commitAllowingStateLoss();

                                }

                            } catch (Exception e) {


                            }

                        }

                    });

                }

            }

        }, 200);

    }


    public void addNoDelayed(final BaseFragment fragment, final String tag, final boolean backstack, final boolean animation, final int containViewId) {


        try {

            if (!isFinishing() && !isDestroyed()) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                if (animation)

                    transaction.setCustomAnimations(R.anim.slide_in_right,

                            R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                transaction.add(containViewId, fragment, tag);

                if (backstack)

                    transaction.addToBackStack(tag);

                transaction.commitAllowingStateLoss();

            }

        } catch (Exception e) {


        }


    }


    public void addNoneSlideIn(final BaseFragment fragment, final String tag, final boolean backstack, final boolean animation, final int containViewId) {

        Utils.hideSoftKeyboard(this);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override

            public void run() {

                if (!isFinishing() && !isDestroyed()) {

                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {

                            try {

                                if (!isFinishing() && !isDestroyed()) {

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                    if (animation)

                                        transaction.setCustomAnimations(0,

                                                R.anim.slide_out_left, 0, R.anim.slide_out_right);

                                    transaction.add(containViewId, fragment, tag);

                                    if (backstack)

                                        transaction.addToBackStack(tag);

                                    transaction.commitAllowingStateLoss();

                                }

                            } catch (Exception e) {


                            }

                        }

                    });

                }

            }

        }, 200);

    }


    public void replace(final BaseFragment fragment, final String tag, final boolean backstack, final boolean animation, final int containViewId) {

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override

            public void run() {

                if (!isFinishing() && !isDestroyed()) {

                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {


                            try {

                                if (!isFinishing() && !isDestroyed()) {

                                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                                    if (animation)

                                        transaction.setCustomAnimations(R.anim.slide_in_right,

                                                R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);


                                    transaction.replace(containViewId, fragment, tag);

                                    if (backstack)

                                        transaction.addToBackStack(tag);

                                    transaction.commitAllowingStateLoss();

                                }

                            } catch (Exception e) {


                            }

                        }

                    });

                }

            }

        }, 200);

    }


    public void onBackPressedNotDelayed() {

        if (!isFinishing() && !isDestroyed()) {

            runOnUiThread(new Runnable() {

                @Override

                public void run() {

                    CommonActivity.super.onBackPressed();

                }

            });

        }

    }


    @Override

    public void onBackPressed() {

        Utils.hideSoftKeyboard(this);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override

            public void run() {

                if (!isFinishing() && !isDestroyed()) {

                    runOnUiThread(new Runnable() {

                        @Override

                        public void run() {

                            CommonActivity.super.onBackPressed();

                        }

                    });

                }

            }

        }, 200);


    }


    public void startNewActivity(Class<?> cls, Bundle bundle) {

        Utils.hideSoftKeyboard(this);

        Intent i = new Intent(this, cls);

        if (bundle != null) {

            i.putExtras(bundle);

        }

        this.startActivity(i);

    }

}
