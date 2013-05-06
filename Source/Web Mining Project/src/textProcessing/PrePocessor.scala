package textProcessing
import scala.io.Source
import java.io._
import java.text.BreakIterator
import java.util.Locale

class PrePocessor {
	def processTextFile(inputFileName: String, outputFileName: String) 
	{
	  var text : String = ""

	  for (l <- Source.fromFile(inputFileName)(io.Codec.UTF8).getLines )
	  {
		  if (l matches "^(0[1-9]|[12][0-9]|3[01])[.](0[1-9]|1[012])[.](19|20)[0-9]{2}$")
		  {		
		    text concat ("\n\n" + l.substring(20) + "\n")
		  }
		  else if(l matches "Results of ResultWriter 'Write as Text'")
		  {
			  //do nothing
		  }
		  else
		  {
		    text concat (l + "\n")
		  }
	  }
	  
	  val writer : BufferedWriter = new BufferedWriter(new FileWriter(new File(outputFileName)))
	  writer.write(text)
	  writer.close()
	}
	
	def splitIntoSentences(inputFileName: String) 
	{
	  var text : String = ""
	  
	  for(l <- Source.fromFile(inputFileName)(io.Codec.UTF8).getLines)
	   	(text concat (l + " "))
	   	
	  val iterator : BreakIterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
	  iterator.setText(text);

	  var start : Int = iterator.first()
	  var end : Int = iterator.next()
	  
	  //var sentences : List[Sentence] = List()
	  
	  while(end != BreakIterator.DONE)
	  {
	    var sentence: String = text.substring(start, end)
	    //Process sentence somehow
	    
	    start = end
	    end = iterator.next()
	  }
	}
	
}