package contentAnalysis;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import model.Sentence;


// TODO: Auto-generated Javadoc
/**
 * The Class Corpus.
 */
public class Corpus 
{
	
	/** The elements. */
	ArrayList<Character> elements;
	
	/** The sentences. */
	ArrayList<Sentence> sentences;
	
	/**
	 * Instantiates a new corpus.
	 *
	 * @param file the file
	 * @throws Exception the exception
	 */
	public Corpus(String file) throws Exception
	{

		this.elements = new ArrayList<Character>();
		this.processFile(file);
	}
	
	/**
	 * Process file.
	 *
	 * @param file the file
	 * @throws Exception the exception
	 */
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
	
	/**
	 * Gets the string.
	 *
	 * @param start the start
	 * @param end the end
	 * @return the string
	 * @throws Exception the exception
	 */
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
