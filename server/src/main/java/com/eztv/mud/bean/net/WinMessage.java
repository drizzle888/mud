package com.eztv.mud.bean.net;

import com.eztv.mud.LuaUtil;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.script.LuaOpen;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.colorString;

public class WinMessage implements LuaOpen.LuaWin{
    private List<String> floatMessage = new ArrayList<>();
    private String desc;
    private List<Choice> choice = new ArrayList<>();
    private int col=3;


    public List<String> getFloatMessage() {
        return floatMessage;
    }

    public void setFloatMessage(List<String> floatMessage) {
        this.floatMessage = floatMessage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Choice> getChoice() {
        return choice;
    }

    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void 添加选项集合(LuaUtil luaUtil){
        this.choice.addAll(luaUtil.getChoice());
        luaUtil.getChoice().clear();
    }

    @Override
    public void 内容(String string) {
        this.desc = colorString(string) ;
    }

    @Override
    public void 列数(int col) {
        this.col = col;
    }

    public void 添加选项(Choice c){
        this.choice.add(c);
    }


}
