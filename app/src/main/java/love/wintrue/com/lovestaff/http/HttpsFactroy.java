package love.wintrue.com.lovestaff.http;

import android.content.Context;

import love.wintrue.com.lovestaff.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * @des:
 * @author:th
 * @email:342592622@qq.com
 * @date: 2017/8/3 0003 17:57
 */

public class HttpsFactroy {

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            InputStream certificate = context.getResources().openRawResource(R.raw.ytjr_ca);
            keyStore.setCertificateEntry("ca", certificateFactory.generateCertificate(certificate));

            if (certificate != null) {
                certificate.close();
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
