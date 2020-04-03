package bottix;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.EmbedBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.sql.Timestamp;
import java.util.Date;

import java.util.stream.Collectors;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import net.dv8tion.jda.api.entities.Message;

import org.json.*;
import java.awt.Color;

public class MyListener extends ListenerAdapter {
    final String BOT_PREFIX = "/15";

    static String getMeteo(String ville) {
        String result = "";
        try {
            String APIkey = "fb557efd2995a66e6f189efa18ae200a";
            String serv = "http://api.openweathermap.org/data/2.5/weather";
            String town = ville;
            String param = "q=" + town + "&units=metric&appid=";
            URL url = new URL(serv + "?" + param + APIkey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code:" + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            result = br.lines().collect(Collectors.joining());
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    static String getImageNasa(String date) {
        String result = "";
        try {
            String serv = "https://api.nasa.gov/planetary/apod?api_key=9OcsTiEIboZbpVhSwqBHjuFaEATzpC6knb4Auepj&date="
                    + date;
            URL url = new URL(serv);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code:" + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            result = br.lines().collect(Collectors.joining());
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    static String getImage(String search, int page) {
        String result = "";
        try {
            String serv = "https://api.unsplash.com/search/photos?client_id=17dd4488ccc46e89e2aff30172d4d77295a5448dd70674140330b75057d61397&page="
                    + page + "&query=" + search;
            URL url = new URL(serv);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code:" + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            result = br.lines().collect(Collectors.joining());
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    static String getQuestion() {
        String result = "";
        try {
            String serv = "http://jservice.io/api/random";
            URL url = new URL(serv);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code:" + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            result = br.lines().collect(Collectors.joining());
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println(event);
        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        System.out.println(content);

        if (content.contains(BOT_PREFIX + " help")) {
            String[] tab = content.split(" ");

            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
            builder.setTitle("Aides pour les commandes");
            builder.addField("Ping", BOT_PREFIX + " ping", true);
            builder.addField("Dice", BOT_PREFIX + " dice *MaxOuRienPour6*", true);
            builder.addField("Chat aléatoire", BOT_PREFIX + " cat", true);
            builder.addField("Chat avec tag", BOT_PREFIX + " cat tag *cute*", true);
            builder.addField("Chat avec texte", BOT_PREFIX + " cat text *VotreTexte*", true);
            builder.addField("Météo", BOT_PREFIX + " meteo *VilleOuRienPourCalais*", true);
            builder.addField("NASA", BOT_PREFIX + " nasa *JJ/MM/AAAAouRien*", true);
            builder.setThumbnail("https://zupimages.net/up/20/14/tebn.png");
            builder.setFooter("Created with ❤ by Nooaah",
                    "https://avatars1.githubusercontent.com/u/47362864?s=460&u=04d5044f526000883d018d140a1b9850a17090fd&v=4");
            builder.setColor(new Color(69, 135, 244));

            MessageChannel channel = event.getChannel();
            channel.sendMessage("Voici les commandes pour t'aider " + event.getMember().getAsMention() + " ! ").queue();
            channel.sendMessage(builder.build()).queue();

        }

        if (content.equals(BOT_PREFIX + " ping")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong " + event.getMember().getAsMention() + " !").queue();
        }

        if (content.contains(BOT_PREFIX + " meteo")) {
            String[] tab = content.split(" ");
            String ville = "Calais";
            if (tab.length != 2)
                ville = tab[2];
            JSONObject meteo = new JSONObject(getMeteo(ville));

            JSONArray arr = meteo.getJSONArray("weather");
            String icon = "";
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jObj = arr.getJSONObject(i);
                icon = jObj.getString("icon");
            }
            Object temp = ((JSONObject) meteo.get("main")).get("temp");
            Object feel = ((JSONObject) meteo.get("main")).get("feels_like");
            Object humidity = ((JSONObject) meteo.get("main")).get("humidity");
            Object country = ((JSONObject) meteo.get("sys")).get("country");
            Object wind = ((JSONObject) meteo.get("wind")).get("speed");
            Object sunrise = ((JSONObject) meteo.get("sys")).get("sunrise");

            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("Catherine Laborde", null,
                    "https://static1.purepeople.com/articles/6/37/00/46/@/5335919-catherine-laborde-33eme-salon-du-livre-624x600-2.jpg");
            builder.setTitle("Météo en direct");
            builder.addField("Ville",
                    ville.substring(0, 1).toUpperCase() + ville.substring(1).toLowerCase() + ", " + country, true);
            builder.addField("Température", temp + "°C", true);
            builder.addField("Ressentis", feel + "°C", true);
            builder.addField("Humidité", humidity + "%", true);
            builder.addField("Vent", wind + "km/h", true);
            builder.setImage("http://openweathermap.org/img/wn/" + icon + "@2x.png");
            builder.setColor(new Color(69, 135, 244));
            builder.setFooter("Created with ❤ by Nooaah",
                    "https://avatars1.githubusercontent.com/u/47362864?s=460&u=04d5044f526000883d018d140a1b9850a17090fd&v=4");

            MessageChannel channel = event.getChannel();
            channel.sendMessage("Here's the weather from " + ville.substring(0, 1).toUpperCase()
                    + ville.substring(1).toLowerCase() + " " + event.getMember().getAsMention() + " ! ").queue();
            channel.sendMessage(builder.build()).queue();

        }

        if (content.contains(BOT_PREFIX + " dice")) {
            MessageChannel channel = event.getChannel();
            String[] tab = content.split(" ");
            int number;
            if (tab.length == 2) {
                number = (int) (Math.random() * 6);
            } else {
                number = (int) (Math.random() * (Integer.valueOf(tab[2])));
            }
            channel.sendMessage("Hmm I'm thinking " + event.getMember().getAsMention() + ", " + number + " !").queue();
        }

        if (content.contains(BOT_PREFIX + " cat")) {
            MessageChannel channel = event.getChannel();
            String[] tab = content.split(" ");
            if (content.contains("tag")) {
                try {
                    URL url = new URL("https://cataas.com/cat/" + tab[2]);
                    BufferedImage img = ImageIO.read(url);
                    File file = new File("temp.jpg");
                    ImageIO.write(img, "jpg", file);
                    event.getChannel().sendFile(file).queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(
                            "P'tit problème technique... Tu peux peut-être réessayer ?! 😎\nN'hésite pas à faire */15 help* pour voir toutes les commandes")
                            .queue();
                }
            } else if (content.contains("text")) {
                String text = "";
                for (int i = 3; i < tab.length; i++) {
                    text += tab[i] + " ";
                }
                try {
                    URL url = new URL("https://cataas.com/cat/says/" + text);
                    BufferedImage img = ImageIO.read(url);
                    File file = new File("temp.jpg");
                    ImageIO.write(img, "jpg", file);
                    event.getChannel().sendFile(file).queue();

                } catch (Exception e) {
                    event.getChannel().sendMessage(
                            "P'tit problème technique... Tu peux peut-être réessayer ?! 😎\nN'hésite pas à faire */15 help* pour voir toutes les commandes")
                            .queue();
                }
            } else {
                try {
                    URL url = new URL("https://cataas.com/cat");
                    BufferedImage img = ImageIO.read(url);
                    File file = new File("temp.jpg");
                    ImageIO.write(img, "jpg", file);
                    event.getChannel().sendFile(file).queue();

                } catch (Exception e) {
                    event.getChannel().sendMessage(
                            "P'tit problème technique... Tu peux peut-être réessayer ?! 😎\nN'hésite pas à faire */15 help* pour voir toutes les commandes")
                            .queue();
                }
            }
        }

        if (content.contains(BOT_PREFIX + " nasa")) {
            MessageChannel channel = event.getChannel();
            String[] tab = content.split(" ");
            String annee, mois, jour;
            if (tab.length != 2) {
                String[] date = tab[2].split("/");
                jour = date[2];
                mois = date[1];
                annee = date[0];
            } else {
                jour = "2020";
                mois = "03";
                annee = "02";
            }

            System.out.println(jour + "-" + mois + "-" + annee);
            try {
                JSONObject result = new JSONObject(getImageNasa(jour + "-" + mois + "-" + annee));

                Object imageUrl = ((JSONObject) result).get("url");
                Object desc = ((JSONObject) result).get("explanation");

                System.out.println(imageUrl);

                channel.sendMessage("Here's a NASA image on this date " + event.getMember().getAsMention() + " !\n"
                        + imageUrl.toString() + "\nDescription : \n" + desc.toString()).queue();

            } catch (Exception e) {
                channel.sendMessage(
                        "Désolé j'ai eu un petit Bug... Il n'y a peut etre aucune image à cette date, réessaie encore et observe ! 😎\nN'hésite pas à faire */15 help* pour voir toutes les commandes")
                        .queue();
            }
        }

        if (content.contains(BOT_PREFIX + " question")) {
            MessageChannel channel = event.getChannel();
            try {

                JSONArray callApi = new JSONArray(getQuestion());
                String question = "";
                String answer = "";
                for (int i = 0; i < callApi.length(); i++) {
                    JSONObject jObj = callApi.getJSONObject(i);
                    question = jObj.getString("question");
                    answer = jObj.getString("answer");
                }

                /*
                 * JSONObject result = new JSONObject(getQuestion());
                 * 
                 * Object question = ((JSONObject) result).get("question"); Object answer =
                 * ((JSONObject) result).get("answer");
                 */
                channel.sendMessage("Hey ! There is the question for" + event.getMember().getAsMention() + " !\n\n"
                        + question.toString() + "\n\nAnswer :\n\n||" + answer.toString() + "||").queue();

            } catch (Exception e) {
                System.out.println(e);
                channel.sendMessage(
                        "Désolé j'ai eu un petit Bug... réessaie encore et observe ! 😎\nN'hésite pas à faire */15 help* pour voir toutes les commandes")
                        .queue();
            }
        }

        if (content.contains(BOT_PREFIX + " image")) {
            String[] tab = content.split(" ");

            MessageChannel channel = event.getChannel();
            int number = (int) (Math.random() * 6);
            try {

                JSONObject callApi = new JSONObject(getImage(tab[2], number));
                JSONArray arr = callApi.getJSONArray("results");
                JSONObject raw = new JSONObject();
                String image = "";
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jObj = arr.getJSONObject(i);
                    raw = jObj.getJSONObject("urls");
                    image = raw.getString("full");
                }

                try {
                    URL url = new URL(image);
                    BufferedImage img = ImageIO.read(url);
                    File file = new File("temp.jpg");
                    ImageIO.write(img, "jpg", file);
                    event.getChannel().sendFile(file).queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage(
                            "P'tit problème technique... Tu peux peut-être réessayer ?! 😎\nN'hésite pas à faire */15 help* pour voir toutes les commandes")
                            .queue();
                }

            } catch (Exception e) {
                System.out.println(e);
                channel.sendMessage(
                        "Désolé j'ai eu un petit Bug... réessaie encore et observe ! 😎\nN'hésite pas à faire */15 help* pour voir toutes les commandes")
                        .queue();
            }
        }

    }
}
