package org.eclipse.dataspaceconnector.extensions.job;

import java.util.Arrays;

public enum JobState {
    UNSAVED(100),
    INITIAL(100),
    IN_PROGRESS(600),
    COMPLETED(800),
    ERROR(-1);

    private final int code;

    JobState(int code) {
        this.code = code;
    }

    public static JobState from(int code) {
        return Arrays.stream(values()).filter(tps -> tps.code == code).findFirst().orElse(null);
    }

    public int code() {
        return code;
    }
}
