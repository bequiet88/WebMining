package contentAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalysis {
	
	

	/**
	 * STANFORD CORE NLP pipeline
	 */
    StanfordCoreNLP pipeline;

    /**
     * input directory 
     */
    File inputDir;

    
    /**
     * output directory 
     */
    File outputDir;
    
    /**
     * new directories for newly created output
     */
	File posTaggedDir;
	File posTagFilteredDir;
	File lemmatizedDir;

	
	/**
	 * Output file including nouns, adjectives and adverbs only 
	 */
	BufferedWriter filteredPosWriter;
	/**
	 * Output file including stemmed tokens
	 */
	BufferedWriter lemmatizedWriter;
	/**
	 * Output file including tokens with their POS tag
	 */
	BufferedWriter posTagWriter;
	
	
	/**
	 * Environment for NLP operations on text input
	 * @param projectDirName
	 */
	public SentimentAnalysis(){
		
		this.setPropertiesForStanfordCoreNLP();
	}

	
	/**
	 * sets properties for Stanford Core NLP (which tools to use)
	 */
	private void setPropertiesForStanfordCoreNLP(){
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    pipeline = new StanfordCoreNLP(props);
	    
	}
	
	/**
	 *  creates new sub-directories for output 
	 */
	public void setOutputFolder(String dirOut){

		this.outputDir = new File(dirOut);
		
		this.posTaggedDir = new File(this.outputDir, "posTagged");
		this.posTagFilteredDir = new File(this.outputDir, "posTagFiltered");
		this.lemmatizedDir = new File (this.outputDir, "lemmatized");
		
		try {
			posTaggedDir.mkdir();
			posTagFilteredDir.mkdir();
			lemmatizedDir.mkdir();
		} catch (Exception e){
			// if they already exist this will be called
			e.printStackTrace();
		}
	}

	/**
	 * opens all file writers for output files
	 * @param name
	 * @throws IOException
	 */
	private void openWriters(String fileName) throws IOException{
				
		// initialize writers
		this.posTagWriter  = new BufferedWriter( new FileWriter( new File( this.posTaggedDir, fileName)));
		this.filteredPosWriter = new BufferedWriter( new FileWriter( new File( this.posTagFilteredDir, fileName )));
		this.lemmatizedWriter = new BufferedWriter( new FileWriter( new File( this.lemmatizedDir, fileName)));

	}
	
	/**
	 * closes output files
	 * @throws IOException
	 */
	private void closeWriters() throws IOException{
		this.filteredPosWriter.close();
		this.lemmatizedWriter.close();
		this.posTagWriter.close();
	}
	

	/**
	 * returns content of file as String
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private String readTextFromFile(String fileName) throws IOException{

	    String text = "";
		BufferedReader reader = new BufferedReader( new FileReader ( new File(this.inputDir, fileName)));
	    String line = reader.readLine();
	    while (line != null){
	    	text = text.concat(line);
	    	line = reader.readLine();
	    }
	    reader.close();
	    return text;
	}
	
	
	
	/*
	 * HERE IS WHERE THE INTERESTING STUFF IS HAPPNING
	 */
	
	/**
	 * applies Stanford Core NLP tools to content of a file
	 * writes processed output to new files
	 * @param fileName
	 * @throws IOException
	 */
	public void analyze(String fileName) throws IOException{

		this.openWriters(fileName);

	    // read some text in the text variable
	    String text = this.readTextFromFile(fileName);  

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    this.pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);   
	        // this is the lemma of the token
	        String lemma = token.get(LemmaAnnotation.class);
	        
	        //Task1.3
	        System.out.println("Word:" + word + ", Lemma: " + lemma + ", POS-Tag: " + pos );
	        	        
	        /*
	         *  HERE IS SOME SPACE FOR YOUR LINES
	         */
	        // write lemmatized tokens instead of tokens
	        this.lemmatizedWriter.write(lemma + " ");
	        
	        // write tokens with their POS tag 
	        this.posTagWriter.write(word + "_" + pos + " ");
	        
	        // filter tokens: keep only adverbs and adjectives
	        if(pos.equals("JJ") || pos.equals("JJR") || pos.equals("JJS") || 
	        		pos.equals("RB") || pos.equals("RBR") || pos.equals("RBS"))
	        {
	        	this.filteredPosWriter.write(word + " ");
	        }
	      }

	    }
	    
	    //close all output files
	    this.closeWriters();
		
	}
	
	/**
	 * iterates over all files in the input dir and analyzes them
	 * @throws IOException
	 */
	public void analyzeAllFiles(String inputDirName) throws IOException{

		this.inputDir = new File (inputDirName);
		String[] fileNames = inputDir.list();
		for (String name: fileNames){
			System.out.println("Processing file: "  + name);
			this.analyze(name);			
		}
	}
	
	
	
	
	public static void main(String[] args) throws IOException{
		/*
		 * (input and) output directory have to exist before running!
		 * ADJUST THE PATHS IF YOU ARE USING MAC OR WINDOWS
		 */
		
		SentimentAnalysis sAnalysis = new SentimentAnalysis();	
		
		//positive reviews
		sAnalysis.setOutputFolder("data/pos/output");
		sAnalysis.analyzeAllFiles("data/pos/input");
		
		//negative reviews
		sAnalysis.setOutputFolder("data/neg/output");
		sAnalysis.analyzeAllFiles("data/neg/input");
	}

}
