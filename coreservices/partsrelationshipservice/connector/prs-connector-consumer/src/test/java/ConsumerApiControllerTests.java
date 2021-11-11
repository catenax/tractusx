import jakarta.ws.rs.core.Response;
import org.eclipse.dataspaceconnector.extensions.api.ConsumerApiController;
import org.eclipse.dataspaceconnector.extensions.api.FileRequest;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.*;
import org.eclipse.dataspaceconnector.transfer.store.memory.InMemoryTransferProcessStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerApiControllerTests {

    Monitor monitor = new ConsoleMonitor();

    TransferProcessStore processStore = new InMemoryTransferProcessStore();

    @Mock
    TransferProcessManager transferProcessManager;

    ConsumerApiController controller = new ConsumerApiController(monitor, transferProcessManager, processStore);

    DataRequest dataRequest = DataRequest.Builder.newInstance()
            .id(UUID.randomUUID().toString())
            .protocol("ids-rest")
            .dataDestination(DataAddress.Builder.newInstance()
                    .type("File")
                    .property("path", "some/path")
                    .build())
            .build();

    @Test
    public void checkHealth_Returns() {
        ConsumerApiController controller = new ConsumerApiController(monitor, null, null);
        assert(controller.checkHealth().equals("I'm alive!"));
    }

    @Test
    public void getStatus_WhenProcessNotInStore_ReturnsNotFound() {
        var processId = UUID.randomUUID().toString();
        var response = controller.getStatus(processId);
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getStatus_WhenProcessInStore_ReturnsStatus() {
        var processId = UUID.randomUUID().toString();

        TransferProcess process = TransferProcess.Builder.newInstance()
                .id(processId)
                .type(TransferProcess.Type.CONSUMER)
                .dataRequest(dataRequest)
                .build();
        processStore.create(process); // Creates a process with state = INITIAL
        processStore.update(process.toBuilder().state(TransferProcessStates.PROVISIONING.code()).build());
        var response = controller.getStatus(processId);
        assertThat(response.getEntity()).isEqualTo(TransferProcessStates.PROVISIONING.toString());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    public void initiateTransfer_WhenFileRequestValid_ReturnsProcessId() {
        FileRequest fileRequest = new FileRequest();
        fileRequest.setFilename("some/source/path");
        fileRequest.setConnectorAddress("http://connector-address");
        fileRequest.setDestinationPath("some/dest/path");
        when(transferProcessManager.initiateConsumerRequest(any()))
                .thenReturn(TransferInitiateResponse.Builder.newInstance().id(dataRequest.getId()).status(ResponseStatus.OK).build());
        var response = controller.initiateTransfer(fileRequest);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

}
