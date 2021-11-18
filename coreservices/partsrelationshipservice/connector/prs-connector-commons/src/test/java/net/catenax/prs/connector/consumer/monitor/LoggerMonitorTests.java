package net.catenax.prs.connector.consumer.monitor;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class LoggerMonitorTests {

    TestLogHandler handler;
    LoggerMonitor sut = new LoggerMonitor();
    Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        Logger logger = Logger.getLogger(LoggerMonitor.class.getName());
        handler = new TestLogHandler();
        handler.setLevel(Level.ALL);
        //To prevent forwarding to other handlers.
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
    }

    @Test
    public void verifyInfoLevel() {
        //Arrange
        var message = faker.lorem().sentence();

        //Act
        sut.info(message);

        //Assert
        assertThat(handler.getMessage()).isEqualTo(message);
        assertThat(handler.getLevel()).isEqualTo(Level.INFO);
    }
}
