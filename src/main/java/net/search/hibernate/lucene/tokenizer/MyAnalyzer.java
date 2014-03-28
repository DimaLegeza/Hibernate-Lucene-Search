package net.search.hibernate.lucene.tokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * @author: Dima Legeza
 * @since: 27.03.14
 */
public final class MyAnalyzer extends Analyzer {
    private static final Analyzer STANDARD = new StandardAnalyzer(Version.LUCENE_36);

    @Override
    public TokenStream tokenStream(String field, final Reader reader)
    {
        if ("aaa".equals(field))
        {
            return new CharTokenizer(Version.LUCENE_36, reader)
            {
                protected boolean isTokenChar(int c)
                {
                    return true;
                }
            };
        }
        else
        {
            // use standard analyzer
            return STANDARD.tokenStream(field, reader);
        }
    }

}