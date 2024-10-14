package net.hynse.linkSM

import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin
import me.nahu.scheduler.wrapper.WrappedScheduler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.event.Listener
import java.net.URI

class LinkSM : FoliaWrappedJavaPlugin(), Listener {
    companion object {
        lateinit var instance: LinkSM
            private set
        val wrappedScheduler: WrappedScheduler by lazy { instance.scheduler }
    }

    override fun onEnable() {
        instance = this
        logger.info("LinkSM plugin is enabled!")
        server.pluginManager.registerEvents(this, this)

        // Load configuration
        saveDefaultConfig()
        reloadConfig()

        // Initialize and update links
        initializeLinks()
        LinkManager.updateLinks()

        logger.info("Links have been updated!")
    }

    override fun onDisable() {
        logger.info("LinkSM plugin is disabled!")
    }

    private fun initializeLinks() {
            val linksConfig = config.getConfigurationSection("links") ?: return
        wrappedScheduler.runTaskAsynchronously() {
            for (key in linksConfig.getKeys(false)) {
                val name = linksConfig.getString("$key.name") ?: continue
                val url = linksConfig.getString("$key.url") ?: continue
                val color = linksConfig.getString("$key.color") ?: "WHITE"

                try {
                    val textColor = TextColor.fromHexString(color) ?: TextColor.color(255, 255, 255)
                    LinkManager.addLink(
                        key,
                        Component.text(name).color(textColor),
                        URI(url)
                    )
                } catch (e: Exception) {
                    logger.warning("Failed to add link $key: ${e.message}")
                }
            }
        }
    }
}