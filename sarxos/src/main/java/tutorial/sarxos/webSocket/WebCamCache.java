package tutorial.sarxos.webSocket;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamUpdater;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WebCamCache implements WebcamUpdater.DelayCalculator, WebcamListener {

    private static final Logger LOG = LoggerFactory.getLogger(WebCamCache.class);

    /**
     * How often images are updated on server
     */
    private static final long DELAY = 10000;

    /**
     * Webcam list
     */
    private Map<String, Webcam> webcams = new HashMap<>();

    private List<WebSocketHandler> handlers = new ArrayList<>();

    private static final WebCamCache CACHE = new WebCamCache();


    public WebCamCache() {
        log.info("WebCamCache() init");
        for (Webcam webcam : Webcam.getWebcams()) {
            webcam.addWebcamListener(this);
            webcam.open(true, this);
            webcams.put(webcam.getName(), webcam);
        }
    }


    @Override
    public long calculateDelay(long snapshotDuration, double deviceFps) {
        return Math.max(DELAY - snapshotDuration, 0);
    }

    public static BufferedImage getImage(String name) {
        Webcam webcam = CACHE.webcams.get(name);
        try {
            return webcam.getImage(); // return capture image from webcam
        } catch (Exception e) {
            LOG.error("Exception when getting image from webcam", e);
        }

        return null;
    }

    public static List<String> getWebcamNames() {
        return new ArrayList<String>(CACHE.webcams.keySet());
    }

    @Override
    public void webcamOpen(WebcamEvent we) {
        // do nothing
    }

    @Override
    public void webcamClosed(WebcamEvent we) {
        // do nothing
    }

    @Override
    public void webcamDisposed(WebcamEvent we) {
        // do nothing
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        for (WebSocketHandler handler : handlers) {
            handler.newImage(we.getSource(), we.getImage());
        }
    }

    public static void subscribe(WebSocketHandler handler) {
        for (Webcam webcam : Webcam.getWebcams()) {
            log.info("subscribe {}", getWebcamNames());
        }

        CACHE.handlers.add(handler);
    }

    public static void unsubscribe(WebSocketHandler handler) {
        CACHE.handlers.remove(handler);
    }

}
