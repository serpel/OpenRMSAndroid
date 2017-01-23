package com.intellisysla.rmsscanner.DAL;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by alienware on 12/9/2016.
 */

public class Item implements Serializable {
    private String item_code, description, departmentName, categoryName;
    private int id, quantity, fisic_quatity, departpment_id, categoryId;
    private float price, offer;
    private Date salesStartDate, salesEndDate;
    private boolean isOfferActive;

    public Item(int id, String item_code, String description, int departpment_id,
                String departmentName, int categoryId, String categoryName, int quantity,
                int fisic_quantity, float price, float offer, Date salesStartDate, Date salesEndDate,
                boolean isOfferActive) {
        this.id = id;
        this.item_code = item_code;
        this.description = description;
        this.departpment_id = departpment_id;
        this.departmentName = departmentName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.fisic_quatity = fisic_quantity;
        this.price = price;
        this.offer = offer;
        this.salesStartDate = salesStartDate;
        this.salesEndDate = salesEndDate;
        this.isOfferActive = isOfferActive;
    }

    public int getId() {
        return id;
    }

    public boolean isOfferActive() {
        return isOfferActive;
    }

    public void setOfferActive(boolean offerActive) {
        isOfferActive = offerActive;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFisic_quatity() {
        return fisic_quatity;
    }

    public void setFisic_quatity(int fisic_quatity) {
        this.fisic_quatity = fisic_quatity;
    }

    public int getDepartpment_id() {
        return departpment_id;
    }

    public void setDepartpment_id(int departpment_id) {
        this.departpment_id = departpment_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getOffer() {
        return offer;
    }

    public void setOffer(float offer) {
        this.offer = offer;
    }

    public Date getSalesStartDate() {
        return salesStartDate;
    }

    public void setSalesStartDate(Date salesStartDate) {
        this.salesStartDate = salesStartDate;
    }

    public Date getSalesEndDate() {
        return salesEndDate;
    }

    public void setSalesEndDate(Date salesEndDate) {
        this.salesEndDate = salesEndDate;
    }
}
