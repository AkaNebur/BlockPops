package com.theplumteam;

import com.theplumteam.registry.ModBlockEntities;
import com.theplumteam.registry.ModBlocks;

public final class ExampleMod {
    public static final String MOD_ID = "blockpops";

    public static void init() {
        // Register blocks and block entities
        ModBlocks.register();
        ModBlockEntities.register();
    }
}
