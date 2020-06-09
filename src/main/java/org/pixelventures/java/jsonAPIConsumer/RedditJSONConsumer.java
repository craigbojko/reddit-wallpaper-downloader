/*
 * Project: jsonAPIConsumer
 * FilePath: /JSONAPIConsumer.java
 * File: JSONAPIConsumer.java
 * Created Date: Saturday, June 6th 2020, 10:32:54 pm
 * Author: Craig Bojko (craig@pixelventures.co.uk)
 * -----
 * Last Modified: Tue Jun 09 2020
 * Modified By: Craig Bojko
 * -----
 * Copyright (c) 2020 Pixel Ventures Ltd.
 * ------------------------------------
 * <<licensetext>>
 */
package org.pixelventures.java.jsonAPIConsumer;

import java.util.logging.*;

import org.json.*;

public class RedditJSONConsumer {
  JSONObject posts;
  JSONArray postIds;

  public RedditJSONConsumer (String jsonString) {
    try {
      JSONObject obj = new JSONObject(jsonString);
      posts = obj.getJSONObject("posts");
      postIds = obj.getJSONArray("postIds");
    } catch (JSONException jex) {
      Logger.getLogger(RedditJSONConsumer.class.getName()).log(java.util.logging.Level.SEVERE, null, jex);
      throw jex;
    }
  }

  public JSONObject getPosts () {
    return posts;
  }

  public JSONArray getPostIds () {
    return postIds;
  }

  public JSONObject getFirstPost () {
    String postId = postIds.getString(0);
    return posts.getJSONObject(postId);
  }

  // public JSONObject parseRedditJSON (String jsonString) {
  //   JSONObject obj = new JSONObject(jsonString);
  //   String pageName = obj.getJSONObject("pageInfo").getString("pageName");
    
  //   JSONArray arr = obj.getJSONArray("posts");
  //   for (int i = 0; i < arr.length(); i++) {
  //     String post_id = arr.getJSONObject(i).getString("post_id");
  //   }
  //   return obj;
  // }

}
