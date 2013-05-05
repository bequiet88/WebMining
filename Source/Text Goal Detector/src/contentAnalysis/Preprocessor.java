package contentAnalysis;

import java.util.ArrayList;

public class Preprocessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try 
		{
			TextSplitter.processTextFile("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_3.txt","/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_7.txt");
			ArrayList<Sentence> sentences = TextSplitter.splitIntoSentences("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_7.txt");
			
			for(Sentence s : sentences)
			{
				s.writeToFile("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_9.txt", true);				
			}
			
			Sentence.writeSentencesToDirectories(sentences, null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}

}
