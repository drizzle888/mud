package com.eztv.mud.command.commands.faction;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BObject;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.doTalk;

public class FactionPanel extends BaseCommand {
    public FactionPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        Msg msg = new Msg();
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        String faction = getClient().getPlayer().getFaction();
        Faction mFaction = FactionCache.factions.get(faction);
        winMsg.setDesc("门派");
        if(BObject.isEmpty(faction)||mFaction==null){//暂无门派
            choice.add(Choice.createChoice("创建门派", Enum.messageType.action,"createFaction", null,null).setBgColor(Enum.color.blue));
            choice.add(Choice.createChoice("查看门派", Enum.messageType.action,"joinFaction", null,null).setBgColor(Enum.color.yellow));
        }else{//有门派了 查看门派信息
            String str=getPropByFile("faction","faction_panel",
                        mFaction.getName()+mFaction.getAlias(),
                        mFaction.getLevel(),
                        mFaction.getDesc(),
                        mFaction.getExpDesc()
                    );
            winMsg.setDesc(str);
            //是帮主才能显示解散门派
            choice.add(Choice.createChoice("帮派成员", Enum.messageType.pop,"memberFaction", "","", Enum.winAction.open)
                    .setBgColor(Enum.color.gray));
            choice.add(Choice.createChoice("帮派任务", Enum.messageType.pop,"factionTask", "","")
                    .setBgColor(Enum.color.blue));
            if(getClient().getPlayer().getAccount().equals(mFaction.getLeader())){
                msg.setCmd("destroyFaction");
                choice.add(Choice.createChoice("解散门派", Enum.messageType.pop,"confirmPanel", object2JsonStr(msg),"这将会解散你的帮派，你确定吗？")
                        .setBgColor(Enum.color.red));
            }else{
                msg.setCmd("exitFaction");
                choice.add(Choice.createChoice("退出门派", Enum.messageType.pop,"confirmPanel", object2JsonStr(msg),"离开该帮派吗？")
                        .setBgColor(Enum.color.red));
            }
//            choice.add(Choice.createChoice("帮派地图", Enum.messageType.pop,"memberFaction", "","", Enum.winAction.open)
//                    .setBgColor(Enum.color.gray));

        }
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.action, doTalk,object2JsonStr(winMsg),null).getBytes());
    }
}
