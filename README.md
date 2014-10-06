Rate-Me
=======

Rate-Me is a Librery for Android2.3+ that provides a Dialog for suggesting to the user a Rate the App. If the Rate is 4 or more stars, the Dialog goes to PlayStore
You can see the [source code](https://github.com/androidsx/rate-me/blob/master/LibraryRateMe/src/com/androidsx/rateme/DialogRateMe.java) and an application example

If the rating is 4 or more stars, the dialog just takes the user to the Google Play store. Otherwise, it prompts them to give feedback to the developer.
You can see the [main class here](https://github.com/androidsx/rate-me/blob/master/LibraryRateMe/src/com/androidsx/rateme/DialogRateMe.java) and an integration example in this image:
[![img1](https://raw.githubusercontent.com/androidsx/rate-me/master/images-readme/image.png)]()

Add RateMe in your project
==========================

##Eclipse
1) Download the ZIP file <br>
2) Extract the RateMe project from the ZIP file <br>
3) In Eclipse: File > Import > Existing projects into workspace > Select directory > add LibraryRateMe <br>
4) Right click on your project <br>
5) Android > Library: Add > LibraryRateMe <br>

##Gradle
Add this to your project's build.gradle file
```xml
dependencies {

	compile 'com.androidsx:ratemelib:X.Y.Z@aar'

}
```
How to use
==============

Using the library is really simple, [that example](https://github.com/androidsx/rate-me/blob/master/SampleProject/src/com/androidsx/rateme/demo1/SampleProject.java))  allows you to know how to link the activity apps through a button.
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
