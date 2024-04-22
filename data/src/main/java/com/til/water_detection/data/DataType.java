package com.til.water_detection.data;

public final class DataType {

    private int id;
    private int userID;
    private String anotherName;


    public DataType(int id, int userID, String anotherName) {
        this.id = id;
        this.userID = userID;
        this.anotherName = anotherName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAnotherName() {
        return anotherName;
    }

    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }
}
