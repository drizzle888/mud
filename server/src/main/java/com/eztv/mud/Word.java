package com.eztv.mud;

import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Item;
import com.eztv.mud.cache.*;
import com.eztv.mud.command.CommandSetHandler;
import com.eztv.mud.script.ScriptExecutor;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BFile;
import com.eztv.mud.utils.BProp;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.getProp;
import static com.eztv.mud.cache.RoomCache.getMaps;

public class Word {
    private HashMap<String, Attribute> baseAttributes = new HashMap<String, Attribute>();
    private Globals globals = JsePlatform.standardGlobals();
    private ScriptExecutor scriptExecutor = new ScriptExecutor(null);
    private String GG = "";
    private Map<String,String> colors = new HashMap<>();
    private static Word Instance;

    //获取单例
    public static Word getInstance() {
        if (Instance == null) Instance = new Word();
        return Instance;
    }

    public void init() {
        initConf();
        initGG();//加载公告
        initColor();//加载公告

        initEnvironment();

        RelationCache.initRelationCache();//加载玩家关系
        PlayerCache.initPlayerCache();//加载所有玩家缓存信息


        initHandler();//装载指令
    }

    public void initColor() {
        synchronized (colors){
            this.scriptExecutor.load(Init_PATH+"color").execute(脚本_初始化,colors);
        }
    }

    public void initConf() {//加载配置
        FIGHT_SPEED = Integer.parseInt(BProp.getInstance().get("fight_speed"));
        DEFAULT_ROOM_ID = BProp.getInstance().get("aliveRoomId");
        DEFAULT_MAP_ID = BProp.getInstance().get("aliveMapId");
        DEFAULT_FACTION_MAP_ID = BProp.getInstance().get("factionMapId");
        DEFAULT_FACTION_ROOM_ID = BProp.getInstance().get("factionRoomId");
        pageLimitCol1 = Integer.parseInt(getProp("page_limit_col1"));
        pageLimitCol2 = Integer.parseInt(getProp("page_limit_col2"));
    }

    public void initEnvironment() {
        GameCache.initGameCache();//加载全局变量缓存
        RoomCache.initRooms();//加载房间
        NpcCache.initNPC(scriptExecutor, getMaps());//加载NPC
        MonsterCache.initMonster(scriptExecutor, getMaps());//加载怪物
        FactionCache.initFactionCache();//加载行会
        ItemCache.initItem(globals);//加载物品
        SkillCache.initSkill(scriptExecutor);//加载技能

        initBaseAttribute();//加载基础属性
        AuctionCache.initAuctionCache();//寄卖缓存
    }


    public void initGG() {//加载公告
        String src = System.getProperty("user.dir") + "/gg";
        GG = BFile.readFromFile(src);
        BDebug.trace("公告加载完成");
    }

    private void initBaseAttribute() {
        //加载属性上线 基础公式表
        baseAttributes.clear();
        List<Attribute> attributeList = DataBase.getInstance().init().createSQL("select * from t_attribute").list(Attribute.class);
        for (Attribute attribute : attributeList) {
            baseAttributes.put(attribute.getLevel() + "", attribute);
        }
        BDebug.trace("基础属性加载完成 最高等级 : Attribute load max_level:【" + baseAttributes.size() + "】");
    }

    //装载指令
    private void initHandler() {
        CommandSetHandler.initActionCommandSet();
        CommandSetHandler.initPanelCommandSet();
        BDebug.trace("装载游戏指令完成");
    }

    public Map<String, String> getColors() {
        return colors;
    }

    public HashMap<String, Attribute> getBaseAttributes() {
        return baseAttributes;
    }

    public List<Item> getItems() {
        return ItemCache.getItems();
    }

    public ScriptExecutor getScriptExecutor() {
        return scriptExecutor;
    }

    public String getGG() {
        return GG;
    }

    public Globals getGlobals() {
        return globals;
    }

}
