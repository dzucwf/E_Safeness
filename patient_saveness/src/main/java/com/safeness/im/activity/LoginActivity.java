/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.safeness.im.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.util.Constant;
import com.safeness.im.db.UserDao;
import com.safeness.im.utils.CommonUtils;
import com.safeness.im.widget.DemoHXSDKHelper;
import com.safeness.patient.R;
import com.safeness.e_saveness_common.model.User;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;
	
	private String currentUsername;
	private String currentPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		setContentView(R.layout.activity_login_im);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);

		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		if (PatientApplication.getInstance().getUserName() != null) {
			usernameEditText.setText(PatientApplication.getInstance().getUserName());
		}
	}

	/**
	 * 登陆
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();
		
		if(TextUtils.isEmpty(currentUsername)){
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(currentPassword)){
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(LoginActivity.this, AlertDialog.class);
		intent.putExtra("editTextShow", true);
		intent.putExtra("titleIsCancel", true);
		intent.putExtra("msg", "请设置当前用户的昵称\n为了ios离线推送不是userid而是nick，详情见注释");
		intent.putExtra("edit_text", currentUsername);
		startActivityForResult(intent, REQUEST_CODE_SETNICK);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_SETNICK) {
				PatientApplication.currentUserNick = data.getStringExtra("edittext");

				progressShow = true;
				final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
				pd.setCanceledOnTouchOutside(false);
				pd.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						progressShow = false;
					}
				});
				pd.setMessage("正在登陆...");
				pd.show();

				final long start = System.currentTimeMillis();
				// 调用sdk登陆方法登陆聊天服务器
				EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

					@Override
					public void onSuccess() {
						//umeng自定义事件，开发者可以把这个删掉
						loginSuccess2Umeng(start);
						
						if (!progressShow) {
							return;
						}
						// 登陆成功，保存用户名密码
						PatientApplication.getInstance().setUserName(currentUsername);
						PatientApplication.getInstance().setPassword(currentPassword);
						runOnUiThread(new Runnable() {
							public void run() {
								pd.setMessage("正在获取好友和群聊列表...");
							}
						});
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

							// 存入内存
							PatientApplication.getInstance().setContactList(userlist);
							// 存入db
							UserDao dao = new UserDao(LoginActivity.this);
							List<User> users = new ArrayList<User>(userlist.values());
							dao.saveContactList(users);

							// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
							EMGroupManager.getInstance().getGroupsFromServer();
						} catch (Exception e) {
							e.printStackTrace();
							//取好友或者群聊失败，不让进入主页面，也可以不管这个exception继续进到主页面
							runOnUiThread(new Runnable() {
                                public void run() {
                                    pd.dismiss();
                                    PatientApplication.getInstance().logout(null);
                                    Toast.makeText(getApplicationContext(), "登录失败: 获取好友或群聊失败",Toast.LENGTH_LONG).show();
                                }
                            });
							return;
						}
						//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
						boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(PatientApplication.currentUserNick.trim());
						if (!updatenick) {
							Log.e("LoginActivity", "update current user nick fail");
						}
						if (!LoginActivity.this.isFinishing())
							pd.dismiss();
						// 进入主页面
						startActivity(new Intent(LoginActivity.this, MainActivity.class));
						finish();
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(final int code, final String message) {
						loginFailure2Umeng(start,code,message);
						if (!progressShow) {
							return;
						}
						runOnUiThread(new Runnable() {
							public void run() {
								pd.dismiss();
								Toast.makeText(getApplicationContext(), "登录失败: " + message, Toast.LENGTH_SHORT).show();
							}
						});
					}
				});

			}
		}
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
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
	
	private void loginSuccess2Umeng(final long start) {
		runOnUiThread(new Runnable() {
			public void run() {
				long costTime = System.currentTimeMillis() - start;
				Map<String, String> params = new HashMap<String, String>();
				params.put("status", "success");
				MobclickAgent.onEventValue(LoginActivity.this, "login1", params, (int) costTime);
				MobclickAgent.onEventDuration(LoginActivity.this, "login1", (int) costTime);
			}
		});
	}
	private void loginFailure2Umeng(final long start, final int code, final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				long costTime = System.currentTimeMillis() - start;
				Map<String, String> params = new HashMap<String, String>();
				params.put("status", "failure");
				params.put("error_code", code + "");
				params.put("error_description", message);
				MobclickAgent.onEventValue(LoginActivity.this, "login1", params, (int) costTime);
				MobclickAgent.onEventDuration(LoginActivity.this, "login1", (int) costTime);
				
			}
		});
	}
}
