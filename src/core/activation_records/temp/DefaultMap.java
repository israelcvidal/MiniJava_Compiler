package core.activation_records.temp;

public class DefaultMap implements TempMap {
	public String tempMap(Temp t) {
	   return t.toString();
	}

	public DefaultMap() {}
}
