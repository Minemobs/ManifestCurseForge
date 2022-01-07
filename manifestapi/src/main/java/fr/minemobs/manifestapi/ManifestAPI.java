package fr.minemobs.manifestapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import marcono1234.gson.recordadapter.RecordTypeAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManifestAPI {

    private final File manifest;
    private final File folder;
    private static final Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapterFactory(RecordTypeAdapterFactory.DEFAULT).create();
    private static final Logger LOGGER = Logger.getLogger("manifest");

    /**
     *
     * @param folder The mods folder. Defaults to {@code mods}.
     * @param manifest The manfiest file. Defaults to {@code manifest.json}.
     * @throws IllegalArgumentException If the manifest file does not exist.
     */
    public ManifestAPI(String folder, String manifest) throws IllegalArgumentException {
        this.folder = getFileOrDefault(folder, new File("mods"));
        if(!this.folder.exists()) this.folder.mkdir();
        File manifestFile = getFileOrDefault(manifest, new File("manifest.json"));
        if(!manifestFile.exists()) {
            throw new IllegalArgumentException("Manifest file doesn't exist");
        }
        this.manifest = manifestFile;
    }

    public ManifestAPI(File folder, File manifest) {
        this.folder = folder;
        if(!this.folder.exists()) this.folder.mkdir();
        if(!manifest.exists()) {
            throw new IllegalArgumentException("Manifest file doesn't exist");
        }
        this.manifest = manifest;
    }

    public Project[] getMods() throws IOException {
        JsonArray files = gson.fromJson(Files.newBufferedReader(manifest.toPath()), JsonObject.class).get("files").getAsJsonArray();
        return gson.fromJson(files, Project[].class);
    }

    public JsonObject getMod(String projectID, String versionID) throws IOException {
        String url = String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s/file/%s", projectID, versionID);
        return gson.fromJson(new InputStreamReader(new URL(url).openStream()), JsonObject.class);
    }

    public String getModName(Project project) throws IOException {
        return getMod(project.getProjectID(), project.getFileID()).get("fileName").getAsString();
    }

    public String getDownloadURL(String projectID, String versionID) throws IOException {
        JsonObject mod = getMod(projectID, versionID);
        String fileName = URLEncoder.encode(mod.get("fileName").getAsString(), StandardCharsets.UTF_8).replace("+", "%20");
        return String.join("/",
                Arrays.copyOf(mod.get("downloadUrl").getAsString().split("/"),
                        mod.get("downloadUrl").getAsString().split("/").length -1)) + "/" + fileName;
    }

    public void downloadMod(Project project) throws IOException {
        URL url = new URL(getDownloadURL(project.getProjectID(), project.getFileID()));
        String fileName = getModName(project);
        File output = new File(folder, fileName);
        if(output.exists()) {
            LOGGER.log(Level.WARNING, () -> fileName + " already exists, skipping");
            return;
        }
        try(InputStream stream = url.openStream()) {
            stream.transferTo(Files.newOutputStream(output.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING));
        }
    }

    public JsonObject getManifest() throws IOException {
        return gson.fromJson(Files.newBufferedReader(manifest.toPath()), JsonObject.class);
    }

    public String getMinecraftVersion() throws IOException {
        return gson.fromJson(getManifest().getAsJsonObject("minecraft"), JsonObject.class).get("version").getAsString();
    }

    public ModLoader[] getModLoaders() throws IOException {
        return gson.fromJson(getManifest().getAsJsonObject("minecraft").getAsJsonArray("modLoaders"), ModLoader[].class);
    }

    private File getFileOrDefault(String fileName, File defaultFile) {
        return fileName == null || !new File(fileName).exists() ? defaultFile : new File(fileName);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
