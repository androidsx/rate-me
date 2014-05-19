rate-me
=======

Rate Me es una librería para Android 2.3+ que proporciona una pantalla para sugerir al usuario que proporciona una puntuación (rating) positiva de la aplicación en el Play Store.

You can see the [source code](https://github.com/androidsx/rate-me/blob/readme/LibraryRateMe/src/com/androidsx/rateme/DialogRateMe.java) and an application example 

[![img1](https://raw.githubusercontent.com/androidsx/rate-me/images-readme/placeit.png)]()

Usage
=======

Using the library is really simple, [that example](SampleProject / src / com / androidsx / rateme / demo1 / SampleProject.java) allows you to know how to link the activity apps through a button.

On the button click event, you have add this code.

            DialogFragment dialogo = DialogRateMe.newInstance(
                        "com.androidsx.smileys");
            dialogo.show(getFragmentManager(), "dialog");
    
    

normally your currency this line of code: "com.androidsx.smileys" by getPackageName() in your app
