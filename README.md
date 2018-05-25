This assignment tries to create inverted index for each word in the documents on Google cloud with hadoop.
Create a new project on Google cloud. Build a datacluster on the cloud and upload InvertedIndex.java, dev_data,update.sh, merge.sh to the google cloud storage. Also, make a dir named jar to put the compile jar files in later.

Connect to the master node through ssh, and export the new enviroment paths.


Set up path on master node
====================
```
export PATH=${JAVA_HOME}/bin:${PATH}
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
export GS_NAME= <your storage path of the project on the cloud , like gs://data_proc...>
```

Upload and compile jar on master node
========================

Upload update.sh, merge.sh to master node from the storage (through terminal). Use the command
```
sh update.sh 
```
To create jar file, and put it back to the cloud storage.


Start hadoop a hadoop job on google cloud
==========================
Create a job on google cloud and run. The jar file is under the jar dir you just created on the storage. The main class is
InvertedIndex.java, first argument is the path to the input document folder, like 'gs://dat_proc.../dev_data',
second argument is the path to put the output document, like 'gs://data_proc.../output'.
Once the job is finished, you can find the result in the output folder for postprocessing.
On the master node, use the command


Merge and get results
========================
```
sh merge.sh
```
To merge the results in output.txt, and find the doc id of certain keywords in index.txt
The output file would be like this

[key word]	[doc_id_1:frequency] [doc_id_2:frequency]

abnegate         85886315:1           80811098:1
ably             9931985:1        




  



