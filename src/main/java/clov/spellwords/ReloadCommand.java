package clov.spellwords;

import com.mojang.brigadier.Command;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class ReloadCommand {

    public static void register() {

        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> {

                dispatcher.register(
                    literal("spellwords")
                    .then(
                        literal("reload")
                        .executes(ctx -> {

                            try {

                                var player = ctx.getSource().getPlayer();

                                if (player != null
                                    && !Permissions.check(
                                            player,
                                            "spellwords.reload",
                                            false)) {

                                    ctx.getSource().sendMessage(
                                        Text.literal("You do not have permission.")
                                    );

                                    return 0;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                return 0;
                            }

                            ChatConfig.load();

                            ctx.getSource().sendMessage(
                                Text.literal("SpellWords config reloaded.")
                            );

                            return Command.SINGLE_SUCCESS;
                        })
                    )
                );
            }
        );
    }
}