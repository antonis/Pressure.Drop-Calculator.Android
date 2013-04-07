package com.euapps.liq;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;

public class Calculator {

	private static final NumberFormat formatter = new DecimalFormat("#.##");

	private Context context;

	private double diameterValue;
	private int diameterUnit;

	private double lengthValue;
	private int lengthUnit;

	private double roughnessValue;
	private int roughnessUnit;

	private String flowMedium;

	private double flowValue;
	private int flowType;
	private int flowUnit;

	private double densityValue;
	private int densityUnit;

	private double viscosityValue;
	private int viscosityType;
	private int viscosityUnit;

	private int units;

	private double velocity;
	private double friction;
	private double reynolds;

	private double result;

	public Calculator(Context context) {
		this.context = context;
	}

	public double calculate() {
		double e = E(); // Roughness height (m)
		double d = D(); // Pipe Diameter (m)
		double r = R(); // Density in (kg/m^3)
		double l = L(); // Pipe length (m)
		double fl = FL(r); // Flow in (m^3/s)
		velocity = V(fl, d); // Velocity (m/s)
		reynolds = Re(velocity, d, r); // Reynolds number
		friction = F(e, d, reynolds); // Friction factor
		result = DP(r, velocity, friction, l, d); // Presure Drop
		return result;
	}

	private double Re(double V, double l, double r) {
		if (viscosityType == 0)
			return ((r * V * l) / DV());
		else
			return ((V * l) / KV());
	}

	private double DV() {// Pas or Ns/m^2 or kg/ms
		if (viscosityUnit == 0)
			return viscosityValue * 0.000001;
		else if (viscosityUnit == 1)
			return Util.g_to_kg(viscosityValue) * 0.000001;
		else if (viscosityUnit == 2)
			return Util.g_to_kg(viscosityValue) * 0.000001 / Util.cms_to_ms(1);
		else if (viscosityUnit == 3)
			return viscosityValue * 0.000001;
		else if (viscosityUnit == 4)
			return Util.mPas_to_Pas(viscosityValue);
		else if (viscosityUnit == 5)
			return viscosityValue * 0.000001;
		else if (viscosityUnit == 6)
			return Util.cP_to_Pas(viscosityValue);
		else if (viscosityUnit == 7)
			return Util.lbAv_to_kg(viscosityValue)
					/ (Util.ft_to_m(1) * Util.hr_to_sec(1));
		else
			return Util.lbAv_to_kg(viscosityValue) / Util.ft_to_m(1);
	}

	private double KV() {// m^2/sec
		if (viscosityUnit == 0)
			return viscosityValue * 0.000001;
		else if (viscosityUnit == 1)
			return Util.cm2_to_m2(viscosityValue);
		else if (viscosityUnit == 2)
			return Util.mm2_to_m2(viscosityValue);
		else if (viscosityUnit == 3)
			return Util.cSt_to_m2perSec(viscosityValue);
		else if (viscosityUnit == 4)
			return Util.ft2_to_m2(viscosityValue) / Util.hr_to_sec(1);
		else
			return Util.ft2_to_m2(viscosityValue);
	}

	private double V(double fl, double d) {
		return fl / (Math.PI * Math.pow(d / 2, 2));
	}

