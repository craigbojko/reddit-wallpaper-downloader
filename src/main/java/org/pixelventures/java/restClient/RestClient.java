/*
 * Project: restClient
 * FilePath: /RestClient.java
 * File: RestClient.java
 * Created Date: Saturday, June 6th 2020, 11:00:19 pm
 * Author: Craig Bojko (craig@pixelventures.co.uk)
 * -----
 * Last Modified: Tue Jun 09 2020
 * Modified By: Craig Bojko
 * -----
 * Copyright (c) 2020 Pixel Ventures Ltd.
 * ------------------------------------
 * <<licensetext>>
 */
package org.pixelventures.java.restClient;

import java.util.logging.*;

import java.net.URI;
import java.net.http.HttpClient;
// import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class RestClient {
  HttpClient httpClient;

  public RestClient () {
    httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .build();
  }

  public HttpClient getHttpClient () {
    return httpClient;
  }

  public String fetchURL(String uri) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(uri))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .build();

      CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
      String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
      return result;

      // HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      // HttpHeaders headers = response.headers();
      // headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
      // System.out.println(response.statusCode());
      // return response.body();

    } catch (InterruptedException ex) {
      Logger.getLogger(RestClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      return ex.getLocalizedMessage();
    } catch (ExecutionException ex) {
      Logger.getLogger(RestClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      return ex.getLocalizedMessage();
    } catch (TimeoutException ex) {
      Logger.getLogger(RestClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      return ex.getLocalizedMessage();
    }
  }
}
