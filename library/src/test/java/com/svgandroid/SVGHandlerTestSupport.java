package com.svgandroid;

import org.junit.Before;
import org.xml.sax.SAXException;

/**
 * Created by Vlad Medvedev on 28.01.2016.
 * vladislav.medvedev@devfactory.com
 */
public class SVGHandlerTestSupport extends SVGTestSupport {
    protected SVGParser.SVGHandler parserHandler;

    protected void startSVG(SVGParser.SVGHandler svgHandler) throws SAXException {
        svgHandler.startElement("", "svg", "svg", new AttributesMock(attr("width", "200"), attr("height", "400"))
        );
    }

    protected void endSVG(SVGParser.SVGHandler svgHandler) throws SAXException {
        svgHandler.endElement("", "svg", "svg");
    }

    protected SVGParser.SVGHandler startElement(SVGParser.SVGHandler svgHandler, AttributesMock attr, String element) throws SAXException {
        svgHandler.startElement("", element, element, attr);
        return svgHandler;
    }

    protected SVGParser.SVGHandler endElement(SVGParser.SVGHandler svgHandler, String element) throws SAXException {
        svgHandler.endElement("", element, element);
        return svgHandler;
    }

    protected AttributesMock.Pair attr(String name, String value) {
        return new AttributesMock.Pair(name, value);
    }

    protected AttributesMock attributes(AttributesMock.Pair... params) {
        return new AttributesMock(params);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        parserHandler = new SVGParser.SVGHandler(picture, 10, 20);
    }
}
