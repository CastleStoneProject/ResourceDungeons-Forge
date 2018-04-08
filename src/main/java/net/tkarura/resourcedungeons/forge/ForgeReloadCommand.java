package net.tkarura.resourcedungeons.forge;

import net.tkarura.resourcedungeons.core.command.DungeonCommand;
import net.tkarura.resourcedungeons.core.command.DungeonCommandSender;
import net.tkarura.resourcedungeons.core.exception.DungeonCommandException;

public class ForgeReloadCommand extends DungeonCommand {

    public ForgeReloadCommand() {
        super("reload");
        this.description = "Setting Reload.";
    }

    @Override
    public void execute(DungeonCommandSender sender) throws DungeonCommandException {
        sender.sendMessage("Reload Start.");
        ResourceDungeonsForge.getInstance().init();
        sender.sendMessage("Reload Complete.");
    }
}
