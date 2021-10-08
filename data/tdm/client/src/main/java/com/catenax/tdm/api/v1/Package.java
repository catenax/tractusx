package com.catenax.tdm.api.v1;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.zeroturnaround.zip.ZipUtil;

import com.catenax.tdm.api.v1.client.AspectModelApi;
import com.catenax.tdm.api.v1.client.BusinessPartnerServiceApi;
import com.catenax.tdm.api.v1.client.MemberCompanyServiceApi;
import com.catenax.tdm.api.v1.client.PartsRelationshipServiceApi;
import com.catenax.tdm.api.v1.client.PartsRelationshipServiceBrokerHttpProxyApiApi;
import com.catenax.tdm.api.v1.client.TdmAdminApi;
import com.catenax.tdm.api.v1.client.TraceabilityApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.swagger.client.model.BusinessPartner;
import io.swagger.client.model.PartAspectUpdate;
import io.swagger.client.model.PartRelationshipUpdate;
import io.swagger.client.model.PartRelationshipUpdateList;
import io.swagger.client.model.PartRelationshipWithInfos;
import io.swagger.client.model.PartTypeNameUpdate;
import io.swagger.client.model.Traceability;

public class Package {

	private static final Logger log = new Logger(Package.class);

	public static void main(String[] args) {
		log.info("Starting test data generation... ");
		log.info("... this may take a while, so grab a coffee, take a cookie, lean back and enjoy :)...");
		
		boolean purge = false;

		String bashPath = null;
		String apiKey = null;
		String configuration = null;

		try {
			bashPath = args[0]; // "http://localhost:8080"
			apiKey = args[1];
			String configFile = args[2];

			StringBuilder contentBuilder = new StringBuilder();

			try (Stream<String> stream = Files.lines(Paths.get(configFile), StandardCharsets.UTF_8)) {
				stream.forEach(s -> contentBuilder.append(s).append("\n"));
			} catch (IOException e) {
				log.error(e.getMessage());
				return;
			}

			configuration = contentBuilder.toString();
		} catch (Exception e2) {
			log.error(e2.getMessage());
			log.info(
					"usage: mvn clean package exec:java -Dexec.mainClass=\"com.catenax.tdm.api.v1.Package\" -Dexec.args=\"[enpoint] [apiKey] [configuration.json]\"");
			return;
		}

		log.info("Set basePath: " + bashPath);

		ApiClient client = new ApiClient();
		client = client.setBasePath(bashPath);

		log.info(" -> basePath: " + client.getBasePath());

		if(purge) {
			TdmAdminApi admin = new TdmAdminApi(client);
			admin.purgeTestData(apiKey);
		}

		int vehicleCount = 1;

		ObjectMapper om = new ObjectMapper();
		try {
			List<VehicleConfiguration> vehicles = om.readValue(configuration,
					new TypeReference<List<VehicleConfiguration>>() {
					});

			for (VehicleConfiguration config : vehicles) {
				createVehicle(config, client);
			}

			BusinessPartnerServiceApi businessPartnerService = new BusinessPartnerServiceApi(client);
			MemberCompanyServiceApi memberCompanyService = new MemberCompanyServiceApi(client);

			TraceabilityApi traceabilityService = new TraceabilityApi(client);

			PartsRelationshipServiceBrokerHttpProxyApiApi prsService = new PartsRelationshipServiceBrokerHttpProxyApiApi(
					client);

			AspectModelApi aspectService = new AspectModelApi(client);

			File output = new File("output");
			if (output.exists()) {
				deleteDirectory(output);
			}
			output.mkdirs();

			ObjectWriter writer = om.writerWithDefaultPrettyPrinter();

			Map<String, List<Traceability>> ts = new HashMap<String, List<Traceability>>();

			List<Traceability> tsall = traceabilityService.getTraceability(" ");
			log.info("Tracebility found: " + tsall.size());
			for (Traceability t : tsall) {
				String bpn = t.getStaticData().getManufactureContractOneID();
				if (ts.containsKey(bpn)) {
					ts.get(bpn).add(t);
				} else {
					List<Traceability> tsl = new ArrayList<Traceability>();
					tsl.add(t);
					ts.put(bpn, tsl);
				}
			}

			List<BusinessPartner> bps = businessPartnerService.getBusinessPartner(" ");

			String content = writer.writeValueAsString(bps);
			writeFile("output/BusinessPartner.json", content);

			content = writer.writeValueAsString(memberCompanyService.getMemberCompany(" "));
			writeFile("output/MemberCompanies.json", content);

			content = writer.writeValueAsString(memberCompanyService.getMemberCompanyRolesAll());
			writeFile("output/MemberCompanyRoles.json", content);

			for (BusinessPartner bp : bps) {
				if (bp.getParent() == null) {
					String bpn = bp.getBpn();
					
					log.info("Exporting files for: " + bpn + " - " + bp.getName1());

					String folderName = "output/" + bp.getBpn() + "_" + bp.getName1();
					String zipFileName = folderName + ".zip";

					output = new File(folderName);
					output.mkdirs();

					// mk aspect dirs
					// File aspectDir = new File(folderName + "/aspect");
					// aspectDir.mkdirs();

					List<String> aspects = Arrays.asList("material", "documentation", "productdescription",
							"productusage", "returnrequest", "technicaldata");

					for (String aspect : aspects) {
						File aspectDir = new File(folderName + "/aspect/" + aspect);
						aspectDir.mkdirs();
					}

					/*
					 * content =
					 * writer.writeValueAsString(traceabilityService.getTraceability(bp.getBpn()));
					 * writeFile(folderName + "/" + bpn + "_Traceability.json", content);
					 */
					if (ts.containsKey(bp.getBpn())) {
						content = writer.writeValueAsString(ts.get(bp.getBpn()));
						writeFile(folderName + "/" + bpn + "_Traceability.json", content);
					}

					
					content = writer.writeValueAsString(prsService.getPartAspectUpdate(bp.getBpn(), null, null));
					writeFile(folderName + "/" + bpn + "_PartAspectUpdate.json", content);

					content = writer.writeValueAsString(prsService.getPartTypeNameUpdate(bp.getBpn(), null, null));
					writeFile(folderName + "/" + bpn + "_PartTypeNameUpdate.json", content);
					
					content = writer
							.writeValueAsString(prsService.getPartRelationshipUpdateList(bp.getBpn(), null, null));
					writeFile(folderName + "/" + bpn + "_PartRelationshipUpdate.json", content);						

					// for(String aspect : aspects) {
					List<Object> aspectList = aspectService.getAspect("all", bp.getBpn(), " ");
					content = writer.writeValueAsString(aspectList);
					writeFile(folderName + "/" + bpn + "_aspect_" + "all" + ".json", content);

					for (Object o : aspectList) {
						AspectMapping mapping = AspectMapping.create(o);

						String suffix = "_" + mapping.getPart().getObjectIDManufacturer();

						content = writer.writeValueAsString(mapping.getMaterial());
						// writeFile(folderName + "/" + bpn + "_aspect_" + "material" + suffix +
						// ".json", content);
						writeFile(folderName + "/" + "/aspect/" + "material/part" + suffix + ".json", content);

						content = writer.writeValueAsString(mapping.getDocuments());
						// writeFile(folderName + "/" + bpn + "_aspect_" + "documentation" +
						// suffix + ".json", content);
						writeFile(folderName + "/" + "/aspect/" + "documentation/part" + suffix + ".json", content);

						content = writer.writeValueAsString(mapping.getProductDescription());
						// writeFile(folderName + "/" + bpn + "_aspect_" + "productdescription"
						// + suffix + ".json", content);
						writeFile(folderName + "/" + "/aspect/" + "productdescription/part" + suffix + ".json",
								content);

						content = writer.writeValueAsString(mapping.getProductUsage());
						// writeFile(folderName + "/" + bpn + "_aspect_" + "productusage" +
						// suffix + ".json", content);
						writeFile(folderName + "/" + "/aspect/" + "productusage/part" + suffix + ".json", content);

						content = writer.writeValueAsString(mapping.getReturnRequest());
						// writeFile(folderName + "/" + bpn + "_aspect_" + "returnrequest" +
						// suffix + ".json", content);
						writeFile(folderName + "/" + "/aspect/" + "returnrequest/part" + suffix + ".json", content);

						content = writer.writeValueAsString(mapping.getTechnicalData());
						// writeFile(folderName + "/" + bpn + "_aspect_" + "technicaldata" +
						// suffix + ".json", content);
						writeFile(folderName + "/" + "/aspect/" + "technicaldata/part" + suffix + ".json", content);
					}
					// }

					// Zip the whole thing
					ZipUtil.pack(new File(folderName), new File(zipFileName));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(e);
		}

		log.info("Test data generation done... ");
	}

	public static List<PartRelationshipWithInfos> createVehicle(VehicleConfiguration config, ApiClient client) {
		log.info("create vehcile: " + config.getBpn() + " " + config.getVehicleType());
		PartsRelationshipServiceApi prs = new PartsRelationshipServiceApi(client);
		return prs.createVehicle(config.getBpn(), config.getCount(), config.getVehicleType());
	}

	private static boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	private static void writeFile(String fname, String content) {
		/*
		 * try (PrintStream out = new PrintStream(new FileOutputStream(fname))) {
		 * out.print(content); } catch (Exception e) { log.error(e.getMessage());
		 * log.error(e); }
		 */
		try {
			Path file = Paths.get(fname);
			List<String> lines = Arrays.asList(content.split(System.getProperty("line.separator")));
			Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(e);
		}
	}
}
