package com.ais.pickmecab.ui.gallery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ais.pickmecab.R;
import com.squareup.picasso.Picasso;

import static com.ais.pickmecab.ui.gallery.GalleryFragment.EXTRA_CREATOR;
import static com.ais.pickmecab.ui.gallery.GalleryFragment.EXTRA_LIKES;
import static com.ais.pickmecab.ui.gallery.GalleryFragment.EXTRA_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocdetailFragment extends Fragment {

    String imageUrl;
    String creatorName;
    int likeCount;
    Button btn_close;
    Fragment me=this;

    public DocdetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




         imageUrl = getArguments().getString(EXTRA_URL);
         creatorName = getArguments().getString(EXTRA_CREATOR);
         likeCount = Integer.parseInt(getArguments().getString(EXTRA_LIKES, "0"));






        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_docdetail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView imageView = getActivity().findViewById(R.id.image_view_detail);
        TextView textViewCreator = getActivity().findViewById(R.id.text_view_creator_detail);
        TextView textViewLikes = getActivity().findViewById(R.id.text_view_like_detail);

        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(creatorName);
        textViewLikes.setText("Likes: " + likeCount);

        btn_close = (Button)getActivity().findViewById(R.id.btnclose);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  getActivity().onBackPressed();
                getActivity().getSupportFragmentManager().beginTransaction().remove(me).commit();
            }
            });
    }
}
