package com.yuncai.call.tdvrcall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yuncai.call.tdvrcall.activity.DutyStatisticsActivity;
import com.yuncai.call.tdvrcall.activity.VoiceRecordActivity;
import com.yuncai.call.tdvrcall.adapter.FragmentAdapter;
import com.yuncai.call.tdvrcall.fragment.QuickDialFragment;
import com.yuncai.call.tdvrcall.leavingmsg.RecordingActivity;
import com.yuncai.call.tdvrcall.service.CallService;
import com.yuncai.call.tdvrcall.service.IncomingCallService;
import com.yuncai.call.tdvrcall.util.TimeThread;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ViewPager viewpager;
    private RadioGroup rgQuickDialGroup;
    private RadioButton rbGroup1;
    private RadioButton rbGroup2;
    private RadioButton rbGroup3;
    private RadioButton rbGroup4;
    private RadioButton rbGroup5;
    private TextView btnGroupEdit;
    private TextView tvSetting;
    private TextView tvVoiceRecord;
    private TextView tvDutyStatistics;
    private TextView tvLeavingMsg;
    private TextView tvAddressBook;
    private TextView tvCallInfo;
    private TextView tvDial;
    private TextView tvDateTime;

    // 组名键值
    private String GROUP_KEY1 = "group1";
    private String GROUP_KEY2 = "group2";
    private String GROUP_KEY3 = "group3";
    private String GROUP_KEY4 = "group4";
    private String GROUP_KEY5 = "group5";

    private Button btnGroupNameCancel;
    private Button btnSaveGroupName;
    private EditText etGroupName5;
    private TextInputLayout groupnameWrapper5;
    private EditText etGroupName4;
    private TextInputLayout groupnameWrapper4;
    private EditText etGroupName3;
    private TextInputLayout groupnameWrapper3;
    private EditText etGroupName2;
    private TextInputLayout groupnameWrapper2;
    private EditText etGroupName1;
    private TextInputLayout groupnameWrapper1;
    private AlertDialog alertEditGroupDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        initView();
        initViewPager();
        initGroupName();
        showSystemDateTime();
        initSetData();
        startPhoneCoreService();
    }

    private void startPhoneCoreService() {
        // 启动来电去电服务
        startService(new Intent(this, IncomingCallService.class));
        startService(new Intent(this, CallService.class));
    }

    private void initGroupName() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // 显示原来的组名
        String strGroup1 = sp.getString(GROUP_KEY1, getString(R.string.group_null_name));
        String strGroup2 = sp.getString(GROUP_KEY2, getString(R.string.group_null_name));
        String strGroup3 = sp.getString(GROUP_KEY3, getString(R.string.group_null_name));
        String strGroup4 = sp.getString(GROUP_KEY4, getString(R.string.group_null_name));
        String strGroup5 = sp.getString(GROUP_KEY5, getString(R.string.group_null_name));
        rbGroup1.setText(strGroup1);
        rbGroup2.setText(strGroup2);
        rbGroup3.setText(strGroup3);
        rbGroup4.setText(strGroup4);
        rbGroup5.setText(strGroup5);
    }

    private void initSetData() {
        // 系统设置
        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        // 录音记录
        tvVoiceRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VoiceRecordActivity.class));
            }
        });
        // 值班统计
        tvDutyStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DutyStatisticsActivity.class));
            }
        });
        // 交办留言
        tvLeavingMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordingActivity.class));
            }
        });
        // 联系人
        tvAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Contacts.People.CONTENT_URI);
                startActivity(intent);
            }
        });
        // 通话记录
        tvCallInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });

        // 拨号
        tvDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
                startActivity(intent);
            }
        });

    }

    private void showSystemDateTime() {
        //实时更新时间（1秒更新一次）
        TimeThread timeThread = new TimeThread(tvDateTime);//tvDate 是显示时间的控件TextView
        timeThread.start();
    }

    private void initView() {
        tvDateTime = findViewById(R.id.tv_date_time);
        viewpager = findViewById(R.id.viewpager);
        rgQuickDialGroup = findViewById(R.id.rg_quick_dial_group);
        rbGroup1 = findViewById(R.id.rb_group1);
        rbGroup2 = findViewById(R.id.rb_group2);
        rbGroup3 = findViewById(R.id.rb_group3);
        rbGroup4 = findViewById(R.id.rb_group4);
        rbGroup5 = findViewById(R.id.rb_group5);
        btnGroupEdit = findViewById(R.id.btn_group_edit);
        tvSetting = findViewById(R.id.tv_setting);
        tvVoiceRecord = findViewById(R.id.tv_voice_record);
        tvDutyStatistics = findViewById(R.id.tv_duty_statistics);
        tvLeavingMsg = findViewById(R.id.tv_leaving_msg);
        tvAddressBook = findViewById(R.id.tv_address_book);
        tvCallInfo = findViewById(R.id.tv_call_info);
        tvDial = findViewById(R.id.tv_dial);
    }
    private void initEditGroupNameDialogView(View view) {
        groupnameWrapper1 =  view.findViewById(R.id.groupnameWrapper1);
        etGroupName1 = view.findViewById(R.id.et_group_name1);
        groupnameWrapper2 =  view.findViewById(R.id.groupnameWrapper2);
        etGroupName2 =  view.findViewById(R.id.et_group_name2);
        groupnameWrapper3 =  view.findViewById(R.id.groupnameWrapper3);
        etGroupName3 =  view.findViewById(R.id.et_group_name3);
        groupnameWrapper4 =  view.findViewById(R.id.groupnameWrapper4);
        etGroupName4 =  view.findViewById(R.id.et_group_name4);
        groupnameWrapper5 =  view.findViewById(R.id.groupnameWrapper5);
        etGroupName5 =  view.findViewById(R.id.et_group_name5);
        btnSaveGroupName =  view.findViewById(R.id.btn_save_group_name);
        btnGroupNameCancel =  view.findViewById(R.id.btn_group_name_cancel);
    }

    private void initViewPager() {
        // 一键拨号，分组
        ArrayList<Integer> titles = new ArrayList<>();
        titles.add(1);
        titles.add(2);
        titles.add(3);
        titles.add(4);
        titles.add(5);

        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            fragments.add(QuickDialFragment.newInstance("" + titles.get(i), ""));
        }
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(fragmentAdapter);
        viewpager.setCurrentItem(0);
        rbGroup1.setChecked(true);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int selectPageIndex) {
                showTitleButtonState(selectPageIndex);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        rgQuickDialGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                showQuickDialContactPage(i);
            }
        });
        btnGroupEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                View dialogEditGroupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.edit_group_name_dialog, null);
                initEditGroupNameDialogView(dialogEditGroupView);
                initGroupNameDialogData();
                alertEditGroupDialog = new AlertDialog.Builder(MainActivity.this).setView(dialogEditGroupView).create();
                alertEditGroupDialog.show();
            }
        });

    }
    // 组名编辑
    private void initGroupNameDialogData() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // 显示原来的组名
        String strGroup1 = sp.getString(GROUP_KEY1, getString(R.string.group_null_name));
        String strGroup2 = sp.getString(GROUP_KEY2, getString(R.string.group_null_name));
        String strGroup3 = sp.getString(GROUP_KEY3, getString(R.string.group_null_name));
        String strGroup4 = sp.getString(GROUP_KEY4, getString(R.string.group_null_name));
        String strGroup5 = sp.getString(GROUP_KEY5, getString(R.string.group_null_name));
        etGroupName1.setText(strGroup1);
        etGroupName2.setText(strGroup2);
        etGroupName3.setText(strGroup3);
        etGroupName4.setText(strGroup4);
        etGroupName5.setText(strGroup5);

        btnSaveGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String groupName1 = etGroupName1.getText().toString();
                 String groupName2 = etGroupName2.getText().toString();
                 String groupName3 = etGroupName3.getText().toString();
                 String groupName4 = etGroupName4.getText().toString();
                 String groupName5 = etGroupName5.getText().toString();
                sp.edit().putString(GROUP_KEY1,groupName1)
                        .putString(GROUP_KEY2,groupName2)
                        .putString(GROUP_KEY3,groupName3)
                        .putString(GROUP_KEY4,groupName4)
                        .putString(GROUP_KEY5,groupName5).apply();
                if(alertEditGroupDialog!=null && alertEditGroupDialog.isShowing()){
                    alertEditGroupDialog.dismiss();
                }
                initGroupName();// 刷新主界面
            }
        });
        btnGroupNameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alertEditGroupDialog!=null && alertEditGroupDialog.isShowing()){
                    alertEditGroupDialog.dismiss();
                }
            }
        });
    }

    private void showQuickDialContactPage(int res_id) {
        switch (res_id) {
            case R.id.rb_group1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.rb_group2:
                viewpager.setCurrentItem(1);
                break;
            case R.id.rb_group3:
                viewpager.setCurrentItem(2);
                break;
            case R.id.rb_group4:
                viewpager.setCurrentItem(3);
                break;
            case R.id.rb_group5:
                viewpager.setCurrentItem(4);
                break;
        }
    }

    private void showTitleButtonState(int selectPageIndex) {
        switch (selectPageIndex) {
            case 0:
                rbGroup1.setChecked(true);
                break;
            case 1:
                rbGroup2.setChecked(true);
                break;
            case 2:
                rbGroup3.setChecked(true);
                break;
            case 3:
                rbGroup4.setChecked(true);
                break;
            case 4:
                rbGroup5.setChecked(true);
                break;
        }
    }

}
