package aiss.bitbucket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BitbucketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitbucketApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {return builder.build(): }
}
