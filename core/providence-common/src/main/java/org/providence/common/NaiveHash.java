package org.providence.common;

import java.util.UUID;

import org.providence.common.dao.dto.Shared;

public class NaiveHash {

    public static long getNaiveHash(String text) {
        UUID uuid = UUID.nameUUIDFromBytes(text.getBytes());

        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }

    public static long getNaiveHash(Shared shared) {
        return getNaiveHash(shared.getSharedText());
    }
}
