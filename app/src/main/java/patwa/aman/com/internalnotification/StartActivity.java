package patwa.aman.com.internalnotification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {

    String  tag_string_req = "string_req";
    RecyclerView rv;

    String url = "https://ops.coutloot.com/internalApp/login";
    String email,deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        rv = (RecyclerView)findViewById(R.id.rv);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        int success = pref.getInt("success", 9);
        email = pref.getString("email",null);
        deviceToken = pref.getString("deviceToken",null);

        System.out.println("email"+email);
        System.out.println("deviceToken:"+deviceToken);

        System.out.println("success:"+success);

        if(success != 1){
            Intent i= new Intent(StartActivity.this,MainActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            email = pref.getString("email",null);
            deviceToken = pref.getString("deviceToken",null);

            System.out.println("email"+email);
            System.out.println("deviceToken:"+deviceToken);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                                                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                            Log.v("Response",response.toString());

                            rv.setLayoutManager(new LinearLayoutManager(StartActivity.this));
                            rv.setHasFixedSize(true);

                            RecyclerAdapter adapter = new RecyclerAdapter(this);

                            rv.setAdapter(adapter);


//                            try {
//                                JSONObject jsonObject=new JSONObject(response);
//                                int s=jsonObject.getInt("success");
//                                Log.v("success",s+"");
//                                if(s==1){
//                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                                    SharedPreferences.Editor editor = pref.edit();
//
//                                    editor.putInt("success",s);
//                                    editor.putString("email",e);
//                                    editor.putString("deviceToken",msg);
//
//
//                                }
//
//                            } catch (JSONException e1) {
//                                e1.printStackTrace();
//                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(StartActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
//                                            params.put(KEY_USERNAME,username);
                    params.put("deviceToken",deviceToken);
                    params.put("email", email);
                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
        }



    }
}
