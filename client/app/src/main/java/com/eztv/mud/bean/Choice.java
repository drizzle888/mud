package com.eztv.mud.bean;

import com.ez.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

public class Choice implements Cloneable{

    private Enum.winAction action= Enum.winAction.close;
    private Enum.messageType type;
    private String name;
    private String cmd;
    private String msg;
    private String key;
    private Enum.color color;

    public String getName() {
        return name;
    }

    public static Choice createChoice(String name,Enum.messageType type, String cmd, String msg, String key){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        c.setType(type);
        c.setKey(key);
        return c;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Enum.messageType getType() {
        return type;
    }

    public void setType(Enum.messageType type) {
        this.type = type;
    }
    public String getKey() {
        return key;
    }

    public Enum.color getColor() {
        return color;
    }

    public void setColor(Enum.color color) {
        this.color = color;
    }

    public Enum.winAction getAction() {
        return action;
    }

    public void setAction(Enum.winAction action) {
        this.action = action;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
