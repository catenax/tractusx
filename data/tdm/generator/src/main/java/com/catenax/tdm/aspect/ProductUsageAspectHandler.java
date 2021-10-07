/*
 *
 */
package com.catenax.tdm.aspect;

import com.catenax.tdm.dao.AspectMappingDao;
import com.catenax.tdm.model.v1.AspectMapping;
import com.catenax.tdm.model.v1.PartId;
import com.catenax.tdm.model.v1.ProductUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductUsageAspectHandler.
 */
@Component
public class ProductUsageAspectHandler implements AspectHandler<ProductUsage> {

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
	public Class<ProductUsage> getMainEntityClass() {
		return ProductUsage.class;
	}

	/**
	 * Retrieve aspect.
	 *
	 * @param part the part
	 * @return the list
	 */
	@Override
	public List<ProductUsage> retrieveAspect(PartId part) {
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
	public List<ProductUsage> retrieveAspect(String oneID, String partUniqueID) {
		final List<ProductUsage> list = new ArrayList<>();

		final String bpn = oneID.trim();
		final String serialNo = partUniqueID.trim();

		final List<AspectMapping> mappings = aspectMappingDao.findAllByBpnAndSerialNo(bpn, serialNo);
		for (final AspectMapping mapping : mappings) {
			list.addAll(mapping.getProductUsage());
		}

		return list;
	}

}