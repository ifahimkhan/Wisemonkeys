/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.wisemonkeys.android.wisemonkeys;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.wisemonkeys.android.wisemonkeys.Interface.LoadMore;
import in.wisemonkeys.android.wisemonkeys.models.Latest;


public class DynamicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public DynamicAdapter(RecyclerView recyclerView, FragmentActivity activity, List<Latest> latests) {
        this.activity = activity;
        this.latests = latests;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(activity, R.anim.anticipate_overshoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Latest> latests;
    List<Integer> listlikePosition = new ArrayList<>(), listdislikePosition = new ArrayList<>();

    int visibleThreshold = 5, lastVisibleItem, totalItemCount;

  /*  DynamicAdapter(RecyclerView recyclerView, Activity activity, List<String> name, List<String> title, List<Integer> post_id, List<Integer> user_id) {

        this.activity = activity;
        this.name = name;
        this.title = title;
        this.post_id = post_id;
        this.user_id = user_id;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public DynamicAdapter(RecyclerView recyclerView, FragmentActivity activity, QuestionsModel questionsModel) {
        this.activity = activity;
        this.questionModel =  questionsModel;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }*/

    @Override
    public int getItemViewType(int position) {
        return latests.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.question_container, parent, false);
            return new NumberViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_loader, parent, false);
            return new Loading(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof NumberViewHolder) {


            // animate(holder);
            final NumberViewHolder viewHolder = (NumberViewHolder) holder;
            viewHolder.tv_name.setText(latests.get(position).getDisplay_name());
            viewHolder.tv_title.setText(latests.get(position).getPost_title());
            viewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "Open profile for User ID: " + latests.get(position).getPost_author(), Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, position + ": Open POST for Post ID : " + latests.get(position).getID(), Toast.LENGTH_LONG).show();
                }
            });

            if(latests.get(position).isVoteup() ){
                viewHolder.voteup.setImageResource(R.drawable.ic_thumbs_up_checked);
            }else{
                viewHolder.voteup.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            }if(latests.get(position).isVotedown() ){
                viewHolder.votedown.setImageResource(R.drawable.ic_thumbs_down_checked);
            }else{
                viewHolder.votedown.setImageResource(R.drawable.ic_thumb_down_black_24dp);
            }

            viewHolder.bind(position);


        } else if (holder instanceof Loading) {
            Loading loading = (Loading) holder;
            loading.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return latests == null ? 0 : latests.size();
    }

    public void setLoaded() {
        isLoading = false;
    }


    class NumberViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView tv_name, tv_title;
        ImageView imageView, voteup, votedown;
        View view;


        public NumberViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.user_photo);
            tv_name = (TextView) itemView.findViewById(R.id.user_name);
            votedown = (ImageView) itemView.findViewById(R.id.votedown);
            voteup = (ImageView) itemView.findViewById(R.id.voteup);
            tv_title = (TextView) itemView.findViewById(R.id.question);

        }

        public void bind(final int position) {
            voteup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Latest latest= latests.get(position);
                    latest.setVoteup(!latest.isVoteup());
                    if(latest.isVotedown()){
                        latest.setVotedown(false);
                    }
                    latests.set(position,latest);
                    notifyDataSetChanged();
                }
            });
            votedown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Latest latest= latests.get(position);
                    latest.setVotedown(!latest.isVotedown());
                    if(latest.isVoteup()){
                        latest.setVoteup(false);
                    }
                    latests.set(position,latest);
                    notifyDataSetChanged();
                }
            });


        }
    }

    class Loading extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        ProgressBar progressBar;
        View view;

        public Loading(View itemView) {
            super(itemView);
            view = itemView;
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar_loader);

        }
    }

    public interface ItemClickListener {
        public void itemClicked(int position);
    }
}

