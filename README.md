Rate-Me [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Rate--Me-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1032)
=======

Rate Me is an Android 3.0+ library that shows dialog to suggest the user to rate the application in the Google Play Store.

With a little twist: if the rating is positive, we take the user to the Play Store directly. Otherwise, we ask him for feedback via email. (This is all configurable.)

[![Screenshot](https://raw.githubusercontent.com/androidsx/rate-me/master/Extras/rateMe_2.png)]()

How to integrate
================

## Eclipse

1. Download and extract the ZIP file
2. Import the sources: File > Import > Existing projects into workspace > Select directory > add `LibraryRateMe`
3. Add it as a library: Right click on your project > Android > Library: Add > LibraryRateMe

## Gradle

Add this to your project's build.gradle file:

```xml
dependencies {
    compile 'com.androidsx:ratemelib:X.Y.Z@aar'
}
```

You can see the last version in [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cratemelib)

How to use
==========

Have a look at [the sample code](https://github.com/androidsx/rate-me/blob/master/SampleProject/src/com/androidsx/rateme/demo1/SampleProject.java) or check out a simple integration:

```java
	new DialogRateMe.Builder(context)
				.setLogoResourceId(R.drawable.ic_launcher)
				.setGoToMail(true)
				.setEmail(getString(R.string.support_email))
				.setShowShareButton(false)
				.build()
				.show(getFragmentManager(), "dialog");
```

The full list of options is:

```java
	new DialogRateMe.Builder(this)
				.setDialogColor(Color.White)
				.setIconCloseColorFilter(Color.DKGRAY)
				.setLineDividerColor(Color.BLUE)
				.setLogoResourceId(R.drawable.ic_launcher)
				.setRateButtonBackgroundColor(Color.BLUE)
				.setRateButtonPressedBackgroundColor(Color.RED)
				.setRateButtonTextColor(Color.White)
				.setGoToMail(true)
				.setShowOKButtonByDefault(false)
				.setEmail(getString(R.string.support_email))
				.setShowShareButton(false)
				.setTextColor(Color.BLUE)
				.setTitleBackgroundColor(Color.White)
				.setTitleTextColor(Color.BLUE)
				.build()
				.show(getFragmentManager(), "dialog");
```

Dynamic opening of the dialog
-----

In case you want the dialog to appear based on the number of times the app has been opened or the install date, have a look at this example:

```java
	@Override
	protected void onStart() {
		super.onStart();

		final int launchTimes = 3;
		final int installDate = 7;

		new RateMeDialogTimer();

		RateMeDialogTimer.onStart(this, Bundle.EMPTY);
		if (RateMeDialogTimer.shouldShowRateDialog(this, installDate, launchTimes)) {
			// show the dialog with the code above
		}

	}
```

License
=======

		Copyright 2014 Lucas Ponzoda

		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at

			http://www.apache.org/licenses/LICENSE-2.0

		Unless required by applicable law or agreed to in writing, software
		distributed under the License is distributed on an "AS IS" BASIS,
		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		See the License for the specific
