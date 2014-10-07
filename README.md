Rate-Me
=======

Rate Me is an Android 2.3+ library that shows dialog to suggest the user to rate the application in the Google Play Store.

With a little twist: if the rating is positive, we take the user to the Play Store directly. Otherwise, we ask him for feedback via email. (This is all configurable.)

[![Screenshot](https://raw.githubusercontent.com/androidsx/rate-me/master/images-readme/image.png)]()

How to integrate
================

## Eclipse

1. Download and extract the ZIP file
2. Import the sources: File > Import > Existing projects into workspace > Select directory > add LibraryRateMe
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

The integration is very simple: mimic [this example](https://github.com/androidsx/rate-me/blob/master/SampleProject/src/com/androidsx/rateme/demo1/SampleProject.java) to link the dialog from your own button.

When you want to use this library you need choose the icon for your dialg.

```java

			.setLogoResourceId(R.drawable.icon)
```

If you don´t pass the icon, the library will default the icon that has the name `ic_launcher`.

If you use the goToMail Dialog in `true`, you need to configure the company email in `setEmail`

You have different options to configure the library and this is a example :

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
						.setLogoResourceId(R.drawable.icon)
						.setShowOKButtonByDefault(false)
						.setEmail(getString(R.string.support_email))
						.setShowShareButton(false)
						.setTextColor(Color.BLUE)
						.setTitleBackgroundColor(Color.White)
						.setTitleTextColor(Color.BLUE)
						.build()
						.show(getFragmentManager(), "dialog");
```

This Library can be opened automatically by calling the method onStart in our project.

```java
			@Override
					protected void onStart() {
							super.onStart();
							final int launchTimes = 5;
							final int installDate = 20;
							new RateMeDialogTimer(installDate, launchTimes);
							RateMeDialogTimer.onStart(this);
							if (RateMeDialogTimer.shouldShowRateDialog(this)) {
									AlertMenu();
							}

					}
```

##CustomCriteria

The default criteria to show the dialog is as below:

* App is launched more than 20 days later than installation.
* App is launched more than 5 times

you can change this settings [here](https://github.com/androidsx/rate-me/blob/master/SampleProject/src/com/androidsx/rateme/demo1/SampleProject.java)

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
