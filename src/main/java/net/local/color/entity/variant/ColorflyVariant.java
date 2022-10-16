package net.local.color.entity.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum ColorflyVariant {
    GREEN(0),
    BLUE(1);

    private static final ColorflyVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator
            .comparingInt(ColorflyVariant::getId)).toArray(ColorflyVariant[]::new);
    private final int id;

    ColorflyVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ColorflyVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
