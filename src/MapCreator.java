import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class MapCreator {
	public void parseMap(String filename) {
		BufferedReader reader;
		HashMap<String, Double[]> map = new HashMap<String, Double[]>();
		try {
			reader = new BufferedReader(new FileReader(filename));
			while (reader.ready()) {
				String line = reader.readLine();
				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				map.put(tokens[0], new Double[] {Double.parseDouble(tokens[1]), 
						Double.parseDouble(tokens[2])});
			}
		} catch (FileNotFoundException e) {
			System.out.println("Incorrect map filename");
		} catch (IOException e) {
			System.out.println("Incorrect map filename");
		}

		
	}
}
