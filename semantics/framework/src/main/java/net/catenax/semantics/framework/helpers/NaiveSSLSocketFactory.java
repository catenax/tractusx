/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.framework.helpers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * a naive socket factory trusting host names by name not by certificate
 */
public class NaiveSSLSocketFactory extends SSLSocketFactory {
    private final SSLSocketFactory sslSocketFactory =
            (SSLSocketFactory) SSLSocketFactory.getDefault();

    private final SSLContext alwaysAllowSslContext;
    private final Set<String> naivelyTrustedHostnames;

    public NaiveSSLSocketFactory(String ... naivelyTrustedHostnames)
            throws NoSuchAlgorithmException, KeyManagementException {

        this.naivelyTrustedHostnames =
                Collections.unmodifiableSet(
                        new HashSet<>(Arrays.asList(naivelyTrustedHostnames)));

        alwaysAllowSslContext = SSLContext.getInstance("TLS");
        TrustManager tm = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        alwaysAllowSslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return sslSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return sslSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return (naivelyTrustedHostnames.contains(host))
                ? alwaysAllowSslContext.getSocketFactory().createSocket(socket, host, port, autoClose)
                : sslSocketFactory.createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return (naivelyTrustedHostnames.contains(host))
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port)
                : sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort) throws IOException, UnknownHostException {
        return (naivelyTrustedHostnames.contains(host))
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port, localAddress, localPort)
                : sslSocketFactory.createSocket(host, port, localAddress, localPort);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return (naivelyTrustedHostnames.contains(host.getHostName()))
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port)
                : sslSocketFactory.createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress host, int port, InetAddress localHost, int localPort) throws IOException {
        return (naivelyTrustedHostnames.contains(host.getHostName()))
                ? alwaysAllowSslContext.getSocketFactory().createSocket(host, port, localHost, localPort)
                : sslSocketFactory.createSocket(host, port, localHost, localPort);
    }
}


