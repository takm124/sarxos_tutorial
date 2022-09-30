package tutorial.sarxos.webSocket;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.Webcam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private WebSocketSession session;

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

    private void setup(WebSocketSession session) {
        this.session = session;

        Map<String, Object> message = new HashMap<>();
        message.put("type", "list");
        message.put("webcams", WebCamCache.getWebcamNames());

        send(message);

        WebCamCache.subscribe(this);
    }

    public void newImage(Webcam webcam, BufferedImage image) {
        LOG.info("New image from {}", webcam);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPG", baos);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        String base64 = null;
        try {
            base64 = new String(Base64.getEncoder().encode(baos.toByteArray()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }

        Map<String, Object> message = new HashMap<>();
        message.put("type", "image");
        message.put("webcam", webcam.getName());
        message.put("image", base64);

        send(message);
    }


    private void send(WebSocketMessage<String> message) {

        if (session.isOpen()) {
            try {
                //session.getRemote().sendStringByFuture(message); // 비동기 메시지 송수신
                session.sendMessage(message);
            } catch (Exception e) {
                LOG.error("Exception when sending string", e);
            }
        }
    }

    private void send(Object object) {
        try {
            send(MAPPER.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }
}
