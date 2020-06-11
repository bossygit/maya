package com.nasande.pharmaciemayamaya;

import com.google.gson.annotations.SerializedName;

public class Title {

    @SerializedName("value")
    private String value;

    private String lang;

    public Title(String value) {
        this.value = value;
    }


    public String getValue() {
        return this.value;
    }
}