package com.eztv.mud.bean;

public class PlayerData {
    private String room;
    private Attribute attribute = new Attribute();


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
