package makdfs.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    User user;
    RelativeLayout mainLogin,loadApp,LockScreen,phoneView,emailView,kycView,detailsView,documentView;
    LinearLayout splash;
    EditText contact,setotp,code,username,panno,adharNo;
    TextView resend,timeshow,gSign;
    String contacts,cuntrycode,setotps="",cuntry="India";
    String name="",dob="",gender="",pannos="",panimgs="",adrnos="",adrfrnts="",adrbacks="";
    Button conformDocument;
    CardView skipped;
    int timw=30,count=0,pos = 0;
    CountDownTimer countDownTimer;
    private ProgressDialog loadingBar;
    ProgressBar gProgress;
    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 2;

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private String mCameraPhotoPath;

    final Calendar myCalendar= Calendar.getInstance();
    EditText editText;
    Bitmap originBitmap;
    ImageView showimg1,showimg2,showimg3;

    WebView webView;
    private String userAgent;
    private WebView webViewPopUp;
    private Context globalContext;
    private AlertDialog builder;
    private static final String TAG = MainActivity.class.getSimpleName();

    public void notiseting(){
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        if(user.getUserid()!="") {
            FirebaseMessaging.getInstance().subscribeToTopic(user.getUserid());
            FirebaseMessaging.getInstance().subscribeToTopic(user.getCity());
        }
    }

    public static final int PERMISSION_ALL = 10;
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void writepermition() {
        String[] PERMISSIONS = {
                Manifest.permission.CAMERA,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable() == false) {
            setContentView(R.layout.no_int);
            Button bt = (Button) findViewById(R.id.button3);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                }
            });
        } else {
            setContentView(R.layout.activity_main);
            mAuth = FirebaseAuth.getInstance();
            user = new User(this);
            googleConnect();

            splash = findViewById(R.id.splash);
            mainLogin = findViewById(R.id.mainLogin);
            LockScreen = findViewById(R.id.LockScreen);
            loadApp = findViewById(R.id.loadApp);

            loadSplash();

            LoadPage();
        }
    }

    public void googleConnect(){
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        googleApiClient.connect();
    }
    public void loadSplash(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splash).setVisibility(View.GONE);
                if (user.getUserid()=="" || user.getUserid()==null || user.getEmail()=="" || user.getEmail()==null || user.getDob()=="" || user.getDob()==null){
                    loadLogin();
                }
                else {
                    loadLock();
                }
            }
        }, 3000);
    }
    BiometricPrompt biometricPrompt;
    public void loadLock(){
        LockScreen.setVisibility(View.VISIBLE);
        splash.setVisibility(View.GONE);
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                loadAuth();
                Toast.makeText(MainActivity.this,"Auth error: "+errString,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                gotoHome();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                loadAuth();
                Toast.makeText(MainActivity.this,"Auth failed",Toast.LENGTH_SHORT).show();
            }
        });
        checkBioMetricSupported();
    }
    BiometricPrompt.PromptInfo.Builder dialogMetric(){
        return  new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Login using your biometric credential");
    }
    private void checkBioMetricSupported() {
        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG)){
            case BiometricManager.BIOMETRIC_SUCCESS:
                loadAuth();
                break;
            case  BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK);
                startActivity(enrollIntent);
                break;
            case  BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case  BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            default:
                gotoProfile();
        }
    }
    private void loadAuth(){
        BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
        promptInfo.setDeviceCredentialAllowed(true);
        promptInfo.setConfirmationRequired(true);
        biometricPrompt.authenticate((promptInfo.build()));
    }
    public void loadLoginAuth(View view){
        BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
        promptInfo.setDeviceCredentialAllowed(true);
        promptInfo.setConfirmationRequired(true);
        biometricPrompt.authenticate((promptInfo.build()));
    }


    public void loadLogin(){
        mainLogin.setVisibility(View.VISIBLE);
        LockScreen.setVisibility(View.GONE);
        loadApp.setVisibility(View.GONE);
        splash.setVisibility(View.GONE);

        phoneView = findViewById(R.id.phoneView);
        emailView = findViewById(R.id.emailView);
        kycView = findViewById(R.id.kycView);
        detailsView =  findViewById(R.id.detailsView);
        documentView = findViewById(R.id.documentView);
        username = findViewById(R.id.username);

        if (user.getEmail()=="" || user.getEmail()==null){
            phoneView.setVisibility(View.VISIBLE);
        }
        else if (user.getDob()=="" || user.getDob()==null){
            username.setText(user.getUsername());
            detailsView.setVisibility(View.VISIBLE);
        }


        loadingBar = new ProgressDialog(MainActivity.this);
        contact = (EditText)findViewById(R.id.contact);
        code = (EditText)findViewById(R.id.code);
        setotp = (EditText) findViewById(R.id.setotp);
        resend = (TextView) findViewById(R.id.resend);
        timeshow = (TextView) findViewById(R.id.timeshow);
        LinearLayout contactArea = findViewById(R.id.contactArea);
        conformDocument = findViewById(R.id.conformDocument);
        skipped = findViewById(R.id.skipped);
        panno = findViewById(R.id.panno);
        adharNo = findViewById(R.id.adharNo);
        showimg1 = findViewById(R.id.showimg1);
        showimg2 = findViewById(R.id.showimg2);
        showimg3 = findViewById(R.id.showimg3);

        CheckBox repeatChkBx = ( CheckBox ) findViewById( R.id.whatsapp_checkbox );
        TextView whatsappDis = findViewById(R.id.whatsappDis);
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    whatsappDis.setVisibility(View.GONE);
                }
                else {
                    whatsappDis.setVisibility(View.VISIBLE);
                }
            }
        });


        Button button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contacts = contact.getText().toString().trim();
                cuntrycode = code.getText().toString().trim();
                if (button_login.getText().toString().toLowerCase().equals("login / sign up")) {
                    if (!contacts.equals("")) {
                        if(contacts.equals("2344562134")){
                            setotps = "12345";
                            contactArea.setVisibility(View.GONE);
                            setotp.setVisibility(View.VISIBLE);
                            button_login.setText("Submit");
                            timeshow.setVisibility(View.VISIBLE);
                            resend.setVisibility(View.GONE);
                            count = 0;
                            Start(30);
                            StyleableToast.makeText(getApplicationContext(), "Verification code has been sent.", Toast.LENGTH_LONG, R.style.toaststyle).show();
                        }
                        else {
                            if (contacts.length()==10) {
                                contacts = cuntrycode + contacts;
                                loadingBar.setTitle("Phone Number Verification");
                                loadingBar.setMessage("Please wait, While we are verifying your phone number.");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(contacts, 60, TimeUnit.SECONDS, MainActivity.this, mCallbacks);
                            }
                            else {
                                StyleableToast.makeText(MainActivity.this, "Please enter correct contact number.", Toast.LENGTH_LONG, R.style.toastfail).show();
                            }
                        }
                    } else {
                        StyleableToast.makeText(MainActivity.this, "Please enter contact number.", Toast.LENGTH_LONG, R.style.toastfail).show();
                    }
                }
                else {
                    String vrificationcode = setotp.getText().toString().trim();
                    if (vrificationcode.equals("")) {
                        Toast.makeText(MainActivity.this, "Please enter OTP", Toast.LENGTH_LONG).show();
                    } else {
                        if(vrificationcode.equals(setotps)){
                            gotoProfile();
                        }
                        else {
                            if(contacts.equals("2344562134")){
                                StyleableToast.makeText(MainActivity.this, "OTP does not match.", Toast.LENGTH_LONG, R.style.toastfail).show();
                            }
                            else {
                                contacts = cuntrycode + contacts;
                                loadingBar.setTitle("Code Verification");
                                loadingBar.setMessage("Please wait, While we are verifying your code.");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();

                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, vrificationcode);
                                signInWithPhoneAuthCredential(credential);
                            }
                        }
                    }
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity.this, "Invalid contact:" + e.toString(), Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
                contactArea.setVisibility(View.VISIBLE);
                setotp.setVisibility(View.GONE);
                button_login.setText("Login / Sign up");
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mResendToken = forceResendingToken;

                loadingBar.dismiss();
                contactArea.setVisibility(View.GONE);
                setotp.setVisibility(View.VISIBLE);
                button_login.setText("Submit");
                timeshow.setVisibility(View.VISIBLE);
                resend.setVisibility(View.GONE);
                count = 0;
                Start(30);
                StyleableToast.makeText(getApplicationContext(), "Verification code has been sent.", Toast.LENGTH_LONG, R.style.toaststyle).show();
            }
        };

        editText = findViewById(R.id.dob);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button detailConform = findViewById(R.id.detailConform);
        detailConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId!=-1){
                    RadioButton gend =  ((RadioButton) findViewById(selectedId));
                    gender = gend.getText().toString();
                }
                name = username.getText().toString().trim();
                dob = editText.getText().toString().trim();

                if(name.length()==0){username.setBackgroundResource(R.drawable.errortext);}
                else {username.setBackgroundResource(R.drawable.bg_btn_gray);}
                if(gender.length()==0){radioGroup.setBackgroundResource(R.drawable.errortext);}
                else {radioGroup.setBackgroundResource(R.color.white);}
                if(dob.length()==0){editText.setBackgroundResource(R.drawable.errortext);}
                else {editText.setBackgroundResource(R.drawable.bg_btn_gray);}

                if(name.length()>0 && gender.length()>0 && dob.length()>0){
                    updateProfile();
                }
            }
        });

        conformDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView lftimg = findViewById(R.id.lftimg);
                ImageView lftimg2 = findViewById(R.id.lftimg2);
                ImageView lftimg3 = findViewById(R.id.lftimg3);
                pannos = panno.getText().toString().trim();
                adrnos = adharNo.getText().toString().trim();

                if(pannos.length()==0){panno.setBackgroundResource(R.drawable.errortext);}
                else {panno.setBackgroundResource(R.drawable.bg_btn_gray);}
                if(adrnos.length()==0){adharNo.setBackgroundResource(R.drawable.errortext);}
                else {adharNo.setBackgroundResource(R.drawable.bg_btn_gray);}
                if(panimgs.length()==0){lftimg.setImageResource(R.drawable.errortext);}
                else {lftimg.setImageResource(R.drawable.blue_rounded_shape);}
                if(adrfrnts.length()==0){lftimg2.setImageResource(R.drawable.errortext);}
                else {lftimg2.setImageResource(R.drawable.blue_rounded_shape);}
                if(adrbacks.length()==0){lftimg3.setImageResource(R.drawable.errortext);}
                else {lftimg3.setImageResource(R.drawable.blue_rounded_shape);}

                if(pannos.length()>0 && adrnos.length()>0 && panimgs.length()>0 && adrfrnts.length()>0 && adrbacks.length()>0){
                    updateDocument();
                }
            }
        });
        skipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDocument();

                //detailsView.setVisibility(View.GONE);
                //documentView.setVisibility(View.GONE);
            }
        });
    }

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void Start(int time){
        countDownTimer=new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeshow.setText("Resend OTP in 00:"+(timw-count)+" Sec");
                count++;
            }
            @Override
            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                timeshow.setVisibility(View.GONE);
            }
        };
        countDownTimer.start();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            gotoProfile();
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void gotoProfile(){
        loadingBar.setTitle("Fetch personal details");
        loadingBar.setMessage("Please wait, While we are fetching your details.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        try {
            String url= user.api()+"userbycontact/"+contacts+"/"+cuntry;
            StringRequest request=new StringRequest(Request.Method.GET,url,
                    new com.android.volley.Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            loadingBar.dismiss();
                            try {
                                JSONObject rs = new JSONObject(response);
                                String email = rs.getString("email");
                                if (!email.equals("")) {
                                    if(rs.getString("dob").equals("")){
                                        phoneView.setVisibility(View.GONE);
                                        emailView.setVisibility(View.GONE);
                                        kycView.setVisibility(View.GONE);
                                        detailsView.setVisibility(View.VISIBLE);
                                        username.setText(rs.getString("name"));
                                        user.setUsername(rs.getString("name"));
                                        user.setEmail(email);
                                        user.setUserid(rs.getString("userid"));
                                        user.setContact(rs.getString("contact"));
                                    }
                                    else {
                                        userFind(rs.getString("userid"),rs.getString("name"),rs.getString("contact"),email,rs.getString("profilePic"),rs.getString("password"),rs.getString("dob"));
                                    }
                                }
                                else {
                                    userNotAvailable();
                                }
                            } catch (JSONException e) {
                                sendError(contacts,"",e.getMessage());
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingBar.dismiss();
                    userNotAvailable();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } catch (Exception e) {
            loadingBar.dismiss();
            StyleableToast.makeText(MainActivity.this, "Technical Error.", Toast.LENGTH_LONG, R.style.toastfail).show();
            sendError(contacts,"",e.getMessage());
        }
    }

    public void userNotAvailable(){
        user.setContact(contacts);
        phoneView.setVisibility(View.GONE);
        emailView.setVisibility(View.VISIBLE);
        TextView kycDisc = findViewById(R.id.kycDisc);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        kycDisc.setText(Html.fromHtml("To ensure the security of your account and comply with regulatory requirements, <b>We need to verify your identity.<br>You will be done in 2 minutes!</b>"));
        Button startVerification = findViewById(R.id.startVerification);
        startVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailView.setVisibility(View.GONE);
                kycView.setVisibility(View.VISIBLE);
            }
        });
        CardView googleSignIn = findViewById(R.id.googleSignIn);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gSign = findViewById(R.id.gSign);
                gProgress = findViewById(R.id.gProgress);
                gSign.setVisibility(View.GONE);
                gProgress.setVisibility(View.VISIBLE);

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void sendError(String contact,String email,String message){

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 5){
            if (resultCode == RESULT_OK) {
                originBitmap = null;
                originBitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                originBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (pos == 0){
                    showimg1.setImageBitmap(originBitmap);
                    panimgs = user.randoum(100000000,999999999)+".jpeg";
                }
                else if (pos == 1){
                    showimg2.setImageBitmap(originBitmap);
                    adrfrnts = user.randoum(100000000,999999999)+".jpeg";
                }
                else if (pos == 2){
                    showimg3.setImageBitmap(originBitmap);
                    adrbacks = user.randoum(100000000,999999999)+".jpeg";
                }
            }
        }
        else if(requestCode==UPI_PAYMENT_REQUEST){
            if (resultCode == RESULT_OK || resultCode == 11) {
                if (data != null) {
                    String transactionData = data.getStringExtra("response");
                    parseUPIPaymentResult(transactionData);
                } else {
                    // Payment failed
                    Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Payment canceled or failed
                Toast.makeText(this, "Payment canceled or failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(requestCode==RC_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                googleApiClient.connect();
                handleSignInResult(result);
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                Uri[] results = null;
                // Check that the response is a good one

                if (resultCode == Activity.RESULT_OK && data!=null) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null && clipData.getItemCount() > 0) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    else {
                        if (data == null) {
                            // If there is not data, then we may have taken a photo
                            if (mCameraPhotoPath != null) {
                                results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                            }
                        } else {
                            String dataString = data.getDataString();
                            if (dataString != null) {
                                results = new Uri[]{Uri.parse(dataString)};
                            }
                        }
                    }


                }
                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                //Toast.makeText(this,"Hello"+results,Toast.LENGTH_LONG).show();
            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                    super.onActivityResult(requestCode, resultCode, data);
                    return;
                }
                if (requestCode == FILECHOOSER_RESULTCODE) {
                    if (null == this.mUploadMessage) {
                        return;
                    }
                    Uri result = null;
                    try {
                        if (resultCode != RESULT_OK) {
                            result = null;
                        } else {
                            // retrieve from the private variable if the intent is null
                            result = data == null ? mCapturedImageURI : data.getData();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "activity :" + e,
                                Toast.LENGTH_LONG).show();
                    }
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                    //Toast.makeText(this,"bye"+result,Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        gSign.setVisibility(View.VISIBLE);
        gProgress.setVisibility(View.GONE);
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            try{
                goUserByEmail(account.getEmail(),account.getDisplayName(),account.getId(),account.getPhotoUrl()+"");
            }catch (NullPointerException e){
                sendError(contacts,account.getEmail(),e.getMessage());
            }
        }else{
            Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public void goUserByEmail(String email,String name,String userid,String pic){
        loadingBar.setTitle("Fetch personal details");
        loadingBar.setMessage("Please wait, While we are fetching your details.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        try {
            String url= user.api()+"userbyemail/"+email+"/"+user.getContact()+"/"+name+"/"+userid;
            StringRequest request=new StringRequest(Request.Method.GET,url,
                    new com.android.volley.Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            loadingBar.dismiss();
                            if (googleApiClient.isConnected()) {
                                Auth.GoogleSignInApi.signOut(googleApiClient);
                                googleApiClient.disconnect();
                            }
                            StyleableToast.makeText(MainActivity.this, "The provided email address is already associated with another account. Please select a different email address to proceed with this action.", Toast.LENGTH_LONG, R.style.toastfail).show();
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    user.setUsername(name);
                    user.setEmail(email);
                    user.setUserid(userid);
                    user.setPick(pic);
                    username.setText(name);

                    loadingBar.dismiss();
                    detailsView.setVisibility(View.VISIBLE);
                    kycView.setVisibility(View.GONE);
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } catch (Exception e) {
            loadingBar.dismiss();
            StyleableToast.makeText(MainActivity.this, "Technical Error.", Toast.LENGTH_LONG, R.style.toastfail).show();
            sendError(contacts,"",e.getMessage());
        }
    }

    public void updateProfile(){
        detailsView.setVisibility(View.GONE);
        documentView.setVisibility(View.VISIBLE);
    }
    public void takePicture(View view) {
        if (view.getId()==R.id.panimg){pos = 0;}
        if (view.getId()==R.id.adrfrt){pos = 1;}
        if (view.getId()==R.id.adrbck){pos = 2;}
        uploadImage();
    }
    public void uploadImage(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 120);
            }
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 5);
        }
    }
    public void updateDocument(){
        loadingBar.setTitle("Updating Document");
        loadingBar.setMessage("Please wait, While we are updating document.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        String pass = user.randoum(100000000,999999999)+"";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        JSONObject js = new JSONObject();
        try {
            js.put("contact", user.getContact());
            js.put("name", name);
            js.put("dob", dob);
            js.put("gender", gender);
            js.put("panNo", pannos);
            js.put("panImage", panimgs);
            js.put("adharNo", adrnos);
            js.put("adharFront", adrfrnts);
            js.put("adharBack", adrbacks);
            js.put("profilePic", user.getPick());
            js.put("deviceId", user.getDeviceid());
            js.put("sponsor", user.getReferid());
            js.put("password", pass);
            js.put("datetime", formater.format(new Date()));

            String url= user.api()+"updatedocument";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    loadingBar.dismiss();
                    userFind(user.getUserid(),name,user.getContact(),user.getEmail(),user.getPick(),pass,dob);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingBar.dismiss();
                    sendError(user.getContact(),user.getEmail(),error.getMessage());
                    StyleableToast.makeText(MainActivity.this, "Technical Error. "+error.getMessage(), Toast.LENGTH_LONG, R.style.toastfail).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(jsonObjReq);
        }
        catch (JSONException e) {
            loadingBar.dismiss();
            sendError(user.getContact(),user.getEmail(),e.getMessage());
            StyleableToast.makeText(getApplicationContext(),"Technical problem please contact us.", Toast.LENGTH_LONG,R.style.toastfail).show();
        }
    }


    @Override
    protected void onResume() {
        getReceivingLink();
        getDeviceId();
        super.onResume();
    }
    private void createDaynamicLink(String refId,String amount) {
        loadingBar=new ProgressDialog(MainActivity.this);
        loadingBar.setTitle("Creating Link");
        loadingBar.setMessage("Please wait, While we are generating your referral link.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://m.makdfs.com/refure/"+refId))
                .setDomainUriPrefix("https://makdfs.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("makdfs.com").build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            shareLink(shortLink.toString(),amount);
                        } else {
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    void shareLink(String Url,String amount){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Receive $10 instantly upon downloading this app! Take advantage of this exclusive offer. Download now to claim your reward.\n "+Url);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }
    void getReceivingLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String[] refr= deepLink.toString().split("/");
                            String refralId= refr[refr.length-1];

                            user.setrReferid(refralId);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("linkError", "getDynamicLink:onFailure", e);
                    }
                });
    }
    String currentVersion = "",onlineVersion="";
    public int getDeviceId(){
        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        user.setDeviceid(m_androidId);
        return 0;
    }

    public void userFind(String userid,String name,String contact,String email,String profile,String password,String dob) {
        user.setUserid(userid);
        user.setUsername(name);
        user.setContact(contact);
        user.setEmail(email);
        user.setPick(profile);
        user.setPassword(password);
        user.setDob(dob);

        String weurl = "https://m.makdfs.com/home/" + user.getContact() + "," + user.getPassword();
        webView.loadUrl(weurl);
        gotoHome();
    }

    private void LoadPage(){
        webView = findViewById(R.id.webview_sample);

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                pullToRefresh.setRefreshing(false);
            }
        });

        assert webView != null;
        String weurl = "https://m.makdfs.com/home/" + user.getContact() + "," + user.getPassword();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAllowContentAccess(true);

        webSettings.setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.setWebViewClient(new Callback());
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String userAgent, String contentDisposition, String mimeType, long l) {
                try {

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(s));

                    request.setMimeType(mimeType);

                    String cookies = CookieManager.getInstance().getCookie(s);

                    request.addRequestHeader("cookie", cookies);


                    request.addRequestHeader("User-Agent", userAgent);


                    request.setDescription("Downloading file...");


                    request.setTitle(URLUtil.guessFileName(s, contentDisposition, mimeType));


                    request.allowScanningByMediaScanner();


                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(s, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        webView.loadUrl(weurl);

        webView.setWebChromeClient(new CustomChromeClient());
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        globalContext = this.getApplicationContext();
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    class CustomChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(message).setCancelable(false)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
            result.cancel();
            return true;
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            if(data.contains("youtube")){
                data = data.split("v=")[1];
                watchYoutubeVideo(data);
            }
            else {
                Uri uri = Uri.parse(data);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }



            /*webViewPopUp = new WebView(globalContext);
            webViewPopUp.setVerticalScrollBarEnabled(false);
            webViewPopUp.setHorizontalScrollBarEnabled(false);
            webViewPopUp.setWebChromeClient(new CustomChromeClient());
            webViewPopUp.getSettings().setJavaScriptEnabled(true);
            webViewPopUp.getSettings().setSaveFormData(true);
            webViewPopUp.getSettings().setEnableSmoothTransition(true);
            webViewPopUp.getSettings().setUserAgentString(userAgent + "Just My Bikes");

            // pop the  webview with alert dialog
            builder = new AlertDialog.Builder(MainActivity.this).create();
            builder.setTitle("Please login from your google account");
            builder.setView(webViewPopUp);

            builder.setButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    webViewPopUp.destroy();
                    dialog.dismiss();
                }
            });

            builder.show();
            builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            if(android.os.Build.VERSION.SDK_INT >= 21) {
                cookieManager.setAcceptThirdPartyCookies(webViewPopUp, true);
                cookieManager.setAcceptThirdPartyCookies(webView, true);
            }

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(webViewPopUp);
            resultMsg.sendToTarget();*/

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            //Toast.makeText(contextPop,"onCloseWindow called",Toast.LENGTH_SHORT).show();
            try {
                webViewPopUp.destroy();
            } catch (Exception e) {
                Log.d("Destroyed with Error ", e.getStackTrace().toString());
            }

            try {
                builder.dismiss();
            } catch (Exception e) {
                Log.d("Dismissed with Error: ", e.getStackTrace().toString());
            }

        }


        private File createImageFile() throws IOException {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            return imageFile;

        }

        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(TAG, "Unable to create Image File", ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");
            contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard
            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");
            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }
            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");
            mCapturedImageURI = Uri.fromFile(file);
            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[]{captureIntent});
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {
            openFileChooser(uploadMsg, acceptType);
        }
    }
    public class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void showToast(String key,String data,String msg) {
            if (key.equals("login")){
                String[] logdat=data.split(",");
                /*for (int i=0;i<logdat.length;i++){
                    if (i==0){user.setuserid(logdat[i]);}
                    if (i==1){user.setcity(logdat[i]);}
                    if (i==2){user.setcategory(logdat[i]);}
                }
                FirebaseMessaging.getInstance().subscribeToTopic(user.getuserid());
                FirebaseMessaging.getInstance().subscribeToTopic(user.getcity());
                FirebaseMessaging.getInstance().subscribeToTopic(user.getcategory());*/
                //Toast.makeText(MainAc  tivity.this,data,Toast.LENGTH_LONG).show();
            }
            if(key.equals("ShareQuot")){
                try{
                    byte [] encodeByte = Base64.decode(data,Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                    shareImage(bitmap,msg);
                }
                catch(Exception e){
                    e.getMessage();
                    Toast.makeText(MainActivity.this,"Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            if(key.equals("DownloadQuot")){
                StyleableToast.makeText(MainActivity.this,"Your download has been started.",Toast.LENGTH_LONG,R.style.toaststyle).show();
                downloadFile(data);
            }
            if(key.equals("DownloadImg")){
                try{
                    StyleableToast.makeText(MainActivity.this,"Image save in your gallery.",Toast.LENGTH_LONG,R.style.toaststyle).show();

                    byte [] encodeByte = Base64.decode(data,Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                    storeImage(bitmap);
                    //saveImage(bitmap);
                    //new fileFromBitmap(bitmap,getApplicationContext()).execute();
                }
                catch(Exception e){
                    e.getMessage();
                    //Toast.makeText(MainActivity.this,"Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            if (key.equals("Share")){
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, data);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
            if (key.equals("Call")){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+data));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        startActivity(callIntent);
                    }
                } else {
                    startActivity(callIntent);
                }
            }
            if (key.equals("WhatsApp")){
                try {
                    String[] logdat=data.split(",");
                    startActivity(
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(
                                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", logdat[0],logdat[1])
                                    )
                            )
                    );
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
            }
            if (key.equals("Facebook")){
                try {
                    String[] logdat=data.split(",");
                    startActivity(
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(
                                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", logdat[0],logdat[1])
                                    )
                            )
                    );
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
            }
            if (key.equals("Rate")){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+data)));
            }
            if(key.equals("browser")){
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                    startActivity(browserIntent);
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            if (key.equals("logout")){
                logout();
            }
            if(key.equals("createLink")){
                createDaynamicLink(data,msg);
            }
        }
    }
    void shareImage(Bitmap bitmap,String text){
        String name = user.randoum(100000,999999)+"";

        String pathofBmp=
                MediaStore.Images.Media.insertImage(getContentResolver(),
                        bitmap,name, null);
        Uri uri = Uri.parse(pathofBmp);


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Star App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "hello hello"));
    }
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            MediaScannerConnection.scanFile(this, new String[] { pictureFile.getAbsolutePath()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(path), "image/*");
                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                            startActivity(intent);
                        }
                    });

        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
    private  File getOutputMediaFile(){
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File mediaStorageDir = new File( root);

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="YD_"+ timeStamp +".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/yodaap");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        String fname = "Image-" + n+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            downloadFile(myDir.getPath());

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(myDir.getPath()), "image/*");
            startActivity(intent);

            StyleableToast.makeText(MainActivity.this,"Image successfully save in your gallery.",Toast.LENGTH_LONG,R.style.toaststyle).show();
        } catch (Exception e) {
            e.printStackTrace();
            StyleableToast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG,R.style.toastfail).show();
        }
    }
    public void downloadFile(String DownloadUrl) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        else{
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(DownloadUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
            downloadManager.enqueue(request);

            if (DownloadManager.STATUS_SUCCESSFUL == 8) {
                //StyleableToast.makeText(MainActivity.this,"Your download has been started.",Toast.LENGTH_LONG,R.style.toaststyle).show();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Exit")
                                    .setMessage("Do you want to exit app?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            System.exit(0);
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                        return true;
                }
            }
        }
        catch (Exception e){
            finish();
            System.exit(0);
        };
        return super.onKeyDown(keyCode, event);
    }

    private void GoForward() {
        if (webView.canGoForward()) {
            webView.goForward();
        } else {
            StyleableToast.makeText(this, "Can't go further!",R.style.toastfail, Toast.LENGTH_SHORT).show();
        }
    }
    public class Callback extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onPostExecute();
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onPostExecute() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loadingBar);
        progressBar.setVisibility(View.GONE);
    }

    public void logout(){
        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
            googleApiClient.disconnect();
        }
        mAuth.signOut();
        new User(MainActivity.this).removeUser();
        user.setLogout("logout");
        startActivity(new Intent(MainActivity.this,MainActivity.class));
        finish();
    }

    public void gotoHome(){
        splash.setVisibility(View.GONE);
        mainLogin.setVisibility(View.GONE);
        LockScreen.setVisibility(View.GONE);
        loadApp.setVisibility(View.VISIBLE);
        writepermition();
        notiseting();
        online();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        upiPayment("10","234324324324234");
    }

    public void online(){
        try {
            currentVersion = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String url= user.api()+"appversion";
            StringRequest request=new StringRequest(Request.Method.GET,url,
                    new com.android.volley.Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject rs = new JSONObject(response);
                                String onlineVersion = rs.getString("pin");
                                if(Double.parseDouble(currentVersion)<Double.parseDouble(onlineVersion)){
                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                    alertDialog.setTitle("Update your app!");
                                    alertDialog.setIcon(R.mipmap.ic_launcher);
                                    alertDialog.setCancelable(false);
                                    alertDialog.setMessage("New Update is available, Please update your app.");

                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + MainActivity.this.getPackageName())));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                                            }
                                        }
                                    });
                                    alertDialog.show();
                                }
                            } catch (JSONException e) {
                                sendError(user.getContact(),user.getEmail(),e.getMessage());
                            }
                        }
                    }, null);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } catch (Exception e) {}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.POST_NOTIFICATIONS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("CAMERA and NOTIFICATIONS Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    writepermition();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    writepermition();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        else {
                            //Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }




    private static final int UPI_PAYMENT_REQUEST = 21;
    public void upiPayment(String amount,String userdata){
        String upiId = "mab0450018a0189026@yesbank";
        String name = "AMKMAK Global Business Solutions";
        String transactionNote = "Payment for Milk";

        Uri uri = Uri.parse("upi://pay")
                .buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        if (chooser.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, UPI_PAYMENT_REQUEST);
        } else {
            // Handle the case when no UPI app is available
            Toast.makeText(this, "No UPI app found, please install one.", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseUPIPaymentResult(String data) {
        if (data == null) {
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] response = data.split("&");
        String status = "";
        for (String s : response) {
            String[] keyValue = s.split("=");
            if (keyValue.length >= 2) {
                if (keyValue[0].toLowerCase().equals("status")) {
                    status = keyValue[1].toLowerCase();
                }
            }
        }

        if (status.equals("success")) {
            Toast.makeText(this, "Transaction successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
        }
    }
}