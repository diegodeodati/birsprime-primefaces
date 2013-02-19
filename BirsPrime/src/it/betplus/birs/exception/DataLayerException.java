package it.betplus.birs.exception;

public class DataLayerException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String faultType;
	Exception exception;
	
	
	/** Costruttore della classe 
	 * 
	 * @param c Codice dell'errore
	 * @param f Testo dell'errore
	 * @param e Eccezione
	 */
	public DataLayerException(String f,Exception e){
		super();
		faultType = f;
		exception = e;
	}
	
	public DataLayerException(String f){
		super();
		faultType = f;
		exception = new Exception();
	}	
	

	public String toString(){
		String ret=" Exception ";
		ret+="Error: "+faultType+" caused by: "+exception;
		return ret;
	}
}
