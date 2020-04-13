package kurento;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.kurento.client.EventListener;
import org.kurento.client.IceCandidate;
import org.kurento.client.IceCandidateFoundEvent;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.kurento.jsonrpc.JsonUtils;
import org.kurento.jsonrpc.client.JsonRpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
 
@ServerEndpoint("/call")
public class WsServerKurento {
	private Session wsSession;
    private HttpSession httpSession;
	     
	    @OnOpen
	    //public void onOpen(){
	    public void onOpen(Session session, EndpointConfig config){
	        System.out.println("Open Connection kuremnko...");
	    	this.wsSession = session;
	    	this.httpSession = (HttpSession) config.getUserProperties()
                                           .get(HttpSession.class.getName());
	    }
	     
	    @OnClose
	    public void onClose(){
	        System.out.println("Close Connection ...");
	    }
	     
	    @OnMessage
	    //public String onMessage(String message){
	    //public String onMessage(Session session, String message){
	    public void onMessage(Session session, String message){
	        System.out.println("Message from the client kurento: " + message);
	        String echoMsg = "Echo from the server : " + message;
	        
	        try {
	        	
	        	TextMessage ts = new TextMessage(message);
	        	//echoMsg = handleTextMessage(wsSession, ts);
	        	handleTextMessage(wsSession, ts);
	            }catch(Exception e) {
	            	e.printStackTrace();
	            }
	        
	        
	        //return echoMsg;
	    }
	 
	    @OnError
	    public void onError(Throwable e){
	        e.printStackTrace();
	    }
	 
	   
	    private static final Logger log = LoggerFactory.getLogger(CallHandler.class);
	    private static final Gson gson = new GsonBuilder().create();

	    private final ConcurrentHashMap<String, CallMediaPipeline> pipelines = new ConcurrentHashMap<>();

	    //@Autowired
	    private KurentoClient kurento;

	    
	    public static UserRegistry registry;

	    
	    //public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	    //public String handleTextMessage(Session session, TextMessage message) throws Exception {
	    public void handleTextMessage(Session session, TextMessage message) throws Exception {
	    	String s = "";
	      JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
	      if (registry == null) {
	    	  registry = new UserRegistry();
	      }
	      
	      if (kurento == null) {
	    	  //JsonRpcClient j = new JsonRpcClient();
	    	  //kurento = new KurentoClient(JsonRpcClient );
	    	  
	      }
	      
	      UserSession user = registry.getBySession(session);

	      if (user != null) {
	        log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
	        System.out.println("Incoming message from user: {}"+ user.getName()+"-"+jsonMessage.toString());
	      } else {
	        log.debug("Incoming message from new user: {}", jsonMessage);
	        System.out.println("Incoming message from new user: {}"+jsonMessage.toString());
	      }

	      switch (jsonMessage.get("id").getAsString()) {
	        case "register":
	          try {
	            //s = register(session, jsonMessage);
	        	register(session, jsonMessage);
	          } catch (Throwable t) {
	            handleErrorResponse(t, session, "registerResponse");
	          }
	          break;
	        case "call":
	          try {
	            //s = call(user, jsonMessage);
	        	  call(user, jsonMessage);
	          } catch (Throwable t) {
	            handleErrorResponse(t, session, "callResponse");
	          }
	          break;
	        case "incomingCallResponse":
	          //s = incomingCallResponse(user, jsonMessage);
	        	incomingCallResponse(user, jsonMessage);
	          break;
	        case "onIceCandidate": {
	          JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();
	          if (user != null) {
	            IceCandidate cand =
	                new IceCandidate(candidate.get("candidate").getAsString(), candidate.get("sdpMid")
	                    .getAsString(), candidate.get("sdpMLineIndex").getAsInt());
	            user.addCandidate(cand);
	          }
	          break;
	        }
	        case "stop":
	          //s = stop(session);
	        	stop(session);
	          break;
	        default:
	          break;
	      }
	      
	      //return s;
	    }

