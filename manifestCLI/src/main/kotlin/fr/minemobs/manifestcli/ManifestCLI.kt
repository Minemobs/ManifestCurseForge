package fr.minemobs.manifestcli

import fr.minemobs.manifestapi.ManifestAPI
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options

fun main(args: Array<String>) {
    val options = Options()
    options.addOption("f", "folder", false, "Defaults to currentDirectory/mods")
    options.addOption("m", "manifest", false, "Defaults to currentDirectory/manifest.json")
    val parser: CommandLineParser = DefaultParser()
    val commandLine = parser.parse(options, args)
    val folder = commandLine.getOptionValue("folder", "mods")
    val manifest = commandLine.getOptionValue("manifest", "manifest.json")
    val manifestAPI = ManifestAPI(folder, manifest)
    manifestAPI.mods.forEach {
        println("Downloading ${manifestAPI.getModName(it)}")
        manifestAPI.downloadMod(it)
    }
    println("Finished downloading mods")
}