package com.svgandroid;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad Medvedev on 26.01.2016.
 * vladislav.medvedev@devfactory.com
 */
public class AttributesMock implements Attributes {
    private List<Pair> params = new ArrayList<>();

    public AttributesMock(Pair... args) {
        for (Pair arg : args) {
            params.add(arg);
        }
    }

    @Override
    public int getLength() {
        return params.size();
    }

    @Override
    public String getURI(int index) {
        return "";
    }

    @Override
    public String getLocalName(int index) {
        return params.get(index).name;
    }

    @Override
    public String getQName(int index) {
        return getLocalName(index);
    }

    @Override
    public String getType(int index) {
        return "";
    }

    @Override
    public String getValue(int index) {
        return params.get(index).val;
    }

    @Override
    public int getIndex(String uri, String localName) {
        return 0;
    }

    @Override
    public int getIndex(String qName) {
        return 0;
    }

    @Override
    public String getType(String uri, String localName) {
        return "";
    }

    @Override
    public String getType(String qName) {
        return "";
    }

    @Override
    public String getValue(String uri, String localName) {
        return "";
    }

    @Override
    public String getValue(String qName) {
        return "";
    }

    public static class Pair {
        private String name;
        private String val;

        public Pair(String name, String val) {
            this.name = name;
            this.val = val;
        }
    }
}
