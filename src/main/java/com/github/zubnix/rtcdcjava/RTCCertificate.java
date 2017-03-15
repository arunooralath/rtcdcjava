package com.github.zubnix.rtcdcjava;


import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class RTCCertificate {

    private final String                fingerPrint;
    private final KeyPair               keyPair;
    private final X509CertificateHolder certificate;

    public static String fingerprint(X509CertificateHolder c)
            throws IOException, CertificateEncodingException {

        byte[] der      = c.getEncoded();
        byte[] sha1     = sha256DigestOf(der);
        byte[] hexBytes = Hex.encode(sha1);
        String hex = new String(hexBytes,
                                "ASCII").toUpperCase();

        final StringBuilder fp = new StringBuilder();
        int                 i  = 0;
        fp.append(hex.substring(i,
                                i + 2));
        while ((i += 2) < hex.length()) {
            fp.append(':');
            fp.append(hex.substring(i,
                                    i + 2));
        }
        return fp.toString();
    }

    private static byte[] sha256DigestOf(byte[] input) {
        SHA256Digest d = new SHA256Digest();
        d.update(input,
                 0,
                 input.length);
        byte[] result = new byte[d.getDigestSize()];
        d.doFinal(result,
                  0);
        return result;
    }

    private static String convertCertificateToPEM(X509Certificate signedCertificate) throws IOException {
        StringWriter signedCertificatePEMDataStringWriter = new StringWriter();
        JcaPEMWriter pemWriter                            = new JcaPEMWriter(signedCertificatePEMDataStringWriter);
        pemWriter.writeObject(signedCertificate);
        pemWriter.close();
        return signedCertificatePEMDataStringWriter.toString();
    }

    public static RTCCertificate generate(String commonName) {

        try {
            //generate certificate
            //TODO sign it by lets-encrypt
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA",
                                                                BouncyCastleProvider.PROVIDER_NAME);
            kpg.initialize(1024);

            KeyPair    keyPair      = kpg.genKeyPair();
            Date       startDate    = new Date(System.currentTimeMillis());// time from which certificate is valid
            Date       expiryDate   = new Date(System.currentTimeMillis() + 365L * 24L * 60L * 60L * 1000L);// time after which certificate is not valid
            BigInteger serialNumber = new BigInteger("1");// serial number for certificate
            X500Name   dnName       = new X500Name("CN=" + commonName);
            SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic()
                                                                                         .getEncoded());


            final X509v1CertificateBuilder x509v1CertificateBuilder = new X509v1CertificateBuilder(dnName,
                                                                                                   serialNumber,
                                                                                                   startDate,
                                                                                                   expiryDate,
                                                                                                   dnName,
                                                                                                   subPubKeyInfo);

            AsymmetricKeyParameter privateKeyAsymKeyParam = PrivateKeyFactory.createKey(keyPair.getPrivate()
                                                                                               .getEncoded());
            AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
            AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
            ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId,
                                                                 digAlgId).build(privateKeyAsymKeyParam);

            final X509CertificateHolder x509CertificateHolder = x509v1CertificateBuilder.build(sigGen);

            return new RTCCertificate(fingerprint(x509CertificateHolder),
                                      keyPair,
                                      x509CertificateHolder);
        }
        catch (IOException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException | OperatorCreationException e) {
            throw new RuntimeException(e);
        }
    }

    private RTCCertificate(String fingerPrint,
                           final KeyPair keyPair,
                           final X509CertificateHolder certificate) {
        this.fingerPrint = fingerPrint;
        this.keyPair = keyPair;
        this.certificate = certificate;
    }

    public String getFingerPrint() {
        return this.fingerPrint;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public X509CertificateHolder getCertificate() {
        return this.certificate;
    }
}
