package com.weiyi.itreader.ui;

import com.weiyi.itreader.common.Constant;
import com.weiyi.itreader.ui.ITActivity;
import com.weiyi.itreader.ui.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class ITBroadcastReceiver extends BroadcastReceiver {

	public ITBroadcastReceiver() {

	}
	public ITBroadcastReceiver(ITActivity itActivity) {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.v("broadcast", action);
		if (Constant.SOCKETTIMEOUT.equals(action)) {// 链接超时
			Toast.makeText(context, R.string.socket_timeout, Toast.LENGTH_SHORT)
					.show();
		}
		if (Constant.UPDATE_ITREADER_NOTIFY.equals(action)) {// 更新软件通知
			// 创建 NotificationManager，其中创建的 notificationManager 对象负责“发出”与“取消”
			// Notification。
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// 参数依次为：icon的资源id，在状态栏上展示的滚动信息，时间。
			Notification notification = new Notification(R.drawable.icon,
					Constant.SPECIAL_TITLE_UPDATE, System.currentTimeMillis());
			// 设置Notification的一些参数
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			String updateUrl = intent.getStringExtra("updateUrl");
			Uri uri = Uri.parse(updateUrl);
			Intent nIntent = new Intent(Intent.ACTION_VIEW, uri);//要跳转到的Activity
			nIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// 创建PendingIntent包装nIntent
			PendingIntent pIntent = PendingIntent.getActivity(context,
					R.string.update_info, nIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);// PendingIntent.getActivity的参数依次为：Context，发送者的请求码（可以填0），用于系统发送的Intent，标志位。
			notification.setLatestEventInfo(context, Constant.IT_APP,
					Constant.SPECIAL_TITLE_UPDATE, pIntent);
			notificationManager.notify(R.string.update_info, notification);
		}
	}

}
