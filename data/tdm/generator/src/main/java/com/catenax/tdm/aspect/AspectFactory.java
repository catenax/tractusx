/*
 *
 */
package com.catenax.tdm.aspect;

import com.catenax.tdm.api.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Aspect objects.
 */
@Component
public class AspectFactory {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(AspectFactory.class);

	private Map<String, AspectHandler<?>> handlers = new HashMap<>();

	@Autowired
	public AspectFactory(AspectMappingHandler aspectMappingHandler, DocumentationAspectHandler documentationAspectHandler, MaterialAspectHandler materialAspectHandler, ReturnRequestAspectHandler returnRequestAspectHandler, ProductUsageAspectHandler productUsageAspectHandler, ProductDescriptionAspectHandler productDescriptionAspectHandler, TechnicalDataAspectHandler technicalDataAspectHandler) {
		this.handlers.put("all", aspectMappingHandler);
		this.handlers.put("documentation", documentationAspectHandler);
		this.handlers.put("material", materialAspectHandler);
		this.handlers.put("returnrequest", returnRequestAspectHandler);
		this.handlers.put("productusage", productUsageAspectHandler);
		this.handlers.put("productdescription", productDescriptionAspectHandler);
		this.handlers.put("technicaldata", technicalDataAspectHandler);
	}

	/**
	 * Creates a new Aspect object.
	 *
	 * @param aspect the aspect
	 * @return the aspect handler< object>
	 */
	public AspectHandler<?> createHandlerForAspect(String aspect) {
		String lowerCaseAspect = aspect.toLowerCase();
		if (!handlers.containsKey(lowerCaseAspect)) {
			throw new RuntimeException("Invalid aspect: " + aspect);
		}
		return handlers.get(lowerCaseAspect);
	}

	/**
	 * Gets the aspects.
	 *
	 * @return the aspects
	 */
	public static Set<String> getAspects() {
		return Set.of("all", "documentation", "material", "returnrequest", "productusage", "productdescription", "technicaldata");
	}

	/**
	 * Gets the aspect URL.
	 *
	 * @return the aspect URL
	 */
	public static String getAspectURL() {
		final String result = Config.BASE_URL + "/catena-x/tdm/1.0/aspect/";

		return result;
	}

	/**
	 * Resolve.
	 *
	 * @param aspect       the aspect
	 * @param oneID        the one ID
	 * @param partUniqueID the part unique ID
	 * @return the list
	 */
	public List<?> resolve(String aspect, String oneID, String partUniqueID) {
		return createHandlerForAspect(aspect).retrieveAspect(oneID, partUniqueID);
	}

}
