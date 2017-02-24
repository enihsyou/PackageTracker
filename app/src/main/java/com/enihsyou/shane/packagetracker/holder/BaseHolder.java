package com.enihsyou.shane.packagetracker.holder;

public abstract class BaseHolder<T> {
    private static final String TAG = "BaseHolder";

    public abstract void bindItem(T entry);
}
