package net.tkarura.resourcedungeons.forge;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.tkarura.resourcedungeons.core.ResourceDungeons;

@Mod(modid = ResourceDungeonsForge.MODID, version = ResourceDungeonsForge.VERSION)
public class ResourceDungeonsForge
{
    public static final String MODID = "resourcedungeons";
    public static final String VERSION = "1.12.2";
    public static final String NAME = "ResourceDungeonsForge";

    private static ResourceDungeonsForge instance;
    private ResourceDungeons core;

    private ServerListener listener = new ServerListener();

    @EventHandler
    public void init(FMLInitializationEvent event) {

        instance = this;

        // ResouceDungeonsの呼び出し
        core = new ResourceDungeons();

        // ロガーの設定
        core.setLogger(Logger.getLogger("ResourceDungeons"));

        // ダンジョンディレクトリの指定
        File dungeons_dir = new File(".//Dungeons");

        // フォルダーの生成
        dungeons_dir.mkdirs();

        // ディレクトリの設定
        core.setDungeonDirectory(dungeons_dir);

        // 初期化
        init();

        MinecraftForge.EVENT_BUS.register(listener);

    }

    @EventHandler
    public void load(FMLServerStartingEvent event){

        ServerCommandManager scm = (ServerCommandManager) event.getServer().getCommandManager();
        scm.registerCommand(new ForgeCommand());

    }

    public void init() {

        core.init();

        ForgeGenerateCommand fdgc = new ForgeGenerateCommand();
        fdgc.setDungeonManager(core.getDungeonManager());
        fdgc.setSessionManager(core.getSessionManager());
        core.getCommandManager().register(fdgc);
        core.getCommandManager().register(new ForgeReloadCommand());

    }

    public static ResourceDungeonsForge getInstance() {
        return instance;
    }

    public ResourceDungeons getResourceDungeons() {
        return this.core;
    }

    public ServerListener getServerListener() {
         return this.listener;
    }
}
