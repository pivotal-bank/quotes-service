package io.pivotal.quotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class QuotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotesApplication.class, args);
    }

    static {
        HostnameVerifier allHostsValid = (name, sslSession) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}

