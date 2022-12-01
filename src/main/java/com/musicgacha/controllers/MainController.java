package com.musicgacha.controllers;
/* MVC code that shows defining a simple Model, calling View, and this file serving as Controller
 * Web Content with Spring MVCSpring Example: https://spring.io/guides/gs/serving-web-con
 */

import com.musicgacha.data.Song;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller  // HTTP requests are handled as a controller, using the @Controller annotation
public class MainController {
    @GetMapping("/")
    // CONTROLLER handles GET request for /greeting, maps it to greeting() and does variable bindings
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) throws IOException, ParseException {
        // @RequestParam handles required and default values, name and model are class variables, model looking like JSON
        ArrayList<Song> lis = new ArrayList<>();


        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/main/resources/static/json/songs.json"));

        JSONObject jsonObject = (JSONObject) obj;

        for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();

            // Song song = new Song();
            JSONObject SongJSON = (JSONObject) jsonObject.get(key);
            lis.add(new Song((String)SongJSON.get("trackTitle"), (String)SongJSON.get("artist"), (String)SongJSON.get("lyrics"), (String)SongJSON.get("spotify"), (String)SongJSON.get("youtube")));

        }

        // Printing song data in console
        Song displaySong = lis.get((int) Math.floor(Math.random() * lis.size()));
        model.addAttribute("song", displaySong );
        model.addAttribute("name", name); // MODEL is passed to html
        return "homesite/index"; // returns HTML VIEW (greeting)

    }

    @GetMapping("/blank")
    // CONTROLLER handles GET request for /greeting, maps it to greeting() and does variable bindings
    public String blank(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        // @RequestParam handles required and default values, name and model are class variables, model looking like JSON
        model.addAttribute("name", name); // MODEL is passed to html
        return "base/blank"; // returns HTML VIEW (greeting)

    }
    @GetMapping("/api")
    public String MethodName(Model model) throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("https://covid-193.p.rapidapi.com/history?country=usa"))
		.header("X-RapidAPI-Key", "42f28884f0msh2ee7d88bea53b8dp146d11jsn2587c1474f6f")
		.header("X-RapidAPI-Host", "covid-193.p.rapidapi.com")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        // System.out.println(response.body());
        Object obj = new JSONParser().parse(response.body());
        JSONObject jo = (JSONObject) obj;
        System.out.println(jo.get("response"));
        model.addAttribute("file", jo.get("response"));
        // model.addAttribute("file", response.body());
        return "api";
    }
    
}