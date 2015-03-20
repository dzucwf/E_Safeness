package com.safeness.patient.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.easemob.util.HanziToPinyin;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.e_saveness_common.model.User;
import com.safeness.e_saveness_common.util.Constant;
import com.safeness.e_saveness_common.util.DateTimeUtil;
import com.safeness.im.activity.ChatActivity;
import com.safeness.im.db.InviteMessgeDao;
import com.safeness.im.db.UserDao;
import com.safeness.im.domain.InviteMessage;
import com.safeness.im.utils.CommonUtils;
import com.safeness.patient.R;
import com.safeness.patient.adapter.BtmNaviSwitchAdapter;
import com.safeness.patient.ui.fragment.NaviFoodFragment;
import com.safeness.patient.ui.fragment.NaviGlucoseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppBaseActivity {




    private static final String TAG = "MainActivity";
    private LinearLayout mSwitcher;
    private ViewPager mSearchVp;
    private final int CB_INDEX_FOOD = 0;
    private final int CB_INDEX_DRUG = 1;
    private final int CB_INDEX_GLUCOSE = 2;
    private final int CB_INDEX_SPORT = 3;
    private final int CB_INDEX_DOCTOR = 4;

    private  final  int OPEN_CALENDAR_RQ=11;
    private  final int SET_DATE_RESULT = 12;

    private ImageButton food_imagebtn;
    private ImageButton drugbtn;
    private ImageButton glucosebtn;
    private ImageButton sportbtn;
    private ImageButton doctorbtn;

    BtmNaviSwitchAdapter switchAdapter;
    //上次选中的
    int lastSelectIndex = 0;

    //打开的日历控件
    private CaldroidFragment dialogCaldroidFragment;
    final String dialogTag = "CALDROID_DIALOG_FRAGMENT";

    //日期控件选择的时间
    private  Calendar saveCalendar = Calendar.getInstance();


    /*
    * 以下是聊天的变量
    *
    *
    * */
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;



    private NewMessageBroadcastReceiver msgReceiver;
    //账号被移除
    private boolean isCurrentAccountRemoved = false;
    // 账号在别处登录
    public boolean isConflict = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)){
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            PatientApplication.getInstance().logout(null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        //登陆之后加入提醒服务，这个要看后期是不是要重复加入的bug
        Intent it = new Intent();
        it.setAction("com.safeness.e_saveness_common.remind.OnBootReceiver");
        it.putExtra("user",PatientApplication.getInstance().getUserName());
        this.sendBroadcast(it);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播接收者
        try {
            unregisterReceiver(msgReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(cmdMessageReceiver);
        } catch (Exception e) {
        }

        // try {
        // unregisterReceiver(offlineMessageReceiver);
        // } catch (Exception e) {
        // }

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        getViews();

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);

        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        //注册一个透传消息的BroadcastReceiver
        IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        cmdMessageIntentFilter.setPriority(3);
        registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);



        // 注册一个离线消息的BroadcastReceiver
        // IntentFilter offlineMessageIntentFilter = new
        // IntentFilter(EMChatManager.getInstance()
        // .getOfflineMessageBroadcastAction());
        // registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);

        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        // 注册群聊相关的listener
        EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();


    }

    @Override
    protected void initializedData() {

    }

    //初始化下层切换
    private void getViews() {
        switchAdapter = new BtmNaviSwitchAdapter(getSupportFragmentManager());
        mSwitcher = (LinearLayout) findViewById(R.id.navi_switcher);
        mSearchVp = (ViewPager) findViewById(R.id.navi_view_pager);
        mSearchVp.setAdapter(switchAdapter);
        mSearchVp.setOnPageChangeListener(mPageChgListener);

        food_imagebtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_food);
        drugbtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_drug);
        glucosebtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_glucose);
        sportbtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_sports);
        doctorbtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_doctor);
        createCalControl();

    }

    private void createCalControl(){
        // Setup caldroid to use as dialog


        dialogCaldroidFragment = new CaldroidFragment();

        // Setup dialogTitle
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(dialogCaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(dialogCaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(dialogCaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(dialogCaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        args.putBoolean(dialogCaldroidFragment.SHOW_NAVIGATION_ARROWS,true);
        dialogCaldroidFragment.setArguments(args);
        dialogCaldroidFragment.setCaldroidListener(Callistener);

    }
    final SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtil.NORMAL_PATTERN);
    // Setup listener
    final CaldroidListener Callistener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            saveCalendar.setTime(date);


            dialogCaldroidFragment.setSelectedDates(date,date);
            //setSelectDateBackground(date);
            dialogCaldroidFragment.dismiss();

            String dateStr = formatter.format(date);
            switch (mSearchVp.getCurrentItem()){
                case CB_INDEX_FOOD:
                    NaviFoodFragment foodFragment = (NaviFoodFragment)switchAdapter.getItem(CB_INDEX_FOOD);
                    foodFragment.setSelectedDate(date);
                    break;
                case R.id.navi_switcher_item_drug:
                    break;
                case CB_INDEX_GLUCOSE:
                    NaviGlucoseFragment glucoseFragment = (NaviGlucoseFragment)switchAdapter.getItem(CB_INDEX_GLUCOSE);
                    glucoseFragment.setSelectedDate(dateStr);
                    break;
                case R.id.navi_switcher_item_sports:
                    break;
                case R.id.navi_switcher_item_doctor:
                    break;
            }

        }

        @Override
        public void onChangeMonth(int month, int year) {
            String text = "month: " + month + " year: " + year;
            Toast.makeText(getApplicationContext(), text,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            Toast.makeText(getApplicationContext(),
                    "Long click " + formatter.format(date),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCaldroidViewCreated() {

        }

    };



    /**
     * 打开日历选择
     * @param view
     */
    public void openCalendar(View view){
        Toast.makeText(this,"打开日历",Toast.LENGTH_LONG).show();
        dialogCaldroidFragment.show(getSupportFragmentManager(),
                dialogTag);

        //以上代码，改成弹窗的形式
        //NaviGlucoseFragment glucoseFragment = (NaviGlucoseFragment)switchAdapter.getItem(CB_INDEX_GLUCOSE);
       // glucoseFragment.showCalControl();

//        Intent it = new Intent(this, CalendarContainnerActivity.class);
//        startActivityForResult(it,OPEN_CALENDAR_RQ);
//        overridePendingTransition(R.anim.in_from_down, R.anim.out_to_down);
    }

    /*
    * 从日历控件返回后设置选择的日期
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == OPEN_CALENDAR_RQ && resultCode == SET_DATE_RESULT){
            String selectedDate = data.getStringExtra("selectedDate");
            NaviGlucoseFragment glucoseFragment = (NaviGlucoseFragment)switchAdapter.getItem(CB_INDEX_GLUCOSE);
            glucoseFragment.setSelectedDate(selectedDate);


        }

    }

    /*
       * 选中导航
       * */
    public void onNaviCheck(View view){
        int cur = CB_INDEX_GLUCOSE;

        switch (view.getId()){


            case R.id.navi_switcher_item_food:
                cur = CB_INDEX_FOOD;

                break;
            case R.id.navi_switcher_item_drug:
                cur = CB_INDEX_DRUG;
                break;
            case R.id.navi_switcher_item_glucose:
                cur = CB_INDEX_GLUCOSE;
                break;
            case R.id.navi_switcher_item_sports:
                cur = CB_INDEX_SPORT;
                break;
            case R.id.navi_switcher_item_doctor:
                cur = CB_INDEX_DOCTOR;
                break;
        }

        if(mSearchVp.getCurrentItem() != cur) {
            mSearchVp.setCurrentItem(cur);

        }

    }

    /*
    * 设置按钮的选择状态
    * */
    private void setSelectButtonState(int curId){


        Log.d(TAG,curId+"");
        switch(curId) {
            case CB_INDEX_FOOD:
                food_imagebtn.setImageResource(R.drawable.nav_myday_selected);
                break;
            case CB_INDEX_DRUG:
                drugbtn.setImageResource(R.drawable.medicine_selected);
                break;
            case CB_INDEX_GLUCOSE:
                glucosebtn.setImageResource(R.drawable.diabetes_nav_glucose_on);
                break;
            case CB_INDEX_SPORT:
                sportbtn.setImageResource(R.drawable.diabetes_nav_act_on);

                break;
            case CB_INDEX_DOCTOR:
                doctorbtn.setImageResource(R.drawable.navigation_refine_05);
                break;
        }

        switch(lastSelectIndex) {
            case CB_INDEX_FOOD:
                food_imagebtn.setImageResource(R.drawable.nav_myday_unselected);
                break;
            case CB_INDEX_DRUG:
                drugbtn.setImageResource(R.drawable.medicine_unselected);
                break;
            case CB_INDEX_GLUCOSE:
                glucosebtn.setImageResource(R.drawable.diabetes_nav_glucose_off);
                break;
            case CB_INDEX_SPORT:
                sportbtn.setImageResource(R.drawable.diabetes_nav_act_off);
                break;
            case CB_INDEX_DOCTOR:
                doctorbtn.setImageResource(R.drawable.navigation_refine_06);
                break;
        }
            lastSelectIndex = curId;
    }

    //测试环信注册
    private void testRegister(){


            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("正在注册...");
            pd.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        EMChatManager.getInstance().createAccountOnServer("pp2", "pp2");
                        runOnUiThread(new Runnable() {
                            public void run() {

                                    pd.dismiss();
                                // 保存用户名
                                PatientApplication.getInstance().setUserName("pp1");
                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (final EaseMobException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                    pd.dismiss();
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NONETWORK_ERROR){
                                    Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                                }else if(errorCode== EMError.USER_ALREADY_EXISTS){
                                    Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
                                }else if(errorCode== EMError.UNAUTHORIZED){
                                    Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();


    }


    private ViewPager.OnPageChangeListener mPageChgListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) { }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {

            int vpItem = mSearchVp.getCurrentItem();
            setSelectButtonState(vpItem);
            switch(vpItem) {
                case CB_INDEX_FOOD:

                    break;
                case CB_INDEX_DRUG:

                    break;
                case CB_INDEX_GLUCOSE:

                    break;
                case CB_INDEX_SPORT:

                    break;
                case CB_INDEX_DOCTOR:

                    break;
            }
        }


    };


    /*
    * 检查当前用户是否被删除
    */
    public boolean getCurrentAccountRemoved(){
        return isCurrentAccountRemoved;
    }

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        if (PatientApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME) != null)
            unreadAddressCountTotal = PatientApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }

    /**
     * 新消息广播接收者
     *
     *
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

            String from = intent.getStringExtra("from");
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
            if (ChatActivity.activityInstance != null) {
                if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                    if (message.getTo().equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                } else {
                    if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                }
            }

            // 注销广播接收者，否则在ChatActivity中会收到这个广播
            abortBroadcast();

            notifyNewMessage(message);



        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");

            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);

                if (msg != null) {

                    // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
                    if (ChatActivity.activityInstance != null) {
                        if (msg.getChatType() == EMMessage.ChatType.Chat) {
                            if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                                return;
                        }
                    }

                    msg.isAcked = true;
                }
            }

        }
    };



    /**
     * 透传消息BroadcastReceiver
     */
    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            EMLog.d(TAG, "收到透传消息");
            //获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            //获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String action = cmdMsgBody.action;//获取自定义action

            //获取扩展属性 此处省略
//			message.getStringAttribute("");
            EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action, message.toString()));
            Toast.makeText(MainActivity.this, "收到透传：action：" + action, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 离线消息BroadcastReceiver sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI
     * 有哪些人发来了离线消息 UI 可以做相应的操作，比如下载用户信息
     */
    // private BroadcastReceiver offlineMessageReceiver = new
    // BroadcastReceiver() {
    //
    // @Override
    // public void onReceive(Context context, Intent intent) {
    // String[] users = intent.getStringArrayExtra("fromuser");
    // String[] groups = intent.getStringArrayExtra("fromgroup");
    // if (users != null) {
    // for (String user : users) {
    // System.out.println("收到user离线消息：" + user);
    // }
    // }
    // if (groups != null) {
    // for (String group : groups) {
    // System.out.println("收到group离线消息：" + group);
    // }
    // }
    // }
    // };

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    /***
     * 好友变化listener
     *
     */
    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Map<String, User> localUsers = PatientApplication.getInstance().getContactList();
            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String username : usernameList) {
                User user = setUserHead(username);
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    userDao.saveContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);


        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = PatientApplication.getInstance().getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + "已把你从他好友列表里移除", Toast.LENGTH_LONG).show();
                        ChatActivity.activityInstance.finish();
                    }

                }
            });

        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            Log.d(TAG, username + "请求加你为好友,reason: " + reason);
            // 设置相应status
            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            Log.d(TAG, username + "同意了你的好友请求");
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactRefused(String username) {
            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        User user = PatientApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
        if (user.getUnreadMsgCount() == 0)
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }

    /**
     * set head
     *
     * @param username
     * @return
     */
    User setUserHead(String username) {
        User user = new User();
        user.setUsername(username);
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
        return user;
    }

    /**
     * 连接监听listener
     *
     */
    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    }else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    } else {


                    }
                }

            });
        }
    }

    /**
     * MyGroupChangeListener
     */
    private class MyGroupChangeListener implements GroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup)
                return;

            // 被邀请
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            // 提示用户被T了，demo省略此步骤
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {


                }
            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            // 群被解散
            // 提示用户群被解散,demo省略
            // 刷新ui
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
            // 用户申请加入群聊
            InviteMessage msg = new InviteMessage();
            msg.setFrom(applyer);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
            msg.setStatus(InviteMessage.InviteMesageStatus.BEAPPLYED);
            notifyNewIviteMessage(msg);
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            // 加群申请被同意
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            msg.setChatType(EMMessage.ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
            // 保存同意消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            // 加群申请被拒绝，demo未实现
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict||!isCurrentAccountRemoved) {

            EMChatManager.getInstance().activityResumed();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        PatientApplication.getInstance().logout(null);

        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle("下线通知");
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }



    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        PatientApplication.getInstance().logout(null);

        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle("移除通知");
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow){
            showConflictDialog();
        }else if(getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow){
            showAccountRemovedDialog();
        }
    }

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
     * 如果不需要，注释掉即可
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if(!EasyUtils.isAppRunningForeground(this)){
            return;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        String ticker = CommonUtils.getMessageDigest(message, this);
        if(message.getType() == EMMessage.Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
        //设置状态栏提示
        mBuilder.setTicker(message.getFrom()+": " + ticker);

        //必须设置pendingintent，否则在2.3的机器上会有bug
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }

 }
