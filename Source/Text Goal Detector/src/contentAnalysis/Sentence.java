package contentAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Sentence {
	private String sentence = null;
	public String getSentence() 
	{
		return sentence;
	}

	private String[] words;
	public String[] getWords() 
	{
		return words;
	}

	private String[] posTags;
	public String[] getPosTags() 
	{
		return posTags;
	}
	
	private String[] namedEntity;	
	public String[] getNamedEntity() {
		return namedEntity;
	}

	private boolean containsGoal;
	public boolean isContainsGoal() {
		return containsGoal;
	}

	public void setContainsGoal(boolean containsGoal) {
		this.containsGoal = containsGoal;
	}
	
	private double goalProbability;
	public double getGoalProbability() {
		return goalProbability;
	}

	public void setGoalProbability(double goalProbability) {
		if(goalProbability > 1.0)
			this.goalProbability = 1.0;
		else if(goalProbability < 0.0)
			this.goalProbability = 0.0;
		else
			this.goalProbability = goalProbability;
	}

	public Sentence(String sentence) throws Exception 
	{
		super();
		this.sentence = sentence;
		this.processWords();
		this.containsGoal = false;
		this.goalProbability = 0.0;
	}
	
	private void processWords() throws IOException
	{
	    StanfordCoreNLP pipeline;
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
	    pipeline = new StanfordCoreNLP(props);

	    // read data
	    String text = this.sentence;
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    // these are all the sentences in this document
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	   
	    for(CoreMap sentence: sentences) 
	    {
	    	this.words = new String[sentence.get(TokensAnnotation.class).size()];
	    	this.posTags = new String[sentence.get(TokensAnnotation.class).size()];
	    	this.namedEntity = new String[sentence.get(TokensAnnotation.class).size()];

	    	int index = 0;
	
	    	/*
	    	 * Iterate over all tokens in sentence, 
	    	 * put information into two arrays (one for words, one for posTags)
	    	 */
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) 
	    	{
	    		String word = token.get(TextAnnotation.class);  
	    		String posTag = token.get(PartOfSpeechAnnotation.class); 
	    		
	    		String ne = token.get(NamedEntityTagAnnotation.class);
	    		this.namedEntity[index] = ne;
	    		
	    		this.words[index] = word;
	    		this.posTags[index] = posTag;
	    		index ++;
	    	}	      	
	    }    
	}
	
	public void writeToFile(String fileName, boolean append) throws IOException
	{
	    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName), append));       
	    for(String s : this.words)
	    {
	    	writer.write(s + " ");
	    }
	    writer.newLine();
	    
	    for(String s : this.posTags)
	    {
	    	writer.write(s + " ");
	    }
	    writer.newLine();
	    
	    for(String s : this.namedEntity)
	    {
	    	writer.write(s + " ");
	    }
//	    writer.newLine();
//	    writer.write("Goal: " + this.containsGoal + ", Goal Probability: " + this.goalProbability);
//	    writer.newLine();
	    
	    writer.newLine();
	    writer.newLine();
	    writer.close();
	}

	public static void writeSentencesToDirectories(ArrayList<Sentence> sentences,String directory) throws IOException
	{
		File dir = new File(directory);
		File posDir = new File(directory + "\\pos");
		File negDir = new File(directory + "\\neg");
		if(dir.exists())
		{
			posDir.mkdir();
			negDir.mkdir();
		}
		else
			throw new IOException("Directory doesn't exist");
		
		for(int i=1; i <= sentences.size(); i++)
		{
			Sentence s = sentences.get(i);
			
			String message = "Does this sentence contain a goal? \\n" + s.sentence + "\\n\\n" + "Remaining sentences: " + (sentences.size() - i);
			if(JOptionPane.showOptionDialog(null, message, "Please decide", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 
					null) == JOptionPane.YES_OPTION)
			{
			    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(posDir + "\\" + i +".txt"), false));       
			    writer.write(s.sentence);
			    writer.close();
			}
			else
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(negDir + "\\" + i +".txt"), false));       
			    writer.write(s.sentence);
			    writer.close();
			}
						
		}
	}

}
