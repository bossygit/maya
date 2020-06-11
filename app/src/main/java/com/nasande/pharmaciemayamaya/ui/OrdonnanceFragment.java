package com.nasande.pharmaciemayamaya.ui;

import androidx.lifecycle.ViewModelProviders;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.nasande.pharmaciemayamaya.ApiService;
import com.nasande.pharmaciemayamaya.Body;
import com.nasande.pharmaciemayamaya.Fichier;
import com.nasande.pharmaciemayamaya.FilesHelper;
import com.nasande.pharmaciemayamaya.LoginData;
import com.nasande.pharmaciemayamaya.MainActivity;
import com.nasande.pharmaciemayamaya.Node;
import com.nasande.pharmaciemayamaya.R;
import com.nasande.pharmaciemayamaya.RetrofitInstance;
import com.nasande.pharmaciemayamaya.SharedPrefManager;
import com.nasande.pharmaciemayamaya.Title;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;

public class OrdonnanceFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private OrdonnanceViewModel mViewModel;
    SharedPrefManager sharedPrefManager;
    private ApiService mApiInstance;
    ProgressDialog mProgressDialog;
    private Button mEnvois;
    private Button btnChooseFile;
    public static final int PICKFILE_RESULT_CODE = 1;
    private int GALLERY_INTENT_CALLED = 108;
    private static final int GALLERY_KITKAT_INTENT_CALLED = 207;
    private Uri fileUri;
    private String filePath;
    private int fid;
    private TextView tvItemPath;
    private String content_disposition;
    private String fichier;
    private RequestBody requestBodyByte;
    private EditText mTitre;
    private EditText mNumero;
    private EditText mComments;


    public static OrdonnanceFragment newInstance() {
        return new OrdonnanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ordonnance_fragment, container, false);
        mEnvois = root.findViewById(R.id.form_envois);
        tvItemPath = root.findViewById(R.id.file_path);
        mTitre = root.findViewById(R.id.nom);
        mNumero = root.findViewById(R.id.telephone);
        mComments = root.findViewById(R.id.comments);
        mProgressDialog = new ProgressDialog(getActivity());
        btnChooseFile = root.findViewById(R.id.photo);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Connexion ...");
        btnChooseFile.setEnabled(false);
        mEnvois.setEnabled(false);
        getMyPerms();

        mTitre.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (s.toString().equals("")) {
                    mTitre.setError("Saisissez votre nom");
                    btnChooseFile.setEnabled(false);
                } else {
                    btnChooseFile.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }


        });


        mNumero.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (s.toString().equals("")) {
                    mNumero.setError("Saisissez un numéro Mtn ou Airtel valide");
                    btnChooseFile.setEnabled(false);
                } else {
                    btnChooseFile.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }


        });



        sharedPrefManager = new SharedPrefManager(getActivity());


        btnChooseFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getMyPerms(); /* Got the permission bug removed */



                if (Build.VERSION.SDK_INT <19){
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("image/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choisir un fichier");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

                } else {

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);




                }




            }
        });



            mEnvois.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!sharedPrefManager.getSPIsLoggedIn()) {
                        Log.d("ODF", "Verifie");
                        attemptLogin();


                    }

                    else {
                        sendFile(fichier);
                    }




                }
            });

        mTitre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                mTitre.setError(null);
                if (mTitre.getText().toString().trim().equalsIgnoreCase("")) {
                    mTitre.setError("Entrer votre nom");
                }
            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrdonnanceViewModel.class);
        // TODO: Use the ViewModel

    }

    public void showDialog() {

        if(mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void hideDialog() {

        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void attemptLogin() {
        showDialog();

        mApiInstance = new RetrofitInstance().ObtenirInstance();

        final String email = "admin";
        final String password = "p@ss_maya";

        mApiInstance.loginRequest(new LoginData(email, password))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            hideDialog();

                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (!jsonRESULTS.getString("csrf_token").isEmpty()) {
                                    String csrf_token = jsonRESULTS.getString("csrf_token");
                                    String logout_token = jsonRESULTS.getString("logout_token");
                                    String user_id = jsonRESULTS.getJSONObject("current_user").getString("uid");
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_CSRF_TOKEN, csrf_token);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_LOGOUT_TOKEN, logout_token);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USER_ID, user_id);
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_LOGGED_IN, true);
                                    String basic_auth = email + ":" + password;
                                    byte[] bytes_basic_auth = basic_auth.getBytes();
                                    String encoded_basic_auth = android.util.Base64.encodeToString(bytes_basic_auth, android.util.Base64.DEFAULT);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_BASIC_AUTH, "Basic " + encoded_basic_auth.trim());
                                    sendFile(fichier);


                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    @AfterPermissionGranted(1000)
    private void getMyPerms(){
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(getActivity(),perms)){

            Toast.makeText(getActivity(), "Permissions granted", Toast.LENGTH_SHORT).show();

        }

        else {
            EasyPermissions.requestPermissions(getActivity(),"Nous avons besoin de recuperer la photo de votre ordonnance",1000,perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List perms) {
        // Add your logic here
    }

    @Override
    public void onPermissionsDenied(int requestCode, List perms) {
        // Add your logic here
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
            FilesHelper fH = new FilesHelper();
            switch (requestCode) {
                case PICKFILE_RESULT_CODE:
                    if (resultCode == -1) {
                        fileUri = data.getData();
                        filePath = fileUri.getPath();
                        tvItemPath.setText(filePath);
                        //fileUpload(fileUri);
                        Toast.makeText(getActivity(), "Lower 19", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case GALLERY_KITKAT_INTENT_CALLED:

                    fileUri = data.getData();
                    fichier= fH.sendPath(fileUri,getActivity());
                    tvItemPath.setText(fichier);
                    mEnvois.setEnabled(true);

                    break;

                default:
                    Toast.makeText(getActivity(), "Default", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendFile(String filePath){


        mApiInstance = new RetrofitInstance().ObtenirInstance();

        File file = new File(filePath);
        String filename=filePath.substring(filePath.lastIndexOf("/")+1);
        content_disposition = "file;filename=\"" + filename + "\"";



        try {
            InputStream fileInputStream = new FileInputStream(
                    filePath);
            byte[] buf = new byte[fileInputStream.available()];
            while (fileInputStream.read(buf) != -1) ;
            requestBodyByte = RequestBody
                    .create(MediaType.parse("application/octet-stream"), buf);

        } catch (FileNotFoundException e) {
            hideDialog();
            e.printStackTrace();
        } catch (IOException e) {
            hideDialog();
            e.printStackTrace();
        }
        Call<ResponseBody> call = mApiInstance.postFile(sharedPrefManager.getSPBasicAuth(),sharedPrefManager.getSPCsrfToken(),content_disposition,requestBodyByte);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    hideDialog();
                    try {
                        String reponse = response.body().string();
                        Log.d("MainActivity",reponse);
                        JSONObject jsonRESULTS = new JSONObject(reponse);
                        fid = jsonRESULTS.getJSONArray("fid").getJSONObject(0).getInt("value");
                        createNode(fid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    mTitre.getText().clear();
                    mNumero.getText().clear();
                    mComments.getText().clear();
                    tvItemPath.setText("Une erreur s'est produite");
                    hideDialog();


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        });
    }

    private void createNode(int fid){
        showDialog();
        ArrayList<Title> title = new ArrayList<>();
        ArrayList<Body> body = new ArrayList<>();
        String titre = mTitre.getText().toString()+ " - " + mNumero.getText().toString();
        body.add(0,new Body(mComments.getText().toString()));
        title.add(0, new Title(titre));
        ArrayList<Fichier>  field_image = new ArrayList<>();
        field_image.add(0,new Fichier(fid));

        Node node = new Node(title,field_image,body);
        mApiInstance = new RetrofitInstance().ObtenirInstance();

        Call<ResponseBody> call =  mApiInstance.addNode(sharedPrefManager.getSPBasicAuth(),sharedPrefManager.getSPCsrfToken(),node);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    mTitre.getText().clear();
                    mNumero.getText().clear();
                    mComments.getText().clear();

                    tvItemPath.setText("Nous avons recons votre ordonnance. Nous contactons dans les plus bref delais");


                    hideDialog();
                    try {
                        String reponse = response.body().string();
                        Log.d("MainActivity",reponse);
                        JSONObject jsonRESULTS = new JSONObject(reponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {

                    mTitre.getText().clear();
                    mNumero.getText().clear();
                    mComments.getText().clear();
                    tvItemPath.setText("Une erreur s'est produite");
                    hideDialog();


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mTitre.getText().clear();
                mNumero.getText().clear();
                mComments.getText().clear();
                tvItemPath.setText("Une erreur s'est produite");
                hideDialog();

            }
        });
    }




}
