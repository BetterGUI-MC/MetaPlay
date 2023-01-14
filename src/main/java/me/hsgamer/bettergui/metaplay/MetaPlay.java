package me.hsgamer.bettergui.metaplay;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public final class MetaPlay extends PluginAddon {
    @Override
    public void onEnable() {
        VariableManager.register("meta_", (original, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return original;
            }
            for (MetadataValue metadataValue : player.getMetadata(original)) {
                if (metadataValue.getOwningPlugin() == getPlugin()) {
                    return metadataValue.asString();
                }
            }
            return "";
        });
        ActionBuilder.INSTANCE.register(input -> new SetMetaAction(getPlugin(), input), "set-meta", "meta");
    }
}
