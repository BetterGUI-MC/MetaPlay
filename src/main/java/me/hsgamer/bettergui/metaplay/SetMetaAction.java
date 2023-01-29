package me.hsgamer.bettergui.metaplay;

import me.hsgamer.bettergui.api.action.Action;
import me.hsgamer.bettergui.api.menu.Menu;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class SetMetaAction implements Action {
    private final MetaPlay addon;
    private final Menu menu;
    private final String value;
    private final String name;
    private final boolean isNumber;

    public SetMetaAction(MetaPlay addon, ActionBuilder.Input input) {
        this.addon = addon;
        this.menu = input.menu;
        this.value = input.value;
        List<String> optionList = input.getOptionAsList();
        this.name = !optionList.isEmpty() ? optionList.get(0) : "";
        this.isNumber = optionList.size() > 1 && optionList.get(1).equalsIgnoreCase("number");
    }

    @Override
    public void accept(UUID uuid, TaskProcess process) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }
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
