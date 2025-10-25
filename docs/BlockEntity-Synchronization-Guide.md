# Block Entity Synchronization Guide

## Overview

This guide explains how block entity data synchronization works in Minecraft Forge 1.20.1, specifically in the context of GeckoLib animated block entities that need real-time updates for rendering.

## The Problem

When you have a block entity (like `BoxBlockEntity`) with custom data (figure position offsets, scale) that needs to be visible on the client side for rendering, you need to synchronize this data from the server to all clients. There are two different synchronization mechanisms in Minecraft:

1. **Chunk Load Synchronization** - Used when chunks are loaded/unloaded
2. **Real-Time Synchronization** - Used for immediate updates while players are watching

## Two Synchronization Systems

### 1. Chunk Load Synchronization (Initial State)

**Methods:**
- `getUpdateTag()` - Server side, called when chunk is sent to client
- `handleUpdateTag(CompoundTag)` - Client side, receives the chunk load data

**When Used:**
- Player enters the area (chunk loads)
- Player rejoins the world
- Chunk is reloaded

**Purpose:**
These methods ensure that when a player first sees your block entity, it has the correct data. However, they do NOT provide real-time updates.

### 2. Real-Time Synchronization (Live Updates)

**Methods:**
- `getUpdatePacket()` - Server side, creates a packet for immediate sync
- `onDataPacket(Connection, ClientboundBlockEntityDataPacket)` - Client side, receives real-time updates
- `getUpdatePacketTag()` - Server side, provides the NBT data for the packet

**When Used:**
- Called by `level.sendBlockUpdated()` on the server
- Triggers immediately when data changes
- Sends updates to all clients watching the block

**Purpose:**
These methods provide real-time synchronization, allowing clients to see changes immediately as they happen (like GUI slider adjustments).

## Implementation in BoxBlockEntity

### Required Imports

```java
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
```

### Method Flow

```
Server Side (Data Changes):
1. setFigureOffset() or setFigureScale() called
2. Update internal fields
3. Call setChanged() to mark dirty for saving
4. Call level.sendBlockUpdated() to trigger sync

Server Side (Packet Creation):
1. getUpdatePacket() is called automatically
2. Returns ClientboundBlockEntityDataPacket.create(this)
3. This internally calls getUpdateTag() to get the NBT data
4. getUpdateTag() returns NBT with all custom data (via saveAdditional)

Client Side (Packet Reception):
1. onDataPacket() receives the packet
2. Extract NBT tag from packet
3. Call load(tag) to update local fields
4. Call level.sendBlockUpdated() to request render refresh
5. Renderer reads new values on next frame
```

**Important Note:** In Minecraft 1.20.1, `ClientboundBlockEntityDataPacket.create(this)` internally calls `getUpdateTag()` to obtain the NBT data. This means `getUpdateTag()` serves BOTH purposes:
- Chunk load synchronization (called by chunk system)
- Real-time synchronization (called by `getUpdatePacket()`)

There is NO separate `getUpdatePacketTag()` method in Minecraft 1.20.1.

### Complete Implementation

```java
// ===== CHUNK LOAD SYNCHRONIZATION =====
// NOTE: getUpdateTag() is ALSO used by getUpdatePacket() for real-time sync!

@Override
public CompoundTag getUpdateTag() {
    // This is sent to the client when the chunk loads AND for real-time updates
    // ClientboundBlockEntityDataPacket.create(this) internally calls this method
    CompoundTag tag = super.getUpdateTag();
    saveAdditional(tag);
    return tag;
}

@Override
public void handleUpdateTag(CompoundTag tag) {
    // This is received on the client during chunk load
    load(tag);
}

// ===== REAL-TIME SYNCHRONIZATION =====

@Override
public Packet<ClientGamePacketListener> getUpdatePacket() {
    // Creates a packet for real-time synchronization
    // Called on the server when level.sendBlockUpdated() is invoked
    // ClientboundBlockEntityDataPacket.create(this) calls getUpdateTag() to get the data
    return ClientboundBlockEntityDataPacket.create(this);
}

@Override
public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
    // Receives the packet on the client for real-time updates
    // This is what actually makes the changes appear immediately
    CompoundTag tag = packet.getTag();
    if (tag != null) {
        load(tag);
        // Request render update so the changes are visible immediately
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
}
```

### Data Update Methods

```java
public void setFigureOffset(double x, double y, double z) {
    this.figureOffsetX = x;
    this.figureOffsetY = y;
    this.figureOffsetZ = z;
    setChanged(); // Mark for saving
    if (level != null && !level.isClientSide) {
        // Trigger real-time sync to clients
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
}
```

## GeckoLib Integration

### No Special GeckoLib Handling Required

GeckoLib does NOT require any special synchronization or cache invalidation. The renderer (`BoxBlockRenderer`) reads data directly from the block entity every frame:

```java
@Override
public void actuallyRender(PoseStack poseStack, BoxBlockEntity animatable, ...) {
    // These values are read fresh every frame
    double offsetX = animatable.getFigureOffsetX();
    double offsetY = animatable.getFigureOffsetY();
    double offsetZ = animatable.getFigureOffsetZ();
    double scale = animatable.getFigureScale();

    poseStack.translate(offsetX, offsetY, offsetZ);
    poseStack.scale((float) scale, (float) scale, (float) scale);
}
```