	    //private void handleErrorResponse(Throwable throwable, WebSocketSession session, String responseId)
	    private void handleErrorResponse(Throwable throwable, Session session, String responseId)
	        throws IOException {
	      stop(session);
	      log.error(throwable.getMessage(), throwable);
	      JsonObject response = new JsonObject();
	      response.addProperty("id", responseId);
	      response.addProperty("response", "rejected");
	      response.addProperty("message", throwable.getMessage());
	      //session.sendMessage(new TextMessage(response.toString()));
	    }

	    //private void register(WebSocketSession session, JsonObject jsonMessage) throws IOException {
	    //private String register(Session session, JsonObject jsonMessage) throws IOException {
	    private void register(Session session, JsonObject jsonMessage) throws IOException {
	      String name = jsonMessage.getAsJsonPrimitive("name").getAsString();

	      UserSession caller = new UserSession(session, name);
	      String responseMsg = "accepted";
	      if (name.isEmpty()) {
	        responseMsg = "rejected: empty user name";
	      } else if (registry.exists(name)) {
	        responseMsg = "rejected: user '" + name + "' already registered";
	      } else {
	        registry.register(caller);
	      }

	      JsonObject response = new JsonObject();
	      response.addProperty("id", "registerResponse");
	      response.addProperty("response", responseMsg);
	      caller.sendMessage(response);
	      
	      //return response.toString();
	    }

	    //private String call(UserSession caller, JsonObject jsonMessage) throws IOException {
	    private void call(UserSession caller, JsonObject jsonMessage) throws IOException {
	      String to = jsonMessage.get("to").getAsString();
	      String from = jsonMessage.get("from").getAsString();
	      JsonObject response = new JsonObject();

	      if (registry.exists(to)) {
	        caller.setSdpOffer(jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString());
	        caller.setCallingTo(to);

	        response.addProperty("id", "incomingCall");
	        response.addProperty("from", from);

	        UserSession callee = registry.getByName(to);
	        callee.sendMessage(response);
	        callee.setCallingFrom(from);
	        //callee.getSession().getBasicRemote().sendText(response.toString());

	      } else {
	        response.addProperty("id", "callResponse");
	        response.addProperty("response", "rejected: user '" + to + "' is not registered");

	        caller.sendMessage(response);
	        //caller.getAsyncRemote().sendText("Message From Java");
	      }
	      
	      //return response.toString();
	    }

