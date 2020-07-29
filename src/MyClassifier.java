import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MyClassifier {
	
	public MyClassifier(File csvFile, File testingCsvFile)
	{
		//Create a classifier model first
		String csvPath = csvFile.getAbsolutePath();
		String modelPath = "C:\\nBayesModel.model";
		
		//Train a NaiveBayes model
		try
		{
			//Declare a data source
			DataSource trainingSource = new DataSource(csvPath);
			//Get the dataset from the source
			Instances trainingDataset = trainingSource.getDataSet();
			//Defines the position of the "class" attribute
			if(trainingDataset.classIndex() == -1)
			{
				trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);
			}
			
			//Process on the attributes to replace string values (disadvantage of NaiveBayes classifier)
			trainingDataset = AttributeProcessor(trainingDataset);
			
			//Build the model
			NaiveBayes nBayesModel = new NaiveBayes();
			nBayesModel.buildClassifier(trainingDataset);
			//Print info about the specific model
			System.out.println(nBayesModel);
			
			//Store the model into disk
			weka.core.SerializationHelper.write(modelPath, nBayesModel);
			
			//Make Evaluation of the trained model
			Evaluation evalTest = new Evaluation(trainingDataset);
			//A variable  useful for debugging
			double[] evaluationArray = evalTest.evaluateModel(nBayesModel, trainingDataset);
			System.out.println("Evaluated with " + evalTest.correct() + " correct predictions and " + evalTest.incorrect() + " wrong predictions!");
			System.out.println();
			LoadModel(modelPath, testingCsvFile);
		}catch(Exception exc)
		{
			exc.printStackTrace();
			System.out.println("Error caused by:\n" + exc.getMessage());
		}
		
	}
	
	private Instances AttributeProcessor(Instances trainingDataset)
	{
		int totalAtts = trainingDataset.numAttributes();
		for(int i=0; i<totalAtts; i++)
		{
			if(trainingDataset.attribute(i).isString() && trainingDataset.attribute(i).name().contentEquals("doors"))
			{
				String attName = trainingDataset.attribute(i).name();
				List<String> nominalValues = new ArrayList<String>();
				nominalValues.add("2");
				nominalValues.add("3");
				nominalValues.add("4");
				nominalValues.add("5more");
				Attribute newAtt = new Attribute(attName, nominalValues);
				
				trainingDataset.replaceAttributeAt(newAtt, i);
				System.out.println();
			}else if(trainingDataset.attribute(i).isString() && trainingDataset.attribute(i).name().contentEquals("persons"))
			{
				String attName = trainingDataset.attribute(i).name();
				List<String> nominalValues = new ArrayList<String>();
				nominalValues.add("2");
				nominalValues.add("4");
				nominalValues.add("more");
				Attribute newAtt = new Attribute(attName, nominalValues);
				System.out.println();
				
				trainingDataset.replaceAttributeAt(newAtt, i);
			}
		}
		System.out.println();
		return trainingDataset;
	}
	
	private void LoadModel(String modelPath, File testingCsvFile)
	{
		//A similar location for the new data for model's test
		String testingCsvLocation = testingCsvFile.getAbsolutePath();
		try
		{
			System.out.println("Now testing new dataset...\n");
			//Loads the classifier model
			NaiveBayes nBayesModel = (NaiveBayes) weka.core.SerializationHelper.read(modelPath);
			
			//Loads a new data set (the same data set in this case, with the "class" attribute excluded)
			DataSource testingSource = new DataSource(testingCsvLocation);
			Instances testingDataset = testingSource.getDataSet();
			
			if(testingDataset.classIndex() == -1)
			{
				testingDataset.setClassIndex(testingDataset.numAttributes() - 1);
			}
			
			//This data source is the same with the original, but resampled
			testingDataset = testingDataset.resample(new Random());
			
			for(int i=0; i<testingDataset.numInstances(); i++)
			{
				System.out.println(testingDataset.instance(i));
				double prediction = nBayesModel.classifyInstance(testingDataset.instance(i));
				String actualValue = testingDataset.instance(i).stringValue(6);
				System.out.println(actualValue);
				System.out.println("Also \"prediction\" says: " + prediction);
				System.out.println();
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
