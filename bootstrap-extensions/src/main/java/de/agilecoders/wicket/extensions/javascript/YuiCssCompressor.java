package de.agilecoders.wicket.extensions.javascript;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.css.ICssCompressor;

import com.yahoo.platform.yui.compressor.CssCompressor;
import org.apache.wicket.util.lang.Args;

public class YuiCssCompressor implements ICssCompressor {

    private int lineBreakPosition = 4000;

    /**
     * Construct.
     */
    public YuiCssCompressor() {
    }

    @Override
    public String compress(String original) {
        try {
            CssCompressor compressor = new CssCompressor(new StringReader(original));
            StringWriter out = new StringWriter(original.length() / 2);
            compressor.compress(out, getLineBreakPosition());
            return out.toString();
        } catch (IOException iox) {
            throw new WicketRuntimeException("A problem occurred while compressing CSS resource: " + iox.getMessage(), iox);
        }
    }

    /**
     * @return the line number after which to add a new line character
     */
    public int getLineBreakPosition() {
        return lineBreakPosition;
    }

    /**
     * @param lineBreakPosition the line number after which to add a new line character
     */
    public void setLineBreakPosition(int lineBreakPosition) {
        Args.isTrue(lineBreakPosition > 0, "Line break position must be a positive number");
        this.lineBreakPosition = lineBreakPosition;
    }
}
