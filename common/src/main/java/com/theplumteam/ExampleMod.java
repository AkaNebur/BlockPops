package com.theplumteam;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.text.Text;

public final class ExampleMod {
    public static final String MOD_ID = "blockpops";

    public static void init() {
        // Register event listener for when a player joins a world
        PlayerEvent.PLAYER_JOIN.register((player) -> {
            // Send "Hello World" message to the player when they join
            player.sendMessage(Text.literal("Hello World!"));
        });
    }
}
