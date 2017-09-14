package com.java.fourteen.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.java.fourteen.App;
import com.java.fourteen.common.Constants;
import com.java.fourteen.mvp.entity.NewsSummary;

/**
 * Created by Hob Den on 2017/9/12.
 */

public class BanwordNewsDetailFilter {
    private static final BanwordNewsDetailFilter ourInstance = new BanwordNewsDetailFilter();

    public static BanwordNewsDetailFilter getInstance() {
        return ourInstance;
    }
    private SharedPreferences sharedPreferences;
    private String[] banwords;

    private BanwordNewsDetailFilter() {
        sharedPreferences = App.getAppContext().getSharedPreferences(
                Constants.SHARES_COLOURFUL_NEWS, Context.MODE_PRIVATE);
        banwords = sharedPreferences.getString(Constants.BANWORD_LIST, "").split(";\\s*");
    }

    public String getBanwordRaw() {
        return sharedPreferences.getString(Constants.BANWORD_LIST, "");
    }

    public void setBanwordRaw(String rawStr) {
        banwords = rawStr.split(";\\s*");
        for (int i = 0; i < banwords.length; i++) {
            if (banwords[i] == null || banwords[i] == "") {
                for (int j = i + 1; j < banwords.length; j++)
                    banwords[j-1] = banwords[j];
                continue;
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.BANWORD_LIST, rawStr);
        editor.apply();
    }

    public boolean isBanned(NewsSummary ns) {
        for (String bw : banwords) {
            if (bw != null && bw.length() > 0 && ns.getTitle().contains(bw)) {
                return true;
            }
        }
        return false;
    }
}
