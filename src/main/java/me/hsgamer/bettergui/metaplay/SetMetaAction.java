package me.hsgamer.bettergui.metaplay;

import me.hsgamer.bettergui.api.action.Action;
import me.hsgamer.bettergui.api.menu.Menu;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.UUID;

public class SetMetaAction implements Action {
    private final MetaPlay addon;
    private final Menu menu;
    private final String actionValue;
    private final boolean isNumber;

    public SetMetaAction(MetaPlay addon, ActionBuilder.Input input) {
        this.addon = addon;
        this.menu = input.menu;
        this.actionValue = input.value;
        this.isNumber = input.option.equalsIgnoreCase("number");
    }

    @Override
    public void accept(UUID uuid, TaskProcess process) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }
        String[] split = actionValue.split(" ", 2);
        String name = StringReplacerApplier.replace(split[0], uuid, this);
        String value = split.length > 1 ? split[1] : "";
        Bukkit.getScheduler().runTask(addon.getPlugin(), () -> {
            String previousValue = addon.getMetadataValue(player, name).map(MetadataValue::asString).orElse(isNumber ? "0" : "");
            String finalValue = value.replace("{value}", previousValue);
            finalValue = StringReplacerApplier.replace(finalValue, uuid, this);
            addon.setMetadataValue(player, name, finalValue);
            process.next();
        });
    }

    @Override
    public Menu getMenu() {
        return menu;
    }
}
