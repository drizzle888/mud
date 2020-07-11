package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.Enum.*;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.RoomDetail;
import com.eztv.mud.Word;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.handler.core.Battle;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.bean.Cmd.*;

public class GameHandler {
    public static void getMapDetail(Client client) {//查看房间
        String roomId = getCurRoomId(client);
        client.getPlayer().getPlayerData().setRoom(roomId);//进入房间
        Word.getInstance().getRooms().get(roomId+"").add(client.getPlayer());//清除其他房间自己的镜像
        RoomDetail room = new RoomDetail();
        room.setNpcList(getRoom(roomId).getNpcList());
        room.setMonsterList(getRoom(roomId).getMonsterList());
        List<GameObject> list = getRoom(roomId).getPlayerList();
        list.remove(client.getPlayer());
        room.setPlayerList(list);
        room.setName(getRoom(roomId).getName());
        room.setDown( getRoomName(getRoom(roomId).getDown()));
        room.setLeft( getRoomName(getRoom(roomId).getLeft()));
        room.setRight( getRoomName(getRoom(roomId).getRight()));
        room.setTop( getRoomName(getRoom(roomId).getTop()));
        client.sendMsg(msgBuild(messageType.action, getMapDetail,object2JsonStr(room),client.getRole()).getBytes());
        list.add(client.getPlayer());
        room.setPlayerList(list);
        //通知其他玩家；
        onObjectInRoom(roomId,client.getPlayer(),client);
    }

    private static void onObjectInRoom(String roomId, GameObject obj,Client client){
        for (Client item: clients) {
            try {
                if(item.getPlayer().getPlayerData().getRoom().equals(roomId)&&item!=client){
                    if (obj instanceof Monster)
                    item.sendMsg(msgBuild(messageType.normal, onObjectInRoom,object2JsonStr(obj),"Monster").getBytes());
                    if (obj instanceof Npc)
                        item.sendMsg(msgBuild(messageType.normal, onObjectInRoom,object2JsonStr(obj),"Npc").getBytes());
                    if (obj instanceof Player)
                        item.sendMsg(msgBuild(messageType.normal, onObjectInRoom,object2JsonStr(obj),"Player").getBytes());
                }
            }catch (Exception e){}
        }
    }

    public static void onObjectOutRoom(String roomId, Player player){
        if(roomId==null)roomId=DEFAULT_ROOM_ID;
        Word.getInstance().getRooms().get(roomId+"").remove(player);
        for (Client item: clients) {
            try {
            if(item.getPlayer().getPlayerData().getRoom().equals(roomId)&&item.getPlayer()!=player)
                item.sendMsg(msgBuild(messageType.normal, onObjectOutRoom,object2JsonStr(player),null).getBytes());
            }catch (Exception e){}
        }
    }

    public static void playerMove(Client client, Msg msg) {//玩家移动模块
        GameHandler.onObjectOutRoom(client.getPlayer().getPlayerData().getRoom(),client.getPlayer());
        switch (msg.getMsg()){
            case "left":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getLeft()+"");
                break;
            case "right":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getRight()+"");
                break;
            case "top":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getTop()+"");
                break;
            case "down":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getDown()+"");
                break;
        }
        getMapDetail(client);
    }



    public static void doChat(Client client, Msg msg) {
        Chat chat = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), Chat.class);
        for (Client item: clients) {
            item.sendMsg(msgBuild(messageType.chat, "chat",object2JsonStr(chat),"").getBytes());
        }
    }
    //获取玩家属性
    public static void getAttribute(Client client) {
        client.sendMsg(msgBuild(messageType.action, getAttribute,object2JsonStr(client.getPlayer().getPlayerData().getAttribute()),client.getRole()).getBytes());
    }

    //玩家攻击
    public static void doAttack(Client client,Msg msg) {
        AttackPack attackPack = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), AttackPack.class);
        if(attackPack.getTargetType()==gameObjectType.monster){
            GameObject obj = getGameObject(gameObjectType.monster,Word.getInstance().getRooms().get(getCurRoomId(client)).getMonsterList(),attackPack.getTarget());
            client.getPlayer().setBattle(client,client.getPlayer(),obj,attackPack.getTarget());
        }
    }


    public static void getGG(Client client) {
        Chat chat = new Chat(Enum.chat.系统,Word.getInstance().getGG());
        client.sendMsg(msgBuild(messageType.normal, getGG,object2JsonStr(chat),"").getBytes());
    }

    public static void doTalk(Client client, Msg msg) {
        GameObject gameObject=null;
        WinMessage winMsg = new WinMessage();
        List<GameObject> list = new ArrayList<>();
        list.addAll(getCurRoom(client).getMonsterList());
        list.addAll(getCurRoom(client).getNpcList());
        list.addAll(getCurRoom(client).getPlayerList());
        for (GameObject o:list) {
            if(o.getKey().equals(msg.getMsg())){
                gameObject = o;
                break;
            }
        }
        if(gameObject==null)return;
        List<Choice> choice = new ArrayList<>();
        if(gameObject instanceof Player){//是玩家
            choice.add(Choice.createChoice("对话", "chat", ""));
            winMsg.setChoice(choice);
            winMsg.setDesc(((Player) gameObject).getName()+"</p><br>&emsp;"+"这是一位凶神恶煞的玩家");
        }else{
            choice.add(Choice.createChoice("攻击", "attack", ""));
            winMsg.setChoice(choice);
            winMsg.setDesc(gameObject.getName()+"</p><br>&emsp;"+(gameObject.getDesc()==null?"":gameObject.getDesc()));
        }
        client.sendMsg(msgBuild(messageType.action, doTAlk,object2JsonStr(winMsg),gameObject.getKey()).getBytes());
    }
}
