package contentAnalysis;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.Sentence;


public class Corpus 
{
	ArrayList<Character> elements;
	ArrayList<Sentence> sentences;
	
	public Corpus(String file) throws Exception
	{

		this.elements = new ArrayList<Character>();
		this.processFile(file);
	}
	
	private void processFile(String file) throws Exception
	{
		//alle characters appenden
	    List<String> lines = Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
	    for(String s : lines)
	    {
	    	for(int i=0; i < s.length(); i++)
	    	{
	    		elements.add(s.charAt(i));
	    	}
	    }
	    
	    System.out.println(elements.size());
//	    String text = null;
//	    for(String l : lines)
//	    {
//	    	text += l + " ";
//	    }
//	    
//	    BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
//	    iterator.setText(text);
//	    int start = iterator.first();
//	    
//	    sentences = new ArrayList<Sentence>();  
//	    for (int end = iterator.next();
//	        end != BreakIterator.DONE;
//	        start = end, end = iterator.next()) 
//	    {
//	    	sentences.add(new Sentence(text.substring(start,end), false));
//	    }
	}
	
	public Sentence getString(long start, long end) throws Exception
	{
		//in der arraylist nach zeichen zwischen start und end suchen und als Sentence zurueckgeben
		//Sentences machen
		String s = "";
		for(long i = start+1; i<=end && i<elements.size();i++)
		{
			s=s.concat((elements.get((int)i)).toString());
		}
		Sentence sentence = new Sentence(s, false);
		
		//find sentence in list and remove it!
//		if(sentences.contains(sentence))
//			sentences.remove(sentence);
		
		return sentence;
	}
	
}
