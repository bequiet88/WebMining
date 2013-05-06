package contentAnalysis;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.*;

import model.Sentence;


// TODO: Auto-generated Javadoc
/**
 * The Class TextSplitter.
 */
public class TextSplitter 
{
	
	/**
	 * Process text file.
	 *
	 * @param inputFile the input file
	 * @param outputFile the output file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void processTextFile(String inputFile, String outputFile) throws IOException
	{
	    BufferedReader reader = new BufferedReader(new FileReader (new File(inputFile)));
	    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFile)));
		Pattern p = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)[0-9]{2}$");

	    String text = "";
	    String line = reader.readLine();
	    
	    while (line != null){
    		Matcher m = p.matcher(line.substring(0, 10));
	    	
	    	if(line.contains("Results of ResultWriter 'Write as Text'"))
	    	{
		    	line = reader.readLine();	
	    	}
	    	else if(m.matches())
	    	{
	    	    text = text.concat(line.substring(20) + "");
	    	    line = reader.readLine();
	    	}
	    	else
	    	{
	    		text = text.concat(line + "");
		    	line = reader.readLine();
	    	}    	
	    }
		
	    reader.close();
	    
	    writer.write(text);
	    writer.close();
	}
	
	/**
	 * Split into sentences.
	 *
	 * @param inputFile the input file
	 * @return the array list
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException the file not found exception
	 * @throws Exception the exception
	 */
	public static ArrayList<Sentence> splitIntoSentences(String inputFile) throws IOException, FileNotFoundException, Exception
	{
	    //List<String> lines = java.nio.file.Files.readAllLines(Paths.get(inputFile), Charset.defaultCharset());
	    List<String> lines = java.nio.file.Files.readAllLines(Paths.get(inputFile), Charset.forName("UTF-8"));
	    
	    String text = null;
	    for(String l : lines)
	    {
	    	text += l + " ";
	    }
	    
	    BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
	    iterator.setText(text);
	    int start = iterator.first();
	    
	    ArrayList<Sentence> sentences = new ArrayList<Sentence>();
	    
	    for (int end = iterator.next();
	        end != BreakIterator.DONE;
	        start = end, end = iterator.next()) 
	    {
	    	sentences.add(new Sentence(text.substring(start,end), true));
	    }
	    return sentences;
	}
	
}
