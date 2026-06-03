package clov.spellwords;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import me.lucko.fabric.api.permissions.v0.Permissions;

import java.util.*;

public class ChatEvents {

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

                // permission check
                if (!trigger.permission.isEmpty()
                        && !Permissions.check(sender, trigger.permission, true)) {

                    runCommand(sender, trigger.denyCommand);

                    return !trigger.cancelMessage;
                }

                // check cooldown
                Map<String, Long> map = COOLDOWNS.computeIfAbsent(uuid, k -> new HashMap<>());
                long last = map.getOrDefault(trigger.command, 0L);
                long cooldownMs = trigger.cooldownSeconds * 1000L;

                if (trigger.cooldownSeconds > 0 && now - last < cooldownMs) {

                long remainingMs = cooldownMs - (now - last);
                long remainingSeconds = (remainingMs + 999) / 1000; // округление вверх

                String cooldownCommand = trigger.cooldownCommand
                    .replace("{player}", sender.getName().getString())
                    .replace("{cooldown}", String.valueOf(remainingSeconds));

                    runCommand(sender, cooldownCommand);

                    return !trigger.cancelMessage;
}

                map.put(trigger.command, now);

                // execute command      
                String command = trigger.command
                        .replace("{player}", sender.getName().getString());

                executeCommand(sender, command, trigger.runAsConsole);

                if (trigger.cancelMessage) {
                    return false;
                }

                break;
            }

            return true;
        });
    }

    // helper method to run a command as the player or console

    private static void runCommand(ServerPlayerEntity player, String command) {
        if (command == null || command.isEmpty()) return;

        executeCommand(player, command, true);
    }

    private static void executeCommand(
            ServerPlayerEntity player,
            String command,
            boolean runAsConsole) {

        var server = player.getCommandSource().getServer();

        try {
            ServerCommandSource source = runAsConsole
                    ? server.getCommandSource()
                    : player.getCommandSource();

            server.getCommandManager()
                    .getDispatcher()
                    .execute(command, source);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}