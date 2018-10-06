package in.wisemonkeys.android.wisemonkeys;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
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

import in.wisemonkeys.android.wisemonkeys.Interface.LoadMore;
import in.wisemonkeys.android.wisemonkeys.app.AppController;
import in.wisemonkeys.android.wisemonkeys.models.Latest;
import in.wisemonkeys.android.wisemonkeys.models.QuestionsModel;

import static in.wisemonkeys.android.wisemonkeys.app.AppController.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragments extends Fragment implements DynamicAdapter.ItemClickListener{

    static boolean loaded = false;

    List<Latest> latests = new ArrayList<>();


    QuestionsModel questionsModel;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    GreenAdapter questionadapter;
    DynamicAdapter dynamicAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private String url = "http://192.168.43.189/wisemonk/questions.php?type=latest";
    private String jsonResponse;
    LinearLayoutManager layoutManager;

    public QuestionFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressbar);
        if (loaded && latests.size() > 0) {
            dynamicLoading();
            dynamicAdapter = new DynamicAdapter(recyclerView, getActivity(), latests);
            dynamicAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(dynamicAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            //Toast.makeText(getActivity(), "not changed", Toast.LENGTH_SHORT).show();
        } else {
            makeJsonArrayRequest();
            loaded = true;

        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                makeJsonArrayRequest();
                loaded = true;
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }


    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            jsonResponse = "";
                            latests.removeAll(latests);

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject question = (JSONObject) response.get(i);
                                Latest latest =new Latest(question.getInt("ID"),question.getInt("post_author"),question.getString("post_title"),question.getString("display_name"));

                                /*latest.setDisplay_name(question.getString("display_name"));
                                latest.setPost_title(question.getString("post_title"));
                                latest.setID(question.getInt("ID"));
                                latest.setPost_author(question.getInt("post_author"));*/
                                latests.add(latest);

                                /* latests.add(new Latest(question.getInt("ID"),question.getInt("post_author"),question.getString("post_title"),question.getString("display_name")));
                                questionsModel=new QuestionsModel(latests);*/

                            }

                            // Toast.makeText(getActivity(), questionsModel.getLatest().get(0).getDisplay_name(), Toast.LENGTH_SHORT).show();
                            // Log.d(TAG, questionsModel.getLatest().get(0).getDisplay_name());
                          /* questionadapter = new GreenAdapter(getActivity(), name, title, post_id, user_id);
                            questionadapter.notifyDataSetChanged();
                            recyclerView.setAdapter(questionadapter);*/

                          dynamicAdapter=new DynamicAdapter(recyclerView,getActivity(),latests);
                            //dynamicAdapter = new DynamicAdapter(recyclerView, getActivity(), name, title, post_id, user_id);
                            dynamicAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(dynamicAdapter);
                            dynamicLoading();
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
                Toast.makeText(getActivity(),
                        "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    private void makeJsonArrayRequestAgain(int offset) {
        offset = latests.size();

        JsonArrayRequest requesting = new JsonArrayRequest(url + "&offset=" + offset,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject question = (JSONObject) response
                                        .get(i);
                                Latest latest =new Latest(question.getInt("ID"),question.getInt("post_author"),question.getString("post_title"),question.getString("display_name"));

                                latests.add(latest);
                            }
                            /*
                            questionadapter = new GreenAdapter(getActivity(), name, title, post_id, user_id);
                            questionadapter.notifyDataSetChanged();
                            recyclerView.setAdapter(questionadapter);
                            */
                            dynamicAdapter.notifyDataSetChanged();
                            dynamicAdapter.setLoaded();

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
                Toast.makeText(getActivity(),
                        "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(requesting);

    }


    public void dynamicLoading() {
        dynamicAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (latests.size() <= 68) {
                    latests.add(null);
                    dynamicAdapter.notifyItemInserted(latests.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            latests.remove(latests.size()-1);
                            dynamicAdapter.notifyItemRemoved(latests.size());
                            makeJsonArrayRequestAgain(latests.size());
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getActivity(), "Loading Complete", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void itemClicked(int position) {

        Latest latest= latests.get(position);
        latest.setVoteup(!latest.isVoteup());
        latests.set(position,latest);
        dynamicAdapter.notifyDataSetChanged();
    }
}
