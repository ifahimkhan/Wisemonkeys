package in.wisemonkeys.android.wisemonkeys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignupActivity extends AppCompatActivity {

    EditText editText_username, editText_email, editText_displayname,
            editText_password, editText_confirm;
    ProgressBar progressBar;

    String mynonce = "", nonce_status;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        new GetNonce().execute();


    }

    /*private void makeNonceRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://wisemonkeys.in/api/get_nonce/?controller=user&method=register", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                /*Gson gson = new Gson();
                nonce = gson.fromJson(response.toString(), Nonce.class);

                try {
                    nonce_status=response.getString("status");
                    mynonce = response.getString("nonce");
                    Toast.makeText(getApplicationContext(),mynonce,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Nonce Request Failed", Toast.LENGTH_SHORT).show();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }*/

    private void init() {
        editText_email = (EditText) findViewById(R.id.input_email);
        editText_username = (EditText) findViewById(R.id.input_username);
        editText_displayname = (EditText) findViewById(R.id.input_displayname);
        editText_password = (EditText) findViewById(R.id.input_password);
        editText_confirm = (EditText) findViewById(R.id.confirm_password);
        progressBar = (ProgressBar) findViewById(R.id.loginRequestLoadingProgressbar);

    }

    public void signin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void signup(View view) throws UnsupportedEncodingException {
        boolean checkpoints = true;

        String email = editText_email.getText().toString().trim();
        String username = editText_username.getText().toString().trim();
        String displayname = editText_displayname.getText().toString().trim();
        String password = editText_password.getText().toString().trim();
        String confirm_password = editText_confirm.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email Address", Toast.LENGTH_SHORT).show();
            checkpoints = false;
            return;
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
            checkpoints = false;
            return;
        }
        if (TextUtils.isEmpty(displayname)) {
            Toast.makeText(this, "Enter Display Name", Toast.LENGTH_SHORT).show();
            checkpoints = false;
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            checkpoints = false;
            return;
        }
        if (TextUtils.isEmpty(confirm_password)) {
            Toast.makeText(this, "Enter confirm Password", Toast.LENGTH_SHORT).show();
            checkpoints = false;
            return;
        }
        if (!confirm_password.equals(password)) {
            Toast.makeText(this, "Password does not Match!", Toast.LENGTH_SHORT).show();
            checkpoints = false;
            return;
        }

        if (checkpoints) {
            //makeNonceRequest();

            if (nonce_status.equals("ok")) {
                url = getString(R.string.wisemonkeys_signup) + URLEncoder.encode(username, "UTF-8")
                        + "&email=" + email
                        + "&nonce=" + mynonce
                        + "&display_name=" + URLEncoder.encode(displayname, "UTF-8")
                        + "&notify=both&user_pass=" + URLEncoder.encode(password, "UTF-8");
                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
                new GetSignup().execute();

            }


        }


    }

    private void letsSingUp(String url) {
        /*JsonObjectRequest onsignupRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("error")) {
                        String error = response.getString("error").toString();
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Success! \n please verify your email link has been sent ", Toast.LENGTH_LONG).show();
                        //new LoginActivity.BackgroundWorker("login",email,password);
                        //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Signup Request Failed", Toast.LENGTH_SHORT).show();

            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        AppController.getInstance().addToRequestQueue(onsignupRequest);

*/
    }


    class GetNonce extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String stream = handler.makeServiceCall("http://wisemonkeys.in/api/get_nonce/?controller=user&method=register");
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String nonce = jsonObject.getString("nonce");
                mynonce = nonce;
                nonce_status = status;
                Toast.makeText(getApplicationContext(), nonce + status, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetSignup extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String stream = handler.makeServiceCall(url);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                if (status.equals("error")) {
                    String error = jsonObject.getString("error");
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration Success! please verify your email link has been sent ", Toast.LENGTH_LONG).show();
                    //new LoginActivity.BackgroundWorker("login",email,password);
                    //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                progressBar.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
