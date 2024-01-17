package net.leawind.mc.math.smoothvalue;


import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface ISmoothValue<T> {
	void setEndValue (@NotNull T endValue);

	void update (double time);

	@NotNull T get ();
}
