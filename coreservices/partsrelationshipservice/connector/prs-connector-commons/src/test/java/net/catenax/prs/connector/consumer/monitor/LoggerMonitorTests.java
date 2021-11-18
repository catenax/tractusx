package net.catenax.prs.connector.consumer.monitor;

import com.github.javafaker.Faker;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    private static Stream<Arguments> provideLogDataWithErrors() {
        return Stream.of(
                Arguments.of(faker.lorem().sentence(), new Throwable[]{new RuntimeException()}),
                Arguments.of(faker.lorem().sentence(), new Throwable[]{new RuntimeException(), new Exception()})
        );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("provideLogDataWithErrors")
    public void loggedOnInfoLevel_WithErrors(String message, Throwable... errors) {

        //Act
        sut.info(() -> message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.INFO, e)).toArray(Tuple[]::new)
                );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("provideLogDataWithErrors")
    public void loggedOnWarningLevel_WithErrors(String message, Throwable... errors) {

        //Act
        sut.warning(() -> message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.WARNING, e)).toArray(Tuple[]::new)
                );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("provideLogDataWithErrors")
    public void loggedOnSevereLevel_WithErrors(String message, Throwable... errors) {

        //Act
        sut.severe(() -> message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.SEVERE, e)).toArray(Tuple[]::new)
                );
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource("provideLogDataWithErrors")
    public void loggedOnDebugLevel_WithErrors(String message, Throwable... errors) {

        //Act
        sut.debug(() -> message, errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactlyInAnyOrder(
                        Arrays.stream(errors).map(e -> tuple(message, Level.FINE, e)).toArray(Tuple[]::new)
                );
    }

    @Test
    public void loggedOnSevereLevel_WithParams() {

        // Arrange
        Map<String, Object> errors = new HashMap<>();
        var message = faker.lorem().sentence();
        var extraParams = faker.lorem().sentence();
        errors.put(message, extraParams);

        //Act
        sut.severe(errors);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown, LogRecord::getParameters)
                .containsExactly(
                        tuple(message, Level.SEVERE, null, new Object[]{extraParams})
                );
    }

    @Test
    public void loggedOnInfoLevel() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.info(() -> message);

        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactly(
                        tuple(message, Level.INFO, null)
                );
    }

    @Test
    public void loggedOnSevereLevel() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.severe(() -> message);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactly(
                        tuple(message, Level.SEVERE, null)
                );
    }

    @Test
    public void loggedOnWarningLevel() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.warning(() -> message);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactly(
                        tuple(message, Level.WARNING, null)
                );
    }

    @Test
    public void loggedOnDebugLevel() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.debug(() -> message);

        //Assert
        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactly(
                        tuple(message, Level.FINE, null)
                );
    }

    /**
     * Additional test to verify {@link LoggerMonitor} varargs null check is in place.
     */
    @Test
    public void loggedOnSevereLevel_WithNullVarArgs() {

        // Arrange
        String message = faker.lorem().sentence();

        //Act
        sut.severe(() -> message, null);

        assertThat(handler.getRecords()).extracting(
                        LogRecord::getMessage, LogRecord::getLevel, LogRecord::getThrown)
                .containsExactly(
                        tuple(message, Level.SEVERE, null)
                );
    }
}
