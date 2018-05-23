/**
 * Copyright 2014 Zhenguo Jin (jinzhenguo1990@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package love.wintrue.com.lovestaff.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;


/**
 * 动画工具类
 *
 * @author zhenguo
 */
public final class AnimationUtils {

    public static final int Duration = 120;
    private static final float Alpha = 1f;
    protected static final String TAG = "AnimatorUtils";
    private static int screenWidth = 0;
    private static int screenHeight = 0;


    private static void getMetrics(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }


    /**
     * 位移动画
     */
    public static void translation(View view, int start_location, int end_location,
                                   Animator.AnimatorListener listener, int durationTime, String type, String moveDirection) {
        ObjectAnimator animator;
        if (TextUtils.equals("moveRight", moveDirection)) {
            animator = ObjectAnimator.ofFloat(view, type, 0, end_location - start_location);
        } else {
            animator = ObjectAnimator.ofFloat(view, type, end_location - start_location, 0);
        }
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (listener != null) {
            animator.addListener(listener);
        }
        animator.setDuration(durationTime);
        animator.start();
    }

    /**
     * 渐变动画效果
     */
    public static ObjectAnimator alphaAnimator(View target, float from, float to) {
        ObjectAnimator mAnimatorAlpha = ObjectAnimator.ofFloat(target, "alpha", from, to);
        mAnimatorAlpha.setRepeatMode(Animation.REVERSE);
        mAnimatorAlpha.setRepeatCount(0);
        mAnimatorAlpha.setDuration(300);
        mAnimatorAlpha.start();
        return mAnimatorAlpha;
    }
}