	private double FL(double r) {
		if (flowType == 0) {
			if (flowUnit == 0)
				return flowValue / Util.hr_to_sec(1);
			else if (flowUnit == 1)
				return flowValue / Util.min_to_sec(1);
			else if (flowUnit == 2)
				return flowValue;
			else if (flowUnit == 3)
				return Util.Nm3_to_m3(flowValue) / Util.hr_to_sec(1);
			else if (flowUnit == 4)
				return Util.Nm3_to_m3(flowValue) / Util.min_to_sec(1);
			else if (flowUnit == 5)
				return Util.Nm3_to_m3(flowValue);
			else if (flowUnit == 6)
				return Util.lit_to_m3(flowValue) / Util.min_to_sec(1);
			else if (flowUnit == 7)
				return Util.lit_to_m3(flowValue);
			else if (flowUnit == 8)
				return Util.ft3_to_m3(flowValue) / Util.hr_to_sec(1);
			else if (flowUnit == 9)
				return Util.ft3_to_m3(flowValue) / Util.min_to_sec(1);
			else if (flowUnit == 10)
				return Util.ft3_to_m3(flowValue);
			else if (flowUnit == 11)
				return Util.petrBr_to_m3(flowValue) / Util.hr_to_sec(1);
			else if (flowUnit == 12)
				return Util.petrBr_to_m3(flowValue) / Util.day_to_sec(1);
			else if (flowUnit == 13)
				return Util.gal_to_m3(flowValue) / Util.hr_to_sec(1);
			else if (flowUnit == 14)
				return Util.gal_to_m3(flowValue) / Util.min_to_sec(1);
			else //MGD
				return Util.mgd_to_m3(flowValue);
		} else {
			if (flowUnit == 0)
				return (flowValue / Util.hr_to_sec(1)) / r;
			else if (flowUnit == 1)
				return (flowValue / Util.min_to_sec(1)) / r;
			else if (flowUnit == 2)
				return (flowValue) / r;
			else if (flowUnit == 3)
				return (Util.lb_to_kg(flowValue) / Util.hr_to_sec(1)) / r;
			else if (flowUnit == 4)
				return (Util.lb_to_kg(flowValue) / Util.min_to_sec(1)) / r;
			else if (flowUnit == 5)
				return (Util.lb_to_kg(flowValue)) / r;
			else if (flowUnit == 6)
				return (Util.oz_to_kg(flowValue) / Util.min_to_sec(1)) / r;
			else
				return (Util.oz_to_kg(flowValue)) / r;
		}
	}

	private double R() {
		if (densityUnit == 0)
			return densityValue;
		else if (densityUnit == 1)
			return densityValue / Util.lit_to_m3(1);
		else if (densityUnit == 2)
			return Util.g_to_kg(densityValue) / Util.lit_to_m3(1);
		else if (densityUnit == 3)
			return Util.g_to_kg(densityValue) / Util.ft3_to_m3(1);
		else if (densityUnit == 4)
			return Util.g_to_kg(densityValue) / Util.gal_to_m3(1);
		else if (densityUnit == 5)
			return Util.oz_to_kg(densityValue) / Util.ft3_to_m3(1);
		else if (densityUnit == 6)
			return Util.oz_to_kg(densityValue) / Util.in3_to_m3(1);
		else if (densityUnit == 7)
			return Util.oz_to_kg(densityValue) / Util.gal_to_m3(1);
		else if (densityUnit == 8)
			return Util.lb_to_kg(densityValue) / Util.ft3_to_m3(1);
		else if (densityUnit == 9)
			return Util.lb_to_kg(densityValue) / Util.in3_to_m3(1);
		else
			return Util.lb_to_kg(densityValue) / Util.gal_to_m3(1);
	}

	private double L() {
		if (lengthUnit == 0)
			return lengthValue;
		else
			return Util.ft_to_m(lengthValue);
	}

	private double D() {
		if (diameterUnit == 0)
			return Util.mm_to_m(diameterValue);
		else
			return Util.in_to_m(diameterValue);
	}

	private double E() {
		if (roughnessUnit == 0)
			return Util.mm_to_m(roughnessValue);
		else
			return Util.in_to_m(roughnessValue);
	}

	private double DP(double r, double V, double f, double l, double d) {
		return Util.pas_to_bar(((r * Math.pow(V, 2)) / 2) * ((f * l) / d));
	}

	private double F(double e, double D, double Re) {
		if (Re < 2000)
			return 64 / Re;
		else
			return (0.25 / Math.pow(Math.log10((e / (3.7 * D))
					+ (5.74 / Math.pow(Re, 0.9))), 2));
	}

	public void setDiameterValue(double diameterValue) {
		this.diameterValue = diameterValue;
	}

