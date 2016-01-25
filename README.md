CustomShapeImageView Demo ([Play Store Demo][1])
-------------------------

Custom shape ImageView using PorterDuffXfermode with paint shapes and SVGs

You can also use this gist https://gist.github.com/MostafaGazar/ee345987fa6c8924d61b if you do not want to add this library project to your codebase.

[![Build Status](https://travis-ci.org/MostafaGazar/CustomShapeImageView.svg)](https://travis-ci.org/MostafaGazar/CustomShapeImageView)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CustomShapeImageView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1197)
[![Android Weekly](http://img.shields.io/badge/Android%20Weekly-%2381-2CB3E5.svg?style=flat)](http://androidweekly.net/issues/issue-81)
[![Coverage Status](https://coveralls.io/repos/github/MostafaGazar/CustomShapeImageView/badge.svg?branch=master)](https://coveralls.io/github/MostafaGazar/CustomShapeImageView?branch=master)

Usage
-----
        <com.meg7.widget.CustomShapeImageView
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:src="@drawable/sample"
            app:shape="circle"
            android:scaleType="centerCrop" />

        <com.meg7.widget.CircleImageView
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:src="@drawable/sample"
            android:scaleType="centerCrop" />

        <com.meg7.widget.RectangleImageView
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:src="@drawable/sample"
            android:scaleType="centerCrop" />

        <com.meg7.widget.SvgImageView
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:src="@drawable/sample"
            app:svg_raw_resource="@raw/shape_star"
            android:scaleType="centerCrop" />
Screenshots
------------
![main](https://raw.githubusercontent.com/MostafaGazar/CustomShapeImageView/master/Screenshot_2016-01-19-09-17-37.png)
Libraries used
---------------
* https://github.com/latemic/svg-android

Developed by
------------
* Mostafa Gazar - <eng.mostafa.gazar@gmail.com>

Donations
------------
If you'd like to support this library, you could make a donation here:

![PayPal Donation](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)

[1]: https://play.google.com/store/apps/details?id=com.meg7.samples
