import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndex {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{

   
    private Text word = new Text();
	private Text doc_cnt = new Text();
    private Text one   = new Text("1");
    //private IntWritable one   = new IntWritable(1);
    public String doc_id;
    public Map<String,Integer> mp_word_count ;
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
	   /*while (itr.hasMoreTokens()) {
		word.set(itr.nextToken());
	   	context.write( word, one);
	   }*/
      
	  if(itr.hasMoreTokens()) doc_id = itr.nextToken();
	  mp_word_count = new HashMap<>();
	  while (itr.hasMoreTokens()) {
		String theKey = itr.nextToken();
        if (! mp_word_count.containsKey(theKey)) mp_word_count.put(theKey, 1);
		else mp_word_count.put(theKey, mp_word_count.get(theKey)+1);
		 // word.set(String.format("%s:%s", itr.nextToken(), doc_id));  
		 // context.write( word, one);
      }
		
      for (Map.Entry<String, Integer> entry : mp_word_count.entrySet())
		 {
				
				word.set ( entry.getKey() );
				doc_cnt.set ( doc_id+":"+String.valueOf(entry.getValue()) ) ;
				context.write( word, doc_cnt);
		 }
    }
  }

  public static class IntSumCombiner
       extends Reducer<Text,Text,Text,Text> {
    private Text result = new Text();
    private int sum=0;
		   
    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
     String [] key_sep  =  key.toString().split(":");
	  sum=0;
      for (Text val : values) {sum++;}
      result.set( String.format("%s:%s", key_sep[1], String.valueOf(sum))) ;
      context.write(new Text(key_sep[0]), result);
    }
  }
	
  public static class IntSumReducer
       extends Reducer<Text,Text,Text,Text> {
    private Text result = new Text();
   
    private StringBuffer sumlist;
    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
     
	  sumlist = new StringBuffer();
      for (Text val : values) {
		 //sum+=  val.get() ;
         sumlist.append(String.format("%s ", val));
      }
      result.set(sumlist.toString());
	  //result.set(String.valueOf(sum));
	  context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Inverted Index");
    job.setJarByClass(InvertedIndex.class);
    job.setMapperClass(TokenizerMapper.class);
    //job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass (IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
	job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
