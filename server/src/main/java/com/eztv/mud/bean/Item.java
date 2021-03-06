package com.eztv.mud.bean;

import com.eztv.mud.constant.Enum;
import com.eztv.mud.script.LuaOpen;

import static com.eztv.mud.GameUtil.colorString;
import static com.eztv.mud.GameUtil.getProp;


public class Item extends GameObject implements Cloneable, LuaOpen.LuaItem {
    private int num;
    private Enum.itemType type;
    private Enum.equipType equipType;
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    public Enum.itemType getType() {
        return type;
    }

    public void setType(Enum.itemType type) {
        this.type = type;
    }


    public String toDesc(Enum.itemType type){
        String str="";
        switch (type){
            case equip:
                str+= getProp("equip_detail_hit",getAttribute().getAtk());
                break;
        }
        return str;
    }

    public Enum.equipType getEquipType() {
        return equipType;
    }

    public void setEquipType(Enum.equipType equipType) {
        this.equipType = equipType;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false ;
        }
        if (obj instanceof Item){
            Item item = (Item) obj;
            if(item.getId() == getId()&&item.getType()==((Item) obj).getType()){
                return true ;
            }
        }
        return false ;
    }

    @Override
    public void 类型(String type) {
        this.equipType = Enum.equipType.valueOf(type);
    }

    @Override
    public void 属性(Attribute attribute) {
        setAttribute(attribute);
    }

    @Override
    public void 内容(String string) {
        setDesc(colorString(string));
    }

    @Override
    public String 到文本(String type) {
        String str="";
        Enum.itemType itemType = Enum.itemType.valueOf(type);
        switch (itemType){
            case equip:
                str+= getProp("equip_detail_hit",getAttribute().getAtk());
                break;
        }
        return str;
    }
}
