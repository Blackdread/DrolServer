package base.engine;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Yoann CAPLAIN
 * @author Nicolas DUPIN
 * @since 18 04 2013
 */
public class ClientServerOut implements Runnable{
    
    private Socket s;
    private ClientServer clientServer;
    private boolean continuer = true;
    private ObjectOutputStream out;
    private Queue<Object> message_queue = new LinkedList<Object>(); 
    
    public ClientServerOut(Socket soc, ClientServer clientServer){
      s = soc;
      this.clientServer = clientServer;
    }
    
    synchronized public void receiveMessage(Object mes){
    	/*
    	if(message_queue.size() > 100){
			message_queue.clear();
			System.err.println("**********\n**********\n**********\n**********\nVIDER SERVER\n**********\n**********\n**********\n**********\n");
		}*/
		message_queue.add(mes);
		//System.out.println("clientOut recu");
	}
    synchronized private Object poll(){
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
					out.flush();
					out.reset();
				} catch (IOException e) {
					e.printStackTrace();
					continuer=false;
					System.out.println("fin clientOut "+clientServer.getId()+" arreter pour cause d'erreur sur le flux out"); 
					break;
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
