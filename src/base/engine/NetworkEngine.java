package base.engine;

/**
 * NetworkEngine server
 * @author Yoann CAPLAIN
 *
 */
public class NetworkEngine extends Engine {
	
	private Partie partie;
	
	public NetworkEngine(EngineManager engineManager)
	{
		super(engineManager);
	}
	
	@Override
	public synchronized boolean processMessage() {
		//while(!this.message_queue.isEmpty()){
		if(!this.message_queue.isEmpty()){
			Object mes = this.message_queue.poll();
			
			if(mes instanceof MessageTchat){
				partie.getListeDesJoueursDansLaPartie().diffTous(mes);
			}else
				if(mes instanceof Message){
					engineManager.receiveMessage((Message)mes);
					if(((Message)mes).i_data.containsKey((MessageKey.P_ID_CLIENT)))
						partie.getListeDesJoueursDansLaPartie().diffTousSaufEmetteur((Message)mes, ((Message)mes).i_data.get(MessageKey.P_ID_CLIENT));
					System.out.println("message recu server");
				}
			
		}else
			return false;
		
		return true;
	}

	public Partie getPartie() {
		return partie;
	}

	public void setPartie(Partie partie) {
		this.partie = partie;
	}

	

}
