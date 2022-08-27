package org.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetAllPlaylists {
    public static void main(String[] args) throws UnirestException {
        final String cookie = "Your cookie here";

        //Get all the songs
        HttpResponse<String> response1 = Unirest.get("https://www.jiosaavn.com/api.php?__call=library.getAll&api_version=4&_format=json&_marker=0&ctx=wap6dot0")
                .header("Cookie", cookie)
                .asString();

        JSONObject libraryDetailsJson = new JSONObject(response1.getBody());


        String playlistDetailsUrl = "https://www.jiosaavn.com/api.php?__call=library.getDetails&entity_type=playlist&api_version=4&_format=json&_marker=0&ctx=wap6dot0&n=1000";

        System.out.println("No of playlists: " + libraryDetailsJson.getJSONArray("playlist").length());
        String playlistIds = "";
        for (Object o : libraryDetailsJson.getJSONArray("playlist")) {
            JSONObject playlist = (JSONObject) o;
            playlistIds += playlist.getString("id") + ",";
        }
        playlistIds = playlistIds.substring(0, playlistIds.length() - 1);

        //Getting Playlist details
        System.out.println("Getting Playlist details");
        HttpResponse<String> response2 = Unirest.get(playlistDetailsUrl)
                .header("Cookie", cookie)
                .queryString("entity_ids", playlistIds)
                .asString();

        JSONArray playListDetailsJson = new JSONArray(response2.getBody());

        for (Object o : playListDetailsJson) {
            JSONObject playlist = (JSONObject) o;
            System.out.println("Name: " + playlist.getString("title") + ", Id: " + playlist.getString("id"));
        }

    }
}
