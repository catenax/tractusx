package net.catenax.prs;

import com.google.common.base.Charsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PrsApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void generatedOpenApiMatchesContract() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/api-docs.yaml",
				String.class))
				.isEqualTo(Files.readString(new File("../api/prs-v0.1.yaml").toPath(), Charsets.UTF_8), port);
	}
}
