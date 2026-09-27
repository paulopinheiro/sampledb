package br.com.paulopinheiro.sampledb.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Activates Jakarta RESTful Web Services (JAX-RS) and defines the base URL prefix.
 * Eliminates the need for traditional web.xml configurations (Zero-XML principle).
 */
@ApplicationPath("/api")
public class JakartaRestConfiguration extends Application {
    // Intentionally left empty. The container automatically scans for resources.
}
