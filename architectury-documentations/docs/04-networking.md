# Networking

Architectury provides a unified networking API that works across both Fabric and Forge, making it easy to send custom packets between client and server.

## Overview

The `NetworkManager` class provides all networking functionality. It abstracts the differences between Fabric's and Forge's networking systems.

## Basic Concepts

### Sides

Networking has two sides:

- `Side.S2C` or `serverToClient()` - Server to Client packets
- `Side.C2S` or `clientToServer()` - Client to Server packets

### Packet Structure

Packets use `FriendlyByteBuf` for serialization, which provides methods to write/read various data types.

## Registering Network Receivers

### Server-side Receiver (Client to Server)

```java
import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;

public class ModNetworking {
    private static final ResourceLocation EXAMPLE_PACKET =
        new ResourceLocation("blockpops", "example_packet");

    public static void init() {
        // Register receiver on the server side
        NetworkManager.registerReceiver(
            NetworkManager.c2s(), // or NetworkManager.Side.C2S
            EXAMPLE_PACKET,
            (buf, context) -> {
                // Read data from buffer
                String message = buf.readUtf();

                // Queue work on the main thread
                context.queue(() -> {
                    // Handle the packet on the server
                    var player = context.getPlayer();
                    // Do something with the data...
                });
            }
        );
    }
}
```

### Client-side Receiver (Server to Client)

```java
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModNetworkingClient {
    private static final ResourceLocation UPDATE_PACKET =
        new ResourceLocation("blockpops", "update_packet");

    public static void init() {
        NetworkManager.registerReceiver(
            NetworkManager.s2c(), // or NetworkManager.Side.S2C
            UPDATE_PACKET,
            (buf, context) -> {
                // Read data
                int value = buf.readInt();

                context.queue(() -> {
                    // Handle on client thread
                    // Update client-side state...
                });
            }
        );
    }
}
```

## Sending Packets

### Sending from Client to Server

```java
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientPacketHandler {
    public static void sendExamplePacket(String message) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        // Write data to buffer
        buf.writeUtf(message);

        // Send to server
        NetworkManager.sendToServer(
            new ResourceLocation("blockpops", "example_packet"),
            buf
        );
    }
}
```

### Sending from Server to Client

```java
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ServerPacketHandler {
    // Send to a specific player
    public static void sendUpdateToPlayer(ServerPlayer player, int value) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeInt(value);

        NetworkManager.sendToPlayer(
            player,
            new ResourceLocation("blockpops", "update_packet"),
            buf
        );
    }

    // Send to multiple players
    public static void sendUpdateToPlayers(Iterable<ServerPlayer> players, int value) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        buf.writeInt(value);

        NetworkManager.sendToPlayers(
            players,
            new ResourceLocation("blockpops", "update_packet"),
            buf
        );
    }
}
```

## Checking Network Capabilities

### Check if Server Can Receive

```java
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientHelper {
    public static void checkServerCapabilities() {
        ResourceLocation packetId = new ResourceLocation("blockpops", "example_packet");

        if (NetworkManager.canServerReceive(packetId)) {
            // Server has this packet registered, safe to send
        } else {
            // Server doesn't have this packet, don't send
        }
    }
}
```

### Check if Player Can Receive

```java
public class ServerHelper {
    public static void sendIfCapable(ServerPlayer player) {
        ResourceLocation packetId = new ResourceLocation("blockpops", "update_packet");

        if (NetworkManager.canPlayerReceive(player, packetId)) {
            // Player's client has this packet registered
            // Safe to send
        }
    }
}
```

## Entity Spawn Packets

Custom entities require special spawn packets:

```java
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

public class CustomEntity extends Entity {
    // ... entity implementation ...

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }
}
```

### Additional Entity Spawn Data

Implement `EntitySpawnExtension` to send/receive additional data:

```java
import dev.architectury.extensions.network.EntitySpawnExtension;
import net.minecraft.network.FriendlyByteBuf;

public class CustomEntity extends Entity implements EntitySpawnExtension {

    @Override
    public void saveAdditionalSpawnData(FriendlyByteBuf buf) {
        // Write additional data when spawning
        buf.writeInt(customData);
    }

    @Override
    public void loadAdditionalSpawnData(FriendlyByteBuf buf) {
        // Read additional data when spawning
        customData = buf.readInt();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }
}
```

## Packet Context

The `PacketContext` provides useful information:

```java
NetworkManager.registerReceiver(side, id, (buf, context) -> {
    // Get the player who sent/is receiving the packet
    Player player = context.getPlayer();

    // Get the environment (CLIENT or SERVER)
    Env environment = context.getEnvironment();
    EnvType envType = context.getEnv();

    // Queue work on the main thread (IMPORTANT!)
    context.queue(() -> {
        // This code runs on the main game thread
        // Safe to access game state here
    });
});
```

