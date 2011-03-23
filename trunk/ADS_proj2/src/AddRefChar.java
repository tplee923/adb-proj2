import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class AddRefChar {
	public static void main(String [] args) {
		String tempString = null;
		try	{
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
			
			for (int i=0; (tempString = reader.readLine()) != null; i++) {	
				String string = "\"" + tempString + "\"";				
				writer.write(string);
				writer.newLine();
				
				
			}
			writer.close();
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
