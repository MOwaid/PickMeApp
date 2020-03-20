package com.ais.pickmecab.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ais.pickmecab.MainActivity;
import com.ais.pickmecab.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import static com.ais.pickmecab.Utils.doc_detail_fragment;

public class GalleryFragment extends Fragment implements doclist_Adapter.OnItemClickListener {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

    private GalleryViewModel galleryViewModel;

    private RecyclerView mRecyclerView;
    private doclist_Adapter mExampleAdapter;
    private ArrayList<doclist_items> mExampleList;
    private RequestQueue mRequestQueue;
    View rootview;
    private static FragmentManager fragmentManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        rootview = root;
      //  final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });

        mRecyclerView = (RecyclerView) root.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mExampleList = new ArrayList<>();

        return root;
    }

    @Override
    public void onItemClick(int position) {

        doclist_items clickedItem = mExampleList.get(position);



        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, clickedItem.getImageUrl());
        bundle.putString(EXTRA_CREATOR, clickedItem.getCreator());
        bundle.putString(EXTRA_LIKES, "12");

        DocdetailFragment fragobj = new DocdetailFragment();
        fragobj.setArguments(bundle);
         fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(R.id.nav_host_fragment, fragobj, doc_detail_fragment)
                 .commit();


     /*   DocdetailFragment nextFrag= new DocdetailFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.layout.fragment_gallery, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel


        fragmentManager = getActivity().getSupportFragmentManager();



       mRequestQueue = Volley.newRequestQueue(((MainActivity)getActivity()).getApplicationContext());
        parseJSON();
    }

    private void parseJSON() {
        String url = "https://pixabay.com/api/?key=15496387-332f9e87151a43e2117950216&q=texture&editors_choice=true&per_page=5&image_type=photo&pretty=true";

        final List<String> mydoclist =new ArrayList<String>();
        mydoclist.add("Photo ID");
        mydoclist.add("Driving Licence");
        mydoclist.add("Registration");
        mydoclist.add("MOT Report");
        mydoclist.add("Insurance");


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hits");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String creatorName =  mydoclist.get(i); //hit.getString("user");
                                String imageUrl = hit.getString("webformatURL");
                                int likeCount = hit.getInt("likes");

                                mExampleList.add(new doclist_items(imageUrl, creatorName, likeCount));
                            }

                            mExampleAdapter = new doclist_Adapter(((MainActivity)getActivity()).getApplicationContext(), mExampleList);
                            mRecyclerView.setAdapter(mExampleAdapter);
                            mExampleAdapter.setOnItemClickListener(GalleryFragment.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }


}