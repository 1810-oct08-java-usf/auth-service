package com.revature.rpm.config;

import org.springframework.beans.factory.annotation.Value;

public class GatewayTokenConfig {
	
	@Value("${rpm.gateway.header:X-RPM-Gateway}")
	private String gatewayHeader;
	
	@Value("${rpm.gateway.salt:local-deploy}")
	private String gatewaySalt;

	@Value("${rpm.gateway.secret:local-deploy}")
	private String gatewaySecret;

	public String getGatewayHeader() {
		return gatewayHeader;
	}

	public String getGatewaySalt() {
		return gatewaySalt;
	}

	public String getGatewaySecret() {
		return gatewaySecret;
	}
	
}
