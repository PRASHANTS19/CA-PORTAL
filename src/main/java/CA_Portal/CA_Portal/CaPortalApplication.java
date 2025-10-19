package CA_Portal.CA_Portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CaPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaPortalApplication.class, args);
	}

}
