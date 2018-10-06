package in.wisemonkeys.android.wisemonkeys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.wisemonkeys.android.wisemonkeys.app.AppController;
import in.wisemonkeys.android.wisemonkeys.modelslogin.RootObject;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;
    EditText musername, mpassword;
    Button btn_signin;
    TextView tv_signup, wait;
    ProgressBar progressBar;
    RootObject rootObject;
    GoogleApiClient mGoogleApiClient;
    SessionManager session;
    RelativeLayout relativeLayout;
    int setid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
        relativeLayout = (RelativeLayout) findViewById(R.id.loginRelative);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        musername = (EditText) findViewById(R.id.input_email);
        mpassword = (EditText) findViewById(R.id.input_password);
        tv_signup = (TextView) findViewById(R.id.link_signup);
        wait = (TextView) findViewById(R.id.wait);
        wait.setVisibility(View.INVISIBLE);
        btn_signin = (Button) findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.loginRequestLoadingProgressbar);
        progressBar.setVisibility(View.INVISIBLE);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkpoints = true;
                String username = musername.getText().toString().trim();
                String password = mpassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                    checkpoints = false;
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                    checkpoints = false;
                    return;
                }
                if (checkpoints) {
                    new BackgroundWorker().execute("login", username, password);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String display_name = acct.getDisplayName().toString();
            String email= acct.getEmail();
            String image_url=acct.getPhotoUrl().toString();
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
            doLogin(getString(R.string.doLogin),email, image_url, display_name);
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogin) {

        if (isLogin) {


        } else {
            /*Snackbar snackbar = Snackbar.make(relativeLayout, "Not Registered User Please First do ", Snackbar.LENGTH_SHORT).setAction("SignUp", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                }
            });
            snackbar.show();*/
        }


    }

    private void doLogin(String url, final String email,final String image_url,final String display_name) {

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
                                     setid = exploreObject.getInt("ID");
                                    //userMetadata.put(key, value);
                                }
                                //loadFetchedData();
                                session.createLoginSession(setid, email);

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //intent.putExtra("rootObject", (Parcelable) rootObject);
                                startActivity(intent);
                                finish();


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
                params.put("email",email );
                params.put("image_url",image_url );
                params.put("display_name",display_name );
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(Stringreq);

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signup(View view) {
        startActivity(new Intent(this, SignupActivity.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context = getApplicationContext();
        AlertDialog alert;


        @Override
        protected String doInBackground(String... params) {
            String type = params[0];

            String username = params[1];
            String password = params[2];

            String load_url = getString(R.string.wisemonkeys_auth);
            load_url += "username=" + username + "&password=" + password;
            if (type.equals("login")) {
                HttpHandler handler = new HttpHandler();

                String stream = handler.makeServiceCall(load_url);

                return stream;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Loading..!", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);
            wait.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {

            progressBar.setVisibility(View.INVISIBLE);
            wait.setVisibility(View.INVISIBLE);
            Gson login = new Gson();
            rootObject = login.fromJson(s, RootObject.class);
            if (rootObject.getStatus().equals("error")) {
                Toast.makeText(getApplicationContext(), "Invalid Login or Passord", Toast.LENGTH_LONG).show();
            } else {

                int id = rootObject.getUser().getId();
                String email = rootObject.getUser().getEmail();
                session.createLoginSession(id, email);

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("rootObject", (Parcelable) rootObject);
                startActivity(intent);
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
        new ExitDialogFragment().show(getSupportFragmentManager(), null);
        //super.onBackPressed();
    }
}
