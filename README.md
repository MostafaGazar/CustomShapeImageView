CustomShapeImageView Demo ([Play Store Demo][1])
-------------------------

Custom shape ImageView using PorterDuffXfermode with paint shapes and SVGs

You can also use this gist https://gist.github.com/MostafaGazar/ee345987fa6c8924d61b if you do not want to add this library project to your codebase.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CustomShapeImageView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1197)
[![Android Weekly](http://img.shields.io/badge/Android%20Weekly-%2381-2CB3E5.svg?style=flat)](http://androidweekly.net/issues/issue-81)

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
![main](https://raw.github.com/MostafaGazar/CustomShapeImageView/master/Screenshot_2013-11-05-23-08-12.png)
Libraries used
---------------
* https://github.com/latemic/svg-android

Developed by
------------
* Mostafa Gazar - <eng.mostafa.gazar@gmail.com>

Donations
------------
If you'd like to support this library, you could make a donation here:

[![Gratipay](http://img.shields.io/gratipay/MostafaGazar.svg)](https://gratipay.com/MostafaGazar)

[1]: https://play.google.com/store/apps/details?id=com.meg7.samples
