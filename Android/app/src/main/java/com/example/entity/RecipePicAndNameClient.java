package com.example.entity;

public class RecipePicAndNameClient {
    private String name;
    private byte[] pic;

    public RecipePicAndNameClient() {
    }

    public RecipePicAndNameClient(String name, byte[] pic) {
        this.name = name;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }
}
