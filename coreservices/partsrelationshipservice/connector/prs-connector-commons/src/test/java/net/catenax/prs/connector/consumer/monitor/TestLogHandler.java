package net.catenax.prs.connector.consumer.monitor;


import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class TestLogHandler extends Handler {

    private LogRecord logRecord;

    @Override
    public void publish(LogRecord record) {
        logRecord = record;
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }

    public LogRecord getLogRecord() {
        return logRecord;
    }
}
