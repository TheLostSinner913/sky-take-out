package com.sky.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @Description:
 * @Author: 刘东钦
 * @Date: 2023/5/6 22:37
 */
public class MyHttpClient {
    public static String HttpGet(String url, Map<String, String> params) throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder builder = new URIBuilder(url);
        params.forEach((k,v)->{
            builder.addParameter(k,v);
        });
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity, "UTF-8");
        httpClient.close();
        response.close();
        return s;
    }

}
