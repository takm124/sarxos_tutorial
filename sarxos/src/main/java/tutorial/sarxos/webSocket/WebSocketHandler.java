package tutorial.sarxos.webSocket;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.Webcam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WebSocketHandler extends AbstractWebSocketHandler {

    private static WebSocketSession session;

    /**
     * session clear
     */
    private void teardown() {
        try {
            session.close();
            session = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void newImage(Webcam webcam, BufferedImage image) {
        log.info("New image from {}", webcam);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPG", baos);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        String base64 = null;
        try {
            base64 = new String(Base64.getEncoder().encode(baos.toByteArray()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        Map<String, Object> message = new HashMap<>();
        message.put("type", "image");
        message.put("webcam", webcam.getName());
        message.put("image", base64);

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket Connection Established");

        Webcam webcam = Webcam.getDefault(); // search webcam
		if (webcam != null) {
			log.info("Webcam: " + webcam.getName());
		} else {
			log.info("No webcam detected");
		}
        webcam.setViewSize(new Dimension(160, 120));

        log.info("webCam setup start");

        this.session = session;

        Map<String, Object> message = new HashMap<>();
        message.put("type", "list");
        message.put("webcams", WebCamCache.getWebcamNames());

        WebCamCache.subscribe(this);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (session.isOpen()) {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                log.error("Exception when sending string", e);
            }
        }
    }
}
