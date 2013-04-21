package base.engine;

/**
 * Une barriere qui est utilise comme un semaphore
 * @author Yoann CAPLAIN
 */
public class Barriere {
    
    private int valMin, valMax;
    private int compteur;
    
    
    public Barriere(int min, int max){
        valMin=min;
        valMax=max;
        compteur = min;
    }
    
    synchronized public void incrementCompteur(){
        if(compteur + 1 <= valMax)
            compteur++;
    }
    synchronized public void decrementCompteur(){
        if(compteur - 1 >= valMin)
            compteur--;
    }
    synchronized public void decrementMaxValue(){
        if(valMax - 1 >= valMin)
        	valMax--;
    }
    synchronized public int getCompteur(){
        return compteur;
    }
    synchronized public boolean isMax(){
        return valMax == compteur;
    }
    synchronized public void resetCompteur(){
        compteur = valMin;
    }
}