	    //private String incomingCallResponse(final UserSession callee, JsonObject jsonMessage)
	    private void incomingCallResponse(final UserSession callee, JsonObject jsonMessage)
	        throws IOException {
	    	String s="";
	      String callResponse = jsonMessage.get("callResponse").getAsString();
	      String from = jsonMessage.get("from").getAsString();
	      from = from.replaceAll("\"", "");
	      final UserSession calleer = registry.getByName(from);
	      String to = calleer.getCallingTo();

	      if ("accept".equals(callResponse)) {
	        log.debug("Accepted call from '{}' to '{}'", from, to);
	        System.out.println("Accepted call from {"+from+"} to {"+to+"}");
	        CallMediaPipeline pipeline = null;
	        try {
	          pipeline = new CallMediaPipeline(kurento);
	          pipelines.put(calleer.getSessionId(), pipeline);
	          pipelines.put(callee.getSessionId(), pipeline);

	          callee.setWebRtcEndpoint(pipeline.getCalleeWebRtcEp());
	          pipeline.getCalleeWebRtcEp().addIceCandidateFoundListener(
	              new EventListener<IceCandidateFoundEvent>() {

	                @Override
	                public void onEvent(IceCandidateFoundEvent event) {
	                  JsonObject response = new JsonObject();
	                  response.addProperty("id", "iceCandidate");
	                  response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
	                  
	                  try {
	                    synchronized (callee.getSession()) {
	                      //callee.getSession().sendMessage(new TextMessage(response.toString()));
	                    callee.getSession().getBasicRemote().sendText(response.toString());
	                    }
	                  } catch (IOException e) {
	                    log.debug(e.getMessage());
	                  }
	                }
	              });

	          calleer.setWebRtcEndpoint(pipeline.getCallerWebRtcEp());
	          pipeline.getCallerWebRtcEp().addIceCandidateFoundListener(
	              new EventListener<IceCandidateFoundEvent>() {

	                @Override
	                public void onEvent(IceCandidateFoundEvent event) {
	                  JsonObject response = new JsonObject();
	                  response.addProperty("id", "iceCandidate");
	                  response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
	                  try {
	                    synchronized (calleer.getSession()) {
	                      //calleer.getSession().sendMessage(new TextMessage(response.toString()));
	                    calleer.getSession().getBasicRemote().sendText(response.toString());
	                    }
	                  } catch (IOException e) {
	                    log.debug(e.getMessage());
	                  }
	                }
	              });

	          String calleeSdpOffer = jsonMessage.get("sdpOffer").getAsString();
	          String calleeSdpAnswer = pipeline.generateSdpAnswerForCallee(calleeSdpOffer);
	          JsonObject startCommunication = new JsonObject();
	          startCommunication.addProperty("id", "startCommunication");
	          startCommunication.addProperty("sdpAnswer", calleeSdpAnswer);

	          synchronized (callee) {
	            callee.sendMessage(startCommunication);
	          }

	          pipeline.getCalleeWebRtcEp().gatherCandidates();

	          String callerSdpOffer = registry.getByName(from).getSdpOffer();
	          String callerSdpAnswer = pipeline.generateSdpAnswerForCaller(callerSdpOffer);
	          JsonObject response = new JsonObject();
	          response.addProperty("id", "callResponse");
	          response.addProperty("response", "accepted");
	          response.addProperty("sdpAnswer", callerSdpAnswer);

	          synchronized (calleer) {
	            calleer.sendMessage(response);
	          }

	          pipeline.getCallerWebRtcEp().gatherCandidates();
	          s = response.toString();
	        } catch (Throwable t) {
	        	t.printStackTrace();
	          log.error(t.getMessage(), t);

	          if (pipeline != null) {
	            pipeline.release();
	          }

	          pipelines.remove(calleer.getSessionId());
	          pipelines.remove(callee.getSessionId());

	          JsonObject response = new JsonObject();
	          response.addProperty("id", "callResponse");
	          response.addProperty("response", "rejected");
	          calleer.sendMessage(response);

	          response = new JsonObject();
	          response.addProperty("id", "stopCommunication");
	          callee.sendMessage(response);
	          s = response.toString();
	        }

	      } else {
	        JsonObject response = new JsonObject();
	        response.addProperty("id", "callResponse");
	        response.addProperty("response", "rejected");
	        calleer.sendMessage(response);
	        s = response.toString();
	      }
	      
	      //return s.toString();
	    }

	    //public void stop(WebSocketSession session) throws IOException {
	    public String stop(Session session) throws IOException {
	    	String s = "";
	      String sessionId = session.getId();
	      if (pipelines.containsKey(sessionId)) {
	        pipelines.get(sessionId).release();
	        CallMediaPipeline pipeline = pipelines.remove(sessionId);
	        pipeline.release();

	        // Both users can stop the communication. A 'stopCommunication'
	        // message will be sent to the other peer.
	        UserSession stopperUser = registry.getBySession(session);
	        if (stopperUser != null) {
	          UserSession stoppedUser =
	              (stopperUser.getCallingFrom() != null) ? registry.getByName(stopperUser
	                  .getCallingFrom()) : stopperUser.getCallingTo() != null ? registry
	                      .getByName(stopperUser.getCallingTo()) : null;

	                      if (stoppedUser != null) {
	                        JsonObject message = new JsonObject();
	                        message.addProperty("id", "stopCommunication");
	                        stoppedUser.sendMessage(message);
	                        stoppedUser.clear();
	                        
	                        s = message.toString();
	                      }
	                      stopperUser.clear();
	        }

	      }
	      return s; 
	    }

	    
	    //public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	    public void afterConnectionClosed(Session session, CloseStatus status) throws Exception {
	      stop(session);
	      registry.removeBySession(session);
	    }
	    
   
	
	
}