	public void setDiameterUnit(int diameterUnit) {
		this.diameterUnit = diameterUnit;
	}

	public void setLengthValue(double lengthValue) {
		this.lengthValue = lengthValue;
	}

	public void setLengthUnit(int lengthUnit) {
		this.lengthUnit = lengthUnit;
	}

	public void setRoughnessValue(double roughnessValue) {
		this.roughnessValue = roughnessValue;
	}

	public void setRoughnessUnit(int roughnessUnit) {
		this.roughnessUnit = roughnessUnit;
	}

	public void setFlowMedium(String flowMedium) {
		this.flowMedium = flowMedium;
	}

	public void setFlowValue(double flowValue) {
		this.flowValue = flowValue;
	}

	public void setFlowUnit(int flowUnit) {
		this.flowUnit = flowUnit;
	}

	public void setDensityValue(double densityValue) {
		this.densityValue = densityValue;
	}

	public void setFlowType(int flowType) {
		this.flowType = flowType;
	}

	public void setDensityUnit(int densityUnit) {
		this.densityUnit = densityUnit;
	}

	public void setViscosityValue(double viscosityValue) {
		this.viscosityValue = viscosityValue;
	}

	public void setViscosityType(int viscosityType) {
		this.viscosityType = viscosityType;
	}

	public void setViscosityUnit(int viscosityUnit) {
		this.viscosityUnit = viscosityUnit;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return context.getString(R.string.result)
				+ " = "
				+ (units == 0 ? (formatter.format(result) + " " + context
						.getString(R.string.bar)) : (formatter.format(Util
						.bar_to_psi(result))
						+ " " + context.getString(R.string.psi)))
				+ "\n\n"
				+ context.getString(R.string.diameter)
				+ " = "
				+ diameterValue
				+ " "
				+ context.getResources().getStringArray(R.array.length_small)[diameterUnit]
				+ "\n"
				+ context.getString(R.string.length)
				+ " = "
				+ lengthValue
				+ " "
				+ context.getResources().getStringArray(R.array.length)[lengthUnit]
				+ "\n"
				+ context.getString(R.string.roughness)
				+ " = "
				+ roughnessValue
				+ " "
				+ context.getResources().getStringArray(R.array.length_small)[roughnessUnit]
				+ "\n"
				+ context.getString(R.string.medium)
				+ " = "
				+ flowMedium
				+ "\n"
				+ (flowType == 0 ? context.getString(R.string.volume_flow)
						+ " = "
						+ flowValue
						+ " "
						+ context.getResources().getStringArray(
								R.array.volume_flow)[flowUnit] + "\n" : context
						.getString(R.string.mass_flow)
						+ " = "
						+ flowValue
						+ " "
						+ context.getResources().getStringArray(
								R.array.mass_flow)[flowUnit] + "\n")
				+ context.getString(R.string.weight_density)
				+ " = "
				+ densityValue
				+ " "
				+ context.getResources().getStringArray(R.array.density)[densityUnit]
				+ "\n"
				+ (viscosityType == 0 ? context
						.getString(R.string.dynamic_viscosity)
						+ " = "
						+ viscosityValue
						+ " "
						+ context.getResources().getStringArray(
								R.array.dynamic_viscosity)[viscosityUnit]
						+ "\n" : context
						.getString(R.string.kinematin_viscosity)
						+ " = "
						+ viscosityValue
						+ " "
						+ context.getResources().getStringArray(
								R.array.kinematic_viscosity)[viscosityUnit]
						+ "\n")
				+ context.getString(R.string.units)
				+ " = "
				+ (units == 0 ? context.getString(R.string.units_metrical)
						: context.getString(R.string.units_us)) + "\n"
				+ context.getString(R.string.reynolds) + " = "
				+ formatter.format(reynolds) + "\n"
				+ context.getString(R.string.friction) + " = "
				+ formatter.format(friction) + "\n"
				+ context.getString(R.string.velocity) + " = "
				+ formatter.format(velocity) + " "
				+ context.getString(R.string.m_per_s) + "\n";
	}
}