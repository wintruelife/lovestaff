package love.wintrue.com.lovestaff.image;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.List;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.utils.ImageUtil;
import love.wintrue.com.lovestaff.utils.LogUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 * 图片浏览PagerAdapter（类似微信的图片浏览查看）
 * 
 * @ClassName: ImageSimpleBrowsePagerAdapter.java
 */
public class ImageSimpleBrowsePagerAdapter extends PagerAdapter {

	private LayoutInflater mLayoutInflater;
	private List<String> mImageUrlList;

	private Context mContext;
	private Activity mCurrentActivity;

	public ImageSimpleBrowsePagerAdapter(Context context, List<String> imageUrlList) {
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageUrlList = imageUrlList;
		this.mContext = context;
	}

	public void setCurrentActivity(Activity currentActivity) {
		mCurrentActivity = currentActivity;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view = mLayoutInflater.inflate(R.layout.adapter_image_simple_browse_item, container, false);
		PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
		photoView.setOnViewTapListener(new OnViewTapListener() {
			@Override
			public void onViewTap(View arg0, float arg1, float arg2) {
				LogUtil.e("onViewTap()");
				if (null != mCurrentActivity) {
					mCurrentActivity.finish();
				}
			}
		});
		String imageUrl = mImageUrlList.get(position);
		ImageUtil.displayImageLarge(imageUrl,photoView);
		container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		return view;
	}

	@Override
	public int getCount() {
		return mImageUrlList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
