package fr.readthedocs.rtb;

import fr.readthedocs.rtb.util.Tuple;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BinCreator {

    public static void createBin(String code, String language, Consumer<Tuple<Integer, String>> consumer) throws IOException, URISyntaxException, InterruptedException {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionTimeToLive(20, TimeUnit.SECONDS)
                .setConnectionManager(cm)
                .build();
        HttpPost httpPost = new HttpPost("https://bin.readthedocs.fr/new");
        httpPost.setEntity(new UrlEncodedFormEntity(
                Arrays.asList(
                        new BasicNameValuePair("code", code),
                        new BasicNameValuePair("lang", language)
                )
        ));
        CloseableHttpResponse response = client.execute(httpPost);
        consumer.accept(new Tuple<>(response.getStatusLine().getStatusCode(), response.getLastHeader("Location").getValue()));
    }
}
