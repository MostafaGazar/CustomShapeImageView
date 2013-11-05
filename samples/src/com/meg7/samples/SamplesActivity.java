package com.meg7.samples;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.meg7.widget.CustomShapeImageView;
import com.meg7.widget.CustomShapeSquareImageView;

import java.util.ArrayList;
import java.util.List;

public class SamplesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samples);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new SvgImagesAdapter(this));
    }

    private class SvgImagesAdapter extends BaseAdapter {
        private List<Integer> mSvgRawResourceIds = new ArrayList<Integer>();

        private Context mContext;

        public SvgImagesAdapter(Context context) {
            mContext = context;

            mSvgRawResourceIds.add(R.raw.shape_star);
            mSvgRawResourceIds.add(R.raw.shape_heart);
            mSvgRawResourceIds.add(R.raw.shape_flower);
            mSvgRawResourceIds.add(R.raw.shape_star_2);
            mSvgRawResourceIds.add(R.raw.shape_star_3);
            mSvgRawResourceIds.add(R.raw.shape_circle_2);
            mSvgRawResourceIds.add(R.raw.shape_5);
        }

        @Override
        public int getCount() {
            return mSvgRawResourceIds.size();
        }

        @Override
        public Integer getItem(int i) {
            return mSvgRawResourceIds.get(i);
        }

        @Override
        public long getItemId(int i) {
            return mSvgRawResourceIds.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return new CustomShapeSquareImageView(mContext, R.drawable.sample_1,CustomShapeImageView.Shape.SVG, getItem(i));// It is just a sample ;)
        }

    }

}
