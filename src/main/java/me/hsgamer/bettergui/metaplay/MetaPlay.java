package me.hsgamer.bettergui.metaplay;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.Optional;

public final class MetaPlay extends PluginAddon {
    @Override
    public void onEnable() {
        VariableManager.register("meta_", (original, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return null;
            }
            return getMetadataValue(player, original).map(MetadataValue::asString).orElse("");
        });
        ActionBuilder.INSTANCE.register(input -> new SetMetaAction(this, input), "set-meta", "meta", "set-meta-number", "meta-number");
    }

    Optional<MetadataValue> getMetadataValue(Player player, String name) {
        for (MetadataValue metadataValue : player.getMetadata(name)) {
            if (metadataValue.getOwningPlugin() == getPlugin()) {
                return Optional.of(metadataValue);
            }
        }
        return Optional.empty();
    }

    void setMetadataValue(Player player, String name, String value) {
        player.setMetadata(name, new FixedMetadataValue(getPlugin(), value));
    }
}
