package com.euapps.liq;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.widget.ArrayAdapter;

public class Util {

	public static double mm_to_inch(double mm) {
		return mm * 0.0393700787;
	}

	public static double pas_to_bar(double pas) {
		return pas * 0.00001;
	}

	public static double cms_to_ms(double cms) {
		return cms * 0.01;
	}

	public static double bar_to_psi(double bar) {
		return bar * 14.5037738;
	}

	public static double lbAv_to_kg(double lbAv) {
		return lbAv * 0.45359237;
	}

	public static double mPas_to_Pas(double mPas) {
		return mPas * 0.001;
	}

	public static double cP_to_Pas(double cP) {
		return cP * 0.001;
	}

	public static double cSt_to_m2perSec(double cSt) {
		return cSt * 0.000001;
	}

	public static double ft2_to_m2(double ft2) {
		return ft2 * 0.09290304;
	}

	public static double mm2_to_m2(double mm) {
		return mm * 0.000001;
	}

	public static double cm2_to_m2(double cm) {
		return cm * 0.0001;
	}

	public static double petrBr_to_m3(double petrBr) {
		return petrBr * 0.158987295;
	}

	public static double Nm3_to_m3(double Nm3) {
		return Nm3;
	}

	public static double min_to_sec(double min) {
		return min * 60;
	}

	public static double hr_to_sec(double hr) {
		return hr * 3600;
	}

	public static double day_to_sec(double hr) {
		return hr * 86400;
	}

	public static double lb_to_kg(double lb) {
		return lb * 0.45359237;
	}

	public static double in3_to_m3(double gal) {
		return gal * 1.6387064 * 0.00001;
	}

	public static double oz_to_kg(double oz) {
		return oz * 0.0283495231;
	}

	public static double gal_to_m3(double gal) {
		return gal * 0.00378541178;
	}

	public static double ft3_to_m3(double ft3) {
		return ft3 * 0.0283168466;
	}

	public static double g_to_kg(double g) {
		return g / 1000;
	}

	public static double lit_to_m3(double lit) {
		return lit / 1000;
	}

	public static double ft_to_m(double ft) {
		return ft * 0.3048;
	}

	public static double mm_to_m(double mm) {
		return mm / 1000;
	}

	public static double in_to_m(double in) {
		return in * 0.0254;
	}

	public static double mgd_to_m3(double mgd) {
		return mgd / 22.8245;
	}

	public static void showInputError(Activity activity, String field) {
		Util.showInfo(activity, activity.getResources().getString(
				R.string.error), field + " "
				+ activity.getString(R.string.not_valid));
	}

	public static void showInfo(Activity activity, String title, String message) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(activity)
					.create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			alertDialog.setButton(activity.getResources()
					.getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alertDialog.show();
		} catch (Exception e) {
		}
	}

	public static void showTwoActionsDialog(Activity activity, String title,
			String message, String msgOne,
			DialogInterface.OnClickListener actionOne, String msgTwo,
			DialogInterface.OnClickListener actionTwo) {
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(activity)
					.create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, msgOne,
					actionOne);
			alertDialog
					.setButton(AlertDialog.BUTTON_NEUTRAL, msgTwo, actionTwo);
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, activity
					.getResources().getString(R.string.done),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
			alertDialog.show();
		} catch (Exception e) {
		}
	}

	public static ArrayAdapter<String> getSpinnerAdapter(Context context,
			String[] values) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, values);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	public static void email(String title, String body, Activity activity) {
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/html");
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body, null, null));
		activity.startActivity(Intent.createChooser(sendIntent, activity
				.getResources().getString(R.string.email)));
	}

	public static void sms(String msg, Activity activity) {
		try {
			Uri smsUri = Uri.parse("tel:");
			Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
			intent.putExtra("sms_body", msg);
			intent.setType("vnd.android-dir/mms-sms");
			activity.startActivity(intent);
		} catch (Exception e) {
			Util.showInfo(activity, activity.getString(R.string.app_name),
					activity.getString(R.string.sms_error));
		}
	}

}
