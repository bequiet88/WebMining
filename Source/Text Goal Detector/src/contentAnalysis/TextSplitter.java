package contentAnalysis;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.*;

public class TextSplitter 
{
	private String inputFile;
	private String outputFile;
	
	public TextSplitter(String input, String output)
	{
		this.inputFile = input;		
		this.outputFile = output;
	}
	
	public void processTextFile() throws IOException
	{
	    BufferedReader reader = new BufferedReader(new FileReader (new File(this.inputFile)));
	    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.outputFile)));
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
	    	    text = text.concat("\n\n" + line.substring(20) + "\n");
	    	    line = reader.readLine();
	    	}
	    	else
	    	{
	    		text = text.concat(line + "\n");
		    	line = reader.readLine();
	    	}    	
	    }
		
	    reader.close();
	    
	    writer.write(text);
	    writer.close();
	}
	
	public ArrayList<Sentence> splitIntoSentences() throws IOException, Exception
	{
	    List<String> lines = java.nio.file.Files.readAllLines(Paths.get(this.inputFile), Charset.defaultCharset());
	    
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
	    	sentences.add(new Sentence(text.substring(start,end)));
	    }
	    return sentences;
	}
	
}