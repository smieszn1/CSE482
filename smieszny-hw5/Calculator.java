import java.io.*;
import java.util.*;
import java.lang.Math;

import java.io.IOException;
import java.util.StringTokenizer;
 
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Calculator {

   public HashMap<String, Integer> mapToID(ArrayList<String> arr) {
     	HashMap<String, Integer> hmap = new HashMap<>();
  	int id = 0;

	for (int i=0; i<arr.size(); i++) {
	    Integer count = hmap.get(arr.get(i));
	    if (count == null) {
		hmap.put(arr.get(i), id++);
	    }
	}
	return hmap;
   }

   public static class entMapper extends Mapper <Object, Text, Text, Text> {
      
      private Text col = new Text();
      private Text val = new Text();   
      public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
         String[] columns = new String[]{"outlook","temperature","construction","class"};
         String[] tokens = value.toString().split(",");
         for (int i=0; i<tokens.length; i++) {
            col.set(columns[i]);
            val.set(tokens[i]);
            context.write(col, val);
         }
      }
    }


   public static class entReducer extends Reducer<Text,Text,Text,DoubleWritable> {
    private DoubleWritable result = new DoubleWritable();
    private List<Iterable<Text>> columns  = new ArrayList<Iterable<Text>>();
    private List<Text> names = new ArrayList<Text>();
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      HashMap<String, Integer> attribID = mapToID(values);
      int numValues = attribID.size();
      for (int i=0; i<numValues; i++) {
        matrix[i] = 0;
      }
      int aID;
      int rowsum[] = new int[numValues];
      int total = 0;
      for (int i=0; i<attrib.size(); i++) {
        aID = attribID.get(attrib.get(i)).intValue();
        matrix[aID] += 1;
        rowsum[aID] += 1;
        total += 1;
	double entValues[] = new double[numValues];
        double p, e, eTotal = 0.;
      }
      for (int i=0; i<numValues; i++) {
        entValues[i] = 0;
        p = matrix[i]*1.0/(rowsum[i] + 1.0e-14);
        e = (-1.0*p*Math.log(p + 1.0e-14)/Math.log(2.0));
	entValues[i] += e;
        eTotal += (rowsum[i]*entValues[i]/total);
      }
      result.set(eTotal);
      context.write(key, result); 
    }
  }



   public double entropy(ArrayList<String>attrib, ArrayList<String>classes) {
	HashMap<String, Integer> attribID = mapToID(attrib);
	HashMap<String, Integer> classLabel = mapToID(classes);

   	int numValues = attribID.size();
   	int numClasses = classLabel.size();
   	int matrix[][] = new int[numValues][numClasses];
   	int aID, cID;

	// Initialize matrix to have zero values
   	for (int i=0; i<numValues; i++) {
       	    for (int j=0; j<numClasses; j++) {
	   	matrix[i][j] = 0;
       	    }
   	}

	// Count frequency for each (attribute value, class) combination

   	int rowsum[] = new int[numValues];
   	int total = 0;
   	for (int i=0; i<attrib.size(); i++) {
       	    aID = attribID.get(attrib.get(i)).intValue();
       	    cID = classLabel.get(classes.get(i)).intValue();
       	    matrix[aID][cID] += 1;
       	    rowsum[aID] += 1;
       	    total += 1;
   	}

	// Calculate entropy for each attribute value and
	// weighted average for the entire attribute
   	double entValues[] = new double[numValues];
   	double p, e, eTotal = 0.;

   	for (int i=0; i<numValues; i++) {
       	    entValues[i] = 0;
       	    for (int j=0; j<numClasses; j++) {
	   	p = matrix[i][j]*1.0/(rowsum[i] + 1.0e-14);
	   	e = (-1.0*p*Math.log(p + 1.0e-14)/Math.log(2.0));
	   	entValues[i] += e;
       	    }
       	    eTotal += (rowsum[i]*entValues[i]/total);
   	}

   	return eTotal;
    }

    public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      Job job = Job.getInstance(conf, "Entroy");
      job.setJarByClass(Calculator.class);
      job.setMapperClass(entMapper.class);
      job.setReducerClass(entReducer.class);
      job.setMapOutputKeyClass(Text.class);
      job.setMapOutputValueClass(Text.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(DoubleWritable.class);
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
