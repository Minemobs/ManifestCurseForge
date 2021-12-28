import fr.minemobs.manifestapi.ManifestAPI;
import fr.minemobs.manifestapi.Project;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ManifestTest {

    private final ManifestAPI api = new ManifestAPI("mods", "manifest.json");
    private static final Logger LOGGER = Logger.getLogger("ManifestTest");

    @Test
    void getDownloadURL() throws IOException {
        ManifestAPI api = new ManifestAPI("mods", "manifest.json");
        if (!api.getDownloadURL("221857", "2270206").contains("Pam%27s+HarvestCraft+1.7.10Lb.jar")) {
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
