package com.catenax.tdm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.catenax.tdm.model.v1.AspectMapping;

public interface AspectMappingDao extends CrudRepository<AspectMapping, Long>, JpaSpecificationExecutor<AspectMapping> {
	
	List<AspectMapping> findAllByParentBpn(String bpn);

	@Query("SELECT a FROM AspectMapping a WHERE a.part.oneIDManufacturer = :bpn AND a.part.objectIDManufacturer = :serialNo")
	List<AspectMapping> findAllByBpnAndSerialNo(String bpn, String serialNo);
}
