package fr.minemobs.manifestcli

import fr.minemobs.manifestapi.ManifestAPI
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.io.IOException

fun main(args: Array<String>) {
    val logger = ManifestAPI.getLogger()
    val options = Options()
    options.addOption("f", "folder", true, "Defaults to currentDirectory/mods")
    options.addOption("m", "manifest", true, "Defaults to currentDirectory/manifest.json")
    val parser: CommandLineParser = DefaultParser()
    val commandLine = parser.parse(options, args)
    val folder = commandLine.getOptionValue("folder", "mods")
    val manifest = commandLine.getOptionValue("manifest", "manifest.json")
    val manifestAPI = ManifestAPI(folder, manifest)
    manifestAPI.mods.forEach {
        logger.info("Downloading ${manifestAPI.getModName(it)}")
        try {
            manifestAPI.downloadMod(it)
        } catch (e: IOException) {
            logger.severe("Error while downloading ${manifestAPI.getModName(it)} : ${e.message}")
            e.printStackTrace()
        }
    }
    logger.info("Finished downloading mods")
}