package org.eclipse.dataspaceconnector.extensions.job;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;

public class Job {

    private String id;
    private String filename;
    private String destinationPath;
    private JobState state;
    private long stateTimestamp;
    private Set<String> transferProcessIds;

    private Job() {
    }

    public String getId() {
        return id;
    }

    public JobState getState() {
        return state;
    }

    public Set<String> getTransferProcessIds() {
        return transferProcessIds;
    }

    public long getStateTimestamp() {
        return stateTimestamp;
    }

    public String getFilename() {
        return filename;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void transitionInitial() {
        transition(JobState.INITIAL, JobState.UNSAVED);
    }

    public void transitionInProgress() {
        transition(JobState.IN_PROGRESS, JobState.INITIAL, JobState.IN_PROGRESS);
    }

    public void addTransferProcess(String transferProcessId) {
        transferProcessIds.add(transferProcessId);
    }

    public void updateStateTimestamp() {
        stateTimestamp = Instant.now().toEpochMilli();
    }

    private void transition(JobState end, JobState... starts) {
        if (Arrays.stream(starts).noneMatch(s -> s == state)) {
            throw new IllegalStateException(format("Cannot transition from state %s to %s", state, end));
        }
        state = end;
        updateStateTimestamp();
    }

    public static class Builder {
        private final Job job;

        private Builder() {
            job = new Job();
        }

        public static Job.Builder newInstance() {
            return new Job.Builder();
        }

        public Job.Builder id(String id) {
            job.id = id;
            return this;
        }

        public Job.Builder transferProcessIds(String... ids) {
            job.transferProcessIds = Set.of(ids);
            return this;
        }

        public Job.Builder state(JobState state) {
            job.state = state;
            return this;
        }

        public Job.Builder filename(String filename) {
            job.filename = filename;
            return this;
        }

        public Job.Builder destinationPath(String destinationPath) {
            job.destinationPath = destinationPath;
            return this;
        }

        public Job.Builder stateTimestamp(long value) {
            job.stateTimestamp = value;
            return this;
        }

        public Job build() {
            Objects.requireNonNull(job.id, "id");
            if (job.state == JobState.UNSAVED && job.stateTimestamp == 0) {
                job.stateTimestamp = Instant.now().toEpochMilli();
            }
            if (job.transferProcessIds == null) {
                job.transferProcessIds = new HashSet<>();
            }
            return job;
        }
    }
}
