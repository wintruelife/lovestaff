package love.wintrue.com.lovestaff.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.bean.AddressBookItemBean;
import love.wintrue.com.lovestaff.widget.circularcontactview.AsyncTaskEx;
import love.wintrue.com.lovestaff.widget.circularcontactview.AsyncTaskThreadPool;
import love.wintrue.com.lovestaff.widget.circularcontactview.CircularContactView;

public class AddressBookAdapter extends me.yokeyword.indexablerv.IndexableAdapter<AddressBookItemBean> implements Filterable {
    private LayoutInflater mInflater;
    private SearchCallBack mSearchCallBack;
    private Context mContext;
    private int mAddressBookType;
    private final int[] PHOTO_TEXT_BACKGROUND_COLORS;
    private final int CONTACT_PHOTO_IMAGE_SIZE;
    private final AsyncTaskThreadPool mAsyncTaskThreadPool = new AsyncTaskThreadPool(1, 2, 10);

    public AddressBookAdapter(Context context, SearchCallBack searchCallBack, int addressBookType) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        PHOTO_TEXT_BACKGROUND_COLORS = mContext.getResources().getIntArray(R.array.contacts_text_background_colors);
        CONTACT_PHOTO_IMAGE_SIZE = mContext.getResources().getDimensionPixelSize(
                R.dimen.dimension_44);
        mAddressBookType = addressBookType;
        mSearchCallBack = searchCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_tv_title_addressbook, parent, false);
        return new IndexVH(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_address_book, parent, false);
        return new ContentVH(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        IndexVH vh = (IndexVH) holder;
        vh.tv.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(final RecyclerView.ViewHolder holder, final AddressBookItemBean entity) {
        final ContentVH vh = (ContentVH) holder;
        vh.tv.setText(entity.getName());
        if (mAddressBookType == 0) {
            Glide.with(mContext).load(entity.getAvatarUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_jz)
                    .error(R.drawable.bg_jz)
                    .crossFade().bitmapTransform(new CropCircleTransformation(mContext)).
                    into(vh.ivAddressBook);
        } else {
            boolean hasPhoto = !TextUtils.isEmpty(entity.getAvatarUrl());
//            if (vh.updateTask != null && !vh.updateTask.isCancelled())
//                vh.updateTask.cancel(true);
//            final Bitmap cachedBitmap = hasPhoto ? ImageCache.INSTANCE.getBitmapFromMemCache(entity.getAvatarUrl()) : null;
//            if (cachedBitmap != null)
//                vh.rlvNameView.setImageBitmap(cachedBitmap);
//            else {
//                final int backgroundColorToUse = PHOTO_TEXT_BACKGROUND_COLORS[holder.getAdapterPosition()
//                        % PHOTO_TEXT_BACKGROUND_COLORS.length];
//                if (TextUtils.isEmpty(entity.getName()))
//                    vh.rlvNameView.setImageResource(R.drawable.user_logo,
//                            backgroundColorToUse);
//                else {
//                    final String characterToShow = TextUtils.isEmpty(entity.getName()) ? "" : entity.getName().substring(0, 1).toUpperCase(Locale.getDefault());
//                    vh.rlvNameView.setTextAndBackgroundColor(characterToShow, backgroundColorToUse);
//                }
            if (hasPhoto) {
                SimpleTarget target = new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        vh.rlvNameView.setImageBitmap(bitmap);
                    }
                };

                Glide.with(mContext).load(entity.getAvatarUrl()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bg_jz)
                        .error(R.drawable.bg_jz)
                        .into(target);
//                    vh.updateTask = new AsyncTaskEx<Void, Void, Bitmap>() {
//
//                        @Override
//                        public Bitmap doInBackground(final Void... params) {
//                            if (isCancelled())
//                                return null;
//                            final Bitmap b = ContactImageUtil.getContactPhotoThumbnail(mContext, entity.getContactId());
//                            if (b != null)
//                                return ThumbnailUtils.extractThumbnail(b, CONTACT_PHOTO_IMAGE_SIZE,
//                                        CONTACT_PHOTO_IMAGE_SIZE);
//                            return null;
//                        }
//
//                        @Override
//                        public void onPostExecute(final Bitmap result) {
//                            super.onPostExecute(result);
//                            if (result == null)
//                                return;
//                            ImageCache.INSTANCE.addBitmapToCache(entity.getAvatarUrl(), result);
//                            vh.rlvNameView.setImageBitmap(result);
//                        }
//                    };
//                    mAsyncTaskThreadPool.executeAsyncTask(vh.updateTask);
            } else {
                final int backgroundColorToUse = PHOTO_TEXT_BACKGROUND_COLORS[holder.getAdapterPosition()
                        % PHOTO_TEXT_BACKGROUND_COLORS.length];
                if (TextUtils.isEmpty(entity.getName()))
                    vh.rlvNameView.setImageResource(R.drawable.user_logo,
                            backgroundColorToUse);
                else {
                    final String characterToShow = TextUtils.isEmpty(entity.getName()) ? "" : entity.getName().substring(0, 1).toUpperCase(Locale.getDefault());
                    vh.rlvNameView.setTextAndBackgroundColor(characterToShow, backgroundColorToUse);
                    entity.setBackgroundColor(backgroundColorToUse);
                }
            }
//            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<AddressBookItemBean> list = new ArrayList<>();
                for (AddressBookItemBean item : getData()) {
                    if (item.getPinyin().startsWith(constraint.toString()) || item.getName().contains(constraint)) {
                        list.add(item);
                    }
                }
                FilterResults results = new FilterResults();
                results.count = list.size();
                results.values = list;
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<AddressBookItemBean> list = (ArrayList<AddressBookItemBean>) results.values;
                if (results.count == 0) {
                    mSearchCallBack.searchEmpty();
                } else {
                    setDatas(list);
                }
            }
        };
    }

    private class IndexVH extends RecyclerView.ViewHolder {
        TextView tv;

        public IndexVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    private class ContentVH extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView ivAddressBook;
        CircularContactView rlvNameView;
        public AsyncTaskEx<Void, Void, Bitmap> updateTask;


        public ContentVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_name);
            ivAddressBook = (ImageView) itemView.findViewById(R.id.iv_address_book);
            rlvNameView = (CircularContactView) itemView.findViewById(R.id.ccv_name_view);
            if (mAddressBookType == 0) {
                ivAddressBook.setVisibility(View.VISIBLE);
                rlvNameView.setVisibility(View.GONE);
            } else {
                ivAddressBook.setVisibility(View.INVISIBLE);
                rlvNameView.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface SearchCallBack {
        void searchEmpty();
    }
}
