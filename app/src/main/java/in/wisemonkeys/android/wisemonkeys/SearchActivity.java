package in.wisemonkeys.android.wisemonkeys;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import in.wisemonkeys.android.wisemonkeys.models.Recycler;

import static in.wisemonkeys.android.wisemonkeys.app.AppController.TAG;

public class SearchActivity extends AppCompatActivity {

    static boolean loaded = false;

    private List<Latest> latests;
    private Recycler contactAdapter;
    RecyclerView recyclerView;
    private String url = "http://192.168.43.189/wisemonk/questions.php?type=latest&offset=0";
    ProgressBar progressBar;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        latests = new ArrayList<>();
        //find view by id and attaching adapter for the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set load more listener for the RecyclerView adapter
        if (loaded && latests.size() > 0) {
            dynamicLoading();
            contactAdapter = new Recycler(recyclerView,  latests,SearchActivity.this);
            contactAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(contactAdapter);
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

                            latests.removeAll(latests);

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject question = (JSONObject) response.get(i);
                                Latest latest = new Latest(question.getInt("ID"), question.getInt("post_author"), question.getString("post_title"), question.getString("display_name"));

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

                            contactAdapter = new Recycler(recyclerView,  latests,SearchActivity.this);
                            //dynamicAdapter = new DynamicAdapter(recyclerView, getActivity(), name, title, post_id, user_id);
                            contactAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(contactAdapter);
                            dynamicLoading();
                            progressBar.setVisibility(View.INVISIBLE);

                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
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
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject question = (JSONObject) response
                                        .get(i);
                                Latest latest = new Latest(question.getInt("ID"), question.getInt("post_author"), question.getString("post_title"), question.getString("display_name"));

                                latests.add(latest);
                            }
                            /*
                            questionadapter = new GreenAdapter(getActivity(), name, title, post_id, user_id);
                            questionadapter.notifyDataSetChanged();
                            recyclerView.setAdapter(questionadapter);
                            */
                            contactAdapter.notifyDataSetChanged();
                            contactAdapter.setLoaded();

                            progressBar.setVisibility(View.INVISIBLE);

                            //txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(requesting);

    }

    public void dynamicLoading() {
        contactAdapter.setOnLoadMoreListener(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (latests.size() <= 69) {
                    latests.add(null);
                    contactAdapter.notifyItemInserted(latests.size());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            latests.remove(latests.size() - 1);
                            contactAdapter.notifyItemRemoved(latests.size());
                            makeJsonArrayRequestAgain(latests.size());
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Complete", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}