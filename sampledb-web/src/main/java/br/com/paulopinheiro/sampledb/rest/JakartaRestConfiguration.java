package br.com.paulopinheiro.sampledb.rest;

import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Activates Jakarta RESTful Web Services (JAX-RS) and defines the base URL prefix.
 * Eliminates the need for traditional web.xml configurations (Zero-XML principle).
 */
@ApplicationPath("/api")
@BasicAuthenticationMechanismDefinition(realmName = "sampledb-realm") // Activates native HTTP Basic security
public class JakartaRestConfiguration extends Application {
    // Intentionally left empty. The container automatically scans for resources.
}
