import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;


public class RateCalculator {
	
	public static void main(String[] args) {
		
		String sourcePostalCode = args[0];
		String destPostalCode = args[1];
		float length = Float.parseFloat(args[2]);
		float width = Float.parseFloat(args[3]);
		float height = Float.parseFloat(args[4]);
		float weight = Float.parseFloat(args[5]);
		String type = args[6];
		
		Parcel parcel = new Parcel(sourcePostalCode, destPostalCode, 
				length,width,height,weight);
		
		if(type.equalsIgnoreCase("xpress")) {
			parcel.setType(Parcel.Type.XPRESS);
		}
		if(type.equalsIgnoreCase("regular")) {
			parcel.setType(Parcel.Type.REGULAR);
		}
		if(type.equalsIgnoreCase("priority")) {
			parcel.setType(Parcel.Type.PRIORITY);
		}
		
		String errorMessage = parcelDimensionChecking(parcel);
		if(errorMessage.length() > 0) {
			System.out.println("ERROR: "+errorMessage);
		} else {
			parcel.setTranslatedPostalCode(codeTranslationTableLookup(parcel.getSourcePostalCode()));
			rateTableLookup(parcel);
		}
	}


	public float calculateRate(Parcel parcel) {

		return 0f;
	}
	
	public static String codeTranslationTableLookup(String postalCode) {
		Reader in;
		String translatedPostalCode = "";
		try {
			in = new BufferedReader(new FileReader("Postal-Tables/Montreal Postal Codes to Rate Codes.csv"));
			Iterable<CSVRecord> records;
			try {
				records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
				for (CSVRecord record : records) {
					String subPostalCode = record.get("Originated");
					if (subPostalCode.equals(postalCode.substring(0,3))) {
						translatedPostalCode = record.get("Translation");
						return translatedPostalCode;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return translatedPostalCode;
	}

	public static float rateTableLookup(Parcel parcel) {
		Reader in;
		String tableName = "";
		if (parcel.getType().toString() == "XPRESS") {
			tableName = "Xpresspost.csv";	
		}
		if (parcel.getType().toString() == "REGULAR") {
			tableName = "Regular.csv";	
		}
		if (parcel.getType().toString() == "PRIORITY") {
			tableName = "Priority.csv";	
		}
		try {
			in = new BufferedReader(new FileReader("Postal-Tables/" + tableName));
			Iterable<CSVRecord> records;
			try {
				records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
				float volume = 0f;
				float price = 0f;
				for (CSVRecord record : records) {
					if(!record.get("Volumetric Equivalent").equals("Over 30")) {
						volume = Float.parseFloat(record.get("Volumetric Equivalent"));
						price = Float.parseFloat(record.get(parcel.getTranslatedPostalCode()));
						if(parcel.getWeight() <= volume) {
							System.out.println("Weight " + volume + ": " + price + "$");
							return price;
						}
					} else {
						float extra = Float.parseFloat(record.get(parcel.getTranslatedPostalCode()))*parcel.getWeight();
						price = price + extra;
						System.out.println("Weight " + volume + ": " + price + "$" + " includes overweight charges (" + extra + "$)");
						return price;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return 0;
	}
	
	public static String parcelDimensionChecking(Parcel parcel){
		String error = "";
		if(parcel.getLength() < 10.0 || parcel.getLength() > 20.0) {
			System.out.println("HERE");
			error += "Invalid length. ";
		}
		if(parcel.getWidth() < 7 || parcel.getWidth() > 20) {
			error += "Invalid width. ";
		}
		if(parcel.getHeight() < 0.1 || parcel.getHeight() > 20) {
			error += "Invalid height. ";
		}
		if(parcel.getWeight() < 0) {
			error += "Invalid weight. ";
		}
		return error;
	}
}
