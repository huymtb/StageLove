package jp.stage.stagelovemaker.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Hashtable;

/**
 * Created by congn on 7/31/2017.
 */

public class UserTypeFace {
    public static final String MISSION;
    public static final String OPENSANS;
    public static final String BOLD;
    public static final String MEDIUM;
    public static final String REGULAR;
    public static final String SEMIBOLD;

    static {
        MISSION = "fonts/Mission-Script.otf";
        OPENSANS = "fonts/OpenSans-Bold.ttf";
        BOLD = "fonts/proximanovasoft-bold.otf";
        MEDIUM = "fonts/proximanovasoft-medium.otf";
        REGULAR = "fonts/proximanovasoft-regular.otf";
        SEMIBOLD = "fonts/proximanovasoft-semibold.otf";
    }

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    private static Typeface getTypeFace(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface typeFace = Typeface.createFromAsset(
                            context.getAssets(), assetPath);
                    cache.put(assetPath, typeFace);
                } catch (Exception e) {
                    Log.e("TypeFaces", "Typeface not loaded.");
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }

    public static void setOpensans(TextView obj) {
        obj.setTypeface(getTypeFace(obj.getContext(), OPENSANS), Typeface.NORMAL);
    }


    public static void setMission(TextView obj) {

        obj.setTypeface(getTypeFace(obj.getContext(), MISSION), Typeface.NORMAL);
    }


    public static void setBold(TextView obj) {
        obj.setTypeface(getTypeFace(obj.getContext(), BOLD), Typeface.NORMAL);
    }

    public static void setSemibold(TextView obj) {
        obj.setTypeface(getTypeFace(obj.getContext(), SEMIBOLD), Typeface.NORMAL);
    }

    public static void setRegular(TextView obj) {
        obj.setTypeface(getTypeFace(obj.getContext(), REGULAR), Typeface.NORMAL);
    }

    public static void setMedium(TextView obj) {
        obj.setTypeface(getTypeFace(obj.getContext(), MEDIUM), Typeface.BOLD);
    }

    public static Typeface getRegular(View obj) {
        return getTypeFace(obj.getContext(), REGULAR);
    }
}
