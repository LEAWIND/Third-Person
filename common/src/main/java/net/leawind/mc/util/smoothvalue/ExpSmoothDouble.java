package net.leawind.mc.util.smoothvalue;


import net.minecraft.util.Mth;

@SuppressWarnings("unused")
public class ExpSmoothDouble extends ExpSmoothValue<Double> {
	public ExpSmoothDouble () {
		value              = 0d;
		target             = 0d;
		smoothFactor       = 0d;
		smoothFactorWeight = 1d;
	}

	public ExpSmoothDouble setValue (double value) {
		this.value = value;
		return this;
	}

	public ExpSmoothDouble setTarget (double target) {
		this.target = target;
		return this;
	}

	public ExpSmoothDouble setSmoothFactor (double k, double t) {
		this.smoothFactor = Math.pow(k, 1 / t);
		return this;
	}

	public ExpSmoothDouble setSmoothFactorWeight (double weight) {
		this.smoothFactorWeight = weight;
		return this;
	}

	public ExpSmoothDouble setSmoothFactorWeight (Double weight) {
		this.smoothFactorWeight = weight;
		return this;
	}

	@Override
	public ExpSmoothDouble setTarget (Double target) {
		this.target = target;
		return this;
	}

	@Override
	public ExpSmoothDouble update (double period) {
		super.preUpdate();
		value = Mth.lerp(1 - Math.pow(smoothFactor, smoothFactorWeight * period), value, target);
		return this;
	}

	@Override
	public ExpSmoothDouble set (Double v) {
		value = target = v;
		return this;
	}

	@Override
	public ExpSmoothDouble setValue (Double v) {
		value = v;
		return this;
	}

	@Override
	public ExpSmoothDouble setSmoothFactor (Double smoothFactor) {
		this.smoothFactor = smoothFactor;
		return this;
	}

	@Override
	public Double get (double delta) {
		return Mth.lerp(delta, lastValue, value);
	}

	@Override
	public ExpSmoothValue<Double> setSmoothFactor (double smoothFactor) {
		this.smoothFactor = smoothFactor;
		return this;
	}

	@Override
	public ExpSmoothDouble setSmoothFactor (Double k, Double t) {
		this.smoothFactor = Math.pow(k, 1 / t);
		return this;
	}
}
