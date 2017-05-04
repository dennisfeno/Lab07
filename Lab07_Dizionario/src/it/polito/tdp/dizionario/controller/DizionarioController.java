package it.polito.tdp.dizionario.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.dizionario.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DizionarioController {

	private Model model = null ;
	
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private TextArea txtResult;
	@FXML
	private TextField inputNumeroLettere;
	@FXML
	private TextField inputParola;
	@FXML
	private Button btnGeneraGrafo;
	@FXML
	private Button btnTrovaVicini;
	@FXML
	private Button btnTrovaGradoMax;
    @FXML
    private Button btnTuttiVicini;
    
	@FXML
	void doReset(ActionEvent event) {
		txtResult.setText("Reset!");
		inputNumeroLettere.setEditable(true);
	}

	@FXML
	void doGeneraGrafo(ActionEvent event) {

		try {
			int numeroLettere = 0 ; 
			
			numeroLettere = Integer.parseInt(inputNumeroLettere.getText() );
			
			List<String> allWord = model.createGraph(numeroLettere) ;
			
			txtResult.appendText(String.format("Grafo creato con %d vertici.\n",allWord.size()));

			inputNumeroLettere.setEditable(false); // così evito errori
			
		} catch (RuntimeException re) {
			txtResult.appendText(re.getMessage()+"\n");
		}
	}

	@FXML
	void doTrovaGradoMax(ActionEvent event) {
		
		try {
			
			txtResult.appendText(model.findMaxDegree()+"\n");
			
		} catch (RuntimeException re) {
			txtResult.appendText(re.getMessage()+"\n");
		}
	}

	@FXML
	void doTrovaVicini(ActionEvent event) {
		
		try {
			
			String parolaInserita = inputParola.getText();
			
			if(parolaInserita.length()!=Integer.parseInt(inputNumeroLettere.getText()))
				throw new RuntimeException("La parola deve essere dello stesso numero di caratteri iniziali.\n");
			
			txtResult.appendText("Vicini di "+parolaInserita+": ");
			
			for(String s : model.displayNeighbours(parolaInserita) )
				txtResult.appendText(s+" ");
			
			txtResult.appendText(".\n");
			
		} catch (RuntimeException re) {
			txtResult.appendText(re.getMessage()+"\n");
		}
	}
	
    @FXML
    void doTrovaTuttiVicini(ActionEvent event) {		try {
		
		String parolaInserita = inputParola.getText();
		
		if(parolaInserita.length()!=Integer.parseInt(inputNumeroLettere.getText()))
			throw new RuntimeException("La parola deve essere dello stesso numero di caratteri iniziali.\n");
		
		txtResult.appendText("Tutti i vicini di "+parolaInserita+": ");
		for(String s : model.displayAllNeighbours(parolaInserita) ) // con funzioni della libreria 
		// for(String s : model.displayAllNeighboursRecursive(parolaInserita) ) 
		// for(String s : model.displayAllNeighboursIterative(parolaInserita) ) molto poco efficiente
			txtResult.appendText(s+" ");
		
		txtResult.appendText(".\n");
		
	} catch (RuntimeException re) {
		txtResult.appendText(re.getMessage()+"\n");
	}
    }
    
	@FXML
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert inputNumeroLettere != null : "fx:id=\"inputNumeroLettere\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert inputParola != null : "fx:id=\"inputParola\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnGeneraGrafo != null : "fx:id=\"btnGeneraGrafo\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnTrovaGradoMax != null : "fx:id=\"btnTrovaTutti\" was not injected: check your FXML file 'Dizionario.fxml'.";
        assert btnTuttiVicini != null : "fx:id=\"btnTuttiVicini\" was not injected: check your FXML file 'Dizionario.fxml'.";
	}

	public void setModel(Model model) {
		this.model = model;
	}
}