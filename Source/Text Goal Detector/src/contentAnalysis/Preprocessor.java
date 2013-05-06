package contentAnalysis;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.ListIterator;

import model.Sentence;
import model.Tuple;


public class Preprocessor {

	public static final Boolean pos = true;
	public static final Boolean neg = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try 
		{
			//TextSplitter.processTextFile("D:/Studium/Classes_Sem2/Web Mining/Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_3.txt","D:/Studium/Classes_Sem2/Web Mining/Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_10.txt");
//			List<Sentence> sentences = TextSplitter.splitIntoSentences("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_7.txt");
			
//			for(Sentence s : sentences)
//			{
//				s.writeToFile("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_9.txt", true);				
//			}
			
//			Sentence.writeSentencesToDirectories(sentences, "/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3");
			
			processResultingXML("D:/Studium/Classes_Sem2/Web Mining/Project/Gate/results_10.txt.xml", "D:/Studium/Classes_Sem2/Web Mining/Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_10.txt");
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	private static void processResultingXML(String xml, String txt) {
		try {
			XmlHandler myHandler = new XmlHandler(xml);
			List<Tuple<BigInteger,BigInteger>> posSentenceTuples = myHandler.getPosSentences();
			System.out.println("# of Positive Sentences:\n"+posSentenceTuples.size());
			List<Tuple<BigInteger,BigInteger>> negSentenceTuples = myHandler.getNegSentences(posSentenceTuples.size());
			System.out.println("# of Negative Sentences:\n"+negSentenceTuples.size());
			
			System.out.println("Size of Corpus:");
			Corpus corp = new Corpus(txt);
			
			ListIterator<Tuple<BigInteger,BigInteger>> posIter = posSentenceTuples.listIterator();
			ListIterator<Tuple<BigInteger,BigInteger>> negIter = negSentenceTuples.listIterator();
			
			Tuple<BigInteger,BigInteger> currTuple = null;
			Sentence currSen = null;
			
			
			while(posIter.hasNext()) {
				currTuple = posIter.next();
				currSen = corp.getString(currTuple.x.longValue(), currTuple.y.longValue());
				currSen.writeSentenceToDirectory("D:/Studium/Classes_Sem2/Web Mining/Project/Gate", Preprocessor.pos);				
			}
			
			while(negIter.hasNext()) {
				currTuple = negIter.next();
				currSen = corp.getString(currTuple.x.longValue(), currTuple.y.longValue());
				currSen.writeSentenceToDirectory("D:/Studium/Classes_Sem2/Web Mining/Project/Gate", Preprocessor.neg);				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
