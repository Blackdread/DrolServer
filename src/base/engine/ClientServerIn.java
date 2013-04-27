package base.engine;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 
 * @author Yoann CAPLAIN
 * @author Nicolas DUPIN
 * @since 18 04 2013
 */
public class ClientServerIn implements Runnable{
    private Socket s;
    private ClientServer clientServer;
    private boolean continuer = true;
     
    public ClientServerIn(Socket soc, ClientServer clientServer){
    	s = soc;
    	this.clientServer = clientServer;
    }
    
    public void run(){
        System.out.println("clientIn demarrer");
        try{
        	ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        	
        	while(continuer){
	        	Object ob = in.readObject();
	        	if(ob instanceof Message){
	        		Message ob2 = (Message)ob;
	        		switch(ob2.instruction){
	        		case MessageKey.I_START_NEW_GAME:
	        			if(clientServer.getPartie() == null)
	        				clientServer.getServer().creePartie(clientServer);
	        			break;
	        		case MessageKey.I_JOIN_GAME:
	        			if(clientServer.getPartie() == null){
	        				if(ob2.i_data.containsKey(MessageKey.P_ID))
	    					{
	    						if(clientServer.getServer().rejoindrePartie(ob2.i_data.get(MessageKey.P_ID), clientServer)){
	    							// TODO Quand le RMI ?? pour eviter ce genre de message...
	    							Message mes = new Message();
	    							mes.instruction = MessageKey.I_CHANGE_VIEW_TO_SALON;
	    							mes.engine = EngineManager.NETWORK_ENGINE;
	    							
	    							clientServer.getOut().receiveMessage(mes);
	    						}
	    						//TODO envoyer au client toute les informations sur la partie
	    					}
	        			}
	        			break;
	        		case MessageKey.I_LEAVE_GAME:
	        			// TODO
	        			break;
	        		case MessageKey.I_CLIENT_END_LOADING:
	        			if(clientServer.getPartie() != null && clientServer.getPartie() instanceof Jeu){
	        				((Jeu)clientServer.getPartie()).playerEndedLoading(clientServer);
	        				
	        				if(((Jeu)clientServer.getPartie()).isPlayingGame()){// Les joueurs jouent deja, c qqun qui rejoins de cours de partie
	        					
	        				}
	        			}
	        			break;
	        		case MessageKey.I_LAUNCH_GAME:
	        			if(clientServer.getPartie() != null)
        					//L'host veut lancer la partie
        					if(clientServer.getId() == clientServer.getPartie().getHost().getId())
        					{
        						clientServer.getServer().lancerPartie((Salon) clientServer.getPartie());
        						
        						//*
        			        	try {
        							Thread.sleep(5);
        						} catch (InterruptedException e) {
        							e.printStackTrace();
        						}
        			        	//*/
        						
        						clientServer.getPartie().getEngineManager().getNetworkEngine().sendToPlayersToChangeViewToTransition();
        					}
	        			break;
	        		default:
	        			if(clientServer.getPartie() != null)
	        			{
	        				ob2.i_data.put(MessageKey.P_ID_CLIENT, clientServer.getId());
	        				clientServer.getPartie().engineManager.receiveMessage(ob2);
	        				clientServer.getPartie().engineManager.getNetworkEngine().receiveMessage(ob2);
	        			}
	        			break;
	        		}
	        	}else{
	        		if(clientServer.getPartie() != null)
	        			clientServer.getPartie().engineManager.getNetworkEngine().receiveMessage(ob);
	        	}
	        	//*
	        	try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	//*/
        	}
        	
        }catch(Exception e){
        	e.printStackTrace();
        	continuer=false;
        }
		
		clientServer.clientDisconnected();
		
        System.out.println("clienIn fin "+clientServer.getId());
    }

	public void setContinuer(boolean continuer) {
		this.continuer = continuer;
	}
}
