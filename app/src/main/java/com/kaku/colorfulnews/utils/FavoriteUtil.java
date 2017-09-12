package com.kaku.colorfulnews.utils;

import com.kaku.colorfulnews.App;
import com.kaku.colorfulnews.common.Constants;
import com.kaku.colorfulnews.mvp.entity.NewsSummary;
import com.socks.library.KLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Keavil on 2017/9/12.
 */

public class FavoriteUtil {

    static private Set<String> favoriteIDs = new TreeSet<String>();

    public static void init() {
        File file = new File(App.getAppContext().getFilesDir(), Constants.FAVORITE_SAVENAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                KLog.e(e);
            }
        }
        try {
            Scanner scn = new Scanner(file);
            while (scn.hasNext()) {
                favoriteIDs.add(scn.nextLine());
            }
            scn.close();
        } catch (FileNotFoundException e) {
            KLog.e(e);
        }
        File file1 = new File(App.getAppContext().getFilesDir(), Constants.FAVORITE_SAVEDIR);
        if (!file1.exists()) {
            file1.mkdir();
        }
    }

    public void modifyFavorite(NewsSummary summary) {
        String postid = summary.getPostid();
        if (favoriteIDs.contains(postid)) {
            favoriteIDs.remove(postid);
            removeFile(summary);
        } else {
            favoriteIDs.add(postid);
            writeFile(summary);
        }
        saveIDs();
    }

    public static boolean contains(String postid) {
        return favoriteIDs.contains(postid);
    }

    public Observable<List<NewsSummary>> getObservableFavoriteList() {
        return Observable.create(new Observable.OnSubscribe<List<NewsSummary>>() {
            @Override
            public void call(Subscriber<? super List<NewsSummary>> subscriber) {
                subscriber.onNext(getFavoriteList());
                subscriber.onCompleted();
            }
        });
    }

    private List<NewsSummary> getFavoriteList() {
        List<NewsSummary> list = new LinkedList<NewsSummary>();
        for (Object id : favoriteIDs.toArray()) {
            list.add(readFile((String) id));
        }
        return list;
    }

    private void saveIDs() {
        File file = new File(App.getAppContext().getFilesDir(), Constants.FAVORITE_SAVENAME);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for (Object id : favoriteIDs.toArray()) {
                out.println((String) id);
            }
            out.close();
        } catch (IOException e) {
            KLog.e(e);
        }
    }

    private void removeFile(NewsSummary summary) {
        File file = new File(App.getAppContext().getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.matches(Constants.FAVORITE_SAVEDIR);
            }
        })[0], summary.getPostid());
        file.delete();
    }

    private void writeFile(NewsSummary summary) {
        File file = new File(App.getAppContext().getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.matches(Constants.FAVORITE_SAVEDIR);
            }
        })[0], summary.getPostid());
        try {
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(summary);
            oos.close();
            fout.close();
        } catch (IOException e) {
            KLog.e(e);
        }
    }

    private NewsSummary readFile(String postid) {
        File file = new File(App.getAppContext().getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.matches(Constants.FAVORITE_SAVEDIR);
            }
        })[0], postid);
        NewsSummary summary = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fin);
            summary = (NewsSummary) oos.readObject();
            oos.close();
            fin.close();
        } catch (IOException e) {
            KLog.e(e);
        } catch (ClassNotFoundException e) {
            KLog.e(e);
        }
        KLog.a(summary);
        return summary;
    }
}
