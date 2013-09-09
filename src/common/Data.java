package common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;

public class Data {
	private static String DATABASE;
	private static final int K = 10;
	private static List<String> DESCRIPTION;

	public static int getK() {
		return K;
	}

	public static String getDATABASE() {
		return DATABASE;
	}

	public static void setDATABASE(String dATABASE) {
		DATABASE = dATABASE;
	}


	public static List<String> getDESCRIPTION() {
		if(DESCRIPTION == null){
			DESCRIPTION = loadList( System.getProperty("user.dir") + 
					"/data/" + DATABASE + "/descriptions.csv");
		}
		return DESCRIPTION;
	}

	private static List<String> loadList(String path) {
		File file = new File(path);
		try {
			List<String> returned = Files.readLines(file, Charset.defaultCharset());
			return returned;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
