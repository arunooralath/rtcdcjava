package com.github.zubnix.rtcdcjava;


import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.DTLSClientProtocol;
import org.bouncycastle.crypto.tls.DTLSServerProtocol;
import org.bouncycastle.crypto.tls.DTLSTransport;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.bouncycastle.crypto.tls.DefaultTlsClient;
import org.bouncycastle.crypto.tls.DefaultTlsEncryptionCredentials;
import org.bouncycastle.crypto.tls.DefaultTlsServer;
import org.bouncycastle.crypto.tls.DefaultTlsSignerCredentials;
import org.bouncycastle.crypto.tls.HashAlgorithm;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.ServerOnlyTlsAuthentication;
import org.bouncycastle.crypto.tls.SignatureAlgorithm;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsEncryptionCredentials;
import org.bouncycastle.crypto.tls.TlsSignerCredentials;
import org.bouncycastle.crypto.util.PrivateKeyFactory;

import java.io.IOException;
import java.security.SecureRandom;

public class DTLSTransportFactory {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public DTLSTransport createServerTransport(final RTCCertificate rtcCertificate,
                                               final DatagramTransport transport) throws IOException {

        final DefaultTlsServer defaultTlsServer = new DefaultTlsServer() {

            private final AsymmetricKeyParameter privateKeyAsymKeyParam = PrivateKeyFactory.createKey(rtcCertificate.getKeyPair()
                                                                                                                    .getPrivate()
                                                                                                                    .getEncoded());
            private final Certificate cCert = new Certificate(new org.bouncycastle.asn1.x509.Certificate[]{rtcCertificate.getCertificate().toASN1Structure()});



            @Override
            public ProtocolVersion getServerVersion() throws IOException {
                return ProtocolVersion.DTLSv12;
            }

            @Override
            protected TlsEncryptionCredentials getRSAEncryptionCredentials() throws IOException {
                return new DefaultTlsEncryptionCredentials(this.context,
                                                           this.cCert,
                                                           this.privateKeyAsymKeyParam);
            }

            @Override
            protected TlsSignerCredentials getRSASignerCredentials() throws IOException {
                return new DefaultTlsSignerCredentials(this.context,
                                                       this.cCert,
                                                       this.privateKeyAsymKeyParam,
                                                       new SignatureAndHashAlgorithm(HashAlgorithm.sha256,
                                                                                     SignatureAlgorithm.rsa));
            }
        };

        return new DTLSServerProtocol(SECURE_RANDOM).accept(defaultTlsServer,
                                                            transport);
    }

    public DTLSTransport createClientTransport(final DatagramTransport transport) throws IOException {


        final DefaultTlsClient defaultTlsClient = new DefaultTlsClient() {

            @Override
            public ProtocolVersion getClientVersion() {
                return ProtocolVersion.DTLSv12;
            }

            @Override
            public TlsAuthentication getAuthentication() throws IOException {

                return new ServerOnlyTlsAuthentication() {
                    @Override
                    public void notifyServerCertificate(final Certificate serverCertificate) throws IOException {
                        //TODO Check if certificate is signed by a trusted party.
                    }
                };
            }
        };

        return new DTLSClientProtocol(SECURE_RANDOM).connect(defaultTlsClient,
                                                             transport);
    }
}
