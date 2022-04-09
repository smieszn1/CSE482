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

public class covidMax {

  public static class maxMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private IntWritable count = new IntWritable();
    private Text county = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String[] counties = new String[]{"Clinton","Eaton","Ingham","Kent",
					"Macomb","Shiawassee","Wayne"};
      String[] tokens = value.toString().split(",");
      for (int i=1; i<tokens.length; i++) {
        county.set(counties[i-1]);
        count.set(Integer.parseInt(tokens[i]));
        context.write(county, count);
      }
    }
  }

  public static class maxReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int maxCount = -999;
      Text city;
      for (IntWritable val : values) {
	if (val.get() > maxCount) {
		maxCount = val.get();
	}
      }
      result.set(maxCount);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Average covid cases");
    job.setJarByClass(covidMax.class);
    job.setMapperClass(maxMapper.class);
    job.setReducerClass(maxReducer.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
