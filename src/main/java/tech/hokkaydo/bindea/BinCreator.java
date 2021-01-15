package tech.hokkaydo.bindea;

import tech.hokkaydo.bindea.util.Tuple;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Consumer;

public class BinCreator {

    public static void createBin(String code, String language, Consumer<Tuple<Integer, String>> consumer) throws IOException, URISyntaxException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .authenticator(Authenticator.getDefault())
                .build();
        HttpRequest request = HttpRequest
                .newBuilder()
                .POST(
                        HttpRequest.BodyPublishers.ofString(
                                "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                                        + "&lang=" + URLEncoder.encode(language, StandardCharsets.UTF_8)
                        )
                )
                .uri(new URI("https://bin.readthedocs.fr/new"))
                .header("Accept-Charset", "UTF-8")
                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        consumer.accept(new Tuple<>(response.statusCode(), response.uri().toString()));
    }
}
