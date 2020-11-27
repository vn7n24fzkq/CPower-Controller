package io.github.vn7n24fzkq.cpower.content;

public enum InstantMessageAlignment {
    Left((byte) 0),
    Center((byte) 1),
    Right((byte) 2);
    public final byte value;

    private InstantMessageAlignment(byte value) {
        this.value = value;
    }
}
