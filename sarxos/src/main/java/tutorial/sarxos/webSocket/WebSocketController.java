package tutorial.sarxos.webSocket;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class WebSocketController {

    @GetMapping("/ws/webcam")
    public String index() {
        log.info("mapping success");
        return "index.html";
    }
}
