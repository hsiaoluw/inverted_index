rm -f ./InvertedIndex.java
hadoop fs -get  gs://${GS_NAME}/InvertedIndex.java  .
hadoop com.sun.tools.javac.Main InvertedIndex.java
jar cf invertedindex.jar InvertedIndex*.class
hadoop fs  -rm  gs://${GS_NAME}/jar/invertedindex.jar
hadoop fs -put ./invertedindex.jar gs://${GS_NAME}/jar
