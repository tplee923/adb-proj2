import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileUtil {
	/*readFileByLines(args[0]);*/
	public int findFirstSpace(String textLine) {
		for (int i=0;i<textLine.length();i++) {
			//System.out.println(textLine.charAt(i));
			if (textLine.charAt(i) == ' ')
				return i;
		}
		return 0;
	}
	
	public void makeQueries(String from, String toLeft, String toRight) {
		String tempString = null;
		try	{
			BufferedReader reader = new BufferedReader(new FileReader(from));
			BufferedWriter writerLeft = new BufferedWriter(new FileWriter(toLeft));
			BufferedWriter writerRight = new BufferedWriter(new FileWriter(toRight));
			for (int i=0; (tempString = reader.readLine()) != null; i++) {
				int splitPoint = findFirstSpace(tempString);
				
				String leftString = tempString.substring(0, splitPoint);				
				String rightString = tempString.substring(splitPoint+1,tempString.length());
				writerLeft.write(leftString);
				writerLeft.newLine();
				writerRight.write(rightString);
				writerRight.newLine();
			}
			writerLeft.close();
			writerRight.close();
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
