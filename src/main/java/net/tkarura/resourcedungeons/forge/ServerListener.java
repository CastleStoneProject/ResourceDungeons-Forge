package net.tkarura.resourcedungeons.forge;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.tkarura.resourcedungeons.core.generate.DungeonCheckPoint;
import net.tkarura.resourcedungeons.core.generate.DungeonGenerateCheck;
import net.tkarura.resourcedungeons.core.script.DungeonScriptEngine;
import net.tkarura.resourcedungeons.core.server.IDungeonWorld;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class ServerListener {

    private Deque<DungeonScriptEngine> handles = new ArrayDeque<>();
    private boolean generate_lock = false;

    public void setGenerateHandle(DungeonScriptEngine handle) {
        this.handles.push(handle);
    }

    @SubscribeEvent
    public void load(TickEvent.ServerTickEvent event) {

        // 渡されたハンドルを実行させる処理
        if (handles.isEmpty()) {
            return;
        }

        generate_lock = true;
        handles.pop().runSessions();
        generate_lock = false;

    }

    @SubscribeEvent
    public void populate(PopulateChunkEvent.Post event) {

        if (generate_lock) {
            return;
        }

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
        engine.setScriptLibraryDir(Loader.instance().getIndexedModList().get(ResourceDungeonsForge.MODID).getSource());

        new Thread(() -> {
            try {
                engine.loadScripts();
                engine.callMainFunction();
                ResourceDungeonsForge.getInstance().getServerListener().setGenerateHandle(engine);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

}
