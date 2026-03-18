package com.gpsit.controle_leads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ControleLeadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleLeadsApplication.class, args);
	}

	@EventListener({ApplicationReadyEvent.class})
	public void applicationReadyEvent() {
		System.out.println("Application started ... launching browser now");
		String url = "http://localhost:8080/leads";
		try {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("win")) {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (os.contains("mac")) {
				Runtime.getRuntime().exec("open " + url);
			} else if (os.contains("nix") || os.contains("nux")) {
				Runtime.getRuntime().exec("xdg-open " + url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
