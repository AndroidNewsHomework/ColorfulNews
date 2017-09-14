package com.java.fourteen.utils.callback;

/**
 * Created by Lenovo on 2017/9/12.
 */

public interface UtilCallback<T, E> {
    void onSuccess(T s, E extraInfo);
}
