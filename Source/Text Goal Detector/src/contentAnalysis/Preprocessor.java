package contentAnalysis;

import java.util.ArrayList;

public class Preprocessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try 
		{
			TextSplitter split = new TextSplitter("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_3.txt","/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_7.txt");
			split.processTextFile();
			
			split = new TextSplitter("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_7.txt", "/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_8.txt");
			ArrayList<Sentence> sentences = split.splitIntoSentences();
			
			for(Sentence s : sentences)
			{
				s.writeToFile("/Users/matthiasr/Documents/Web Mining Project/WebMining/RapidMiner/crawls/bbc_041513_3/results_9.txt", true);				
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
	}

}
