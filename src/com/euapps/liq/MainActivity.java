package com.euapps.liq;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {

	private Calculator calculator;

	private Spinner inputUnits;

	private EditText diameterValue;
	private Spinner diameterUnit;

	private EditText lengthValue;
	private Spinner lengthUnit;

	private EditText roughnessValue;
	private Spinner roughnessUnit;
	private Spinner roughnessHint;

	private EditText flowMedium;

	private Spinner flowType;
	private EditText flowValue;
	private Spinner flowUnit;

	private EditText densityValue;
	private Spinner densityUnit;

	private Spinner viscosityType;
	private EditText viscosityValue;
	private Spinner viscosityUnit;

	private Spinner units;
	private Button clear;
	private Button calculate;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear:
			reset();
			return true;
		case R.id.go:
			calculate();
			return true;
		case R.id.info:
			Intent intent = new Intent(MainActivity.this, AboutActivity.class);
			this.startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		calculator = new Calculator(this);

		try {
			ComponentName comp = new ComponentName(this, this.getClass());
			PackageInfo pinfo = getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			setTitle(getString(R.string.app_name) + " "
					+ getString(R.string.version) + pinfo.versionName);
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			setTitle(getString(R.string.app_name));
		}

		inputUnits = (Spinner) findViewById(R.id.input_units);
		inputUnits.setAdapter(Util.getSpinnerAdapter(this, new String[] {
				getString(R.string.units_metrical),
				getString(R.string.units_us) }));
		inputUnits.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (inputUnits.getSelectedItemPosition() == 0)
					metricUnits();
				else
					usUnits();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});

		diameterValue = (EditText) findViewById(R.id.diameter_value);
		diameterUnit = (Spinner) findViewById(R.id.diameter_unit);
		diameterUnit.setAdapter(Util.getSpinnerAdapter(this, getResources()
				.getStringArray(R.array.length_small)));

		lengthValue = (EditText) findViewById(R.id.length_value);
		lengthUnit = (Spinner) findViewById(R.id.length_unit);
		lengthUnit.setAdapter(Util.getSpinnerAdapter(this, getResources()
				.getStringArray(R.array.length)));

		roughnessValue = (EditText) findViewById(R.id.roughness_value);
		roughnessUnit = (Spinner) findViewById(R.id.roughness_unit);
		roughnessHint = (Spinner) findViewById(R.id.roughness_hint);
		roughnessUnit.setAdapter(Util.getSpinnerAdapter(this, getResources()
				.getStringArray(R.array.length_small)));
		roughnessHint.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.pipe_roughness)));
		roughnessHint.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (roughnessUnit.getSelectedItemPosition() == 0)// mm
					roughnessValue.setText(getResources().getStringArray(
							R.array.pipe_roughness_values)[position]);
				else
					// inches
					roughnessValue.setText(Util.mm_to_inch(Double
							.parseDouble(getResources().getStringArray(
									R.array.pipe_roughness_values)[position]))
							+ "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});

		flowMedium = (EditText) findViewById(R.id.flow_medium);

		flowUnit = (Spinner) findViewById(R.id.flow_unit);
		flowType = (Spinner) findViewById(R.id.flow_type);
		flowType.setAdapter(Util.getSpinnerAdapter(this,
				new String[] { getString(R.string.volume_flow),
						getString(R.string.mass_flow) }));
		flowType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (position == 0)
					flowUnit.setAdapter(Util.getSpinnerAdapter(
							MainActivity.this, getResources().getStringArray(
									R.array.volume_flow)));
				else
					flowUnit.setAdapter(Util.getSpinnerAdapter(
							MainActivity.this, getResources().getStringArray(
									R.array.mass_flow)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
		flowValue = (EditText) findViewById(R.id.flow_value);

		densityValue = (EditText) findViewById(R.id.density_value);
		densityUnit = (Spinner) findViewById(R.id.density_unit);
		densityUnit.setAdapter(Util.getSpinnerAdapter(this, getResources()
				.getStringArray(R.array.density)));

		viscosityUnit = (Spinner) findViewById(R.id.viscosity_unit);
		viscosityType = (Spinner) findViewById(R.id.viscosity_type);
		viscosityType.setAdapter(Util.getSpinnerAdapter(this, new String[] {
				getString(R.string.dynamic_viscosity),
				getString(R.string.kinematin_viscosity) }));
		viscosityType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (position == 0)
					viscosityUnit.setAdapter(Util.getSpinnerAdapter(
							MainActivity.this, getResources().getStringArray(
									R.array.dynamic_viscosity)));
				else
					viscosityUnit.setAdapter(Util.getSpinnerAdapter(
							MainActivity.this, getResources().getStringArray(
									R.array.kinematic_viscosity)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
		viscosityValue = (EditText) findViewById(R.id.viscosity_value);

		units = (Spinner) findViewById(R.id.units);
		units.setAdapter(Util.getSpinnerAdapter(this, new String[] {
				getString(R.string.units_metrical),
				getString(R.string.units_us) }));

		clear = (Button) findViewById(R.id.clear);
		clear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				reset();
			}
		});

		calculate = (Button) findViewById(R.id.calculate);
		calculate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				calculate();
			}
		});

		reset();
		metricUnits();
	}

	private void calculate() {
		if (validateInput()) {
			calculator.calculate();
			final String calculation = calculator.toString();
			Util.showTwoActionsDialog(MainActivity.this,
					getString(R.string.result), calculation,
					getString(R.string.sms),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Util.sms(calculation, MainActivity.this);
							return;
						}
					}, getString(R.string.email),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Util.email(getString(R.string.app_name),
									calculation, MainActivity.this);
							return;
						}
					});
		}
	}

	private void reset() {
		diameterValue.setText("");
		lengthValue.setText("");
		roughnessValue.setText("");
		flowMedium.setText(getString(R.string.medium_default));
		flowValue.setText("");
		densityValue.setText(getString(R.string.weight_density_default));
		viscosityValue.setText("");
		diameterValue.requestFocus();
	}

	private void metricUnits() {
		diameterUnit.setSelection(0);
		lengthUnit.setSelection(0);
		roughnessUnit.setSelection(0);
		if (flowType.getSelectedItemPosition() == 0)// Volume
			flowUnit.setSelection(2);
		else
			// Mass
			flowUnit.setSelection(2);
		densityUnit.setSelection(0);
		if (viscosityType.getSelectedItemPosition() == 0)// Dynamic
			viscosityUnit.setSelection(0);
		else
			// Kinematic
			viscosityType.setSelection(0);
		units.setSelection(0);
	}

	private void usUnits() {
		diameterUnit.setSelection(1);
		lengthUnit.setSelection(1);
		roughnessUnit.setSelection(1);
		if (flowType.getSelectedItemPosition() == 0)// Volume
			flowUnit.setSelection(8);
		else
			// Mass
			flowUnit.setSelection(3);
		densityUnit.setSelection(5);
		if (viscosityType.getSelectedItemPosition() == 0)// Dynamic
			viscosityUnit.setSelection(7);
		else
			// Kinematic
			viscosityType.setSelection(4);
		units.setSelection(1);
	}

	private boolean validateInput() {
		calculator.setFlowMedium(flowMedium.getText().toString());
		calculator.setUnits(units.getSelectedItemPosition());
		try {
			calculator.setDiameterValue(Double.parseDouble(diameterValue
					.getText().toString()));
			calculator.setDiameterUnit(diameterUnit.getSelectedItemPosition());
		} catch (Exception e) {
			Util
					.showInputError(MainActivity.this,
							getString(R.string.diameter));
			diameterValue.requestFocus();
			return false;
		}
		try {
			calculator.setLengthValue(Double.parseDouble(lengthValue.getText()
					.toString()));
			calculator.setLengthUnit(lengthUnit.getSelectedItemPosition());
		} catch (Exception e) {
			Util.showInputError(MainActivity.this, getString(R.string.length));
			lengthValue.requestFocus();
			return false;
		}
		try {
			calculator.setRoughnessValue(Double.parseDouble(roughnessValue
					.getText().toString()));
			calculator
					.setRoughnessUnit(roughnessUnit.getSelectedItemPosition());
		} catch (Exception e) {
			Util.showInputError(MainActivity.this,
					getString(R.string.roughness));
			roughnessValue.requestFocus();
			return false;
		}
		try {
			calculator.setFlowValue(Double.parseDouble(flowValue.getText()
					.toString()));
			calculator.setFlowType(flowType.getSelectedItemPosition());
			calculator.setFlowUnit(flowUnit.getSelectedItemPosition());
		} catch (Exception e) {
			Util.showInputError(MainActivity.this, flowType.getSelectedItem()
					.toString());
			flowValue.requestFocus();
			return false;
		}
		try {
			calculator.setDensityValue(Double.parseDouble(densityValue
					.getText().toString()));
			calculator.setDensityUnit(densityUnit.getSelectedItemPosition());
		} catch (Exception e) {
			Util.showInputError(MainActivity.this,
					getString(R.string.weight_density));
			densityValue.requestFocus();
			return false;
		}
		try {
			calculator.setViscosityValue(Double.parseDouble(viscosityValue
					.getText().toString()));
			calculator
					.setViscosityType(viscosityType.getSelectedItemPosition());
			calculator
					.setViscosityUnit(viscosityUnit.getSelectedItemPosition());
		} catch (Exception e) {
			Util.showInputError(MainActivity.this, viscosityType
					.getSelectedItem().toString());
			viscosityValue.requestFocus();
			return false;
		}
		Log.d(getClass().getName(), calculator.toString());
		return true;
	}

}