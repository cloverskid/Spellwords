package clov.spellwords;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ChatConfig {

    public static final File FILE = new File("config/chat_triggers.json");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ConfigData DATA = new ConfigData();

    public static void load() {
        try {
            if (!FILE.exists()) {
                FILE.getParentFile().mkdirs();
                DATA = createDefault();
                save();
                ClovMod.LOGGER.info("Created default config file at " + FILE.getAbsolutePath());
            }

            FileReader reader = new FileReader(FILE);
            DATA = GSON.fromJson(reader, ConfigData.class);
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(FILE);
            GSON.toJson(DATA, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== DEFAULT CONFIG =====
    private static ConfigData createDefault() {
        ConfigData data = new ConfigData();

        Trigger fireball = new Trigger();
            fireball.words = Arrays.asList("fireball");
            fireball.command = "summon fireball ~ ~1 ~ {ExplosionPower:30}";
            fireball.exactMatch = true;
            fireball.ignoreCase = true;
            fireball.cancelMessage = true;
            fireball.cooldownSeconds = 10;

        Trigger banword = new Trigger();
            banword.words = Arrays.asList("badword","anotherbadword","плохое слово");
            banword.command = "tellraw @s \"Sorry, but that word is banned!\"";
            banword.exactMatch = false;
            banword.ignoreCase = false;
            banword.cancelMessage = true;
            banword.cooldownSeconds = 0;

        Trigger awadakadabra = new Trigger();
            awadakadabra.words = Arrays.asList("AvAdA kAdAbRa");
            awadakadabra.command = "kill @s";
            awadakadabra.exactMatch = true;
            awadakadabra.ignoreCase = true;
            awadakadabra.cancelMessage = false;
            awadakadabra.cooldownSeconds = 0;

        data.triggers.add(fireball);
        data.triggers.add(banword);
        data.triggers.add(awadakadabra);

        return data;
    }


    // ===== DATA =====

    public static class ConfigData {
        public List<Trigger> triggers = new ArrayList<>();
    }

    public static class Trigger {
        public List<String> words = new ArrayList<>();
        public String command;

        public boolean exactMatch = false;
        public boolean ignoreCase = true;
        public boolean cancelMessage = false;

        public int cooldownSeconds = 0;
    }
}