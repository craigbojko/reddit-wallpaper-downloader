/*
 * Project: imageGrabber
 * FilePath: /ImageGrabber.java
 * File: ImageGrabber.java
 * Created Date: Thursday, June 4th 2020, 1:35:00 pm
 * Author: Craig Bojko (craig@pixelventures.co.uk)
 * -----
 * Last Modified: Tue Jun 09 2020
 * Modified By: Craig Bojko
 * -----
 * Copyright (c) 2020 Pixel Ventures Ltd.
 * ------------------------------------
 * <<licensetext>>
 */

package org.pixelventures.java.imageGrabber;

import java.io.*;
import java.util.logging.*;

import org.json.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.pixelventures.java.jsonAPIConsumer.RedditJSONConsumer;
import org.pixelventures.java.restClient.RestClient;

public class ImageGrabber {
  final String url = "https://www.reddit.com/r/wallpaper/";
  final String gatewayUrl = "https://gateway.reddit.com/desktopapi/v1/subreddits/wallpaper?geo_filter=GB";
  final String outputDir = "../../output";
  private File directory;
  private RestClient redditGateway;
  private RedditJSONConsumer redditJSON;

  public ImageGrabber () {
    try {
      // Determine & set output directory
      directory = new File(ImageGrabber.class.getProtectionDomain().getCodeSource().getLocation().toURI().resolve(outputDir));

      // Init and call initial Reddit wallpaper URL
      redditGateway = new RestClient();
      requestRedditGateway();

    } catch (java.net.URISyntaxException ex) {
      Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      return;
    } catch (Exception ex) {
      Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      return;
    }
  }

  public File getDirectory () {
    return directory;
  }

  public String parser () {
    return "Test String!";
  }

  public String requestRedditGateway () throws Exception {
    try {
      String json = redditGateway.fetchURL(gatewayUrl);
      redditJSON = new RedditJSONConsumer(json);
      JSONObject firstPost =  redditJSON.getFirstPost();
      fetchRedditImage(firstPost);

      // System.out.println(json);
      return json;
    } catch (Exception ex) {
      Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      throw ex;
    }
  }

  public boolean fetchRedditImage (JSONObject redditPost) {
    String filename = redditPost.getString("title");
    JSONObject postMedia = redditPost.getJSONObject("media");
    String uri = postMedia.getString("content");
    // JSONArray postMediaResolutions = postMedia.getJSONArray("resolutions");
    // JSONObject postMediaBestQuality = postMediaResolutions.getJSONObject(postMediaResolutions.length() - 1);
    // String uri = postMediaBestQuality.getString("url");

    // Overrides
    Boolean enabled = true;

    if (checkOutputDirExists()) { // && new File(directory.toPath() + "/").listFiles().length == 0) {
      try {
        URL url = new URL(uri);
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
        
        
        int imageSize = httpcon.getContentLength();
        int bytesStillToRead = imageSize;

        double imageSizeMB = imageSize / 1048576.0D;
        int round = (int)(imageSizeMB * 1000.0D);
        imageSizeMB = round / 1000.0D;
        byte[] bufferBytes = new byte['⠀'];
        
        InputStream inputStream = new BufferedInputStream(httpcon.getInputStream());
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        
        while ((bytesStillToRead > 0) && (enabled)) {
          int bytesRead = inputStream.read(bufferBytes);
          bytesStillToRead -= bytesRead;
          bytesOutputStream.write(bufferBytes, 0, bytesRead);
          int progressPercentage = (imageSize - bytesStillToRead) * 100 / imageSize;
          System.out.println(filename + " (" + imageSizeMB + "MB) : " + progressPercentage + "%");
        }
        inputStream.close();
        
        if (bytesStillToRead == 0) {
          byte[] imageByteArray = bytesOutputStream.toByteArray();
          String ext = uri.substring(uri.length() - 3);
          // java.text.SimpleDateFormat sdfDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
          // String strDate = sdfDate.format(new java.util.Date());
          
          File file = new File(directory.toPath() + "/" + filename + "." + ext);
          FileOutputStream fos = new FileOutputStream(file);
          fos.write(imageByteArray);
          fos.close();
          fos = null;
          imageByteArray = null;

        } else {
          Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.WARNING, "Unable to fetch [!0 bytes]: " + redditPost.get("id"));
          return false;
        }
        return true;
      } catch (Exception ex) {
        Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.WARNING, "Unable to fetch: " + redditPost.get("id"), ex);
        return false;
      }
    }
    return false; // true;
  }

  public Boolean testWriteFile () {
    String exampleContent = "This is the example test content.";
    java.text.SimpleDateFormat sdfDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
    String strDate = sdfDate.format(new java.util.Date());

    if (checkOutputDirExists()) {
      try {
        File file = new File(directory + "/" + strDate + "-testFile.txt");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(exampleContent.getBytes());
        fos.close();
        fos = null;

        return true;
      } catch (IOException error) {
        Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.SEVERE, null, error);
        return false;
      }
    }
    return false;
  }

  private Boolean checkOutputDirExists () {
    try {
      if (! directory.exists()){
          directory.mkdir();
          return true;
      }
      return true;
    } catch (SecurityException sex) {
      Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.SEVERE, null, sex);
      return false;
    }  catch (Exception ex) {
      Logger.getLogger(ImageGrabber.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      return false;
    }
  }
}