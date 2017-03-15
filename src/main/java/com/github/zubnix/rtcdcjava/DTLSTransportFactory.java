package com.github.zubnix.rtcdcjava;


import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.tls.Certificate;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.ClientCertificateType;
import org.bouncycastle.crypto.tls.DTLSClientProtocol;
import org.bouncycastle.crypto.tls.DTLSServerProtocol;
import org.bouncycastle.crypto.tls.DTLSTransport;
import org.bouncycastle.crypto.tls.DatagramTransport;
import org.bouncycastle.crypto.tls.DefaultTlsClient;
import org.bouncycastle.crypto.tls.DefaultTlsServer;
import org.bouncycastle.crypto.tls.DefaultTlsSignerCredentials;
import org.bouncycastle.crypto.tls.HashAlgorithm;
import org.bouncycastle.crypto.tls.SignatureAlgorithm;
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.util.Arrays;

import java.io.IOException;
import java.security.KeyPair;
import java.security.SecureRandom;

public class DTLSTransportFactory {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public DTLSTransport createServerTransport(RTCCertificate rtcCertificate,
                                               DatagramTransport transport) throws IOException {
        final DefaultTlsServer defaultTlsServer = new DefaultTlsServer() {
            @Override
            public void notifyClientCertificate(final Certificate clientCertificate) throws IOException {
                //TODO Check if certificate is signed by a trusted party.
            }
        };

        return new DTLSServerProtocol(SECURE_RANDOM).accept(defaultTlsServer,
                                                            transport);
    }

    public DTLSTransport createClientTransport(RTCCertificate rtcCertificate,
                                               DatagramTransport transport) throws IOException {


        final DefaultTlsClient defaultTlsClient = new DefaultTlsClient() {
            @Override
            public TlsAuthentication getAuthentication() throws IOException {
                return new TlsAuthentication() {
                    @Override
                    public void notifyServerCertificate(final Certificate serverCertificate) {
                        //TODO Check if certificate is signed by a trusted party.
                    }

                    @Override
                    public TlsCredentials getClientCredentials(final CertificateRequest certificateRequest) throws IOException {

                        short[] certificateTypes = certificateRequest.getCertificateTypes();
                        if (certificateTypes == null || !Arrays.contains(certificateTypes,
                                                                         ClientCertificateType.rsa_sign)) {
                            return null;
                        }

                        final KeyPair keyPair = rtcCertificate.getKeyPair();
                        AsymmetricKeyParameter privateKeyAsymKeyParam = PrivateKeyFactory.createKey(keyPair.getPrivate()
                                                                                                           .getEncoded());
                        SignatureAndHashAlgorithm signatureAndHashAlgorithm = new SignatureAndHashAlgorithm(HashAlgorithm.sha256,
                                                                                                            SignatureAlgorithm.rsa);

                        Certificate cCert = new Certificate(new org.bouncycastle.asn1.x509.Certificate[]{rtcCertificate.getCertificate().toASN1Structure()});

                        return new DefaultTlsSignerCredentials(context,
                                                               cCert,
                                                               privateKeyAsymKeyParam,
                                                               signatureAndHashAlgorithm);
                    }
                };
            }
        };

        return new DTLSClientProtocol(SECURE_RANDOM).connect(defaultTlsClient,
                                                             transport);
    }
}
