package in.wisemonkeys.android.wisemonkeys.models;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.wisemonkeys.android.wisemonkeys.Interface.LoadMore;
import in.wisemonkeys.android.wisemonkeys.R;

/**
 * Created by HSBC on 20-10-2017.
 */
public class Recycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private Activity activity;
    private List<Latest> contacts;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private LoadMore onLoadMoreListener;
    public void setOnLoadMoreListener(LoadMore mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
    public Recycler(RecyclerView recyclerView, List<Latest> contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return contacts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            activity= (Activity) parent.getContext();
            View view = LayoutInflater.from(activity).inflate(R.layout.question_container, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_loader, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserViewHolder) {
            final Latest latest = contacts.get(position);
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.tv_name.setText(latest.getDisplay_name());
            userViewHolder.tv_title.setText(latest.getPost_title());
            userViewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "Open profile for User ID: " + latest.getPost_author(), Toast.LENGTH_SHORT).show();
                }
            });
            userViewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, position + ": Open POST for Post ID : " + latest.getID(), Toast.LENGTH_LONG).show();
                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /*// Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }*/
    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_title, voteup, votedown;
        ImageView imageView;
        View view;

        public UserViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.user_photo);
            tv_name = (TextView) itemView.findViewById(R.id.user_name);
            votedown = (TextView) itemView.findViewById(R.id.votedown);
            voteup = (TextView) itemView.findViewById(R.id.voteup);
            tv_title = (TextView) itemView.findViewById(R.id.question);

        }

    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar_loader);
        }
    }

}