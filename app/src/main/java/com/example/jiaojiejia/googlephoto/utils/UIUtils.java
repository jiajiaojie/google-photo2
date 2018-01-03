package com.example.jiaojiejia.googlephoto.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.jiaojiejia.googlephoto.application.MyApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UIUtils {

	private static int screenHeight;
	private static int screenWidth;
	private static float screenDensity;
	private static DisplayMetrics displayMetrics;

	private static Handler mHandler;

	private static Context getContext() {
		return MyApplication.getContext();
	}

	public static void initHandler() {
		mHandler = new Handler();
	}

	/** dip转换px */
	public static int dip2px(float dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** px转换dip */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/** sp值转换为px值 */
	public static int sp2px(float spValue) {
		final float fontScale = UIUtils.getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/** 判断当前的线程是不是在主线程 */
//	public static boolean isRunInMainThread() {
//		return android.os.Process.myTid() == android.os.Process.myTid();
//	}

	private static Toast mToast = null;

	public static void showToast(final String str) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
				} else {
					mToast.setText(str);
					mToast.setDuration(Toast.LENGTH_SHORT);
				}

				mToast.show();
			}
		});
	}

	public static void setVisibility(View view, int visibility) {
		if(view.getVisibility() != visibility) {
			view.setVisibility(visibility);
		}
	}

	/** 显示键盘 */
	public static void showKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	/** 影藏键盘 */
	public static void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/** 切换键盘显示状态 */
	public static void toggleSoftInput() {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static int getScreenHeight() {
		if (screenHeight <= 0) {
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics outMetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(outMetrics);
			screenHeight = outMetrics.heightPixels;
			screenWidth = outMetrics.widthPixels;
		}
		return screenHeight;
	}
	public static int getScreenWidth() {
		if (screenWidth <= 0) {
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics outMetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(outMetrics);
			screenHeight = outMetrics.heightPixels;
			screenWidth = outMetrics.widthPixels;
		}
		return screenWidth;
	}

	public static float getScreenDensity() {
		if (screenDensity <= 0) {
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics outMetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(outMetrics);
			screenHeight = outMetrics.heightPixels;
			screenWidth = outMetrics.widthPixels;
			screenDensity = outMetrics.density;
		}
		return screenDensity;
	}

	public static DisplayMetrics getDisplayMetrics() {
		if (displayMetrics == null) {
			displayMetrics = getContext().getResources().getDisplayMetrics();
		}
		return displayMetrics;
	}

	//判断是否有虚拟按键
	public static boolean checkDeviceHasNavigationBar(Context context) {
		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasNavigationBar = rs.getBoolean(id);
		}
		try {
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return hasNavigationBar;

	}
	// 获取NavigationBar的高度：
	public static int getNavigationBarHeight(Context context) {
		int navigationBarHeight = 0;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if (id > 0 && checkDeviceHasNavigationBar(context)) {
			navigationBarHeight = rs.getDimensionPixelSize(id);
		}
		return navigationBarHeight;
	}

	// 获取StatusBar的高度：
	public static int getStatusBarHeight() {
		int result = 0;
		int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getContext().getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}





