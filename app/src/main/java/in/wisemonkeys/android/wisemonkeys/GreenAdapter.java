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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GreenAdapter extends RecyclerView.Adapter<GreenAdapter.NumberViewHolder> {

    private static final String TAG = GreenAdapter.class.getSimpleName();

    List<String> name;
    List<String> title;
    List<Integer> post_id;
    List<Integer> user_id;
    Context context;
    List<Integer> listlikePosition = new ArrayList<>(), listdislikePosition = new ArrayList<>();


    public GreenAdapter(Context context, List<String> name, List<String> title, List<Integer> post_id, List<Integer> user_id) {
        this.name = name;
        this.title = title;
        this.post_id = post_id;
        this.user_id = user_id;
        this.context = context;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.question_container;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return name.size();
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


        void bind(final int listIndex) {

            tv_name.setText(name.get(listIndex));
            tv_title.setText(title.get(listIndex));
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Open profile for User ID: " + user_id.get(listIndex), Toast.LENGTH_SHORT).show();
                }
            });
            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Open POST for Post ID : " + post_id.get(listIndex), Toast.LENGTH_LONG).show();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Open profile for User ID: " + user_id.get(listIndex), Toast.LENGTH_SHORT).show();

                }
            });


            voteup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < name.size(); i++) {
                        if (getAdapterPosition() == i || listlikePosition.contains(i)) {
                            listlikePosition.add(getAdapterPosition());
                            voteup.setImageResource(R.drawable.ic_thumbs_up_checked);
                            votedown.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                        }else{
                            voteup.setImageResource(R.drawable.ic_thumb_up_black_24dp);

                        }
                    }



                }


            });
            votedown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listdislikePosition.add(getAdapterPosition());
                    votedown.setImageResource(R.drawable.ic_thumbs_down_checked);

                }
            });

        }


    }

}
