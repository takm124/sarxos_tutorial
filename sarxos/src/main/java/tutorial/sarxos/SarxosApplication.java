package tutorial.sarxos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SarxosApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SarxosApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SarxosApplication.class, args);

	}

}
