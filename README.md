CustomShapeImageView Demo ([Play Store Demo][1])
-------------------------

A library for supporting custom shaped ImageView(s) using SVGs and paint shapes

You can also use this gist https://gist.github.com/MostafaGazar/ee345987fa6c8924d61b if you do not want to add this library project to your codebase.

[![Build Status](https://travis-ci.org/MostafaGazar/CustomShapeImageView.svg)](https://travis-ci.org/MostafaGazar/CustomShapeImageView)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CustomShapeImageView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1197)
[![Android Weekly](http://img.shields.io/badge/Android%20Weekly-%2381-2CB3E5.svg?style=flat)](http://androidweekly.net/issues/issue-81)
[![PayPal Donations](https://img.shields.io/badge/paypal-donate-yellow.svg?style=flat)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=mmegazar%40gmail%2ecom&lc=NZ&item_name=Mostafa%20Gazar&item_number=GitHub&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
[![Coverage Status](https://coveralls.io/repos/github/MostafaGazar/CustomShapeImageView/badge.svg?branch=master)](https://coveralls.io/github/MostafaGazar/CustomShapeImageView?branch=master)

Usage
-----
```xml
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
```

Download
------------
Add the `customshapeimageview` dependency to your `build.gradle` file:

[![Maven Central](https://img.shields.io/maven-central/v/com.mostafagazar/customshapeimageview.svg)](http://search.maven.org/#search%7Cga%7C1%7Ccustomshapeimageview)
```groovy
dependencies {
    ...
    compile 'com.mostafagazar:customshapeimageview:1.0.4'
    ...
}
```

Proguard
------------
If you're using proguard for code shrinking and obfuscation, make sure to add the following:
```proguard
   -keep class com.meg7.widget.** { *; }
```

Screenshots
------------
![main](https://raw.githubusercontent.com/MostafaGazar/CustomShapeImageView/master/Screenshot_2016-01-19-09-17-37.png)

Libraries used
---------------
* https://github.com/latemic/svg-android

Developed by
------------
* Mostafa Gazar - <mmegazar@gmail.com>

License
------------
    Copyright 2013-2016 Mostafa Gazar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
Donations
------------
If you'd like to support this library, you could make a donation here:

[![PayPal Donation](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=mmegazar%40gmail%2ecom&lc=NZ&item_name=Mostafa%20Gazar&item_number=GitHub&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)



[1]: https://play.google.com/store/apps/details?id=com.meg7.samples
