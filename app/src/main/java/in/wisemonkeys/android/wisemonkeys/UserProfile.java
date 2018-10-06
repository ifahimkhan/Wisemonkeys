package in.wisemonkeys.android.wisemonkeys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.wisemonkeys.android.wisemonkeys.app.AppController;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = "UserProfile";
    private static final int ACTIVITY_NUM = 4;
    FragmentManager manager = getSupportFragmentManager();


    private Context mContext = UserProfile.this;
    SessionManager sessionManager;
    HashMap<String, String> userMetadata = new HashMap<String, String>();
    String id = "", myemail = "", url = "";

    ImageView imageView;

    TextView description, name, question_count, answer_count, link_fb, link_gplus, link_twitter, email, location,pointsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        id = String.valueOf(sessionManager.prefId());
        myemail = sessionManager.prefEmail();
        init();
        url = getString(R.string.profile);
        UserData(url, id);
        setupBottomNavigationView();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.data_container, new QuestionFragments(), "question");
        transaction.commit();

    }

    private void init() {

        location = (TextView) findViewById(R.id.location);
        email = (TextView) findViewById(R.id.email);
        link_twitter = (TextView) findViewById(R.id.twitter);
        link_gplus = (TextView) findViewById(R.id.google);
        link_fb = (TextView) findViewById(R.id.facebook);
        answer_count = (TextView) findViewById(R.id.answers_count);
        question_count = (TextView) findViewById(R.id.questions_count);
        name = (TextView) findViewById(R.id.username);
        description = (TextView) findViewById(R.id.description);
        imageView = (ImageView) findViewById(R.id.user_image);
        pointsText= (TextView) findViewById(R.id.pointstext);
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    public void latest(View view) {
        if (manager.findFragmentByTag("question") != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.data_container, manager.findFragmentByTag("question"), "question");
            transaction.commit();
            Toast.makeText(getApplicationContext(), "Not null", Toast.LENGTH_SHORT).show();
        } else {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.data_container, new QuestionFragments(), "question");
            transaction.commit();
        }
    }

    public void unAnswered(View view) {

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.data_container, new UnAnsweredFragment(), "unanswered");
        transaction.commit();
    }

    private void UserData(String url, final String id) {

        StringRequest Stringreq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if (status) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject exploreObject = jsonArray.getJSONObject(i);
                                    String key = exploreObject.getString("meta_key");
                                    String value = exploreObject.getString("meta_value");
                                    userMetadata.put(key, value);
                                }
                                loadFetchedData();

                            } else {
                                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(Stringreq);

    }

    public void loadFetchedData() {

        String data = "";
        String imgurl = userMetadata.get("et_avatar_url");
        if (TextUtils.isEmpty(imgurl)) {
            Picasso.with(getApplicationContext()).load(R.mipmap.ic_launcher).into(imageView);
        } else {
            Picasso.with(getApplicationContext()).load(imgurl).into(imageView);
        }
        if (userMetadata.get("first_name").toString().equals(""))
            name.setText(userMetadata.get("nickname").toUpperCase() + "");
        else {
            name.setText(userMetadata.get("first_name").toUpperCase() + " " + userMetadata.get("last_name").toUpperCase());
        }

        if (userMetadata.get("description").toString().equals("")) {
            description.setVisibility(View.INVISIBLE);
        } else {
            description.setText("" + userMetadata.get("description"));
        }

        if ((data = userMetadata.get("user_location")).equals("")) {
            location.setVisibility(View.INVISIBLE);
        } else {
            location.setText(data);
        }

        if (userMetadata.get("show_email").equals("off")) {
            email.setText("Email is hidden");
        } else {
            email.setText(myemail.toString());
        }

        if ((data = userMetadata.get("user_facebook")).equals("")) {
            link_fb.setVisibility(View.INVISIBLE);
        } else {
            link_fb.setText(data);
        }
        if ((data = userMetadata.get("user_gplus")).equals("")) {
            link_gplus.setVisibility(View.INVISIBLE);
        } else {
            link_gplus.setText(data);
        }
        if ((data = userMetadata.get("user_twitter")).equals("")) {
            link_twitter.setVisibility(View.INVISIBLE);
        } else {
            link_twitter.setText(data);
        }
        if ((data = userMetadata.get("et_question_count")).equals("")) {
            question_count.setVisibility(View.INVISIBLE);
        } else {
            question_count.setText(data);
        }
        if ((data = userMetadata.get("et_answer_count")).equals("")) {
            answer_count.setVisibility(View.INVISIBLE);
        } else {
            answer_count.setText(data);
        }
        if(userMetadata.get("qa_point").equals("")){
            pointsText.setText("0");
        }else{
            pointsText.setText(userMetadata.get("qa_point")+"");
        }


    }

    public void social(View view) {
        TextView txt = (TextView) view;
        String url = txt.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void sendEmail(View view) {
        TextView txt = (TextView) view;
        if (txt.getText().toString().equals("Email is hidden")) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, txt.getText().toString());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello I'm a Wisemonkeys member");
        intent.putExtra(Intent.EXTRA_TEXT, "");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
