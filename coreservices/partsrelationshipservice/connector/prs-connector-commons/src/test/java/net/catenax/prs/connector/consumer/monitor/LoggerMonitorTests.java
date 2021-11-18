package net.catenax.prs.connector.consumer.monitor;

import com.github.javafaker.Faker;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class LoggerMonitorTests {

    static Faker faker = new Faker();
    TestLogHandler handler = new TestLogHandler();;
    LoggerMonitor sut = new LoggerMonitor();

    @BeforeEach
    public void setUp() {
        Logger logger = Logger.getLogger(LoggerMonitor.class.getName());
        handler.setLevel(Level.ALL);
        //To prevent forwarding to other handlers.
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
    }

    private static Stream<Arguments> logWithErrorsArguments() {
        return Stream.of(
                Arguments.of(faker.lorem().sentence(), new Throwable[]{new RuntimeException()}),
                Arguments.of(faker.lorem().sentence(), new Throwable[]{new RuntimeException(), new Exception()})
        );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("logWithErrorsArguments")
    public void verifyInfoLog(String message, Throwable... errors) {

        //Act
        sut.info(message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.INFO, e)).toArray(Tuple[]::new)
                );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("logWithErrorsArguments")
    public void verifyWarningLog(String message, Throwable... errors) {

        //Act
        sut.warning(message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.WARNING, e)).toArray(Tuple[]::new)
                );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("logWithErrorsArguments")
    public void verifySevereLog(String message, Throwable... errors) {

        //Act
        sut.severe(message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.SEVERE, e)).toArray(Tuple[]::new)
                );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("logWithErrorsArguments")
    public void verifyDebugLog(String message, Throwable... errors) {

        //Act
        sut.debug(message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.FINE, e)).toArray(Tuple[]::new)
                );
    }

    @Test
    public void verifyInfoLog() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.info(message);

        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        tuple(message, Level.INFO, null)
                );
    }

    @Test
    public void verifySevereLog() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.severe(message);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        tuple(message, Level.SEVERE, null)
                );
    }

    @Test
    public void verifyWarningLog() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.warning(message);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        tuple(message, Level.WARNING, null)
                );
    }

    @Test
    public void verifyDebugLog() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.debug(message);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        tuple(message, Level.FINE, null)
                );
    }
}
