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
		if (Constant.SOCKETTIMEOUT.equals(action)) {// ���ӳ�ʱ
			Toast.makeText(context, R.string.socket_timeout, Toast.LENGTH_SHORT)
					.show();
		}
		if (Constant.UPDATE_ITREADER_NOTIFY.equals(action)) {// �������֪ͨ
			// ���� NotificationManager�����д����� notificationManager �����𡰷������롰ȡ����
			// Notification��
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// ��������Ϊ��icon����Դid����״̬����չʾ�Ĺ�����Ϣ��ʱ�䡣
			Notification notification = new Notification(R.drawable.icon,
					Constant.SPECIAL_TITLE_UPDATE, System.currentTimeMillis());
			// ����Notification��һЩ����
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			String updateUrl = intent.getStringExtra("updateUrl");
			Uri uri = Uri.parse(updateUrl);
			Intent nIntent = new Intent(Intent.ACTION_VIEW, uri);//Ҫ��ת����Activity
			nIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// ����PendingIntent��װnIntent
			PendingIntent pIntent = PendingIntent.getActivity(context,
					R.string.update_info, nIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);// PendingIntent.getActivity�Ĳ�������Ϊ��Context�������ߵ������루������0��������ϵͳ���͵�Intent����־λ��
			notification.setLatestEventInfo(context, Constant.IT_APP,
					Constant.SPECIAL_TITLE_UPDATE, pIntent);
			notificationManager.notify(R.string.update_info, notification);
		}
	}

}