## Advanced: Packet Transformers (Experimental)

Packet transformers allow you to modify packets before they're sent:

```java
import dev.architectury.networking.transformers.PacketTransformer;
import java.util.List;

NetworkManager.registerReceiver(
    side,
    id,
    List.of(/* packet transformers */),
    (buf, context) -> {
        // Handle packet
    }
);
```

**Note**: This feature is experimental and may change in future versions.

## Complete Example: Chat Sync

### Common Code (Both Sides)

```java
public class ChatSyncNetworking {
    public static final ResourceLocation CHAT_SYNC_PACKET =
        new ResourceLocation("blockpops", "chat_sync");
}
```

### Server-side Registration

```java
public class ServerNetworking {
    public static void init() {
        NetworkManager.registerReceiver(
            NetworkManager.c2s(),
            ChatSyncNetworking.CHAT_SYNC_PACKET,
            (buf, context) -> {
                String message = buf.readUtf();

                context.queue(() -> {
                    ServerPlayer player = (ServerPlayer) context.getPlayer();

                    // Process message on server
                    System.out.println(player.getName().getString() + ": " + message);

                    // Broadcast to all players
                    FriendlyByteBuf response = new FriendlyByteBuf(Unpooled.buffer());
                    response.writeUtf(player.getName().getString() + ": " + message);

                    NetworkManager.sendToPlayers(
                        player.server.getPlayerList().getPlayers(),
                        ChatSyncNetworking.CHAT_SYNC_PACKET,
                        response
                    );
                });
            }
        );
    }
}
```

### Client-side Registration

```java
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientNetworking {
    public static void init() {
        NetworkManager.registerReceiver(
            NetworkManager.s2c(),
            ChatSyncNetworking.CHAT_SYNC_PACKET,
            (buf, context) -> {
                String message = buf.readUtf();

                context.queue(() -> {
                    // Display message on client
                    Minecraft.getInstance().gui.getChat().addMessage(
                        Component.literal(message)
                    );
                });
            }
        );
    }

    public static void sendChatMessage(String message) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(message);

        NetworkManager.sendToServer(ChatSyncNetworking.CHAT_SYNC_PACKET, buf);
    }
}
```

## Best Practices

1. **Always Use context.queue()**: Never modify game state directly in the network handler. Always queue work to run on the main thread.

2. **Validate Data**: Always validate data received from the network, especially from clients.

3. **Check Capabilities**: Use `canServerReceive()` and `canPlayerReceive()` before sending packets to avoid errors.

4. **Separate Client Code**: Use `@Environment(EnvType.CLIENT)` for client-only networking code.

5. **Resource Location Naming**: Use your mod ID as the namespace for all packet IDs.

6. **Buffer Management**: Create new buffers for each packet send. Don't reuse buffers.

7. **Thread Safety**: Remember that network receivers run on the network thread. Use `context.queue()` for any game state access.

## Common FriendlyByteBuf Methods

### Writing Data

```java
buf.writeInt(42);
buf.writeFloat(3.14f);
buf.writeDouble(2.718);
buf.writeBoolean(true);
buf.writeUtf("Hello");
buf.writeUUID(uuid);
buf.writeBlockPos(pos);
buf.writeResourceLocation(id);
buf.writeNbt(compoundTag);
```

### Reading Data

```java
int value = buf.readInt();
float f = buf.readFloat();
double d = buf.readDouble();
boolean flag = buf.readBoolean();
String str = buf.readUtf();
UUID uuid = buf.readUUID();
BlockPos pos = buf.readBlockPos();
ResourceLocation id = buf.readResourceLocation();
CompoundTag tag = buf.readNbt();
```

## Troubleshooting

### Packet Not Received

- Check that the receiver is registered on the correct side
- Verify the ResourceLocation matches exactly
- Ensure `@Environment` annotations are correct
- Check that mods are present on both client and server

### Crashes on Packet Receive

- Make sure you're using `context.queue()` for game state access
- Verify buffer read/write operations match
- Check for null values and validate data

### Desync Issues

- Ensure client and server have the same packet format
- Validate all data on the receiving end
- Don't trust client-sent data without verification

## See Also

- [Events](05-events-overview.md) - Using events as an alternative to custom packets
- [Platform Abstraction](02-platform-abstraction.md) - Detecting client vs server
- Minecraft Wiki: https://minecraft.fandom.com/wiki/Data_types

---

*Documentation generated from Architectury API 9.2.14 source code*
