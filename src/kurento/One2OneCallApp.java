package kurento;

import javax.websocket.server.ServerEndpoint;

import org.kurento.client.KurentoClient;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Video call 1 to 1 demo (main).
 *
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @author Micael Gallego (micael.gallego@gmail.com)
 * @since 4.3.1
 */

//@SpringBootApplication
@EnableWebSocket
public class One2OneCallApp implements WebSocketConfigurer {

  //@Bean
  public CallHandler callHandler() {
    return new CallHandler();
  }

  //@Bean
  public UserRegistry registry() {
    return new UserRegistry();
  }

  //@Bean
  public KurentoClient kurentoClient() {
    return KurentoClient.create();
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(callHandler(), "/kurento");
  }

  public static void main(String[] args) throws Exception {
    //SpringApplication.run(One2OneCallApp.class, args);
  }
}
