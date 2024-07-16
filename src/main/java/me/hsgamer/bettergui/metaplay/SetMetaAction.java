package me.hsgamer.bettergui.metaplay;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

public class SetMetaAction implements Action {
    private final MetaPlay addon;
    private final String value;
    private final String name;
    private final boolean isNumber;

    public SetMetaAction(MetaPlay addon, ActionBuilder.Input input) {
        this.addon = addon;
        this.value = input.getValue();
        List<String> optionList = input.getOptionAsList();
        this.name = !optionList.isEmpty() ? optionList.get(0) : "";
        this.isNumber = optionList.size() > 1 && optionList.get(1).equalsIgnoreCase("number");
    }

    @Override
    public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            process.next();
            return;
        }
        SchedulerUtil.entity(player).run(() -> {
            try {
                String previousValue = addon.getMetadataValue(player, name).map(MetadataValue::asString).orElse(isNumber ? "0" : "");
                String finalValue = value.replace("{value}", previousValue);
                finalValue = stringReplacer.replaceOrOriginal(finalValue, uuid);
                addon.setMetadataValue(player, name, finalValue);
            } finally {
                process.next();
            }
        }, process::next);
    }
}
