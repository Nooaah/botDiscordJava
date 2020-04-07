package bottix;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;

public class Bot {
    public static void main(String[] args) throws LoginException {
        System.out.println("Launching Bottix...");
        try {
            JDA jda = new JDABuilder("Njk0MTQ1NzU4NDYyNDc2MzM4.Xoxk3Q.01RO1uiowXlp-N5by1lkUaqc4gk").build();
            jda.addEventListener(new MyListener());
            System.out.println("Bottix is online !");
        } catch (Exception e) {
            System.out.println("Erreur lors du démarrage de Bottix : " + e);
        }
    }
}