package com.catenax.tdm.aspect;

import java.util.ArrayList;
import java.util.List;

import com.catenax.tdm.dao.AspectMappingDao;
import com.catenax.tdm.dao.QueueDao;
import com.catenax.tdm.model.v1.AspectMapping;
import com.catenax.tdm.model.v1.PartId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AspectMappingHandler implements AspectHandler<AspectMapping> {

	@Autowired
	private AspectMappingDao aspectMappingDao;

	@Override
	public void createAspect(PartId pPart) {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public Class<AspectMapping> getMainEntityClass() {
		return AspectMapping.class;
	}
	
	@Override
	public List<AspectMapping> retrieveAspect(PartId pPart) {
		return retrieveAspect(pPart.getOneIDManufacturer(), pPart.getObjectIDManufacturer());
	}

	@Override
	public List<AspectMapping> retrieveAspect(String pBpn, String pPartUniqueID) {
		List<AspectMapping> list = new ArrayList<AspectMapping>();

		final String bpn = pBpn.trim();
		final String serialNo = pPartUniqueID.trim();

		final List<AspectMapping> mappings = aspectMappingDao.findAllByBpnAndSerialNo(bpn, serialNo);
		for (final AspectMapping mapping : mappings) {
			list.add(mapping);
		}
		

		return list;
	}

}
