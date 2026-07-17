package me.andreasmelone.fmschattweaks.client.quick;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import me.andreasmelone.fmschattweaks.client.FermentMilksChatTweaksClient;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class QuickCommandsManager {
    private static final QuickCommandsManager INSTANCE = new QuickCommandsManager();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Object FILE_LOCK = new Object();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final AtomicBoolean needsWriting = new AtomicBoolean();

    private final Set<String> quickCommands = ConcurrentHashMap.newKeySet();
    private final Set<Consumer<String>> addedListeners = ConcurrentHashMap.newKeySet();
    private final Set<Consumer<String>> removedListeners = ConcurrentHashMap.newKeySet();

    public void addQuickCommand(String quickCommand) {
        this.quickCommands.add(quickCommand);
        this.addedListeners.forEach(s -> s.accept(quickCommand));
        this.needsWriting.set(true);
    }

    public void removeQuickCommand(String quickCommand) {
        this.quickCommands.remove(quickCommand);
        this.removedListeners.forEach(s -> s.accept(quickCommand));
        this.needsWriting.set(true);
    }

    public void onAdd(Consumer<String> listener) {
        this.addedListeners.add(listener);
    }

    public void onRemove(Consumer<String> listener) {
        this.removedListeners.add(listener);
    }

    public void removeOnAdd(Consumer<String> listener) {
        this.addedListeners.remove(listener);
    }

    public void removeOnRemove(Consumer<String> listener) {
        this.removedListeners.remove(listener);
    }

    public void init() {
        LOGGER.info("Initializing saver thread!");

        scheduler.scheduleAtFixedRate(() -> {
            if(needsWriting.getAndSet(false)) {
                saveImmediately();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    public void shutdown() {
        LOGGER.info("Shutting down saver thread!");
        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void saveImmediately() {
        synchronized (FILE_LOCK) {
            writeJson(serialize(), FermentMilksChatTweaksClient.QUICK_COMMANDS_CONFIG);
        }
    }

    public void save() {
        this.needsWriting.set(true);
    }

    public void load() {
        synchronized (FILE_LOCK) {
            deserialize(readJson(FermentMilksChatTweaksClient.QUICK_COMMANDS_CONFIG));
        }
    }

    public Set<String> getQuickCommands() {
        return new HashSet<>(quickCommands);
    }

    private JsonObject serialize() {
        JsonArray array = new JsonArray();
        for (String quickCommand : quickCommands) {
            array.add(quickCommand);
        }
        JsonObject obj = new JsonObject();
        obj.add("quickCommands", array);
        return obj;
    }

    private void deserialize(JsonObject object) {
        JsonArray array = (object.has("quickCommands") && object.get("quickCommands").isJsonArray()) ?
                object.getAsJsonArray("quickCommands") :
                new JsonArray();
        for (JsonElement element : array) {
            if(!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) continue;
            this.quickCommands.add(element.getAsString());
        }
    }

    public static QuickCommandsManager getInstance() {
        return INSTANCE;
    }

    private static void writeJson(JsonObject tag, Path path) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            LOGGER.error("Failed to create directory: {}", path.getParent(), e);
        }
        try (Writer out = new OutputStreamWriter(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            GSON.toJson(tag, out);
        } catch (IOException e) {
            LOGGER.error("Failed to write json!", e);
        }
    }

    private static JsonObject readJson(Path path) {
        if(!Files.exists(path)) return new JsonObject();
        try(InputStream in = Files.newInputStream(path)) {
            return GSON.fromJson(new InputStreamReader(in), JsonObject.class);
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.error("Failed to read json!", e);
            return new JsonObject();
        }
    }
}
