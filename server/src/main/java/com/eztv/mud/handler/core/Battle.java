package com.eztv.mud.handler.core;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.utils.BObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.eztv.mud.Constant.FIGHT_SPEED;
import static com.eztv.mud.GameUtil.*;


public class Battle implements ActionListener {
    private GameObject who;
    private GameObject target;
    private Timer timer = new Timer(FIGHT_SPEED, null);
    private Client client;
    private String targetKey;

    public Battle() {
    }

    public void destroy(){
        if(this.timer.isRunning())this.timer.stop();
    }

    public Battle(Client client, GameObject who, GameObject target, String targetKey) {
        init(client,who,target,targetKey);
    }
    public void init(Client client, GameObject who, GameObject target, String targetKey){
        if(this.timer!=null)destroy();
        this.client = client;
        this.targetKey = targetKey;
        this.who = who;
        this.target = target;
        if(initFail()) return;
        this.timer = new Timer(FIGHT_SPEED, this);
        this.timer.start();
    }

    private boolean initFail() {
        boolean noFind=false;//玩家和目标不同地图
        try {
            if(target instanceof Player){
                noFind = !client.getPlayer().getPlayerData().getRoom().equals(((Player)target).getPlayerData().getRoom()+"");
            }else{
                noFind = !client.getPlayer().getPlayerData().getRoom().equals(target.getMap()+"");
            }
        }catch (Exception e){return  true;}
        //BDebug.trace("测试"+noFind);
        if(BObject.isNotEmpty(target)&&BObject.isNotEmpty(client.getPlayer()))
        if(noFind||client.getPlayer().getPlayerData().getAttribute().getHp()<1||target.getAttribute().getHp()<1){//目标已死
            return true;
        }
        return false;
    }

    private  boolean fight(){//判断在房间里 并且没有死
        try{
            if(initFail()){
                //战斗结束
                timer.stop();
                getAttribute(client);//获取玩家属性
                return false;
            }
        }catch (Exception e){
            timer.stop();
            getAttribute(client);//获取玩家属性
            return false;
        }
        timer.setDelay(FIGHT_SPEED);
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(fight()){
            if(BObject.isNotEmpty(who)&&BObject.isNotEmpty(target)){
                who.Attack(target,client);
                target.Attack(who,client);
            }
        }
    }
}
