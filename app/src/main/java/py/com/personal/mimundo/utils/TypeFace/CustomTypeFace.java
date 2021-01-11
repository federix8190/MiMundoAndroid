package py.com.personal.mimundo.utils.TypeFace;
import android.content.Context;
import android.graphics.Typeface;

public class CustomTypeFace {
	
	private static final int NUM_OF_CUSTOM_FONTS = 2;
	
	public static final int PLATFORM_MEDIUM = 0;
	
	public static final int TYPOGRACIA = 1;

	private static boolean fontsLoaded = false;

	private static Typeface[] fonts = new Typeface[2];

	private static String[] fontPath = {
	    "fonts/Platform-Medium.otf",
	    "fonts/ProximaNova-Regular.otf"
	};

	
	
	public static Typeface getTypeface(Context context, int fontIdentifier) {
	    if (!fontsLoaded) {
	        loadFonts(context);
	    }
	    return fonts[fontIdentifier];
	}


	private static void loadFonts(Context context) {
	    for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
	        fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
	    }
	    fontsLoaded = true;

	}
}
