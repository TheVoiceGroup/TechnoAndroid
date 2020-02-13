package tvg.com.technoandy;

import android.content.Intent;

public interface ActivityResult {
    void onImageActivityResult(int RequestCode, int ResultCode, Intent intent);
}
