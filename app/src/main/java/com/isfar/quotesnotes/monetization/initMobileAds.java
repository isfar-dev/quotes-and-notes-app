package com.isfar.quotesnotes.monetization;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.MobileAds;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.concurrent.atomic.AtomicBoolean;


public class initMobileAds {
    static ConsentInformation consentInformation;
    static AtomicBoolean isAdSdkCalled = new AtomicBoolean(false);


    ////------------------------------------------------------->>>
    public static void requestConsentForm(Activity context) {

        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(context)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("250D2D9C45DD3BDA3B3288F8F2F2DD31")
                .build();


        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                //Comment the next line to turn off testing
                //.setConsentDebugSettings(debugSettings)
                .setTagForUnderAgeOfConsent(false)
                .build();


        consentInformation = UserMessagingPlatform.getConsentInformation(context);
        consentInformation.requestConsentInfoUpdate(
                context,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            context,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.d("GdprJuba", String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered. Now init Ads SDK
                                if (consentInformation.canRequestAds()) {
                                    initMobileAdsSDK(context);
                                }
                            }


                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w("GdprJuba", String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });


        if (consentInformation.canRequestAds()) {
            initMobileAdsSDK(context);
        }




    }

    // End of requestConsentForm()




    //-----------------------------------------------------
    //-----------------------------------------------------
    private static void initMobileAdsSDK(Context context) {
        // Don't init if sdk already initialized
        if (isAdSdkCalled.getAndSet(true)) {
            return;
        }

        //Init MobileAds SDK here
        new Thread(
                () -> {
                    MobileAds.initialize(context, initializationStatus -> {
                    });
                })
                .start();

        //If you are testing then uncomment the line below
        //consentInformation.reset();

    }
    //-----------------------------------------------------
    //-----------------------------------------------------


}
