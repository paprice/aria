/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Language;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.coref.mention.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 *
 * @author despa
 */
public class LanguageProcessing {

    public static String Parse(String sentence) throws IOException {

        
        POSModel model = new POSModelLoader()	
		.load(new File("nerc-fr/models/pos/fr-pos-ftb-morpho.bin"));
	PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
	POSTaggerME tagger = new POSTaggerME(model);
 
	ObjectStream<String> lineStream = new PlainTextByLineStream(
			new StringReader(sentence));
 
	perfMon.start();
	String line;
        String ret = new String();
	while ((line = lineStream.read()) != null) {
 
		String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
				.tokenize(line);
		String[] tags = tagger.tag(whitespaceTokenizerLine);
 
		POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
		//System.out.println(sample.toString());
                ret = sample.toString();
		perfMon.incrementCounter();
	}
	perfMon.stopAndPrintFinalResult();
        

        return ret;
    }

}
