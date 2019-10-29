package com.revature.rpm.web.filters;

import com.revature.rpm.exceptions.GatewaySubversionException;
import com.revature.rpm.security.config.ZuulConfig;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Denies all traffic that has not been routed through the gateway. This is accomplished by looking
 * for the value of the header from named RPM_ZUUL_ACCESS_HEADER, which is applied by the gateway
 * when the request is first intercepted.
 */
public class GatewaySubversionFilter extends GenericFilterBean {

  private ZuulConfig zuulConfig;

  /**
   * A constructor for GatewaySubversionFilter that instantiates the ZuulConfig field.
   *
   * @param zuulConfig - Provides configuration for validating that requests came from Zuul.
   */
  public GatewaySubversionFilter(ZuulConfig zuulConfig) {
    this.zuulConfig = zuulConfig;
  }

  /**
   * Applies filtering to the request by validating the header to ensure it matches the one added by
   * the gateway. Requests that fail validation are deemed as subverting the gateway unless they are
   * destined for the actuator.
   *
   * @param req - A request from the client to the servlet.
   * @param resp - A response that can be sent back to the client.
   * @param chain - A chain of filters that are invoked in succession to protect a resource.
   * @throws IOException if an I/O error occurs during this filter's processing of the request.
   * @throws ServletException if the processing fails for any other reason.
   */
  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {

    final String GATEWAY_SUBVERTED = "gateway-subverted";
    final String REQUEST_TYPE = "request-type";

    HttpServletRequest httpReq = (HttpServletRequest) req;
    HttpServletResponse httpResp = (HttpServletResponse) resp;

    String headerZuul = httpReq.getHeader(zuulConfig.getHeader());

    try {

      if (httpReq.getRequestURI().contains("/actuator")) {
        httpReq.setAttribute(GATEWAY_SUBVERTED, true);
        httpReq.setAttribute(REQUEST_TYPE, "health-check");
      } else if (validateHeader(headerZuul)) {
        httpReq.setAttribute(GATEWAY_SUBVERTED, false);
        httpReq.setAttribute(REQUEST_TYPE, "resource");
      } else {
        httpReq.setAttribute(GATEWAY_SUBVERTED, true);
        httpReq.setAttribute(REQUEST_TYPE, "resource");
        SecurityContextHolder.clearContext();
        httpResp.setStatus(401);
        throw new GatewaySubversionException("No gateway header found on request.");
      }

    } catch (GatewaySubversionException gse) {
      gse.printStackTrace();
    }

    chain.doFilter(httpReq, httpResp);
  }

  /**
   * Performs a SHA-512 hash.
   *
   * @param password - Represents the password that will be hashed.
   * @return A SHA-512 hashed value.
   */
  public String getHash(String password) {

    String generatedPassword = null;

    try {

      MessageDigest md = MessageDigest.getInstance("SHA-512");
      md.update(zuulConfig.getSalt().getBytes(StandardCharsets.UTF_8));
      byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < bytes.length; i++) {
        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
      }

      generatedPassword = sb.toString();

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return generatedPassword;
  }

  /**
   * Checks if the header value matches the expected value.
   *
   * @param header - The retrieved header from the request.
   * @return True if the header matches that of zuulConfig's secret and false otherwise.
   */
  public boolean validateHeader(String header) {
    if (header == null) {
      return false;
    }
    return header.equals(getHash(zuulConfig.getSecret()));
  }
}
