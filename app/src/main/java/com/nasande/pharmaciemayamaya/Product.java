package com.nasande.pharmaciemayamaya;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price__number")
    @Expose
    private String priceNumber;
    @SerializedName("field_image")
    @Expose
    private String fieldImage;

    public Product(String title, String priceNumber) {
        this.title = title;
        this.priceNumber = priceNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceNumber() {
        return priceNumber;
    }

    public void setPriceNumber(String priceNumber) {
        this.priceNumber = priceNumber;
    }

    public String getFieldImage() {
        return fieldImage;
    }

    public void setFieldImage(String fieldImage) {
        this.fieldImage = fieldImage;
    }

}