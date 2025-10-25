# Block Entity Synchronization - Quick Reference

## TL;DR - Real-Time Sync Implementation

To enable real-time synchronization of block entity data from server to client:

### 1. Add Required Imports

```java
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
```

### 2. Override Three Methods

```java
// For chunk load AND real-time sync
@Override
public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    saveAdditional(tag); // Include your custom data
    return tag;
}

// For real-time updates (triggers immediately)
@Override
public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
}

// Receive real-time updates on client
@Override
public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
    CompoundTag tag = packet.getTag();
    if (tag != null) {
        load(tag);
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
}
```

### 3. Trigger Sync When Data Changes

```java
public void setYourData(YourType data) {
    this.yourField = data;
    setChanged(); // Mark for disk save
    if (level != null && !level.isClientSide) {
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
}
```

## Key Points

1. **`getUpdateTag()`** is used for BOTH chunk loading and real-time sync (via `getUpdatePacket()`)
2. **`getUpdatePacket()`** returns a packet that internally uses `getUpdateTag()` for data
3. **`onDataPacket()`** receives the packet on the client and updates local fields
4. **`level.sendBlockUpdated()`** on server triggers the real-time sync
5. **No GeckoLib-specific handling needed** - renderer reads data directly each frame

## Common Issues

### Updates Don't Work
- Missing `getUpdatePacket()` or `onDataPacket()` override
- Not calling `level.sendBlockUpdated()` after data changes
- Calling `sendBlockUpdated()` on client instead of server

### Updates Only Work After Chunk Reload
- You only have `getUpdateTag()` and `handleUpdateTag()`
- Need to add `getUpdatePacket()` and `onDataPacket()` for real-time sync

### Packet Contains No Data
- `getUpdateTag()` doesn't call `saveAdditional(tag)`
- Your custom fields aren't being serialized to NBT

## File Locations

- **Full Guide:** `docs/BlockEntity-Synchronization-Guide.md`
- **Implementation:** `forge/src/main/java/com/theplumteam/blockentity/BoxBlockEntity.java`

---

**Minecraft Version:** 1.20.1
**Forge Version:** 47.4.9
**Last Updated:** 2025-10-25
