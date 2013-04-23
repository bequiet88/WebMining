package contentAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class PreprocessedReviews {

	/**
	 * List of POS-tagged sentences
	 */
	public Vector<TaggedSentence> taggedSentences;
	/**
	 * contains all nouns and their frequencies within the whole corpus
	 * key: noun
	 * value: frequency
	 */
	HashMap<String, Integer> nounFrequencies;


	public PreprocessedReviews(String fileName) throws IOException{

		taggedSentences = new Vector<TaggedSentence>();
		nounFrequencies = new HashMap<String, Integer>();
		this.preprocessReviews(fileName);
	}
	
	/**
	 * returns the frequency of the given noun within the whole corpus
	 * @param noun
	 * @return frequency
	 */
	public int getFrequencyForNoun(String noun){
		if (this.nounFrequencies.get(noun) != null){
			return this.nounFrequencies.get(noun);
		}
		else {
			return 0;
		}	
	}
	
	

	/**
	 * returns true if posTag refers to any type of noun, false otherwise
	 * @param posTag
	 * @return
	 */
	private boolean isNoun(String posTag){
		if( posTag.equalsIgnoreCase("NN") || posTag.equalsIgnoreCase("NNP") || posTag.equalsIgnoreCase("NNPS") || posTag.equalsIgnoreCase("NNS")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * applies Stanford Core NLP tools to content of a file
	 * writes processed output to new files
	 * @param fileName
	 * @throws IOException
	 */
	private void preprocessReviews(String fileName) throws IOException{

		
		/**
		 * STANFORD CORE NLP pipeline
		 */
	    StanfordCoreNLP pipeline;

		
		
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    pipeline = new StanfordCoreNLP(props);

	    
	    
	    // read data
	    BufferedReader reader = new BufferedReader(new FileReader (new File(fileName)));
	    String text = "";
	    String line = reader.readLine();
	    while (line != null){
	    	text = text.concat(line + "\n");
	    	line = reader.readLine();
	    }

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    

	    /*
	     * Iterate over all sentences
	     */
	   
	    for(CoreMap sentence: sentences) {
	    	
	    
	    	// create arrays for sentence
	    	String[] words = new String[sentence.get(TokensAnnotation.class).size()];
	    	String[] posTags = new String[sentence.get(TokensAnnotation.class).size()];
	    	int index = 0;


	    	/*
	    	 * Iterate over all tokens in sentence, 
	    	 * put information into two arrays (one for words, one for posTags)
	    	 */
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {

	    		// this is the text of the token
	    		String word = token.get(TextAnnotation.class); 
	    		// this is the POS tag of the token 
	    		String posTag = token.get(PartOfSpeechAnnotation.class); 
	    		
	    		
	    		// track occurrence frequencies of all nouns
	    		if (isNoun(posTag)){
	    			if (this.nounFrequencies.containsKey(word)){
	    				this.nounFrequencies.put(word, this.nounFrequencies.get(word) + 1);
	    			}
	    			else {
	    				this.nounFrequencies.put(word, 1);
	    			}
	    		}
	    		
	    		
	    		// put words and posTags in arrays
	    		words[index] = word;
	    		posTags[index] = posTag;
	    		index ++;
	    	}	      
	    	
	    	//create a TaggedSentence object and save it in list 
	    	taggedSentences.add(new TaggedSentence(words, posTags));
	    	//extractInformation(words, posTags);
	    }

	}
}
