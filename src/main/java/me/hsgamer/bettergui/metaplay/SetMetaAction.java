package me.hsgamer.bettergui.metaplay;

import me.hsgamer.bettergui.api.action.Action;
import me.hsgamer.bettergui.api.menu.Menu;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.task.BatchRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class SetMetaAction implements Action {
    private final Plugin plugin;
    private final Menu menu;
    private final String actionValue;

    public SetMetaAction(Plugin plugin, ActionBuilder.Input input) {
        this.plugin = plugin;
        this.menu = input.menu;
        this.actionValue = input.value;
    }

    @Override
    public void accept(UUID uuid, BatchRunnable.Process process) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }
        String[] split = actionValue.split(" ", 2);
        String name = StringReplacerApplier.replace(split[0], uuid, this);
        String value = split.length > 1 ? StringReplacerApplier.replace(split[1], uuid, this) : "";
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.setMetadata(name, new FixedMetadataValue(plugin, value));
            process.next();
        });
    }

    @Override
    public Menu getMenu() {
        return menu;
    }
}
