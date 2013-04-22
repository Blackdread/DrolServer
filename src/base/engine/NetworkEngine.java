package base.engine;

/**
 * NetworkEngine server
 * @author Blackdread
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
			Message mes = this.message_queue.poll();
			engineManager.receiveMessage(mes);
			if(mes.i_data.containsKey((MessageKey.P_ID_CLIENT)))
				partie.getListeDesJoueursDansLaPartie().diffTousSaufEmetteur(mes, mes.i_data.get(MessageKey.P_ID_CLIENT));
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
