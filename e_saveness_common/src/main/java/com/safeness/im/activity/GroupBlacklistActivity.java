package com.safeness.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.safeness.e_saveness_common.R;

import java.util.Collections;
import java.util.List;

public class GroupBlacklistActivity extends BaseActivity {
	private ListView listView;
	private ProgressBar progressBar;
	private BlacklistAdapter adapter;
	private String groupId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_group_blacklist);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		listView = (ListView) findViewById(R.id.list);

		groupId = getIntent().getStringExtra("groupId");
		// 注册上下文菜单
		registerForContextMenu(listView);
		new Thread(new Runnable() {

			public void run() {
				try {
					List<String> blockedList = EMGroupManager.getInstance().getBlockedUsers(groupId);
					if(blockedList != null){
						Collections.sort(blockedList);
						adapter = new BlacklistAdapter(GroupBlacklistActivity.this, 1, blockedList);
						runOnUiThread(new Runnable() {
							public void run() {
								listView.setAdapter(adapter);
								progressBar.setVisibility(View.INVISIBLE);
							}
						});
					}
				} catch (EaseMobException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "获取失败,请检查网络或稍后再试", Toast.LENGTH_SHORT).show();
							progressBar.setVisibility(View.INVISIBLE);
						}
					});
				}
			}
		}).start();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.remove_from_blacklist, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.remove) {
			final String tobeRemoveUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 移出黑名单
			removeOutBlacklist(tobeRemoveUser);
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * 移出黑民单
	 * 
	 * @param tobeRemoveUser
	 */
	void removeOutBlacklist(final String tobeRemoveUser) {
		try {
			// 移出黑民单
			EMGroupManager.getInstance().unblockUser(groupId, tobeRemoveUser);
			adapter.remove(tobeRemoveUser);
		} catch (EaseMobException e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "移出失败", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	/**
	 * adapter
	 * 
	 */
	private class BlacklistAdapter extends ArrayAdapter<String> {

		public BlacklistAdapter(Context context, int textViewResourceId, List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.row_contact, null);
			}

			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(getItem(position));

			return convertView;
		}

	}
	
}
