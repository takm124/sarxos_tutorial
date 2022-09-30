package tutorial.sarxos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SarxosApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SarxosApplication.class);

	public static void main(String[] args) throws Exception{
		SpringApplication.run(SarxosApplication.class, args);

		/*Webcam webcam = Webcam.getDefault(); // search webcam
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}*/


		/*for (String name : WebCamCache.getWebcamNames()) {
			LOG.info("Will read webcam {}", name);
		}*/

		/*Server server = new Server(12345); // port
		WebSocketHandler handler =  new WebSocketHandler() {
			@Override
			public void configure(WebSocketServletFactory factory) {
				factory.register(WebSocketHandler.class);
			}
		};

		server.setHandler(handler);
		server.start();
		server.join();*/


	}

}
