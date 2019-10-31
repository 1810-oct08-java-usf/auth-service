package com.revature.rpm.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Serves as a simple configuration bean which provides values needed for the
 * retrieval and validation of gateway tokens. Gateway tokens serve as a method
 * of validating that a request was sent through the gateway-service of this MSE.
 *
 */
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
