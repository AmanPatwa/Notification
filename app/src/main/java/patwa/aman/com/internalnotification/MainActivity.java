package patwa.aman.com.internalnotification;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText password;
    AutoCompleteTextView email;
    Button btn,register;

    String msg;
//    DatabaseReference notiData;
//    FirebaseRecyclerOptions<NotificationModel> options;
//    FirebaseRecyclerAdapter<NotificationModel,NotificationAdapter> adapter;

    String  tag_string_req = "string_req";

    String url = "https://ops.coutloot.com/internalApp/login";

    ProgressDialog pDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        btn = (Button)findViewById(R.id.email_sign_in_button);
        register = (Button)findViewById(R.id.email_register_button);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        if (task.getResult() != null){
                            msg = task.getResult().getToken();

                        }else {
                            Log.v("MainToken", "result is null");
                        }

                        btn.setOnClickListener(new View.OnClickListener() {


                            @Override
                            public void onClick(View view) {
                                pDialog = new ProgressDialog(MainActivity.this);
                                pDialog.setMessage("Loading...");
                                pDialog.show();

                                final String e = email.getText().toString();
                                final String p = password.getText().toString();

                                Log.v("email",e+"");
                                Log.v("pass",p+"");

                                if(TextUtils.isEmpty(e) || TextUtils.isEmpty(p)){
                                    Toast.makeText(MainActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
//                                                    Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                                                    Log.v("Response",response.toString());
                                                    try {
                                                        JSONObject jsonObject=new JSONObject(response);
                                                        int s=jsonObject.getInt("success");
                                                        Log.v("success",s+"");
                                                        if(s==1){
                                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                                            SharedPreferences.Editor editor = pref.edit();

                                                            editor.putInt("success",s);
                                                            editor.putString("email",e);
                                                            editor.putString("deviceToken",msg);
                                                            editor.commit();
                                                            pDialog.dismiss();
                                                            Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
                                                            startActivity(startIntent);
                                                        }

                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }

                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                                    pDialog.dismiss();
                                                }
                                            }){
                                        @Override
                                        protected Map<String,String> getParams(){
                                            Map<String,String> params = new HashMap<String, String>();
//                                            params.put(KEY_USERNAME,username);
                                            params.put("password",p);
                                            params.put("email", e);
                                            params.put("deviceToken",msg);
                                            return params;
                                        }

                                    };

                                    AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
                                }
                            }
                        });


//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });


//        String phoneNo = getIntent().getStringExtra("phoneno");
//        String text = getIntent().getStringExtra("text");
//        String deviceToken = getIntent().getStringExtra("deviceToken");

//        Log.v("phoneMain",phoneNo+"");
//        Log.v("textMain",text+"");

//        Log.d("DeviceTokenMain",deviceToken);
//        Log.v("TokenMain",msg + "aaa");



//        if(phoneNo!=null && text!=null && deviceToken!=null){
//            if(deviceToken.equals(token)){
//                final HashMap<String,String> data = new HashMap<>();
//                data.put("number",phoneNo);
//                data.put("mess",text);
//
//
//                notiData.child(deviceToken).push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Log.v("Successfully added db",data+"");
//                    }
//                });
//
//
//                options=new FirebaseRecyclerOptions.Builder<NotificationModel>()
//                        .setQuery(notiData, NotificationModel.class)
//                        .build();
//
//                adapter = new FirebaseRecyclerAdapter<NotificationModel, NotificationAdapter>(options) {
//                    @Override
//                    protected void onBindViewHolder(NotificationAdapter holder, int position, NotificationModel model) {
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public NotificationAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                        View view= (View) LayoutInflater.from(MainActivity.this).inflate(R.layout.notificationlayout,viewGroup,false);
//                        NotificationAdapter viewHolder=new NotificationAdapter(view,MainActivity.this);
//                        return viewHolder;
//                    }
//                };
//            }
//        }



////        String[] imageUrl = getIntent().getStringArrayExtra("imageUrl");
//
//        Log.v("PhoneNo", phoneNo + "");
//        Log.v("Text", text + "");
////        Log.v("imageUrl",imageUrl + "");
//
////        PackageManager pm=getPackageManager();
////        PackageInfo info= null;
////        PackageInfo pk = null;
////        try {
////            info = pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_META_DATA);
////            pk = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
////        } catch (PackageManager.NameNotFoundException e) {
////            e.printStackTrace();
////        }
//
//        if (phoneNo != null && text != null) {
//            Uri uri = Uri.parse("https://wa.me/<" + phoneNo + ">/?text=" + text );
//            Intent i = new Intent(Intent.ACTION_VIEW, uri);
//            i.putExtra(Intent.EXTRA_TEXT, text);
//            i.setPackage("com.whatsapp"); //For whatsapp business use "com.whatsapp.w4b"  && For normal whatsapp use "com.whatsapp" as a package name.
//            startActivity(Intent.createChooser(i, "Share with"));
//
//        }
    }

}
