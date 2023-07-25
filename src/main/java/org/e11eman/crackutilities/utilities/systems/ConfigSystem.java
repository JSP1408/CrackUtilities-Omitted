package org.e11eman.crackutilities.utilities.systems;

public class ConfigSystem {
    public final File configPath;
    private final Gson gson = new Gson();

    private final Path modConfigPath = FabricLoader.getInstance().getConfigDir();

    private JsonObject config;

    public ConfigSystem(File configPath) {
        this.configPath = configPath;
        updateConfig();

    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    private void createBrokenConfig() {
        File brokenConfig = new File(modConfigPath + "DirectoryOmitted");

        try {
            brokenConfig.createNewFile();
            FileWriter fileWriter = new FileWriter(brokenConfig);
            fileWriter.write(getConfig().toString());
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    public void fixConfig() {
        try {
            if (configPath.exists()) {
                createBrokenConfig();
            }

            Files.createDirectories(Paths.get(modConfigPath + "DirectoryOmitted"));
            FileWriter fixConfig = new FileWriter(configPath);

            configPath.delete();
            configPath.createNewFile();

            fixConfig.write(jsonConfigDefault);
            fixConfig.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateConfig() {
        try {
            Reader reader = Files.newBufferedReader(configPath.toPath());
            this.config = gson.fromJson(reader, JsonObject.class);
            reader.close();
        } catch (IOException e) {
            fixConfig();

            updateConfig();
        }
    }

    public JsonObject getConfig() {
        if (configPath.exists()) {
            try {
                return config;
            } catch (Exception e) {
                fixConfig();

                return getConfig();
            }
        } else {
            fixConfig();

            return getConfig();
        }
    }

    public JsonObject getCategory(JsonObject config, String categoryName) {
        try {
            return config.getAsJsonObject(categoryName);
        } catch (Exception e) {
            fixConfig();

            return config.getAsJsonObject(categoryName);
        }
    }

    public String jsonConfigDefault = """
            {
              "commandSystem": {
                "prefix": "."
              },
              "commandCoreSystem": {
                "layers": 3,
                "maxDistance": 128,
                "enabled": false
              },
              "chatQueueSystem": {
                "delay": 150
              },
              "selfCareSystem": {
                "delay": 150,
                "checkCreative": false,
                "checkOperator": false
              },
              "chatFormatting": {
                "enabled": false,
                "formatting": [
                  {"color": "dark_gray", "text": "["},
                  {"color": "blue", "bold": true, "text": "CUtilities"},
                  {"color": "dark_gray", "text": "] ", "bold": false},
                  {"color": "gray", "text": "%username% "},
                  {"color": "dark_gray", "text": "> "},
                  {"color": "gray", "text": "%message%"}
                ]
              },
              "ircSystem": {
                "host": "",
                "port": 83424560,
                "timeout": 5000
              },
              "keyBindings": {
                "spin": {
                  "increment": 1
                }
              }
            }
            """;
}
