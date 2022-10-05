package tutorial.sarxos.webSocket;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.Webcam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Component
@Slf4j
public class WebSocketHandler extends AbstractWebSocketHandler {

    private static List<WebSocketSession> sessions = new ArrayList<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket Connection Established");
        sessions.add(session);

        WebCamCache cache = WebCamCache.getInstance();

        cache.subscribe(this);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.removeIf(sess -> sess.equals(session));
        log.info("session closed {}", session);
    }

    public void newImage(Webcam webcam, BufferedImage image) {
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

        try {
            String msg = MAPPER.writeValueAsString(message);
            log.trace("send message {}", msg);

            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(msg));
            }

        } catch (Exception e) {
            log.error("new image error {}", e.getMessage());
        }

    }




    public boolean checkSession() {
        return false;
    }

}
