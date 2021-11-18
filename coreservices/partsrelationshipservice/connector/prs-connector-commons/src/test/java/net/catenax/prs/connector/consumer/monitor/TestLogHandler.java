package net.catenax.prs.connector.consumer.monitor;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

@Getter
public class TestLogHandler extends Handler {

    private final List<LogRecord> records = new ArrayList<>();

    @Override
    public void publish(LogRecord record) {
        records.add(record);
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
