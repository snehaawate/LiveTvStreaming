package com.sneha.livestreaming.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.sneha.livestreaming.R;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {


    public static String mCurrentPath;
	public static Toast toast;
	public static ProgressDialog progressDialog;


	public static void showProgressDialog() {
		progressDialog = new ProgressDialog(Global.getAppContext());
		progressDialog.setMessage("loading");
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}


	public static void hideProgressDialog() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * @param context
	 * @return Returns true if there is network connectivity
	 */
	public static boolean isInternetConnection(final Context context, ViewGroup view, View.OnClickListener onClickListener) {
		boolean HaveConnectedWifi = false;
		boolean HaveConnectedMobile = false;

		try {

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				if (ni.getType() == ConnectivityManager.TYPE_WIFI)
					if (ni.isConnectedOrConnecting())
						HaveConnectedWifi = true;
				if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
					if (ni.isConnectedOrConnecting())
						HaveConnectedMobile = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(HaveConnectedMobile || HaveConnectedWifi){

		}else {
			if(view!=null) {
				Snackbar.make(view, "Had a snack at Snackbar", 8000).setText(context.getResources().getString(R.string.no_internet_title)).setAction(
						context.getResources().getString(R.string.retry), onClickListener
				).setActionTextColor(context.getResources().getColor(R.color.sky_blue)).show();
			}
			else {
				Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
			}
		}
		return HaveConnectedWifi || HaveConnectedMobile;
	}


	/**
	 * Display Toast Message
	 **/
	public static void showToastMessageShort(Context context, String message) {

		if (toast == null)
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		else
			toast.setText(message);

		toast.show();
	}

	/**
	 * Display Toast Message
	 **/
	public static void showToastMessageLong(Context context, String message) {
		if (toast == null)
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		else
			toast.setText(message);

		toast.show();
	}



	/**
	 * Get IP address from first non-localhost interface
	 *
	 * @param useIPv4
	 *            true=return ipv4, false=return ipv6
	 * @return address or empty string
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						// boolean isIPv4 =
						// InetAddressUtils.isIPv4Address(sAddr);
						boolean isIPv4 = sAddr.indexOf(':') < 0;

						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 zone
																// suffix
								return delim < 0 ? sAddr.toUpperCase() : sAddr
										.substring(0, delim).toUpperCase();
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
	}


	/**
	 * @param context
	 * @return Returns true if there is network connectivity
	 */
	public static boolean isInternetConnection(Context context) {
		boolean HaveConnectedWifi = false;
		boolean HaveConnectedMobile = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				if (ni.getType() == ConnectivityManager.TYPE_WIFI)
					if (ni.isConnectedOrConnecting())
						HaveConnectedWifi = true;
				if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
					if (ni.isConnectedOrConnecting())
						HaveConnectedMobile = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return HaveConnectedWifi || HaveConnectedMobile;
	}

	 public void showNoInternetDialog(final Activity mContext) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
					R.style.AppTheme);
			builder.setTitle(mContext.getResources().getString(R.string.no_internet_title));
			builder.setMessage(mContext.getResources().getString(R.string.no_internet));
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
				}
			});

			builder.show();
		} catch (Exception e) {
			showToastMessageLong(mContext,
					mContext.getResources().getString(R.string.no_internet));
		}
	}


	 public static void showAlertWithSingleButton(Context context, String message, final OnAlertOkClickListener onAlertOkClickListener){
		AlertDialog.Builder builder =
				new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
		builder.setTitle("Error");
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onAlertOkClickListener.onOkButtonClicked();
				dialog.dismiss();
			}
		});
		builder.show();
	}

	public static String getColoredSpanned(String text, String color) {
		String input = "<font color=" + color + ">" + text + "</font>";
		return input;


	}

	public static boolean containAlphanumeric(final String str) {
		byte counter = 0;
		boolean checkdigit = false, checkchar = false;
		for (int i = 0; i < str.length() && counter < 2; i++) {
			// If we find a non-digit character we return false.
			if (!checkdigit && Character.isDigit(str.charAt(i))) {
				checkdigit = true;
				counter++;
			}
			String a = String.valueOf(str.charAt(i));
			if (!checkchar && a.matches("[a-zA-Z]*")) {
				checkchar = true;
				counter++;
			}
		}
		if (checkdigit && checkchar) {
			return true;
		}
		return false;
	}
	/**
	 * For email verification valid or not
	 */

	public static boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}


	public interface OnAlertOkClickListener {
		 void onOkButtonClicked();
	}



	//print Hash Key
	public static String printKeyHash(Context context) {
		PackageInfo packageInfo;
		String key = null;
		try {
			//getting application package name, as defined in manifest
			String packageName = context.getApplicationContext().getPackageName();

			//Retriving package info
			packageInfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);

			Log.e("Package Name=", context.getApplicationContext().getPackageName());

			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));

				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key_Hash=", key);
			}
		} catch (PackageManager.NameNotFoundException e1) {
			Log.e("Name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("No such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}
		return key;
	}




	public static void hideKeyboard(Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	public static void shareApp(Context context) {
		// share app with your friends
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/*");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
		shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this Live Tv Streaming App: https://play.google.com/store/apps/details?id=" + context.getPackageName());
		context.startActivity(Intent.createChooser(shareIntent, "Share Using"));
	}



	public static float dpToPx(Context context, float valueInDp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
	}


}
