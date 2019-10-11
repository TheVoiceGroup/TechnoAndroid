package tvg.com.technoandy;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class VisibilityTransition {

    public static void fadeOut(Context context, View view, int visibility){
        Animation out = AnimationUtils.makeOutAnimation(context, true);
        view.startAnimation(out);
        view.setVisibility(visibility);
    }

    public static void fadeIn(Context context, View view, int visibility){
        Animation in = AnimationUtils.makeInAnimation(context, true);
        view.startAnimation(in);
        view.setVisibility(visibility);
    }
}
