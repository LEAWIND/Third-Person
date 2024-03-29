package net.leawind.mc.util.math.vector.impl;


import net.leawind.mc.util.math.vector.api.Vector2d;
import org.jetbrains.annotations.NotNull;

public class Vector2dImpl implements Vector2d {
	private double x;
	private double y;

	public Vector2dImpl (double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode () {
		int  l = 31, r = 1;
		long t;
		t = Double.doubleToLongBits(x);
		r = l * r + (int)(t ^ (t >>> 32));
		t = Double.doubleToLongBits(y());
		r = l * r + (int)(t ^ (t >>> 32));
		return r;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Vector2d other = (Vector2d)obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x())) {
			return false;
		}
		return Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y());
	}

	@Override
	public String toString () {
		return String.format("Vector2d(%f, %f)", x(), y());
	}

	@Override
	public double x () {
		return x;
	}

	@Override
	public double y () {
		return y;
	}

	@Override
	public void x (double x) {
		this.x = x;
	}

	@Override
	public void y (double y) {
		this.y = y;
	}

	@Override
	public Vector2d set (double d) {
		return set(d, d);
	}

	@Override
	public Vector2d set (@NotNull Vector2d v) {
		return set(v.x(), v.y());
	}

	@Override
	public Vector2d set (double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Vector2d add (@NotNull Vector2d v, @NotNull Vector2d dest) {
		return add(v.x(), v.y(), dest);
	}

	@Override
	public Vector2d add (double x, double y, @NotNull Vector2d dest) {
		dest.x(this.x + x);
		dest.y(this.y + y);
		return dest;
	}

	@Override
	public Vector2d add (@NotNull Vector2d v) {
		return add(v.x(), v.y());
	}

	@Override
	public Vector2d add (double d) {
		return add(d, d);
	}

	@Override
	public Vector2d add (double x, double y) {
		this.x = this.x + x;
		this.y = this.y + y;
		return this;
	}

	@Override
	public Vector2d sub (@NotNull Vector2d v, @NotNull Vector2d dest) {
		return sub(v.x(), v.y(), dest);
	}

	@Override
	public Vector2d sub (double x, double y, @NotNull Vector2d dest) {
		dest.x(this.x - x);
		dest.y(this.y - y);
		return dest;
	}

	@Override
	public Vector2d sub (@NotNull Vector2d v) {
		return sub(v.x(), v.y());
	}

	@Override
	public Vector2d sub (double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	@Override
	public Vector2d mul (@NotNull Vector2d v, @NotNull Vector2d dest) {
		return mul(v.x(), v.y(), dest);
	}

	@Override
	public Vector2d mul (double x, double y, @NotNull Vector2d dest) {
		dest.x(this.x * x);
		dest.y(this.y * y);
		return dest;
	}

	@Override
	public Vector2d mul (@NotNull Vector2d v) {
		return mul(v.x(), v.y());
	}

	@Override
	public Vector2d mul (double d) {
		return mul(d, d);
	}

	@Override
	public Vector2d mul (double x, double y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	@Override
	public Vector2d div (@NotNull Vector2d v, @NotNull Vector2d dest) {
		return div(v.x(), v.y(), dest);
	}

	@Override
	public Vector2d div (double x, double y, @NotNull Vector2d dest) {
		dest.x(this.x / x);
		dest.y(this.y / y);
		return dest;
	}

	@Override
	public Vector2d div (@NotNull Vector2d v) {
		return div(v.x(), v.y());
	}

	@Override
	public Vector2d div (double x, double y) {
		this.x /= x;
		this.y /= y;
		return this;
	}

	@Override
	public Vector2d pow (@NotNull Vector2d v, @NotNull Vector2d dest) {
		return pow(v.x(), v.y(), dest);
	}

	@Override
	public Vector2d pow (double d, @NotNull Vector2d dest) {
		return pow(d, d, dest);
	}

	@Override
	public Vector2d pow (double x, double y, @NotNull Vector2d dest) {
		dest.x(Math.pow(this.x, x));
		dest.y(Math.pow(this.y, y));
		return dest;
	}

	@Override
	public Vector2d pow (@NotNull Vector2d v) {
		return pow(v.x(), v.y());
	}

	@Override
	public Vector2d pow (double d) {
		return pow(d, d);
	}

	@Override
	public Vector2d pow (double x, double y) {
		this.x = Math.pow(this.x, x);
		this.y = Math.pow(this.y, y);
		return this;
	}

	@Override
	public double length () {
		return Math.sqrt(x * x + y * y);
	}

	@Override
	public double lengthSquared () {
		return x * x + y * y;
	}

	@Override
	public double distance (@NotNull Vector2d v) {
		return distance(v.x(), v.y());
	}

	@Override
	public double distance (double x, double y) {
		double dx = this.x - x;
		double dy = this.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	@Override
	public double distanceSquared (double x, double y) {
		double dx = this.x - x;
		double dy = this.y - y;
		return dx * dx + dy * dy;
	}

	@Override
	public Vector2d normalize () {
		double len = length();
		x /= len;
		y /= len;
		return this;
	}

	@Override
	public Vector2d normalize (double length) {
		double len = length() / length;
		x /= len;
		y /= len;
		return this;
	}

	@Override
	public Vector2d normalizeSafely () {
		double len = length();
		if (len == 0) {
			throw new IllegalStateException("Vector length is 0");
		}
		x /= len;
		y /= len;
		return this;
	}

	@Override
	public Vector2d normalizeSafely (double length) {
		double len = length() / length;
		if (len == 0) {
			throw new IllegalStateException("Vector length is 0");
		}
		x /= len;
		y /= len;
		return this;
	}

	@Override
	public Vector2d rotateTo (@NotNull Vector2d direction) {
		return direction.normalize(length());
	}

	@Override
	public Vector2d zero () {
		x = y = 0;
		return this;
	}

	@Override
	public Vector2d negate () {
		x = -x;
		y = -y;
		return this;
	}

	@Override
	public double dot (@NotNull Vector2d v) {
		return x() * v.x() + y() * v.y();
	}

	@Override
	public Vector2d clamp (double min, double max) {
		x = Math.min(Math.max(x, min), max);
		y = Math.min(Math.max(y, min), max);
		return this;
	}

	@Override
	public Vector2d clamp (@NotNull Vector2d min, @NotNull Vector2d max) {
		x = Math.min(Math.max(x, min.x()), max.x());
		y = Math.min(Math.max(y, min.y()), max.y());
		return this;
	}

	@Override
	public Vector2d lerp (@NotNull Vector2d end, double t) {
		x = x + (end.x() - x) * t;
		y = y + (end.y() - y) * t;
		return this;
	}

	@Override
	public Vector2d lerp (@NotNull Vector2d end, @NotNull Vector2d t) {
		x = x + (end.x() - x) * t.x();
		y = y + (end.y() - y) * t.y();
		return this;
	}

	@Override
	public Vector2d absolute () {
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}

	@Override
	public Vector2d copy () {
		return Vector2d.of(x, y);
	}
}
