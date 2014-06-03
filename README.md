Rate-Me
=======

Rate-Me is a library for Android 2.3+ that provides a dialog that suggests the user to rate the app.

If the rating is 4 or more stars, the dialog just takes the user to the Google Play store. Otherwise, it prompts them to give feedback to the developer.

You can see the [main class here](https://github.com/androidsx/rate-me/blob/readme/LibraryRateMe/src/com/androidsx/rateme/DialogRateMe.java) and an integration example in this image:

[![img1](https://raw.githubusercontent.com/androidsx/rate-me/master/images-readme/image.png)]()


##How to use

###Download

			git clone git@github.com:androidsx/rate-me.git
			

### Eclipse

Import `rate-me` in Eclipse and then link your app project with this library.

### Use the Library

Using the library is really simple, [that example] RateMe / src / com / androidsx / rateme / demo2 / HelloWorldActivity.java allows you to know how to link the activity apps through a button.

On the button click event, you have add this code.

            DialogFragment dialogo = DialogRateMe.newInstance(
                        "com.androidsx.smileys");
            dialogo.show(getFragmentManager(), "dialog");
    

Normally your currency this line of code: "com.androidsx.smileys" by getPackageName() in your app

This Library can be opened automatically by calling the method onStart in our project:

		    @Override
		    protected void onStart() {
		        super.onStart();
		        RateMeDialogTimer.onStart(this);
		        if (RateMeDialogTimer.shouldShowRateDialog(this)) {
		            DialogFragment dialogo = DialogRateMe.newInstance(
		                    myPackageName);
		            dialogo.show(getFragmentManager(), "dialog");
		        }
        
		    }
Yo can see this in [that example](RateMe / src / com / androidsx / rateme / demo2 / HelloWorldActivity.java)

###CustomCriteria

The default criteria to show the dialog is as below:

* App is launched more than 20 days later than installation.
* App is launched more than 10 times

you can change this settings [here]( LibraryRateMe / src / com / androidsx / rateme / RateMeDialogTimer.java) in the variable `INSTALL_DAYS` and `LAUNCH_TIMES`
