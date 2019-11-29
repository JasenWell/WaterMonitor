package com.android.zht.waterwatch.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.zht.waterwatch.R;
import com.android.zht.waterwatch.bean.VersionInfo;
import com.android.zht.waterwatch.net.HttpHelper;
import com.android.zht.waterwatch.net.HttpType;
import com.android.zht.waterwatch.ui.LoginActivity;
import com.android.zht.waterwatch.ui.MainActivity;
import com.android.zht.waterwatch.ui.ModifyPwdActivity;
import com.android.zht.waterwatch.widget.AppTitleLayout;
import com.android.zht.waterwatch.widget.CommonHintDialog;
import com.android.zht.waterwatch.widget.ShowItemLayout;
import com.hjh.baselib.base.LBaseFragment;
import com.hjh.baselib.constants.ModuleConfig;
import com.hjh.baselib.entity.RequestData;
import com.hjh.baselib.utils.AppPresences;
import com.hjh.baselib.utils.AppUtil;
import com.hjh.baselib.utils.AppVersionManager;
import com.hjh.baselib.utils.PathUtil;
import com.hjh.baselib.widget.AppActivityManager;
import com.hjh.baselib.widget.PullToRefreshLayout;
import com.hjh.baselib.widget.PullableScrollView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author hjh
 * 2016-5-21下午9:26:41
 */
public class MineFragment extends LBaseFragment implements ShowItemLayout.OnClickItemListener {

	@BindView(R.id.title_bar)
	AppTitleLayout mTitleLayout;

	@BindView(R.id.suggest_layout)
	ShowItemLayout mSuggestLayout;

	@BindView(R.id.cache_layout)
	ShowItemLayout mCacheLayout;

	@BindView(R.id.pwd_layout)
	ShowItemLayout mPwdLayout;

	@BindView(R.id.version_layout)
	ShowItemLayout mVersionLayout;

	@BindView(R.id.login_out_btn)
	Button mButton;

