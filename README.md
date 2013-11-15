Custom shape ImageView using PorterDuffXfermode with paint shapes and SVGs

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
