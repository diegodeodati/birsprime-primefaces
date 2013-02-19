package it.betplus.birs.primitive;

public class SistemaGioco {

	
	
	public String NOME;
	public long AAMS_SISTEMAGIOCO_ID;
	
	public SistemaGioco(String nOME, long aAMS_SISTEMAGIOCO_ID) {
		super();
		NOME = nOME;
		AAMS_SISTEMAGIOCO_ID = aAMS_SISTEMAGIOCO_ID;
	}

	public String getNOME() {
		return NOME;
	}

	public void setNOME(String nOME) {
		NOME = nOME;
	}

	public long getAAMS_SISTEMAGIOCO_ID() {
		return AAMS_SISTEMAGIOCO_ID;
	}

	public void setAAMS_SISTEMAGIOCO_ID(long aAMS_SISTEMAGIOCO_ID) {
		AAMS_SISTEMAGIOCO_ID = aAMS_SISTEMAGIOCO_ID;
	}

	@Override
	public String toString() {
		return "SistemaGioco [NOME=" + NOME + ", AAMS_SISTEMAGIOCO_ID="
				+ AAMS_SISTEMAGIOCO_ID + "]";
	}
	
	
	
	
}
