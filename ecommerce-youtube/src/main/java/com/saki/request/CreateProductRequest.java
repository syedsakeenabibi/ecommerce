package com.saki.request;

import java.util.HashSet;
import java.util.Set;

import com.saki.model.Size;

public class CreateProductRequest {
    
    private String title;
    private String description;
    private String price;
    private String discountPrice;
    private String discountPresent;
    private String quantity;
    private String brand;
    private String color;
    private Set<Size> size = new HashSet<>();
    private String imageUrl;
    
    //men/clothing/mens_shirt
    private String topLavelCategory;
    private String secondLavelCategory;
    private String thirdlavelCategory;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountPresent() {
        return discountPresent;
    }

    public void setDiscountPresent(String discountPresent) {
        this.discountPresent = discountPresent;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Size> getSize() {
        return size;
    }

    public void setSize(Set<Size> size) {
        this.size = size;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTopLavelCategory() {
        return topLavelCategory;
    }

    public void setTopLavelCategory(String topLavelCategory) {
        this.topLavelCategory = topLavelCategory;
    }

    public String getSecondLavelCategory() {
        return secondLavelCategory;
    }

    public void setSecondLavelCategory(String secondLavelCategory) {
        this.secondLavelCategory = secondLavelCategory;
    }

    public String getThirdlavelCategory() {
        return thirdlavelCategory;
    }

    public void setThirdlavelCategory(String thirdlavelCategory) {
        this.thirdlavelCategory = thirdlavelCategory;
    }
}
