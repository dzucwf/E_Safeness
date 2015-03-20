package com.safeness.patient.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.model.User;
import com.safeness.e_saveness_common.net.SourceJsonHandler;
import com.safeness.e_saveness_common.util.Constant;
import com.safeness.im.db.UserDao;
import com.safeness.patient.R;
import com.safeness.patient.bussiness.WebServiceName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppBaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOGIN_RQ = 21;

    private static final int LOGIN_ERROR_RQ = 22;

    private static final int LOGIN_IM=23;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;/*
        //Insert An Object
        IBaseDao<User> userDao = DaoFactory.createGenericDao(mContext, User.class);

        try{
            userDao.insert(new User(1,"AAAA"));
        }
        catch (Exception ex){
            Log.v("E", ex.getMessage());
        }


        //Insert Object List
        List<User> insertUserList = new ArrayList<User>();
        for(int i = 0; i<10;++i){
            insertUserList.add(new User(i+10, "BBB"+i));
        }
        userDao.batchInsert(insertUserList);

        List<User> userList = userDao.queryByCondition("username=?", "AAAA");

        //InsertOrUpdate
        userDao.insertOrUpdate(new User(1, "AAAA"), "username"); //update where user_name='AAAA'
        userDao.insertOrUpdate(new User(1, "CCCC"), "username"); //insert CCCC

        for (User user : userList) {
            Log.i("asdf",user.getUsername() + ";");
        }
*/
    }

    private Handler hander = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case LOGIN_RQ:


                    //登陆完服务器后再次登陆聊天服务器
                    attemptLoginIM();

                    break;
                case LOGIN_ERROR_RQ:
                    String messageStr = msg.getData().getString("message");
                    Toast.makeText(mContext, messageStr, Toast.LENGTH_LONG).show();
                    break;
                case LOGIN_IM:
                    if (isSuccess) {
                        //登陆判断成功
                        Intent it = new Intent();
                        it.setClass(mContext, MainActivity.class);
                        LoginActivity.this.startActivity(it);
                        finish();
                        Toast.makeText(getApplicationContext(), "登录聊天服务器成功", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "登录聊天服务器失败", Toast.LENGTH_LONG).show();
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
                    }
                    break;
            }
            super.handleMessage(msg);
            dissProgressDialog();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupView() {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    //login();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void initializedData() {

    }

    private void populateAutoComplete() {
        if (VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)
            getSupportLoaderManager().initLoader(0, null, this);
        } else if (VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }

    private void login() {

        verificationLogin();
    }


    private void verificationLogin() {

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            loginServer(email, password);

        }
    }

    private void loginServer(String userName, String password) {

        String url = Constant.getServier() + WebServiceName.login;
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("uName", userName);
        parameter.put("uPass", password);
        this.request(parameter, url, LOGIN_RQ, this, new SourceJsonHandler());

    }

    @Override
    public void onSuccess(Object obj, int reqCode) {

        if (reqCode == LOGIN_RQ) {
            JSONObject jsobject = (JSONObject) obj;
            Message msg = new Message();
            Bundle b = new Bundle();
            try {
                b.putString("message", jsobject.getString("message"));
                msg.setData(b);

                if (jsobject.getString("code").equals("USER_LOGIN_SUCCESS")) {


                    msg.what = LOGIN_RQ;


                } else {
                    msg.what = LOGIN_ERROR_RQ;
                }
                hander.sendMessage(msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onFail(int errorCode, int reqCode) {
        super.onFail(errorCode, reqCode);
    }

    /**
     * 登陆即时通讯服务器
     */
    public void attemptLoginIM() {


        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        //showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        //puchao
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<String>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }

        @Override
        protected void onPostExecute(List<String> emailAddressCollection) {
            addEmailsToAutoComplete(emailAddressCollection);
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    //登陆聊天是否成功
    boolean isSuccess = false;

    /**
     * 登陆聊天服务器
     *
     * @param currentUsername
     * @param currentPassword
     */
    private void loginIM(final String currentUsername, final String currentPassword) {


        //puchao
        PatientApplication.currentUserNick = currentUsername;


        isSuccess = false;

        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {


            @Override
            public void onSuccess() {
                //umeng自定义事件，开发者可以把这个删掉
                //loginSuccess2Umeng(start);


                // 登陆成功，保存用户名密码
                PatientApplication.getInstance().setUserName(currentUsername);
                PatientApplication.getInstance().setPassword(currentPassword);

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    // conversations in case we are auto login
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();

                    // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
                    List<String> usernames = EMContactManager.getInstance().getContactUserNames();
                    EMLog.d("roster", "contacts size: " + usernames.size());
                    Map<String, User> userlist = new HashMap<String, User>();
                    for (String username : usernames) {
                        User user = new User();
                        user.setUsername(username);
                        setUserHearder(username, user);
                        userlist.put(username, user);
                    }
                    //puchao
                        /*
                        // 添加user"申请与通知"
                        User newFriends = new User();
                        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
                        newFriends.setNick("申请与通知");
                        newFriends.setHeader("");
                        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
                        // 添加"群聊"
                        User groupUser = new User();
                        groupUser.setUsername(Constant.GROUP_USERNAME);
                        groupUser.setNick("群聊");
                        groupUser.setHeader("");
                        userlist.put(Constant.GROUP_USERNAME, groupUser);
                        */
                    // 存入内存
                    PatientApplication.getInstance().setContactList(userlist);
                    // 存入db
                    UserDao dao = new UserDao(LoginActivity.this);
                    List<User> users = new ArrayList<User>(userlist.values());
                    dao.saveContactList(users);

                    // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
                    EMGroupManager.getInstance().getGroupsFromServer();
                    isSuccess = true;
                    hander.sendEmptyMessage(LOGIN_IM);

                } catch (Exception e) {
                    e.printStackTrace();
                    //取好友或者群聊失败，不让进入主页面，也可以不管这个exception继续进到主页面
                    runOnUiThread(new Runnable() {
                        public void run() {

                            PatientApplication.getInstance().logout(null);
                            Toast.makeText(getApplicationContext(), "登录失败: 获取好友或群聊失败", Toast.LENGTH_LONG).show();
                        }
                    });
                    isSuccess = false;
                    hander.sendEmptyMessage(LOGIN_IM);
                }
                //更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(PatientApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }


            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                //loginFailure2Umeng(start,code,message);
                isSuccess = false;

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(getApplicationContext(), "登录消息失败: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
                hander.sendEmptyMessage(LOGIN_IM);
            }
        });





    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param username
     * @param user
     */
    protected void setUserHearder(String username, User user) {
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {



           loginIM(mEmail, mPassword);



            return isSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);


        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



