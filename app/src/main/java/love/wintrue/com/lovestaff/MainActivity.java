package love.wintrue.com.lovestaff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import love.wintrue.com.lovestaff.base.MApplication;
import love.wintrue.com.lovestaff.ui.mine.MineActivity;
import love.wintrue.com.lovestaff.utils.ActivityUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MApplication.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"测试版本控制");

//        ActivityUtil.next(this, MineActivity.class);
    }
}
