import fr.minemobs.manifestapi.ManifestAPI;
import fr.minemobs.manifestapi.Project;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ManifestTest {

    private final ManifestAPI api = new ManifestAPI(new File("mods"), getManifest());
    private static final Logger LOGGER = Logger.getLogger("ManifestTest");

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            File manifest = new File("manifest.json");
            if (manifest.exists()) manifest.delete();
        }));
    }

    File getManifest() {
        File file = new File("manifest.json");
        if(file.exists()) return file;
        try {
            file.createNewFile();
            new URL("https://gist.githubusercontent.com/Minemobs/5aacb59c325b61511763247703aa26eb/raw/cafaf8848036c207a60cec5b6434d31b65781634/manifest.json").openStream()
                    .transferTo(new FileOutputStream(file));
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    @Test
    void getDownloadURL() throws IOException {
        ManifestAPI api = new ManifestAPI("mods", "manifest.json");
        if (!api.getDownloadURL("221857", "2270206").contains("Pam%27s%20HarvestCraft%201.7.10Lb.jar")) {
            fail("The download URL is not correct");
        }
    }

    @Test
    void getFirstMod() throws IOException {
        ManifestAPI api = new ManifestAPI("mods", "manifest.json");
        assertEquals("221857", api.getMods()[0].getProjectID());
    }

    @Test
    void downloadMods() throws IOException {
        ManifestAPI api = new ManifestAPI("mods", "manifest.json");
        String modName = "";
        for (Project mod : api.getMods()) {
            modName = api.getModName(mod);
            LOGGER.info("Downloading mod " + mod.getProjectID() + "-" + mod.getFileID());
            api.downloadMod(mod);
        }
        assertEquals("Pam's HarvestCraft 1.7.10Lb.jar", modName);
    }
}
