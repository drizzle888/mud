package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Client;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.utils.BDebug;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class DataHandler {

    //获取角色信息。绑定游戏数据专用函数
    public static Player getPlayer(Object client , Player player) {
        if (client != null) {
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            if (player.getAttribute().getHp() < 1) {//玩家身上没有信息
                Attribute attribute = new Attribute();
                attribute.setHp(base.getHp_max());
                attribute.setMp(base.getMp_max());
                if(player.getLevel()>1)
                attribute.setExp(0);
                attribute.setHp_max(base.getHp_max());
                attribute.setExp_max(base.getExp_max());
                attribute.setMp_max(base.getMp_max());
                attribute.setAck(base.getAck());
                player.getPlayerData().setAttribute(attribute);
            }
        }
        return player;
    }

    //获取角色信息。绑定游戏数据专用函数
    public static Player getPlayerByUpLevel(Object client , Player player) {
        if (client != null) {
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            Attribute attribute = new Attribute();
            attribute.setHp(base.getHp_max());
            attribute.setMp(base.getMp_max());
            if(player.getLevel()<2)
                attribute.setExp(0);
            attribute.setHp_max(base.getHp_max());
            attribute.setExp_max(base.getExp_max());
            attribute.setMp_max(base.getMp_max());
            attribute.setAck(base.getAck());
            player.getPlayerData().setAttribute(attribute);
        }
        return player;
    }


    //获取角色信息。绑定游戏数据专用函数
    public static void sendReward(Client client , List<String> list) {
        WinMessage winMessage = new WinMessage();
        winMessage.setFloatMessage(list);
        client.sendMsg(msgBuild(Enum.messageType.action,"reward",object2JsonStr(winMessage),null));
    }

    //获取基础属性//不包括当前血量等等
    public static Attribute getBaseAttribute( int level) {
        HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
        Attribute base = attributes.get(level + "");
        Attribute attribute = new Attribute();
        return attribute.add(base);
    }

}
