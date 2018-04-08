package net.tkarura.resourcedungeons.forge;

import net.minecraftforge.fml.common.Loader;
import net.tkarura.resourcedungeons.core.command.DungeonCommandSender;
import net.tkarura.resourcedungeons.core.command.DungeonGenerateCommand;
import net.tkarura.resourcedungeons.core.exception.DungeonScriptException;
import net.tkarura.resourcedungeons.core.script.DungeonScriptEngine;

import java.io.File;

public class ForgeGenerateCommand extends DungeonGenerateCommand {

    @Override
    public void generate(DungeonCommandSender sender, DungeonScriptEngine engine) throws DungeonScriptException {

        // ライブラリディレクトリを設定
        engine.setScriptLibraryDir(Loader.instance().getIndexedModList().get(ResourceDungeonsForge.MODID).getSource());

        new Thread(() -> {
            try {
                engine.loadScripts();
                engine.callMainFunction();
                ResourceDungeonsForge.getInstance().getServerListener().setGenerateHandle(engine);
                sender.sendMessage("Dungeon Generate Complete.");
            } catch (DungeonScriptException e) {
                e.printStackTrace();
                sender.sendMessage("Dungeon Generate Failed. reason: " + e.getLocalizedMessage());
            }
        }).start();

    }

}
