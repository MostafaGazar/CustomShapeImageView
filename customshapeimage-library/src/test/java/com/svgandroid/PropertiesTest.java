package com.svgandroid;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Vlad Medvedev on 26.01.2016.
 * vladislav.medvedev@devfactory.com
 */
public class PropertiesTest {

    @Test
    public void getFloat_defValue() {
        SVGParser.Properties properties = new SVGParser.Properties(
                new AttributesMock(new AttributesMock.Pair("someProperty2", "2.0")));

        assertThat(properties.getFloat("someProperty", 1.0f), is(1.0f));
        assertThat(properties.getFloat("someProperty2", 1.0f), is(2.0f));
    }

    @Test
    public void getFloat() {
        SVGParser.Properties properties = new SVGParser.Properties(
                new AttributesMock(new AttributesMock.Pair("someProperty", "1.0"))
        );

        assertThat(properties.getFloat("someProperty"), is(1.0f));
    }

    @Test
    public void getFloat_wrongFloatFormat() {
        SVGParser.Properties properties = new SVGParser.Properties(
                new AttributesMock(new AttributesMock.Pair("someProperty", "foo"))
        );

        assertThat(properties.getFloat("someProperty"), is(nullValue()));
    }
}