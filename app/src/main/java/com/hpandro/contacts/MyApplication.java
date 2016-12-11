package com.hpandro.contacts;

import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.hpandro.contacts.utils.UserBlankAvatar;


/**
 * Created by hpAndro on 12/8/2016.
 */
public class MyApplication extends MultiDexApplication {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    public static MyApplication instance;
    public static Typeface semiboldFont, lightFont, regularFont, boldFont;
    int[] userBlankColors;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userBlankColors = getResources().getIntArray(R.array.userblankavatar);
        instance = this;
    }

    public void setBlankAvatar(UserBlankAvatar avatar, String name, boolean isCircle) {
        if (TextUtils.isEmpty(name))
            return;

        char firstChar = name.charAt(0);
        boolean isDigit = ((firstChar >= '0') && (firstChar <= '9'));

        if (isDigit) {
            //this is black bg
            avatar.setColor(userBlankColors[userBlankColors.length - 1]);
            avatar.setName("#");
        } else {
            //get first char position with alphabet
            String upperName = name.toUpperCase();

            char first = upperName.charAt(0);

            int position = first - 'A';

            if (position < 0 || position > userBlankColors.length - 1) {
                position = userBlankColors.length - 1;
                first = '#';
            }

            avatar.setIsCircle(isCircle);
            avatar.setName(String.valueOf(first));
            avatar.setColor(userBlankColors[position]);
        }
    }
}