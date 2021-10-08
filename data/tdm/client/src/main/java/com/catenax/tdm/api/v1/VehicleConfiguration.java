package com.catenax.tdm.api.v1;

public class VehicleConfiguration {

	private String bpn = null;
	private String vehicleType = null;
	private int count = 0;
	
	public VehicleConfiguration() {
		
	}
	
	public VehicleConfiguration(String bpn, String vehicleType, Integer count) {
		this.bpn = bpn;
		this.vehicleType = vehicleType;
		this.count = count;
	}

	public String getBpn() {
		return this.bpn;
	}

	public void setBpn(String pBpn) {
		this.bpn = pBpn;
	}

	public String getVehicleType() {
		return this.vehicleType;
	}

	public void setVehicleType(String pVehicleType) {
		this.vehicleType = pVehicleType;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int pCount) {
		this.count = pCount;
	}

}
