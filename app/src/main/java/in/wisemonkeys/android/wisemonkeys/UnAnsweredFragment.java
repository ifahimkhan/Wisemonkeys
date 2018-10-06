package in.wisemonkeys.android.wisemonkeys;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.wisemonkeys.android.wisemonkeys.app.AppController;

import static in.wisemonkeys.android.wisemonkeys.app.AppController.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnAnsweredFragment extends Fragment {

    List<String> name = new ArrayList<String>();
    List<String> title = new ArrayList<String>();
    List<Integer> post_id = new ArrayList<Integer>();
    List<Integer> user_id = new ArrayList<Integer>();

    ProgressBar progressBar;
    RecyclerView recyclerView;
    GreenAdapter adapter;
    private String url = "http://192.168.43.189/wisemonk/questions.php?type=unanswered";
    private String jsonResponse;
    LinearLayoutManager layoutManager;

    public UnAnsweredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_un_answered, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview1);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar1);
        if (adapter == null && name.isEmpty()) {

            makeJsonArrayRequest();
        } else {
            //adapter = new GreenAdapter(getActivity(), name, title, post_id, user_id);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
        }
        recyclerView.setClickable(true);

    }

    private void makeJsonArrayRequest() {

        JsonArrayRequest req1 = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
//                            name.clear();title.clear();post_id.clear();user_id.clear();
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject question = (JSONObject) response
                                        .get(i);

                                name.add(question.getString("display_name"));
                                title.add(question.getString("post_title"));
                                post_id.add(question.getInt("ID"));
                                user_id.add(question.getInt("post_author"));
                            }
                            adapter = new GreenAdapter(getActivity(), name, title, post_id, user_id);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);


                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req1);

    }

}
