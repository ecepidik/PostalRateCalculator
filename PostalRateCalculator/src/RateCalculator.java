import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;


public class RateCalculator {
	
	public static void main(String[] args) {
		
		Parcel parcel = new Parcel("A1X1T3", "A1X1T3", 10f,10f,10f,5f, Parcel.Type.REGULAR);
		parcel.setTranslatedPostalCode(codeTranslationTableLookup(parcel.getSourcePostalCode()));
		rateTableLookup(parcel);
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
						System.out.println(translatedPostalCode);
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
						float extra = Float.parseFloat(record.get(parcel.getTranslatedPostalCode()));
						price = price + extra*parcel.getWeight();
						System.out.println("Weight " + volume + ": " + price + "$");
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
}
