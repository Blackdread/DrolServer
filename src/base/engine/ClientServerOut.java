package base.engine;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author 
 */
public class ClientServerOut implements Runnable{
    
    private Socket s;
    private ClientServer clientServer;
    private boolean continuer = true;
    private ObjectOutputStream out;
    private Queue<Message> message_queue = new LinkedList<Message>(); 
    
    public ClientServerOut(Socket soc, ClientServer clientServer){
      s = soc;
      this.clientServer = clientServer;
    }
    
    synchronized public void receiveMessage(Message mes){
		message_queue.add(mes);
	}
    synchronized private Message poll(){
    	return message_queue.poll();
    }
    synchronized private boolean isEmpty(){
    	return message_queue.isEmpty();
    }
    
    public void run(){
        System.out.println("clientOut demarrer");
        
        try {
			out = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        while(continuer){
        	while(!isEmpty()){
        		try {
					out.writeObject(poll());
				} catch (IOException e) {
					e.printStackTrace();
					continuer=false;
					System.out.println("fin clientOut "+clientServer.getId()+" arreter pour cause d'erreur sur le flux out");   
				}
        	}
        	//*
        	try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	//*/
        }
        
         System.out.println("fin clientOut "+clientServer.getId());   
    }

	public boolean isContinuer() {
		return continuer;
	}

	public void setContinuer(boolean continuer) {
		this.continuer = continuer;
	}
    
}
