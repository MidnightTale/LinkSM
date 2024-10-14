package net.hynse.linkSM

import net.hynse.linkSM.LinkSM.Companion.wrappedScheduler
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ServerLinks
import java.net.URI

object LinkManager {
    private val serverLinks: ServerLinks = Bukkit.getServer().serverLinks
    private val links = mutableMapOf<String, Pair<Component, URI>>()

    fun addLink(key: String, displayName: Component, url: URI) {
        links[key] = Pair(displayName, url)
    }

    fun removeLink(key: String) {
        links.remove(key)
    }

    fun clearLinks() {
        links.clear()
    }

    fun getLink(key: String): Pair<Component, URI>? {
        return links[key]
    }

    fun getLinkKeys(): Set<String> {
        return links.keys
    }

    fun updateLinks() {
        wrappedScheduler.runTaskAsynchronously() {
            serverLinks.links.forEach { serverLinks.removeLink(it) }
            links.forEach { (_, pair) ->
                serverLinks.addLink(pair.first, pair.second)
            }
        }
    }
}