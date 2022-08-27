package org.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class AddAllLikedSongsToPlaylist {
    public static void main( String[] args ) throws UnirestException {
        final String cookie = "Your cookie here";
        final String listId = "List Id here";

        //Get all the songs
        HttpResponse<String> response1 = Unirest.get("https://www.jiosaavn.com/api.php?__call=library.getAll&api_version=4&_format=json&_marker=0&ctx=wap6dot0")
                .header("Cookie", cookie)
                .asString();

        JSONObject libraryDetailsJson = new JSONObject(response1.getBody());


        String songDetailsUrl = "https://www.jiosaavn.com/api.php?__call=library.getDetails&entity_type=song&api_version=4&_format=json&_marker=0&ctx=wap6dot0&n=1000";

        System.out.println("No of songs: " + libraryDetailsJson.getJSONArray("song").length());
        String songIds = "";
        for (Object o : libraryDetailsJson.getJSONArray("song")) {
            String songId = (String) o;
            songIds += songId + ",";
        }
        songIds = songIds.substring(0, songIds.length() - 1);

        //Getting songs details
        System.out.println("Getting songs details");
        HttpResponse<String> response2 = Unirest.get(songDetailsUrl)
                .header("Cookie", cookie)
                .queryString("entity_ids", songIds)
                .asString();

        JSONObject songDetailsJson = new JSONObject(response2.getBody());

        //Adding to playlist
        System.out.println("Adding to playlist");
        for (Object o : songDetailsJson.getJSONArray("songs")) {
            JSONObject song = (JSONObject) o;

            final String addToPlaylistUrl = "https://www.jiosaavn.com/api.php?__call=playlist.add&api_version=4&_format=json&_marker=0&ctx=wap6dot0";

            HttpResponse<String> response3 = Unirest.get(addToPlaylistUrl)
                    .header("Cookie", cookie)
                    .queryString("listid", listId)
                    .queryString("contents", "~~" + song.getString("id") + "~" + song.getString("language"))
                    .asString();

            if (response3.getStatus() == 200) {
                System.out.println("Song added to playlist: " + song.getString("title"));
            } else {
                System.out.println("Error adding song to playlist: " + response3.getBody());
            }
        }


    }
}
