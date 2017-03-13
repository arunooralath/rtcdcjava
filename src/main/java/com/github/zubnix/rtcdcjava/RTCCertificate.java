package com.github.zubnix.rtcdcjava;


import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.x509.X509V1CertificateGenerator;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class RTCCertificate {

    private final String          fingerPrint;
    private final X509Certificate certificate;

    public static String fingerprint(X509Certificate c)
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

    public static RTCCertificate generate(String commonName) throws NoSuchAlgorithmException, CertificateEncodingException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException {

        //generate certificate
        Date       startDate    = new Date(System.currentTimeMillis());// time from which certificate is valid
        Date       expiryDate   = new Date(System.currentTimeMillis() + 365L * 24L * 60L * 60L * 1000L);// time after which certificate is not valid
        BigInteger serialNumber = new BigInteger("1");// serial number for certificate

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA",
                                                            BouncyCastleProvider.PROVIDER_NAME);
        kpg.initialize(1024);
        KeyPair keyPair = kpg.genKeyPair();

        X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
        X500Principal              dnName  = new X500Principal("CN=" + commonName);
        certGen.setSerialNumber(serialNumber);
        certGen.setIssuerDN(dnName);
        certGen.setNotBefore(startDate);
        certGen.setNotAfter(expiryDate);
        certGen.setSubjectDN(dnName);// note: same as issuer
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WITHRSA");

        X509Certificate cert = certGen.generate(keyPair.getPrivate(),
                                                "BC");

        return new RTCCertificate(fingerprint(cert),
                                  cert);
    }

    private RTCCertificate(String fingerPrint,
                           final X509Certificate certificate) {
        this.fingerPrint = fingerPrint;
        this.certificate = certificate;
    }

    public String getFingerPrint() {
        return this.fingerPrint;
    }

    public X509Certificate getCertificate() {
        return this.certificate;
    }
}
