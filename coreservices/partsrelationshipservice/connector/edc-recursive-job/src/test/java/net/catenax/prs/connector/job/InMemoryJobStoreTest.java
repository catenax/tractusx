package net.catenax.prs.connector.job;

import com.github.javafaker.Faker;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class InMemoryJobStoreTest {

    Monitor monitor = new ConsoleMonitor();
    InMemoryJobStore sut = new InMemoryJobStore(monitor);
    Faker faker = new Faker();
    TestMother generate = new TestMother();
    MultiTransferJob job = generate.job(JobState.UNSAVED);
    MultiTransferJob job2 = generate.job(JobState.UNSAVED);
    MultiTransferJob originalJob = job.toBuilder().build();
    String otherJobId = faker.lorem().characters();
    String processId = faker.lorem().characters();
    String processId2 = faker.lorem().characters();
    String errorDetail = faker.lorem().sentence();

    @Test
    void find_WhenNotFound() {
        assertThat(sut.find(otherJobId)).isEmpty();
    }

    @Test
    void findByProcessId_WhenFound() {
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        sut.create(job2);
        sut.addTransferProcess(job2.getJobId(), processId2);

        assertThat(sut.findByProcessId(processId)).contains(job);
    }

    @Test
    void findByProcessId_WhenNotFound() {
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);

        assertThat(sut.findByProcessId(processId2)).isEmpty();
    }

    @Test
    void create_and_find() {
        sut.create(job);
        assertThat(sut.find(job.getJobId())).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(originalJob.toBuilder().state(JobState.INITIAL).build());
        assertThat(sut.find(otherJobId)).isEmpty();
    }

    @Test
    void addTransferProcess() {
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        assertThat(job.getTransferProcessIds()).containsExactly(processId);
    }

    @Test
    void completeTransferProcess_WhenJobNotFound() {
        sut.completeTransferProcess(otherJobId, processId);
    }

    @Test
    void completeTransferProcess_WhenTransferFound() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);

        // Act
        sut.completeTransferProcess(job.getJobId(), processId);

        // Assert
        assertThat(job.getTransferProcessIds()).isEmpty();
    }

    @Test
    void completeTransferProcess_WhenTransferNotFound() {
        // Act
        sut.completeTransferProcess(job.getJobId(), processId);

        // Assert
        assertThat(job.getTransferProcessIds()).isEmpty();
    }

    @Test
    void completeTransferProcess_WhenTransferAlreadyCompleted() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        sut.completeTransferProcess(job.getJobId(), processId);

        // Act
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> sut.completeTransferProcess(job.getJobId(), processId));

        // Assert
        assertThat(job.getTransferProcessIds()).isEmpty();
    }

    @Test
    void completeTransferProcess_WhenNotLastTransfer_DoesNotTransitionJob() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        sut.addTransferProcess(job.getJobId(), processId2);

        // Act
        sut.completeTransferProcess(job.getJobId(), processId);

        // Assert
        assertThat(job.getState()).isEqualTo(JobState.IN_PROGRESS);
    }

    @Test
    void completeTransferProcess_WhenLastTransfer_TransitionsJob() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        sut.addTransferProcess(job.getJobId(), processId2);

        // Act
        sut.completeTransferProcess(job.getJobId(), processId);
        sut.completeTransferProcess(job.getJobId(), processId2);

        // Assert
        assertThat(job.getState()).isEqualTo(JobState.TRANSFERS_FINISHED);
    }

    @Test
    void completeJob_WhenJobNotFound() {
        // Arrange
        sut.create(job);
        // Act
        sut.completeJob(otherJobId);
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.INITIAL);
    }

    @Test
    void completeJob_WhenJobInInitialState() {
        // Arrange
        sut.create(job);
        sut.create(job2);
        // Act
        sut.completeJob(job.getJobId());
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.COMPLETED);
        assertThat(job2.getState()).isEqualTo(JobState.INITIAL);
    }

    @Test
    void completeJob_WhenJobInTransfersCompletedState() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        sut.completeTransferProcess(job.getJobId(), processId);
        // Act
        sut.completeJob(job.getJobId());
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.COMPLETED);
    }

    @Test
    void completeJob_WhenJobInTransfersInProgressState() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        // Act
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> sut.completeJob(job.getJobId()));
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.IN_PROGRESS);
    }

    @Test
    void markJobInError_WhenJobNotFound() {
        // Arrange
        sut.create(job);
        // Act
        sut.markJobInError(otherJobId, errorDetail);
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.INITIAL);
    }

    @Test
    void markJobInError_WhenJobInInitialState() {
        // Arrange
        sut.create(job);
        sut.create(job2);
        // Act
        sut.markJobInError(job.getJobId(), errorDetail);
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.ERROR);
        assertThat(job2.getState()).isEqualTo(JobState.INITIAL);
        assertThat(job.getErrorDetail()).isEqualTo(errorDetail);
    }

    @Test
    void markJobInError_WhenJobInTransfersCompletedState() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        sut.completeTransferProcess(job.getJobId(), processId);
        // Act
        sut.markJobInError(job.getJobId(), errorDetail);
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.ERROR);
    }

    @Test
    void markJobInError_WhenJobInTransfersInProgressState() {
        // Arrange
        sut.create(job);
        sut.addTransferProcess(job.getJobId(), processId);
        // Act
        sut.markJobInError(job.getJobId(), errorDetail);
        // Assert
        assertThat(job.getState()).isEqualTo(JobState.ERROR);
    }
}