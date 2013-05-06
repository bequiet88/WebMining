package contentAnalysis;

import java.math.BigInteger;
import java.util.List;

import model.Tuple;


public class Preprocessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try 
		{
//			TextSplitter.processTextFile("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_3.txt","/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_7.txt");
//			ArrayList<Sentence> sentences = TextSplitter.splitIntoSentences("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_7.txt");
			
//			for(Sentence s : sentences)
//			{
//				s.writeToFile("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_9.txt", true);				
//			}
			
//			Sentence.writeSentencesToDirectories(sentences, "/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3");
			XmlHandler myHandler = new XmlHandler("D:/Studium/Classes_Sem2/Web Mining/Project/WebMining/Gate/Results/bbc_sample_result.xml");
			List<Tuple<BigInteger,BigInteger>> posSentenceTuples = myHandler.getPosSentences();
			List<Tuple<BigInteger,BigInteger>> negSentenceTuples = myHandler.getNegSentences(posSentenceTuples.size());
			
			System.out.println("done");
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}

}
