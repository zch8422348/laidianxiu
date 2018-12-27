package com.yuncai.call.tdvrcall.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuncai.call.tdvrcall.R;
import com.yuncai.call.tdvrcall.bean.QuickContact;
import com.yuncai.call.tdvrcall.db.QuickDialLab;
import com.yuncai.call.tdvrcall.util.PxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Created by TianMing.Xiong on 18-12-18.
 */

public class RecyclerViewQuickDialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String mGroupId;
    private final QuickDialLab dbTool;
    private Button btnDialogCancel;
    private Button btnDialogDelete;
    private Button btnDialogSave;
    private EditText etPhone;
    private EditText etName;
    private AlertDialog alertDialog;
    private List<QuickContact> quickContacts;
    private final int ADD_QUICK_DIAL_TYPE = 0 ;
    private final int NORMAL_QUICK_DIAL_TYPE = 1 ;

    public RecyclerViewQuickDialAdapter(Context mContext, String mGroupId, ArrayList<QuickContact> quickContacts) {
        this.mContext = mContext;
        this.mGroupId = mGroupId;
        dbTool = new QuickDialLab(mContext);
        this.quickContacts = quickContacts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        DisplayMetrics mts = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(mts);
        int realHeight = (mts.heightPixels - PxUtil.dp2px(mContext, 88) * 2 - PxUtil.dp2px(mContext, 10) * 4) / 4;
//        Log.e("===========","====height:"+realHeight);
        if (viewType == 0) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.add_quick_view, viewGroup, false);
            inflate.getLayoutParams().height = realHeight;
            return new RecyclerViewQuickDialAdapter.AddViewHolder(inflate);
        } else if (viewType == 1) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.quick_dial_list_item, viewGroup, false);
            itemView.getLayoutParams().height = realHeight;
            return new DialViewHolder(itemView);
        } else {
            return null;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 || getItemCount() == 1) {
            return ADD_QUICK_DIAL_TYPE;// 尾部的添加键
        } else {
            return NORMAL_QUICK_DIAL_TYPE;// 正常拨号按钮
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        switch (getItemViewType(position)) {
            case ADD_QUICK_DIAL_TYPE:
                // 添加号码
                if (viewHolder instanceof AddViewHolder) {
                    ((RecyclerViewQuickDialAdapter.AddViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Toast.makeText(mContext, "你点击了添加", Toast.LENGTH_SHORT).show();
                            View dialogView = LayoutInflater.from(mContext).inflate(R.layout.update_quick_contact_dialog, null);
                            initDialogView(dialogView);
                            btnDialogDelete.setVisibility(View.GONE);
                            initDataDialog(true,position);
                            alertDialog = new AlertDialog.Builder(mContext).setView(dialogView).create();
                            alertDialog.show();
                        }
                    });
                }
                break;
            case NORMAL_QUICK_DIAL_TYPE:
                final QuickContact quickContact = quickContacts.get(position);
                if (quickContact != null) {
                    ((RecyclerViewQuickDialAdapter.DialViewHolder) viewHolder).tvQuickContactName.setText(quickContact.getqName());
                    ((RecyclerViewQuickDialAdapter.DialViewHolder) viewHolder).tvQuickContactPhone.setText(quickContact.getqPhoneNumber());
                }

                View view = viewHolder.itemView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 直接拨号
                        String phone = quickContacts.get(viewHolder.getAdapterPosition()).getqPhoneNumber();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(mContext, "未授权直接拨号功能！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mContext.startActivity(intent);
                    }
                });
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(getItemViewType(position)==0){
                            return true;
                        }
                        // 拿到当前条目数据
                        QuickContact quickContact = quickContacts.get(viewHolder.getAdapterPosition());
                        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.update_quick_contact_dialog, null);
                        initDialogView(dialogView);
                        String name = quickContact.getqName();
                        String phoneNumber = quickContact.getqPhoneNumber();
                        etName.setText(name);
                        etPhone.setText(phoneNumber);
                        initDataDialog(false,position);
                        alertDialog = new AlertDialog.Builder(mContext).setView(dialogView).create();
                        alertDialog.show();
                        // 修改数据
                        return true;
                    }
                });
                break;
        }

    }

    private void initDataDialog(final boolean isInsert,final int position) {
         btnDialogCancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(alertDialog.isShowing()){
                     alertDialog.dismiss();
                 }
             }
         });
         btnDialogDelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 QuickContact quickContact = quickContacts.get(position);
                 dbTool.deleteQuickDial(quickContact);
                 quickContacts.remove(quickContact);
                 notifyItemRemoved(position);
                 notifyItemRangeRemoved(position,quickContacts.size());
                 if(alertDialog.isShowing()){
                     alertDialog.dismiss();
                 }
             }
         });
         btnDialogSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String etNameText = etName.getText().toString();
                 String etPhoneText = etPhone.getText().toString();
                 if(!TextUtils.isEmpty(etNameText) && !TextUtils.isEmpty(etPhoneText)){
                     QuickContact newContact = new QuickContact();
                     newContact.setqGroupId(mGroupId);
                     newContact.setqName(etNameText);
                     newContact.setqPhoneNumber(etPhoneText);
                     if(isInsert){
                         boolean isInsert = dbTool.addQuickDial(newContact);
                         if(!isInsert){
                             Toast.makeText(mContext, "请勿重复添加！", Toast.LENGTH_SHORT).show();
                             alertDialog.dismiss();
                             return;
                         }
                         quickContacts.add(newContact);
                         notifyItemInserted(position);
                         notifyItemRangeChanged(position,quickContacts.size()-position);// ****通知数据与界面重新绑定
                     }else {
                         dbTool.updateQuickDial(quickContacts.get(position),newContact);
                         quickContacts.set(position,newContact);
                         notifyItemChanged(position);
                     }

                 }else {
                     Toast.makeText(mContext, "号码或者名字不能为空！", Toast.LENGTH_SHORT).show();
                 }
                 alertDialog.dismiss();
             }
         });
    }

    @Override
    public int getItemCount() {
        return quickContacts.size()+1;
    }

    public void addData(List<QuickContact> quickContacts) {
        this.quickContacts = quickContacts;
        notifyDataSetChanged();
    }


    static class DialViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tvQuickContactName;
        private TextView tvQuickContactPhone;

        public DialViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvQuickContactName = itemView.findViewById(R.id.tv_quick_contact_name);
            tvQuickContactPhone = itemView.findViewById(R.id.tv_quick_contact_phone);
        }
    }
    static class AddViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    private void initDialogView(View view) {
        etName = view.findViewById(R.id.et_name);
        etPhone =  view.findViewById(R.id.et_phone);
        btnDialogSave =  view.findViewById(R.id.btn_dialog_save);
        btnDialogCancel = view.findViewById(R.id.btn_dialog_cancel);
        btnDialogDelete = view.findViewById(R.id.btn_dialog_delete);
    }

}
