//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.job;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

/**
 * Entity for recursive jobs that potentially comprise multiple transfers.
 */
@ToString
@Builder(toBuilder = true)
public class MultiTransferJob {

    /**
     * Job identifier.
     */
    @Getter
    private final String jobId;
    /**
     * Collection of transfer IDs that have not yet completed for the job.
     */
    @Singular
    private final Set<String> transferProcessIds;
    /**
     * Job state.
     */
    @Getter
    private JobState state;
    /**
     * Arbitrary data attached to the job.
     */
    @Getter
    @Singular("jobDatum")
    private Map<String, String> jobData;
    /**
     * Error detail, potentially set if {@link #getState() state} is {@link JobState#ERROR}.
     */
    @Getter
    private String errorDetail;
    @Getter
    /**
     * Collection of transfers that have completed for the job.
     */
    @Singular
    private List<TransferProcess> completedTransfers;

    /**
     * Transition the job to the {@link JobState#INITIAL} state.
     */
    /* package */ void transitionInitial() {
        transition(JobState.INITIAL, JobState.UNSAVED);
    }

    /**
     * Transition the job to the {@link JobState#IN_PROGRESS} state.
     */
    /* package */ void transitionInProgress() {
        transition(JobState.IN_PROGRESS, JobState.INITIAL, JobState.IN_PROGRESS);
    }

    /**
     * Transition the job to the {@link JobState#TRANSFERS_FINISHED} state.
     */
    /* package */ void transitionTransfersFinished() {
        transition(JobState.TRANSFERS_FINISHED, JobState.IN_PROGRESS);
    }

    /**
     * Transition the job to the {@link JobState#COMPLETED} state.
     */
    /* package */ void transitionComplete() {
        transition(JobState.COMPLETED, JobState.TRANSFERS_FINISHED, JobState.INITIAL);
    }

    /**
     * Transition the job to the {@link JobState#ERROR} state.
     */
    /* package */ void transitionError(final @Nullable String errorDetail) {
        state = JobState.ERROR;
        this.errorDetail = errorDetail;
    }

    private void transition(final JobState end, final JobState... starts) {
        if (Arrays.stream(starts).noneMatch(s -> s == state)) {
            throw new IllegalStateException(format("Cannot transition from state %s to %s", state, end));
        }
        state = end;
    }

    /* package */ Collection<String> getTransferProcessIds() {
        return Collections.unmodifiableSet(this.transferProcessIds);
    }
}
