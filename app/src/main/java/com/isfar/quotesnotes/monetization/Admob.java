package com.isfar.quotesnotes.monetization;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.isfar.quotesnotes.R;

public class Admob {

    public static Boolean IS_AD_ON = false;
    private static InterstitialAd mInterstitialAd;
    private static int clickCount = 2;


    //========= Loading Banner Ad =========================================================
    public static void setBannerAd(LinearLayout adContainerView, Context context) {

        if (!IS_AD_ON) return;

        AdView adView = new AdView(context);
        adView.setAdUnitId(context.getString(R.string.BANNER_AD_UNIT_ID));

        AdSize adSize = getAdSize(context);
        adView.setAdSize(adSize);

        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }// set banner Ad end...


    //========= Banner Ad Size =========================================================
    public static AdSize getAdSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int adWidthPixels = displayMetrics.widthPixels;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getCurrentWindowMetrics();
            adWidthPixels = windowMetrics.getBounds().width();
        }

        float density = displayMetrics.density;
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);

    }// banner size end...


    //========= showing Interstitial Ad in every 3 times =====================================
    public static void showInterstitialAdinThreeClick(Activity activity) {

        if (!IS_AD_ON) return;

        clickCount++;

        if (clickCount >= 3) {
            clickCount = 0;

            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
                mInterstitialAd = null; // Clear ad after showing
                loadInterstitialAd(activity); // Preload next ad
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                loadInterstitialAd(activity);
            }

        }

    }// show interstitial Ad in Three Click end.....


    //========= Loading Interstitial Ad =========================================================
    public static void loadInterstitialAd(Activity activity) {

        if (!IS_AD_ON) return;

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity,activity.getString(R.string.INTERSTITIAL_AD_UNIT_ID), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;

                        // interstitial Ad callback....
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                loadInterstitialAd(activity);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                            }

                        });

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });

    }// load interstitial ad end....



}// public class end....
