package tutorial.sarxos.webSocket;

import com.github.sarxos.webcam.*;
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

    private static final long DELAY = 33; // 30 frame

    private static Webcam webCam;
    private WebSocketHandler handler;
    private static WebCamCache CACHE;


    public WebCamCache() {
        log.info("WebCamCache() init");

        webCam = Webcam.getDefault();

        Dimension frameSize = new Dimension(160, 120);
        webCam.setCustomViewSizes(new Dimension[] { frameSize });
        webCam.setViewSize(frameSize);

        webCam.addWebcamListener(this);
        webCam.open(true, this);
    }

    public static WebCamCache getInstance()
    {
        log.info("WebCamCache getInstance()");
        if (CACHE == null) {
            synchronized (WebCamCache.class) {
                CACHE = new WebCamCache();
            }
        }
        return CACHE;
    }

    public static String getWebcamName() {
        return CACHE.webCam.getName();
    }

    public void subscribe(WebSocketHandler handler) {
        log.info("subscribe {}", getWebcamName());
        CACHE.handler = handler;
    }

    @Override
    public long calculateDelay(long snapshotDuration, double deviceFps) {
        return Math.max(DELAY - snapshotDuration, 0);
    }

    @Override
    public void webcamImageObtained(WebcamEvent we) {
        handler.newImage(we.getSource(), we.getImage());
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





}
