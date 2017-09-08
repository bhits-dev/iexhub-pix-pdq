package gov.samhsa.c2s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class IexhubPixPdqApplication {

	public static void main(String[] args) {
		SpringApplication.run(IexhubPixPdqApplication.class, args);
	}
}
