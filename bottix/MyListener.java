package bottix;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


import net.dv8tion.jda.api.entities.Message;

public class MyListener extends ListenerAdapter {
    final String BOT_PREFIX = "/15";

    static String getMeteo() {
        String result = "";
        try {
            String APIkey = "fb557efd2995a66e6f189efa18ae200a";
            String serv = "http://api.openweathermap.org/data/2.5/weather";
            String ville = "Calais";
            String param = "q=" + ville + "&units=metric&appid=";
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

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println(event);
        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        String content = message.getContentRaw();
        System.out.println(content);

        if (content.equals(BOT_PREFIX + " ping")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Pong " + event.getMember().getAsMention() + " !").queue();
            System.out.println(getMeteo());
            JSONObject jsonObject = new JSONObject(getMeteo());

        }

        if (content.contains(BOT_PREFIX + " dice")) {
            MessageChannel channel = event.getChannel();
            String[] tab = content.split(" ");
            int number;
            if (tab.length == 2) {
                number = 6;
            } else {
                number = (int) (Math.random() * (Integer.valueOf(tab[2])));
            }
            // getAuthor().getName()
            channel.sendMessage("Hmm I'm thinking " + event.getMember().getAsMention() + ", " + number + " !").queue();
        }
        // Pour la méteo : embed
        if (content.contains(BOT_PREFIX + " cat")) {
            MessageChannel channel = event.getChannel();
            String[] tab = content.split(" ");
            if (content.contains("tag")) {

                try {
                    URL url = new URL("https://cataas.com/cat/" + tab[3]);
                    BufferedImage img = ImageIO.read(url);
                    File file = new File("temp.jpg");
                    ImageIO.write(img, "jpg", file);
                    event.getChannel().sendFile(file).queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("Error fetching image 1.").queue();
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
                    event.getChannel().sendMessage("Error fetching image 2.").queue();
                }

            } else {
                try {
                    URL url = new URL("https://cataas.com/cat");
                    BufferedImage img = ImageIO.read(url);
                    File file = new File("temp.jpg");
                    ImageIO.write(img, "jpg", file);
                    event.getChannel().sendFile(file).queue();

                } catch (Exception e) {
                    event.getChannel().sendMessage("Error fetching image.").queue();
                }

            }

        }

    }
}
