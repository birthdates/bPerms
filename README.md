# bPermissions

This plugin is a cross-server & cross-platform permission system. This means you can have the same permissions on a
Bukkit & BungeeCord server. Or you can have two Bukkit servers or vice versa.

bPermissions uses [Redis](https://redis.io/) for its primary database and its lighting fast cross-server messaging
system.

# Supported Platforms

* Bukkit/Spigot (with Vault support)
* BungeeCord
* Sponge

# Permission Groups

This is one of the main features I focused on which is what I found most annoying about the inheritance based
permissions system. Permission groups are like ranks but just offer a list of permissions you can add to a rank/player.
This can be useful for donator ranks. For example, if you have the Titan rank and above it is the God rank, and you
don't want God to have a Titan kit, you can create a permission group and give it to both groups giving them both the
same permissions. Then you can add the separate permissions to the ranks.

# Server-based Ranks/Permissions

All ranks and permissions can be for every server or, a certain few. However, if you want to bypass this, you can
set `bypassServerBasedPermissions` to `true` in the configuration (this can be useful for Hub servers). To set what
server you're on, set `serverId` in the configuration.

# Default Configuration

```toml
serverId = "test"
bypassServerBasedPermissions = false

[redis]
host = "localhost"
port = 6379
database = 0
username = ""
password = ""
```