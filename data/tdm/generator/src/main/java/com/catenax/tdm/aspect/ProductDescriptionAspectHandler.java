/*
 *
 */
package com.catenax.tdm.aspect;

import com.catenax.tdm.dao.AspectMappingDao;
import com.catenax.tdm.model.v1.AspectMapping;
import com.catenax.tdm.model.v1.PartId;
import com.catenax.tdm.model.v1.ProductDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductDescriptionAspectHandler.
 */
@Component
public class ProductDescriptionAspectHandler implements AspectHandler<ProductDescription> {

	@Autowired
	private AspectMappingDao aspectMappingDao;

	/**
	 * Creates the aspect.
	 *
	 * @param part the part
	 */
	@Override
	public void createAspect(PartId part) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the main entity class.
	 *
	 * @return the main entity class
	 */
	@Override
	public Class<ProductDescription> getMainEntityClass() {
		return ProductDescription.class;
	}

	/**
	 * Retrieve aspect.
	 *
	 * @param part the part
	 * @return the list
	 */
	@Override
	public List<ProductDescription> retrieveAspect(PartId part) {
		return retrieveAspect(part.getOneIDManufacturer(), part.getObjectIDManufacturer());
	}

	/**
	 * Retrieve aspect.
	 *
	 * @param oneID        the one ID
	 * @param partUniqueID the part unique ID
	 * @return the list
	 */
	@Override
	public List<ProductDescription> retrieveAspect(String oneID, String partUniqueID) {
		final List<ProductDescription> list = new ArrayList<>();

		final String bpn = oneID.trim();
		final String serialNo = partUniqueID.trim();

		final List<AspectMapping> mappings = aspectMappingDao.findAllByBpnAndSerialNo(bpn, serialNo);
		for (final AspectMapping mapping : mappings) {
			list.addAll(mapping.getProductDescription());
		}

		return list;
	}

}