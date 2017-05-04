package it.polito.tdp.dizionario.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.dizionario.db.WordDAO;

public class Model {

	WordDAO db = new WordDAO(); 
	UndirectedGraph<String, DefaultEdge> grafo; 
	
	public List<String> createGraph(int numeroLettere) {

		grafo = new SimpleGraph<String, DefaultEdge> (DefaultEdge.class) ; //inizializzo il grafo
		
		List<String> allWord = db.getAllWordsFixedLength(numeroLettere);
		
		for(String s : allWord)
			grafo.addVertex(s) ;
		
		for(String s: grafo.vertexSet()) {
			
			//List<String> similarWord = db.getAllSimilarWords(s, numeroLettere) ; con il db
			
			List<String> similarWord = this.getAllSimilarWords(s, allWord, numeroLettere) ;
			
			//System.out.println(similarWord);
			
			for (String s2 : similarWord){
				if(!s.equals(s2))
					grafo.addEdge(s, s2) ; 
			}
			
		}		
		
//		System.out.println(grafo);
		
		return allWord;
	}

	private List<String> getAllSimilarWords(String s, List<String> allWord, int numeroLettere) {

		List<String> similarWords = new ArrayList<String>() ;
		int count = 0 ; 
		
		for(String wildcard : allWord){ //ciclo su tutto allwor
			count = 0 ;

			for(int i = 0 ; i  < numeroLettere ; i++){
			if(s.charAt(i) != wildcard.charAt(i)) // se ho un carattere diverso
					count++ ;
			}
			
			if(count==1) {
				similarWords.add(wildcard);
			}
			
		}
		
		return similarWords;
	}

	public List<String> displayNeighbours(String parolaInserita) {		
		
		List<String> neighbours = new ArrayList<String>() ; 
		
		for(DefaultEdge d : grafo.edgesOf(parolaInserita) ){
			//System.out.println(d);
			neighbours.add(Graphs.getOppositeVertex(grafo, d, parolaInserita) ) ; // è tutto memorizzato all'interno del grafo
		}
		return neighbours;
	}

	public String findMaxDegree() {
		int maxDegree = 0 ; 
		
		for(String vertex : grafo.vertexSet()){
			if (grafo.degreeOf(vertex) > maxDegree ){
//				System.out.println(vertex); parola con più vicini. 
				maxDegree = grafo.degreeOf(vertex) ;
			}
		}
			
		
		return "MaxDegree: "+ maxDegree;
	}

	public List<String> displayAllNeighbours(String parolaInserita) {

		BreadthFirstIterator<String, DefaultEdge> bfv = new BreadthFirstIterator<>(grafo, parolaInserita) ;
		List<String> allNeighbours = new ArrayList<String>() ; 

		while(bfv.hasNext()){
			//System.out.println(bfv.next());
			allNeighbours.add(bfv.next());
		}
		
		return allNeighbours;
	}
	
	public List<String> displayAllNeighboursRecursive(String parolaInserita){
		List<String> allNeighbours = new ArrayList<String>() ; 
		
		if(grafo.containsVertex(parolaInserita))
			recursive(allNeighbours,parolaInserita);
		else
			return null ;
		
		return allNeighbours;
	}

	private void recursive(List<String> allNeighbours, String parola) {
		
		for(DefaultEdge d : grafo.edgesOf(parola) ){
			String vicino = Graphs.getOppositeVertex(grafo, d, parola) ;
			if(!allNeighbours.contains(vicino)){
				allNeighbours.add(vicino) ;
				recursive(allNeighbours, vicino) ;
			}
		}		
	}

	public List<String> displayAllNeighboursIterative(String parolaInserita){
		List<String> visitati = new ArrayList<String>() ; 
		List<String> daVisitare = new ArrayList<String>() ; 
		String attuale = null , vicino = null; 
		daVisitare.add(parolaInserita) ;
		
		while(!daVisitare.isEmpty()){
			
			attuale = daVisitare.get(0) ; // prendo il primo della lista.
			visitati.add(attuale) ; // lo metto in quelli visitati
			daVisitare.remove(attuale) ; // tolgo da quelli da visitare quello appena visitato
			
			for(DefaultEdge d : grafo.edgesOf(attuale) ){
				vicino = Graphs.getOppositeVertex(grafo, d, attuale) ;
				if(!visitati.contains(vicino)){ // se i visitati non contengono questo vicino
					daVisitare.add(vicino) ;	// lo metto tra quelli da visitare. 
				}
			}
			
			System.out.println("visitati: "+ visitati);
			System.out.println("davisitare: "+ daVisitare) ;
			
		}
		
		return visitati ; 
	}
}
