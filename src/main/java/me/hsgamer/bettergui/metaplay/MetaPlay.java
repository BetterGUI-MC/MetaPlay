package me.hsgamer.bettergui.metaplay;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public final class MetaPlay extends PluginAddon {
    @Override
    public void onEnable() {
        VariableManager.register("meta_", (original, uuid) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return null;
            }
            boolean isNumber = original.toLowerCase(Locale.ROOT).startsWith("number_");
            String name;
            if (isNumber) {
                name = original.substring(7);
            } else {
                name = original;
            }
            Optional<MetadataValue> optional = player.getMetadata(name).stream().findFirst();
            if (optional.isPresent()) {
                MetadataValue metadataValue = optional.get();
                if (isNumber) {
                    try {
                        double value = metadataValue.asDouble();
                        NumberFormat numberFormat = NumberFormat.getInstance(Locale.ROOT);
                        numberFormat.setMinimumFractionDigits(0);
                        return numberFormat.format(value);
                    } catch (Exception ignored) {
                        return "-1";
                    }
                }
                return metadataValue.asString();
            }
            return isNumber ? "0" : "";
        });
        ActionBuilder.INSTANCE.register(input -> new SetMetaAction(this, input), "set-meta", "meta");
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
