package clov.spellwords;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatEvents {

    // player -> trigger -> last use time
    private static final Map<UUID, Map<String, Long>> COOLDOWNS = new HashMap<>();

    public static void initialize() {

        ChatConfig.load();

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {

            String text = message.getContent().getString();

            UUID uuid = sender.getUuid();
            long now = System.currentTimeMillis();

            for (ChatConfig.Trigger trigger : ChatConfig.DATA.triggers) {

                String input = trigger.ignoreCase ? text.toLowerCase() : text;

                boolean match = false;

                for (String word : trigger.words) {

                    String w = trigger.ignoreCase ? word.toLowerCase() : word;

                    if (trigger.exactMatch) {
                        match = input.equals(w);
                    } else {
                        match = input.contains(w);
                    }

                    if (match) break;
                }

                if (!match) continue;

                // ===== COOLDOWN =====
                if (trigger.cooldownSeconds > 0) {

                    COOLDOWNS.putIfAbsent(uuid, new HashMap<>());
                    Map<String, Long> map = COOLDOWNS.get(uuid);

                    long last = map.getOrDefault(trigger.command, 0L);
                    long cooldownMs = trigger.cooldownSeconds * 1000L;

                    if (now - last < cooldownMs) {
                        return !trigger.cancelMessage;
                    }

                    map.put(trigger.command, now);
                }

                // ===== EXECUTE =====
                String command = trigger.command
                        .replace("{player}", sender.getName().getString());

                executeAsPlayer(sender, command);

                // ===== CANCEL =====
                if (trigger.cancelMessage) {
                    return false;
                }

                break;
            }

            return true;
        });
    }

    private static void executeAsPlayer(ServerPlayerEntity player, String command) {

        var server = player.getCommandSource().getServer();

        try {
            server.getCommandManager()
                    .getDispatcher()
                    .execute(command, player.getCommandSource());

        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}