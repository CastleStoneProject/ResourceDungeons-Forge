package net.tkarura.resourcedungeons.forge;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.tkarura.resourcedungeons.core.generate.DungeonCheckPoint;
import net.tkarura.resourcedungeons.core.generate.DungeonGenerateCheck;
import net.tkarura.resourcedungeons.core.script.DungeonScriptEngine;
import net.tkarura.resourcedungeons.core.server.IDungeonWorld;

import java.util.*;

public class ServerListener {

    private Deque<DungeonScriptEngine> handles = new ArrayDeque<>();

    public void setGenerateHandle(DungeonScriptEngine handle) {
        this.handles.push(handle);
    }

    @SubscribeEvent
    public void load(TickEvent.ServerTickEvent event) {

        // 渡されたハンドルを実行させる処理
        if (handles.isEmpty()) {
            return;
        }

        handles.pop().runSessions();

    }

    @SubscribeEvent
    public void populate(PopulateChunkEvent.Post event) {

        IDungeonWorld world = new ForgeWorld(event.getWorld());

        int width = 16;
        int height = event.getWorld().getHeight();
        int length = 16;
        int base_x = event.getChunkX() * width;
        int base_y = 0;
        int base_z = event.getChunkZ() * length;

        DungeonGenerateCheck check = new DungeonGenerateCheck(
                base_x, base_y, base_z,
                width, height, length
        );
        check.setDungeonManager(ResourceDungeonsForge.getInstance().getResourceDungeons().getDungeonManager());
        check.setWorld(world);
        check.search();

        if (check.getCheckPoints().isEmpty()) {
            return;
        }

        List<DungeonCheckPoint> points = check.getCheckPoints();

        Collections.shuffle(points);

        DungeonCheckPoint point = points.get(0);
        DungeonScriptEngine engine = new DungeonScriptEngine(point.getDungeon());
        engine.setBaseLocation(point.getX(), point.getY(), point.getZ());
        engine.setWorld(world);
        engine.setSessionManager(ResourceDungeonsForge.getInstance().getResourceDungeons().getSessionManager());
        engine.setScriptLibraryDir(ResourceDungeonsForge.getDungeonScriptLibraryDir());

        new Thread(() -> {
            try {
                engine.loadScripts();
                engine.callMainFunction();
                ResourceDungeonsForge.getInstance().getServerListener().setGenerateHandle(engine);
                FMLLog.log.info(
                        "Dungeon Generated. id: " + point.getDungeon().getId() +
                                " x: " + point.getX() + " y: " + point.getY() + " z: " + point.getZ());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

}
