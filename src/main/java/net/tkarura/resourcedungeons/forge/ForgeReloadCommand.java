package net.tkarura.resourcedungeons.forge;

import net.tkarura.resourcedungeons.core.command.DungeonCommand;
import net.tkarura.resourcedungeons.core.command.DungeonCommandSender;
import net.tkarura.resourcedungeons.core.exception.DungeonCommandException;

import static net.tkarura.resourcedungeons.core.ResourceDungeons.PREFIX_MES;

public class ForgeReloadCommand extends DungeonCommand {

    public ForgeReloadCommand() {
        super("reload");
        this.description = "設定の再読込を行います。";
    }

    @Override
    public void execute(DungeonCommandSender sender) throws DungeonCommandException {
        sender.sendMessage(PREFIX_MES + " &6リロードをします。");
        ResourceDungeonsForge.getInstance().init();
        sender.sendMessage(PREFIX_MES + " &aリロードが完了しました。");
    }
}
