package tvg.com.technoandy;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.internal.api.NativeAdLayoutApi;

public class TechnoFBNativeLayout extends NativeAdLayout {
    public TechnoFBNativeLayout(Context context) {
        super(context);
    }

    public TechnoFBNativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TechnoFBNativeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TechnoFBNativeLayout(Context context, NativeAdLayoutApi nativeAdLayoutApi) {
        super(context, nativeAdLayoutApi);
    }
}
