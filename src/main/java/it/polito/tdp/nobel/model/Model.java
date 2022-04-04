package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
		
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		// ripristino soluzione migliore
		this.migliore = new HashSet<Esame>();
		this.mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		//this.cerca1(parziale, 0, m);
		this.cerca2(parziale,0,m);
		
		return migliore;	
	}

	/*
	 * complessita' 2^N
	 */
	private void cerca2(Set<Esame> parziale, int l, int m) {
		// TODO Auto-generated method stub
		// controllare i casi terminali
		int sommaCrediti = this.sommaCrediti(parziale);
		if (sommaCrediti > m) {
			return;
		}
		if (sommaCrediti == m) {
			// soluzione valida! controlliamo se e' la migliore
			// (fino a qui)
			double mediaVoti = this.calcolaMedia(parziale);
			if (mediaVoti > this.mediaMigliore) {
				this.migliore = new HashSet<Esame>(parziale); // errore fare
															  // migliore = parziale!!!
				this.mediaMigliore = mediaVoti;
			}
			return;
		}
		
		// sicuramente, crediti < m
		if (l == esami.size()) {
			return;
		}
		
		// generiamo i sottoproblemi
		// provo a daggiungere esami[l]
		parziale.add(this.esami.get(l));
		this.cerca2(parziale, l+1, m);
		
		// provo a non aggiungere esami[l]
		parziale.remove(this.esami.get(l));
		this.cerca2(parziale, l+1, m);
	}

	/*
	 * complessita' N!
	 */
	private void cerca1(Set<Esame> parziale, int l, int m) {
		// TODO Auto-generated method stub
		// controllare i casi terminali
		int sommaCrediti = this.sommaCrediti(parziale);
		if (sommaCrediti > m) {
			return;
		}
		if (sommaCrediti == m) {
			// soluzione valida! controlliamo se e' la migliore
			// (fino a qui)
			double mediaVoti = this.calcolaMedia(parziale);
			if (mediaVoti > this.mediaMigliore) {
				this.migliore = new HashSet<Esame>(parziale); // errore fare
															  // migliore = parziale!!!
				this.mediaMigliore = mediaVoti;
			}
			return;
		}
		
		// sicuramente, crediti < m
		if (l == esami.size()) {
			return;
		}
		
		// generiamo i sottoproblemi
		for (Esame e : this.esami) {
			if (!parziale.contains(e)) {
				parziale.add(e);
				this.cerca1(parziale, l+1, m);
				parziale.remove(e);	// se parziale fosse una lista, il backtracking
									// si farebbe con parziale.remove(parziale.size() - 1)
			}
		}
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
