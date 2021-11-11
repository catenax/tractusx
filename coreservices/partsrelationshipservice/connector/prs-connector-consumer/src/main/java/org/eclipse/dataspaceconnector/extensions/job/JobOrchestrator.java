package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.extensions.transferprocess.SequentTransferProcess;
import org.eclipse.dataspaceconnector.extensions.transferprocess.TransferProcessFile;
import org.eclipse.dataspaceconnector.extensions.transferprocess.TransferProcessFileHandler;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessListener;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.util.Collection;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class JobOrchestrator implements TransferProcessListener {

    private final TransferProcessManager processManager;
    private final JobStore jobStore;
    private final TransferProcessFileHandler transferProcessFileHandler;

    public JobOrchestrator(TransferProcessManager processManager, JobStore jobStore, TransferProcessFileHandler transferProcessFileHandler) {
        this.processManager = processManager;
        this.jobStore = jobStore;
        this.transferProcessFileHandler = transferProcessFileHandler;
    }

    public JobInitiateResponse startJob(String filename, String connectorAddress, String destinationPath) {
        Job job = Job.Builder.newInstance()
                .id(randomUUID().toString())
                .filename(filename)
                .destinationPath(destinationPath)
                .state(JobState.UNSAVED)
                .build();
        jobStore.create(job);

        startTransferProcess(job, filename, connectorAddress);

        return JobInitiateResponse.Builder.newInstance().id(job.getId()).status(ResponseStatus.OK).build();
    }

    public void startTransferProcess(Job job, String filename, String connectorAddress) {
        var dataRequest = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString()) //this is not relevant, thus can be random
                .connectorAddress(connectorAddress) //the address of the provider connector
                .protocol("ids-rest") //must be ids-rest
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance() //the data entry is the source asset
                        .id(filename)
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type("File") //the provider uses this to select the correct DataFlowController
                        .property("path", job.getDestinationPath()) //where we want the file to be stored
                        .build())
                .managedResources(false) //we do not need any provisioning
                .build();

        TransferInitiateResponse response = processManager.initiateConsumerRequest(dataRequest);

        job.addTransferProcess(response.getId());
        job.transitionInProgress();
        jobStore.update(job);
    }

    public void handleTransferProcessCompleted(Job job, TransferProcess transferProcess) {
        TransferProcessFile result = transferProcessFileHandler.parse(transferProcess);
        Collection<SequentTransferProcess> transferProcesses = result.getTransferProcesses();

        for (SequentTransferProcess process : transferProcesses) {
            startTransferProcess(job, process.getFile(), process.getConnectorUrl());
        }
    }

    @Override
    public void completed(TransferProcess process) {
        Job job = jobStore.findByProcessId(process.getId());
        handleTransferProcessCompleted(job, process);
    }

    public Job findJob(String jobId) {
        return jobStore.find(jobId);
    }
}
