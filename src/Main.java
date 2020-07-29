import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	public static void main(String args[]) throws Exception
	{
		//The data path of the raw data
		String txtDataPath = "C:\\carDataset.txt";
		
		//Calls that to convert the data from .data to .csv and then to .arff file
		ConvertToCSV(txtDataPath);
	}
	
	private static void ConvertToCSV(String txtDataPath) throws Exception
	{
		//This method adds the header of the attributes (names) and normalizes the data to a .csv format (comma separated based format)
		
		//Writes to bellow file
		String csvLocationPath = "C:\\CsvLikeFile.csv";
		//Location for the file that will test the NaiveBayes model
		String testingCsvLocation = "C:\\testingFile.csv";
		FileWriter fWriter = new FileWriter(csvLocationPath);
		FileWriter testingFWriter = new FileWriter(testingCsvLocation);
		
		//Reads from bellow file
		File rawData = new File(txtDataPath);
		Scanner	input;
		try
		{
			input = new Scanner(rawData);
			
			//This string is the names of the attributes and it is added on the very first line of the file
			String line = "buying,maint,doors,persons,lug_boot,safety,class";
			do
			{
				//Writes the new .csv file
				fWriter.write(line + "\n");
				testingFWriter.write(line + "\n");
				//Reads the next line to format it and write it as well
				line = input.nextLine();
				line = line.replaceAll(", ", ",");
//				System.out.println(line);
			}while(input.hasNext());
			//Closing threads for error avoidance
			fWriter.close();
			testingFWriter.close();
			input.close();
		} catch (FileNotFoundException fnfExc)
		{
			fnfExc.printStackTrace();
		}catch(IOException ioExc)
		{
			ioExc.printStackTrace();
		}
		
		File testingCsvFile = new File(testingCsvLocation);
		
		File csvFile = new File(csvLocationPath);
		MyClassifier myClassObj = new MyClassifier(csvFile, testingCsvFile);
	}
	
}
