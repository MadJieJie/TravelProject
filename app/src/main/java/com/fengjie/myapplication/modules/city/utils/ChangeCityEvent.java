package com.fengjie.myapplication.modules.city.utils;

public class ChangeCityEvent
{

    String city;
    boolean isSetting;

    public ChangeCityEvent () {
    }

    public ChangeCityEvent ( boolean isSetting) {
        this.isSetting = isSetting;
    }

    public ChangeCityEvent ( String city) {
        this.city = city;
    }
}
