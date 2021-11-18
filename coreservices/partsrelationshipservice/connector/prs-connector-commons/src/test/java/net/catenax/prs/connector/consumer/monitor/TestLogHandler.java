package net.catenax.prs.connector.consumer.monitor;

import lombok.Getter;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

@Getter
public class TestLogHandler extends Handler {

    private Level level;
    private String message;

    @Override
    public void publish(LogRecord record) {
        level = record.getLevel();
        message = record.getMessage();
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
