package it.eng.cobo.consolepec.util.objects;

public class Ref<T> {
	private T obj;

	private Ref(T obj) {
		super();
		this.obj = obj;
	}

	public static <V extends T, T> Ref<T> of(V obj) {
		return new Ref<T>(obj);
	}

	public T get() {
		return obj;
	}

	public void set(T obj) {
		this.obj = obj;
	}

}
