package love.wintrue.com.lovestaff.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import love.wintrue.com.lovestaff.R;
import love.wintrue.com.lovestaff.bean.AddressBookItemBean;
import love.wintrue.com.lovestaff.widget.CircleImageView;

public class AddressBookAdapter extends me.yokeyword.indexablerv.IndexableAdapter<AddressBookItemBean> implements Filterable {
    private LayoutInflater mInflater;
    private SearchCallBack mSearchCallBack;
    private Context mContext;

    public AddressBookAdapter(Context context, SearchCallBack searchCallBack) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
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
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, AddressBookItemBean entity) {
        ContentVH vh = (ContentVH) holder;
        vh.tv.setText(entity.getName());
        Glide.with(mContext).load(entity.getAvatarUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bg_jz)
                .error(R.drawable.bg_jz)
                .crossFade().into(vh.ivAddressBook);
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
        CircleImageView ivAddressBook;


        public ContentVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_name);
            ivAddressBook = (CircleImageView) itemView.findViewById(R.id.iv_address_book);
        }
    }

    public interface SearchCallBack {
        void searchEmpty();
    }
}
