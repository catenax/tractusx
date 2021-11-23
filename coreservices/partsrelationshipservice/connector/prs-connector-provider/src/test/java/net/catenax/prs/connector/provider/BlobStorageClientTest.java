package net.catenax.prs.connector.provider;

import com.azure.storage.blob.BlobClient;
import com.github.javafaker.Faker;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.provision.azure.AzureSasToken;
import org.eclipse.dataspaceconnector.schema.azure.AzureBlobStoreSchema;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.types.TypeManager;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlobStorageClientTest {

    @Spy
    Monitor monitor = new ConsoleMonitor();

    @Mock
    Vault vault;

    @Mock
    TypeManager typeManager;

    @Mock
    BlobStorageClient.BlobClientFactory blobClientFactory;

    @Captor
    ArgumentCaptor<ByteArrayInputStream> byteArrayInputStreamCaptor;

    BlobStorageClient sut;

    Faker faker = new Faker();
    String accountName = faker.lorem().word();
    String containerName = faker.lorem().word();
    String blobName = faker.lorem().word();
    String data = faker.lorem().characters();
    String keyName = faker.lorem().word();
    String secret = faker.lorem().word();

    DataAddress dataAddress = DataAddress.Builder.newInstance()
            .type(AzureBlobStoreSchema.TYPE)
            .property(AzureBlobStoreSchema.ACCOUNT_NAME, accountName)
            .property(AzureBlobStoreSchema.CONTAINER_NAME, containerName)
            .keyName(keyName)
            .build();

    AzureSasToken azureSasToken = mock(AzureSasToken.class);
    BlobClient blobClient = mock(BlobClient.class);


    @BeforeEach
    public void before() {
        sut = new BlobStorageClient(monitor, typeManager, vault, blobClientFactory);
    }

    @Test
    public void writeToBlob() {
        // Arrange
        when(vault.resolveSecret(keyName)).thenReturn(secret);
        when(typeManager.readValue(secret, AzureSasToken.class)).thenReturn(azureSasToken);
        when(blobClientFactory.getBlobClient(blobName, containerName, accountName, azureSasToken)).thenReturn(blobClient);

        // Act
        sut.writeToBlob(dataAddress, blobName, data);

        // Assert
        verify(blobClient).upload(byteArrayInputStreamCaptor.capture(), eq((long)data.getBytes().length), eq(true));
        assertThat(new String(byteArrayInputStreamCaptor.getValue().readAllBytes())).isEqualTo(data);
    }

    @Test
    public void writeToBlob_noSASToken_Fails() {
        // Arrange
        when(vault.resolveSecret(keyName)).thenReturn(null);

        // Act
        assertThatThrownBy(() -> sut.writeToBlob(dataAddress, blobName, data)).hasMessage("Can not retrieve SAS token");
    }

    @Test
    public void writeToBlob_invalidSASToken_Fails() {
        // Arrange
        var message = faker.lorem().sentence();
        RuntimeException cause = new RuntimeException(message);
        when(vault.resolveSecret(keyName)).thenReturn(secret);
        when(typeManager.readValue(secret, AzureSasToken.class)).thenThrow(cause);

        // Act
        assertThatThrownBy(() -> sut.writeToBlob(dataAddress, blobName, data))
                .hasMessage("Invalid SAS token")
                .hasCause(cause);
    }
}