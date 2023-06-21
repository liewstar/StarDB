package top.liaoxin.StarDB.backend.utils;

import java.nio.ByteBuffer;

public class Parser {

    public static long parseLong(byte[] buf) {
        ByteBuffer buffer = ByteBuffer.wrap(buf, 0, 8);
        return buffer.getLong();
    }
}