Once the block entity fields are updated on the client via `onDataPacket()`, the next render frame will automatically use the new values.

### GeckoLib's AnimatableInstanceCache

The `AnimatableInstanceCache` is for animation state, NOT for synchronizing custom data. It handles:
- Animation playback state
- Animation controllers
- Current animation time

Your custom positioning data (offsets, scale) is separate and uses standard Minecraft synchronization.

## Common Mistakes

### Mistake 1: Only Implementing getUpdateTag/handleUpdateTag

```java
// WRONG - Only works on chunk load
@Override
public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    saveAdditional(tag);
    return tag;
}
```

**Problem:** Updates only work when chunks reload (leaving/rejoining world).

**Solution:** Also implement `getUpdatePacket()` and `onDataPacket()`, and ensure `getUpdateTag()` includes custom data.

### Mistake 2: Not Overriding getUpdateTag

```java
// WRONG - Packet has no custom data
@Override
public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
}
// Missing getUpdateTag() override!
```

**Problem:** `ClientboundBlockEntityDataPacket.create()` calls `getUpdateTag()` internally. If you don't override it to include your custom data, only default block entity data is sent.

**Solution:** Override `getUpdateTag()` to call `saveAdditional()` with your custom NBT data.

### Mistake 3: Not Calling level.sendBlockUpdated

```java
// WRONG - Data changes but no sync
public void setFigureOffset(double x, double y, double z) {
    this.figureOffsetX = x;
    this.figureOffsetY = y;
    this.figureOffsetZ = z;
    setChanged(); // Only saves to disk
}
```

**Problem:** Data is updated locally but never sent to clients.

**Solution:** Call `level.sendBlockUpdated()` after changing data on the server.

### Mistake 4: Calling sendBlockUpdated on Client

```java
// WRONG - Called on client side
if (level != null) { // No client check!
    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
}
```

**Problem:** This would send packets from client to server (wrong direction) and cause issues.

**Solution:** Always check `!level.isClientSide` before calling `sendBlockUpdated()` in setter methods.

## Network Flow with GUI

When a player adjusts sliders in the GUI:

```
Client Side:
1. Player moves slider in FigurePositionScreen
2. Client sends FigurePositionPacket to server (via custom packet)

Server Side:
3. FigurePositionPacket.handle() receives packet
4. Calls boxBlockEntity.setFigureOffset()
5. setFigureOffset() updates fields and calls level.sendBlockUpdated()
6. getUpdatePacket() is called, creating ClientboundBlockEntityDataPacket
7. Packet is sent to ALL clients watching the block

All Clients (including the one who moved slider):
8. onDataPacket() receives the packet
9. load() updates local block entity fields
10. Next render frame, renderer reads new values
11. Figure position updates visually
```

## Performance Considerations

### Frequency of Updates

Be cautious about update frequency. Each `level.sendBlockUpdated()` call sends a packet to all watching clients.

**Good Practice:**
```java
// Only update when value actually changes
public void setFigureOffset(double x, double y, double z) {
    if (this.figureOffsetX == x && this.figureOffsetY == y && this.figureOffsetZ == z) {
        return; // No change, skip sync
    }
    // ... rest of method
}
```

**Consider Throttling:**
For continuous updates (like sliders), consider throttling on the client before sending packets:
```java
// Only send packet every 50ms (20 times per second max)
private long lastUpdateTime = 0;
if (System.currentTimeMillis() - lastUpdateTime < 50) {
    return;
}
lastUpdateTime = System.currentTimeMillis();
```

## Debugging Tips

### Enable Logging

The implementation includes extensive logging. Check both client and server logs:

```
Server Log:
[INFO] setFigureOffset called at [X, Y, Z] - X=0.5, Y=0.2, Z=0.3
[INFO] Sending block update to clients
[INFO] getUpdatePacket called - creating real-time sync packet: X=0.5, Y=0.2, Z=0.3, Scale=1.0

Client Log:
[INFO] onDataPacket received on client - before: X=0.0, Y=0.1, Z=0.0, Scale=1.0
[INFO] onDataPacket received on client - after: X=0.5, Y=0.2, Z=0.3, Scale=1.0
```

### Verify Packet Flow

1. Check server logs for "Sending block update to clients"
2. Check server logs for "getUpdatePacket called"
3. Check client logs for "onDataPacket received on client"
4. Verify values change from "before" to "after"

### Test Scenarios

1. **Chunk Load Test:** Place block, leave area, return - data should persist
2. **Real-Time Test:** Open GUI, move slider, check immediate visual update
3. **Multi-Client Test:** Have another player watch while you adjust - they should see updates
4. **Fast Update Test:** Rapidly move slider - should remain smooth (test throttling)

## References

- Minecraft BlockEntity JavaDoc
- Forge Documentation: Block Entity Synchronization
- GeckoLib 4.7.4 Documentation: Blocks
- ClientboundBlockEntityDataPacket source code

---

**Last Updated:** 2025-10-25
**Minecraft Version:** 1.20.1
**Forge Version:** 47.4.9
**GeckoLib Version:** 4.7.4
