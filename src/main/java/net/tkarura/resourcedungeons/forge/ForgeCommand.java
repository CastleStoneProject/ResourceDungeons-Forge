package net.tkarura.resourcedungeons.forge;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.tkarura.resourcedungeons.core.ResourceDungeons;
import net.tkarura.resourcedungeons.core.command.CommandManager;
import net.tkarura.resourcedungeons.core.command.DungeonCommand;

public class ForgeCommand extends CommandBase {

    public final static String COMMAND_NAME = "ResourceDungeons";

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/ResourceDungeons help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        // コマンド情報を取得
        ResourceDungeonsForge forge = ResourceDungeonsForge.getInstance();
        ResourceDungeons core = forge.getResourceDungeons();
        CommandManager cm = core.getCommandManager();
        ForgeCommandSender fcs = new ForgeCommandSender(sender, args);

        // 実行引数を取得 (引数を指定しない場合はhelpに設定)
        String name = args.length > 0 ? args[0] : "help";

        DungeonCommand cmd = cm.getCommand(name);
        DungeonCommand cmd_ = cmd != null ? cmd : cm.getCommand("help");

        // コマンド実行
        cmd_.runCommand(fcs);

    }

}