	/**
	 * 传入fragment的参数
	 */
	protected Bundle fragmentArgs;
	private MainActivity mActivity;
	private Intent intent = new Intent();
	private CommonHintDialog updateDialog;
	private CommonHintDialog mDialog;

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 999){
				try {
					mCacheLayout.setContent(AppUtil.getInstance().getTotalCacheSize(mActivity));
					mCacheLayout.setFlag(2,MineFragment.this);
					showToast("已清除!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	};

	public MineFragment(){

	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onAfterCreate(Bundle savedInstanceState) {
		super.onAfterCreate(savedInstanceState);
		mTitleLayout.setTitleText("个人信息");
		mTitleLayout.disableLeftButton();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentArgs = getArguments();
		AppVersionManager.init(mActivity);
		AppVersionManager.getInstance().configApkPath(PathUtil.getInstance().getFilePath(), "water_watch.apk");
		mSuggestLayout.setFlag(1,this);
		mCacheLayout.setFlag(2, this);
		mPwdLayout.setFlag(3, this);
		mVersionLayout.setFlag(4, this);
		try {
			mVersionLayout.setContent(AppVersionManager.getInstance().getAppVersionName(mActivity));
			mCacheLayout.setContent(AppUtil.getInstance().getTotalCacheSize(mActivity));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getContentLayout() {
		return R.layout.fragment_mine;
	}

	@OnClick({R.id.login_out_btn})
	public void onViewClicked(View view){
		if(view.getId() == R.id.login_out_btn){
			loginOut();
		}
	}

	private void loginOut(){
		if(mDialog == null){
			mDialog = new CommonHintDialog(mActivity, new CommonHintDialog.OnClickHintDialogListener() {

				@Override
				public boolean onClickItem(int index) {
					if(index == 0){//确定
						if(!AppPresences.getInstance().getString(ModuleConfig.KEY_TOKEN).equals("")) {
							AppPresences.getInstance().putString(HttpHelper.ACTION.KEY_USER_ID,"");
							AppPresences.getInstance().putString(ModuleConfig.KEY_TOKEN, "");
							AppPresences.getInstance().putString(HttpHelper.ACTION.KEY_USER_PHONE,"");
							mActivity.startActivityWithAnim(mActivity, new Intent(mActivity, LoginActivity.class));
							AppActivityManager.getInstance().finishAllActivity();
						}
					}

					return true;
				}
			});

			mDialog.setContent("确认退出?").setLeftText("确定").setRightText("取消")
					.setTitleVisibility(View.GONE).setNoticeVisibility(View.VISIBLE);
		}

		mDialog.show();
	}

	private void onRequest(){

	}

	public MineFragment setOwner(MainActivity mainActivity){
		mActivity = mainActivity;
		return this;
	}

    @Override
    public void onPause() {
    	super.onPause();
    }

	@Override
    public void onResume() {
    	super.onResume();
		onRequest();
    }

	@Override
    public void onRefreshView(){
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClickItem(int flag) {
		switch (flag) {
			case 1:
//				intent.setClass(mActivity, SuggestActivity.class);
//				mActivity.startActivityWithAnim(mActivity, intent);
				break;
			case 2:
				new Thread(new Runnable() {

					@Override
					public void run() {
						PathUtil.getInstance().deleteFile(PathUtil.getInstance().generatePath(mActivity, "cache"));
						AppUtil.getInstance().clearAllCache(mActivity);
						mHandler.sendEmptyMessage(999);
					}
				}).start();

				mCacheLayout.setFlag(2,null);
				break;
			case 3://修改密码
				intent.setClass(mActivity, ModifyPwdActivity.class);
				mActivity.startActivityWithAnim(mActivity, intent);
				break;
			case 4:
				checkVersion();
				break;
			default:
				break;
		}
	}

	private void checkVersion(){
		RequestData requestData = new RequestData(HttpType.CHECK_UPDATE);
		Map<String,String> map = new HashMap<>();
		map.put("account",AppPresences.getInstance().getString(ModuleConfig.KEY_LOGIN_ACCOUNT));
		map.put("userId",AppPresences.getInstance().getString(ModuleConfig.KEY_USER_ID));
		map.put("clientType","1");
		map.put("clientCode",AppVersionManager.getInstance().getAppVersionCode(mActivity)+"");
		requestData.setRequestParameters(map);
		mActivity.showLoadDialog();
//				VersionInfo info = responseJson.getData();
//				info.setForceUpgrade(false);
//				if(info.isForceUpgrade()){
//					AppVersionManager.getInstance().updateApk(info.getApkPath(), updateListener);
//				}else {
//					if(info.getMajorCode() <= AppVersionManager.getInstance().getAppVersionCode(mActivity)){
//						showToast("已是最新版本");
//					}else {
//						showUpdateDialog(info);
//					}
//				}

		//test
//		ResponseVersion version = new ResponseVersion();
//		version.setContent("1、修复细节bug&2、修复登录异常");
	}

	private void showUpdateDialog(final VersionInfo version){
		if(updateDialog == null){
			updateDialog = new CommonHintDialog(mActivity, new CommonHintDialog.OnClickHintDialogListener() {
				@Override
				public boolean onClickItem(int index) {
					if(index == 0){
						AppVersionManager.getInstance().updateApk(version.getApkPath(),updateListener);
					}

					return true;
				}
			});

			updateDialog.setTitleVisibility(View.GONE).setNoticeVisibility(View.VISIBLE)
					.setContent("已检查到新版本")
					.setNextContentText(version.getUpdateText().replaceAll("&","\n"))
					.setLeftText("更新").setRightText("忽略")
					.setContentColor(getAppColor(R.color.main_color))
					.setLayoutWidth(ModuleConfig.SCREEN_WIDTH - getPixel(R.dimen.margin_normal_size)*8);
		}

		updateDialog.show();
	}

	private AppVersionManager.OnUpdateListener updateListener = new AppVersionManager.OnUpdateListener() {
		@Override
		public void onClickBackground() {
			showToast("转为后台下载");
		}

		@Override
		public void onDownLoadFailed() {
			showToast("下载失败");
		}

		@Override
		public void onPause() {
			showToast("暂停下载");
		}

		@Override
		public Activity getActivity() {
			return mActivity;
		}
	};

}
