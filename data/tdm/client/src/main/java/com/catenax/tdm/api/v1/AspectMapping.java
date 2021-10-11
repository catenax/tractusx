/*
 *
 */
package com.catenax.tdm.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.client.model.DocumentsInner;
import io.swagger.client.model.Material;
import io.swagger.client.model.PartId;
import io.swagger.client.model.ProductDescription;
import io.swagger.client.model.ProductUsage;
import io.swagger.client.model.ReturnRequest;
import io.swagger.client.model.TechnicalData;

// TODO: Auto-generated Javadoc
/**
 * The Class AspectMapping.
 */

public class AspectMapping {
	
	private static final Logger log = new Logger(AspectMapping.class);
	
	public static AspectMapping create(Object o) {
		// log.info("Create AspectMapping from: " + o.getClass());
		try {
			ObjectMapper om = new ObjectMapper();
			String s = om.writeValueAsString(o);
			AspectMapping m = om.readValue(s, AspectMapping.class);
			return m;
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String parentBpn = null;

	/** The part. */
	private PartId part = null;

	/** The material. */
	private List<Material> material = new ArrayList<>();

	/** The documents. */
	private List<DocumentsInner> documents = new ArrayList<>();

	/** The technical data. */
	private List<TechnicalData> technicalData = new ArrayList<>();

	/** The product description. */
	private List<ProductDescription> productDescription = new ArrayList<>();

	/** The product usage. */
	private List<ProductUsage> productUsage = new ArrayList<>();

	/** The return request. */
	private List<ReturnRequest> returnRequest = new ArrayList<>();

	/**
	 * Instantiates a new aspect mapping.
	 */
	public AspectMapping() {

	}

	/**
	 * Instantiates a new aspect mapping.
	 *
	 * @param partId the part id
	 */
	public AspectMapping(PartId partId) {
		this.part = partId;
	}

	/**
	 * Gets the documents.
	 *
	 * @return the documents
	 */
	public List<DocumentsInner> getDocuments() {
		return documents;
	}

	/**
	 * Gets the material.
	 *
	 * @return the material
	 */
	public List<Material> getMaterial() {
		return material;
	}

	/**
	 * Gets the part.
	 *
	 * @return the part
	 */
	public PartId getPart() {
		return part;
	}

	/**
	 * Gets the product description.
	 *
	 * @return the product description
	 */
	public List<ProductDescription> getProductDescription() {
		return productDescription;
	}

	/**
	 * Gets the product usage.
	 *
	 * @return the product usage
	 */
	public List<ProductUsage> getProductUsage() {
		return productUsage;
	}

	/**
	 * Gets the return request.
	 *
	 * @return the return request
	 */
	public List<ReturnRequest> getReturnRequest() {
		return returnRequest;
	}

	/**
	 * Gets the technical data.
	 *
	 * @return the technical data
	 */
	public List<TechnicalData> getTechnicalData() {
		return technicalData;
	}

	/**
	 * Sets the documents.
	 *
	 * @param documents the new documents
	 */
	public void setDocuments(List<DocumentsInner> documents) {
		this.documents = documents;
	}

	/**
	 * Sets the material.
	 *
	 * @param material the new material
	 */
	public void setMaterial(List<Material> material) {
		this.material = material;
	}

	/**
	 * Sets the part.
	 *
	 * @param part the new part
	 */
	public void setPart(PartId part) {
		this.part = part;
	}

	/**
	 * Sets the product description.
	 *
	 * @param productDescription the new product description
	 */
	public void setProductDescription(List<ProductDescription> productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * Sets the product usage.
	 *
	 * @param productUsage the new product usage
	 */
	public void setProductUsage(List<ProductUsage> productUsage) {
		this.productUsage = productUsage;
	}

	/**
	 * Sets the return request.
	 *
	 * @param returnRequest the new return request
	 */
	public void setReturnRequest(List<ReturnRequest> returnRequest) {
		this.returnRequest = returnRequest;
	}

	/**
	 * Sets the technical data.
	 *
	 * @param technicalData the new technical data
	 */
	public void setTechnicalData(List<TechnicalData> technicalData) {
		this.technicalData = technicalData;
	}

	public String getParentBpn() {
		return this.parentBpn;
	}

	public void setParentBpn(String pParentBpn) {
		this.parentBpn = pParentBpn;
	}

}
