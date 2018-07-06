package net.tkarura.resourcedungeons.forge;

import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.tkarura.resourcedungeons.core.command.DungeonCommandSender;
import net.tkarura.resourcedungeons.core.server.IDungeonWorld;

public class ForgeCommandSender implements DungeonCommandSender {

    private ICommandSender sender;
    private Entity entity;
    private String[] args;

    public ForgeCommandSender(ICommandSender sender, String[] args) {
        this.sender = sender;
        this.entity = sender.getCommandSenderEntity();
        this.args = args;
    }

    @Override
    public void sendMessage(String message) {
        this.sender.sendMessage(new TextComponentString(colorFormat(message)));
    }

    private String colorFormat(String message) {
        return message.replaceAll("&", "§");
    }

    @Override
    public UUID getUUID() {
        return this.entity != null ? this.entity.getUniqueID() : null;
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public String[] getArgs() {
        return this.args;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.canUseCommand(2, ForgeCommand.COMMAND_NAME);
    }

    @Override
    public IDungeonWorld getWorld() {
        return new ForgeWorld(this.sender.getEntityWorld());
    }

    @Override
    public int getX() {
        return this.entity.getPosition().getX();
    }

    @Override
    public int getY() {
        return this.entity.getPosition().getY();
    }

    @Override
    public int getZ() {
        return this.entity.getPosition().getZ();
    }

}
