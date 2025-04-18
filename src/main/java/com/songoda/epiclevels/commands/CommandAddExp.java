package com.songoda.epiclevels.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.core.utils.NumberUtils;
import com.songoda.epiclevels.EpicLevels;
import com.songoda.epiclevels.players.EPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandAddExp extends AbstractCommand {
    private final EpicLevels instance;

    public CommandAddExp(EpicLevels instance) {
        super(CommandType.CONSOLE_OK, "AddExp");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 2) {
            return ReturnType.SYNTAX_ERROR;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        if (!player.hasPlayedBefore() && !player.isOnline()) {
            this.instance.getLocale().getMessage("command.general.notonline")
                    .processPlaceholder("notonline", args[0])
                    .sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        if (!NumberUtils.isInt(args[1]) && !player.isOnline()) {
            this.instance.getLocale().getMessage("command.general.notint")
                    .processPlaceholder("number", args[1])
                    .sendPrefixedMessage(sender);
            return ReturnType.SYNTAX_ERROR;
        }

        long amount = Long.parseLong(args[1]);
        EPlayer ePlayer = this.instance.getPlayerManager().getPlayer(player);
        ePlayer.addExperience(amount);
        this.instance.getDataHelper().updatePlayer(ePlayer);

        this.instance.getLocale().getMessage("command.addexp.success")
                .processPlaceholder("amount", amount)
                .processPlaceholder("player", player.getName())
                .sendPrefixedMessage(sender);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epiclevels.addexp";
    }

    @Override
    public String getSyntax() {
        return "/levels AddExp <Player> <Amount>";
    }

    @Override
    public String getDescription() {
        return "Add experience to a player.";
    }
}
