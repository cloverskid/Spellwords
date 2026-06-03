package clov.spellwords;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClovMod implements ModInitializer {

    public static final String MOD_ID = "clovmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("SpellWords initializing...");

        // check for LuckPerms 
        if (FabricLoader.getInstance().isModLoaded("luckperms")) {
            LOGGER.info("LuckPerms is detected");
        } else {
            LOGGER.info("LuckPerms is NOT detected");
        }

        // load config and register events/commands
        ChatConfig.load();
        ChatEvents.initialize();
        ReloadCommand.register();

        LOGGER.info("SpellWords loaded successfully");
    }
}