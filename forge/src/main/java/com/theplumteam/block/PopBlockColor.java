package com.theplumteam.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.MapColor;

public enum PopBlockColor implements StringRepresentable {
    ORIGINAL("original", MapColor.TERRACOTTA_WHITE),
    BLACK("black", MapColor.COLOR_BLACK),
    BLUE("blue", MapColor.COLOR_BLUE),
    BROWN("brown", MapColor.COLOR_BROWN),
    CYAN("cyan", MapColor.COLOR_CYAN),
    GRAY("gray", MapColor.COLOR_GRAY),
    GREEN("green", MapColor.COLOR_GREEN),
    LIGHT_BLUE("light_blue", MapColor.COLOR_LIGHT_BLUE),
    LIGHT_GRAY("light_gray", MapColor.COLOR_LIGHT_GRAY),
    LIME("lime", MapColor.COLOR_LIGHT_GREEN),
    MAGENTA("magenta", MapColor.COLOR_MAGENTA),
    ORANGE("orange", MapColor.COLOR_ORANGE),
    PINK("pink", MapColor.COLOR_PINK),
    PURPLE("purple", MapColor.COLOR_PURPLE),
    RED("red", MapColor.COLOR_RED),
    YELLOW("yellow", MapColor.COLOR_YELLOW);

    private final String name;
    private final MapColor mapColor;

    PopBlockColor(String name, MapColor mapColor) {
        this.name = name;
        this.mapColor = mapColor;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public MapColor getMapColor() {
        return this.mapColor;
    }

    public String getTextureName() {
        return switch (this) {
            case ORIGINAL -> "original";
            case BLACK -> "black";
            case BLUE -> "blue";
            case BROWN -> "brown";
            case CYAN -> "cyan";
            case GRAY -> "gray";
            case GREEN -> "green";
            case LIGHT_BLUE -> "light_blue";
            case LIGHT_GRAY -> "light_gray";
            case LIME -> "lime";
            case MAGENTA -> "magenta";
            case ORANGE -> "orange";
            case PINK -> "pink";
            case PURPLE -> "purple";
            case RED -> "red";
            case YELLOW -> "yellow";
        };
    }
}
