package contentAnalysis;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.Annotation;
import model.GateDocument;
import model.Tuple;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlHandler.
 */
public class XmlHandler {

	/** The xml file. */
	private File xmlFile = null;
	
	/** The root. */
	private GateDocument root = null;
	
	/** The iter. */
	private ListIterator<Annotation> iter = null;
	
	/** The sentences. */
	private List<Tuple<Annotation,Boolean>> sentences = new ArrayList<Tuple<Annotation,Boolean>>();
	
	/** The goals. */
	private List<Tuple<BigInteger, BigInteger>> goals = new ArrayList<Tuple<BigInteger, BigInteger>>();

	/**
	 * Instantiates a new xml handler.
	 *
	 * @param inputPath the input path
	 */
	public XmlHandler(String inputPath) {

		xmlFile = new File(inputPath);
		initializeJAXB();
		handleAnnotations();
	}

	/**
	 * Initialize jaxb.
	 */
	private void initializeJAXB() {

		try {

			// Initialize JAXB
			JAXBContext jaxbContext = JAXBContext
					.newInstance(GateDocument.class.getPackage().getName());

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// Retrieve Root Element
			root = (GateDocument) jaxbUnmarshaller.unmarshal(xmlFile);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Handle annotations.
	 */
	private void handleAnnotations() {

		iter = root.getAnnotationSet().get(0).getAnnotation().listIterator();

		Annotation currAnno = null;

		while (iter.hasNext()) {

			currAnno = iter.next();

			if (currAnno.getType().equals("Sentence")) {

				sentences.add(new Tuple<Annotation,Boolean>(currAnno, Preprocessor.neg));
			} else if (currAnno.getType().equals("Goal")) {
				Tuple<BigInteger, BigInteger> goalTuple = new Tuple<BigInteger, BigInteger>(
						currAnno.getStartNode(), currAnno.getEndNode());
				goals.add(goalTuple);

			} else {
				continue;
			}
		}

	}

	/**
	 * Find sentence.
	 *
	 * @param goal the goal
	 * @return the tuple
	 */
	private Tuple<Annotation,Boolean> findSentence(Tuple<BigInteger, BigInteger> goal) {

		ListIterator<Tuple<Annotation,Boolean>> senIter = sentences.listIterator();
		Tuple<BigInteger, BigInteger> currTuple = null;
		Tuple<Annotation,Boolean> currSen = null;

		while (senIter.hasNext()) {
			currSen = senIter.next();
			currTuple = new Tuple<BigInteger, BigInteger>(
					currSen.x.getStartNode(), currSen.x.getEndNode());
			if (currTuple.x.longValue() <= goal.x.longValue()
					&& currTuple.y.longValue() >= goal.y.longValue()) {
				senIter.remove();
				Tuple<Annotation,Boolean> sentencePos = new Tuple<Annotation,Boolean>(currSen.x, Preprocessor.pos);
				sentences.add(sentencePos);
				return currSen;
			}
		}
		return null;
	}

	/**
	 * Gets the pos sentences.
	 *
	 * @return the pos sentences
	 */
	public List<Tuple<BigInteger, BigInteger>> getPosSentences() {

		List<Tuple<BigInteger, BigInteger>> result = new ArrayList<Tuple<BigInteger, BigInteger>>();
		ListIterator<Tuple<BigInteger, BigInteger>> goalIter = goals
				.listIterator();
		Tuple<BigInteger, BigInteger> currTuple = null;
		while (goalIter.hasNext()) {
			
			currTuple = goalIter.next();
			Tuple<Annotation,Boolean> sentence = findSentence(currTuple);

			if (sentence != null) {
				Tuple<BigInteger, BigInteger> sentenceTuple = new Tuple<BigInteger, BigInteger>(
						sentence.x.getStartNode(), sentence.x.getEndNode());
				result.add(sentenceTuple);
			}
		}
		return result;
	}

	/**
	 * Gets the neg sentences.
	 *
	 * @param lengthPos the length pos
	 * @return the neg sentences
	 */
	public List<Tuple<BigInteger, BigInteger>> getNegSentences(int lengthPos) {

		List<Tuple<BigInteger, BigInteger>> result = new ArrayList<Tuple<BigInteger, BigInteger>>();
		ListIterator<Tuple<Annotation,Boolean>> senIter = sentences.listIterator();		
		Tuple<Annotation,Boolean> currSen = null;
		
		for (int i = 0; i < lengthPos && senIter.hasNext(); i++) {

			currSen = senIter.next();

				if (!currSen.y) {
					Tuple<BigInteger, BigInteger> sentenceTuple = new Tuple<BigInteger, BigInteger>(
							currSen.x.getStartNode(), currSen.x.getEndNode());
					result.add(sentenceTuple);
				}
		}
		return result;
	}
}
