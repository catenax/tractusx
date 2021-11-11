package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.EdcException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class InMemoryJobStore implements JobStore {

    private static final int TIMEOUT = 1000;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, Job> jobsById = new HashMap<>();
    private final Map<JobState, List<Job>> stateCache = new HashMap<>();

    @Override
    public Job find(String id) {
        return readLock(() -> jobsById.get(id));
    }

    @Override
    public Job findByProcessId(String id) {
        return readLock(() -> jobsById.values().stream()
                .filter(j -> j.getTransferProcessIds().contains(id))
                .findFirst().orElseThrow(() -> new EdcException("Unable to find job for process id " + id)));
    }

    @Override
    public void create(Job job) {
        writeLock(() -> {
            job.transitionInitial();
            delete(job.getId());
            jobsById.put(job.getId(), job);
            stateCache.computeIfAbsent(job.getState(), k -> new ArrayList<>()).add(job);
            return null;
        });
    }

    @Override
    public List<Job> nextForState(JobState state, int max) {
        return readLock(() -> {
            var set = stateCache.get(state);
            return set == null ? Collections.emptyList() : set.stream()
                    .sorted(Comparator.comparingLong(Job::getStateTimestamp)) //order by state timestamp, oldest first
                    .limit(max)
                    .collect(toList());
        });
    }

    @Override
    public void delete(String processId) {
        writeLock(() -> {
            Job job = jobsById.remove(processId);
            if (job != null) {
                var tempCache = new HashMap<JobState, List<Job>>();
                stateCache.forEach((key, value) -> {
                    var list = value.stream().filter(p -> !p.getId().equals(processId)).collect(Collectors.toCollection(ArrayList::new));
                    tempCache.put(key, list);
                });
                stateCache.clear();
                stateCache.putAll(tempCache);
            }
            return null;
        });
    }

    @Override
    public void update(Job job) {
        writeLock(() -> {
            job.updateStateTimestamp();
            delete(job.getId());
            jobsById.put(job.getId(), job);
            stateCache.computeIfAbsent(job.getState(), k -> new ArrayList<>()).add(job);
            return null;
        });
    }

    private <T> T readLock(Supplier<T> work) {
        try {
            if (!lock.readLock().tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
                throw new EdcException("Timeout acquiring read lock");
            }
            try {
                return work.get();
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new EdcException(e);
        }
    }

    private <T> T writeLock(Supplier<T> work) {
        try {
            if (!lock.writeLock().tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
                throw new EdcException("Timeout acquiring write lock");
            }
            try {
                return work.get();
            } finally {
                lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new EdcException(e);
        }
    }
}
