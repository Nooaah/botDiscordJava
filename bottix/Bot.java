package bottix;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.EventListener;


public class Bot {
    public static void main(String[] args) throws LoginException {
        System.out.println("Run Bottix...");
        try {
            JDA jda = new JDABuilder("Njk0MTQ1NzU4NDYyNDc2MzM4.XoSw7w.L8iJwQ4LoQyDhT3xOsY0Fy5pD1E").build();

            jda.addEventListener(new MyListener());

        } catch (Exception e) {
            System.out.println("Erreur lors du démarrage de Bottix : " + e);
        }
    }
}

//javac -cp .:JDA-4.1.1_101-withDependencies.jar:json-20190722.jar bottix/Bot.java
//java -cp .:JDA-4.1.1_101-withDependencies.jar:json-20190722.jar  bottix/Bot