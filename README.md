Rate-Me
=======

Rate-Me is a Librery for Android2.3+ that provides a Dialog for suggesting to the user a Rate the App. If the Rate is 4 or more stars, the Dialog goes to PlayStore
You can see the ([source code](https://github.com/androidsx/rate-me/blob/master/LibraryRateMe/src/com/androidsx/rateme/DialogRateMe.java)) and an application example 

If the rating is 4 or more stars, the dialog just takes the user to the Google Play store. Otherwise, it prompts them to give feedback to the developer.
You can see the [main class here](https://github.com/androidsx/rate-me/blob/master/LibraryRateMe/src/com/androidsx/rateme/DialogRateMe.java) and an integration example in this image:
[![img1](https://raw.githubusercontent.com/androidsx/rate-me/master/images-readme/image.png)]()

## How to use

Using the library is really simple, ()[that example](https://github.com/androidsx/rate-me/blob/master/SampleProject/src/com/androidsx/rateme/demo1/SampleProject.java))  allows you to know how to link the activity apps through a button.
When you want to use this library you need choose the color of the title and the coor of body of the Dialog, and if you want a button to share the App

			boolean showShareButton = true;
			int titleColor = Color.BLACK;
			int dialogColor = Color.GRAY;
			
Yo need a string in Values for configure the email data in your App
			<string name="email_address" translatable="false">sample-project@mycompany.com</string>
			
and then you send this variables to the Dialog in the method `alertMenu()`

			private void AlertMenu() {
			        boolean showShareButton = true;
			        int titleColor = Color.BLACK;
			        int dialogColor = Color.GRAY;
			        DialogFragment dialog = DialogRateMe.newInstance(MY_PACKAGE_NAME, getString(R.string.email_address),
			                showShareButton, titleColor, dialogColor);
			        dialog.show(getFragmentManager(), "dialog");
			    }
normally use `getPackageName()` instead of variable MY_PACKAGE_NAME = `"com.androidsx.smileys".`

This Library can be opened automatically by calling the method onStart in our project.

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

###CustomCriteria

The default criteria to show the dialog is as below:

* App is launched more than 20 days later than installation.
* App is launched more than 5 times

you can change this settings ([here](https://github.com/androidsx/rate-me/blob/master/SampleProject/src/com/androidsx/rateme/demo1/SampleProject.java))
