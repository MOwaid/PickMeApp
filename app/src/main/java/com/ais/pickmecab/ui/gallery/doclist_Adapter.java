package com.ais.pickmecab.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ais.pickmecab.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


    public class doclist_Adapter extends RecyclerView.Adapter<doclist_Adapter.ExampleViewHolder> {
        private Context mContext;
        private ArrayList<doclist_items> mExampleList;
        private OnItemClickListener mListener;
        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public doclist_Adapter(Context context, ArrayList<doclist_items> exampleList) {
            mContext = context;
            mExampleList = exampleList;
        }

        @Override
        public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.doclist_items, parent, false);
            return new ExampleViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ExampleViewHolder holder, int position) {
            doclist_items currentItem = mExampleList.get(position);

            String imageUrl = currentItem.getImageUrl();
            String creatorName = currentItem.getCreator();
            int likeCount = currentItem.getLikeCount();

            holder.mTextViewCreator.setText(creatorName);
           // holder.mTextViewLikes.setText("Likes: " + likeCount);
            Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        public class ExampleViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public TextView mTextViewCreator;
            public TextView mTextViewLikes;

            public ExampleViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.image_view);
                mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
                mTextViewLikes = itemView.findViewById(R.id.text_view_likes);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                mListener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }
    }

