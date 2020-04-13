package server.ws;
 
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.web.socket.TextMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import kurento.UserSession;

 
@ServerEndpoint("/websocketendpoint")
public class WsServer {
	 private Session wsSession;
     private HttpSession httpSession;

     
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        System.out.println("Open Connection ...");
        this.wsSession = session;
        this.httpSession = (HttpSession) config.getUserProperties()
                                           .get(HttpSession.class.getName());
    
    }
     
    @OnClose
    public void onClose(){
        System.out.println("Close Connection ...");
    }
     
    @OnMessage
    public String onMessage(String message) throws IOException{
        System.out.println("Message from the client Ali: " + message);
        String echoMsg = "Echo from the server : " + message;
        
        //wsSession.getBasicRemote().sendText(message);
        try {
        	
        	TextMessage ts = new TextMessage(message);
        	//echoMsg = handleTextMessage(wsSession, ts);
        	handleTextMessage(wsSession, ts);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        
        
        return echoMsg;
    }
 
    @OnError
    public void onError(Throwable e){
        e.printStackTrace();
    }
 
    
    //---------------------------------------------
    //--esta es la lógica
    //---------------------------------------------
    private int clients=0; 
    private static final Gson gson = new GsonBuilder().create();
    public void handleTextMessage(Session session, TextMessage message) throws Exception {
    	String s = "";
    	JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
    	
    	if (jsonMessage.get("id")==null) {
    		return;
    	}
    	
    	switch (jsonMessage.get("id").getAsString()) {
        case "NewClient":
          try {
            //s = register(session, jsonMessage);
        	register(session, jsonMessage);
          } catch (Throwable t) {
        	  t.printStackTrace();
            //handleErrorResponse(t, session, "registerResponse");
          }
          break;
        case "Offer":
            try {
              //s = register(session, jsonMessage);
            	SendOffer(session, jsonMessage);
            } catch (Throwable t) {
          	  t.printStackTrace();
              //handleErrorResponse(t, session, "registerResponse");
            }
            break;
        case "Answer":
            try {
              //s = register(session, jsonMessage);
            	SendAnswer(session, jsonMessage);
            } catch (Throwable t) {
          	  t.printStackTrace();
              //handleErrorResponse(t, session, "registerResponse");
            }
            break;
        default:
	          break;
    	}
    	
      }
    
    private void register(Session session, JsonObject jsonMessage) throws IOException {
	      String name = jsonMessage.getAsJsonPrimitive("name").getAsString();

	      UserSession caller = new UserSession(session, name);
	      String responseMsg = "accepted";
	      
	      JsonObject response = new JsonObject();
	      if(clients < 2){
				if(clients <= 1){
					response.addProperty("id", "CreatePeer");
					responseMsg = "accepted";
				}
			}
			else{
				response.addProperty("id", "SessionActive");
				responseMsg = "rejected: user already registered";
				//this.emit('SessionActive')
			}
			clients++;
	      
	      response.addProperty("response", responseMsg);
	      caller.sendMessage(response);
	      
	      //return response.toString();
	    }
    
    private void Disconnect(){
    	if(clients > 0)
    		clients--;
    }

    private void SendOffer(Session session, JsonObject jsonMessage) throws IOException{
    	//this.broadcast.emit("BackOffer", offer)
    	//String offer = jsonMessage.get("name").getAsString();
    	
    	UserSession caller = new UserSession(session, "");
	    String responseMsg = "accepted";
    	
    	JsonObject response = new JsonObject();
    	response.addProperty("id", "BackOffer");
		//responseMsg = offer;
		response.addProperty("response", jsonMessage.get("stream").toString());
	    caller.sendMessage(response);
		
    }

    private void SendAnswer(Session session, JsonObject jsonMessage) throws IOException{
    	//this.broadcast.emit("BackAnswer", data)
    	String data = jsonMessage.get("name").getAsString();
    	
    	UserSession caller = new UserSession(session, data);
	    String responseMsg = "accepted";
    	
    	JsonObject response = new JsonObject();
    	response.addProperty("id", "BackAnswer");
		responseMsg = data;
		response.addProperty("response", responseMsg);
	    caller.sendMessage(response);
    }
   
    
}