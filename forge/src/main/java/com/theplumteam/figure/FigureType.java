package com.theplumteam.figure;

import net.minecraft.util.StringRepresentable;

public enum FigureType implements StringRepresentable {
    NONE("none"),
    DEFAULT("default");

    private final String name;

    FigureType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public static FigureType fromString(String name) {
        for (FigureType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return NONE;
    }

    public boolean hasFigure() {
        return this != NONE;
    }

    public String getModelPath() {
        if (this == NONE) {
            return null;
        }
        return "geo/figure/box_figure_" + name + ".geo.json";
    }
}
