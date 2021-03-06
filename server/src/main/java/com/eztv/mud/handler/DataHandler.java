package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.PlayerData;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDebug;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.Init_PATH;
import static com.eztv.mud.Constant.脚本_初始化;
import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class DataHandler {
    static Gson gson = new GsonBuilder().
            registerTypeAdapter(Date.class,
                    (JsonDeserializer<Date>)(json, typeOfT, context)->
                            new Date(json.getAsJsonPrimitive().getAsLong())
            ).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //获取角色信息。绑定游戏数据专用函数
    public synchronized static void getPlayer(Client client, Player player) {
        if (client != null) {
            try {
                String data = new String(Base64.getDecoder().decode(player.getData()));
                PlayerData pd = gson.fromJson(data, PlayerData.class);
                player.setPlayerData(pd);
            } catch (Exception e) {
                e.printStackTrace();
                BDebug.trace("玩家没有数据:" + e.toString());
            }
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            if (player.getAttribute().getHp() < 1) {//玩家身上没有信息
                base.setHp(base.getHp_max());
                base.setMp(base.getMp_max());
                if (player.getLevel() > 1)
                    base.setExp(0);
            } else {
                base.setHp(player.getAttribute().getHp());
                base.setMp(player.getAttribute().getMp());
                base.setExp(player.getAttribute().getExp());
            }
            //这里可以绑定玩家的一些属性
            player.getPlayerData().setAttribute(base);
            client.getScriptExecutor().load(Init_PATH+"player").
                    execute(脚本_初始化,player);
        }
    }

    //获取角色信息。绑定游戏数据专用函数
    public static Player getPlayerByUpLevel(Object client, Player player) {
        if (client != null) {
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            base.setHp(base.getHp_max());
            base.setMp(base.getMp_max());
            if (player.getLevel() < 2)
                base.setExp(0);
            player.getPlayerData().setAttribute(base);
        }
        return player;
    }


    //获取角色信息。绑定游戏数据专用函数
    public static void sendReward(Client client, List<String> list) {
        WinMessage winMessage = new WinMessage();
        winMessage.setFloatMessage(list);
        client.sendMsg(msgBuild(Enum.messageType.action, "reward", object2JsonStr(winMessage), null));
    }

    //获取基础属性//不包括当前血量等等
    public static Attribute getBaseAttribute(int level) {
        HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
        Attribute base = attributes.get(level + "");
        Attribute attribute = new Attribute();
        return attribute.add(base);
    }

}
