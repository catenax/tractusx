package org.eclipse.dataspaceconnector.extensions.job;

import java.util.Collection;

public interface JobStore {
    Job find(String id);

    Job findByProcessId(String id);

    void create(Job job);

    Collection<Job> nextForState(JobState state, int max);

    void delete(String processId);

    void update(Job job);

}
