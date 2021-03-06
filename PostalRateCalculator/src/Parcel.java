
public class Parcel {

	private String sourcePostalCode;
	private String destPostalCode;
	private float length;
	private float width;
	private float height;
	private float weight;
	private Type type;
	private String translatedPostalCode;
	

	enum Type {
		REGULAR,
		XPRESS,
		PRIORITY
	}

	Parcel (String source, String destination, 
			float l, float wi, float h, float we) {
		this.sourcePostalCode = source;
		this.destPostalCode = destination;
		this.length = l;
		this.width = wi;
		this.height = h;
		this.weight = we;
		
	}
	public String getTranslatedPostalCode() {
		return translatedPostalCode;
	}

	public void setTranslatedPostalCode(String translatedPostalCode) {
		this.translatedPostalCode = translatedPostalCode;
	}

	public String getSourcePostalCode() {
		return sourcePostalCode;
	}

	public void setSourcePostalCode(String sourcePostalCode) {
		this.sourcePostalCode = sourcePostalCode;
	}

	public String getDestPostalCode() {
		return destPostalCode;
	}

	public void setDestPostalCode(String destPostalCode) {
		this.destPostalCode = destPostalCode;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}