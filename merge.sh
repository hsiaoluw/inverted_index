rm ./output.txt
rm ./index.txt
hadoop fs -rm gs://${GS_NAME}/output.txt
hadoop fs -rm gs://${GS_NAME}/index.txt
hadoop fs -getmerge gs://${GS_NAME}/output ./output.txt
hadoop fs -copyFromLocal ./output.txt gs://${GS_NAME}/
grep -w '^little' ./output.txt>>index.txt
grep -w '^jewel' ./output.txt>>index.txt
grep -w '^believe' ./output.txt>>index.txt
grep -w '^jovian' ./output.txt>>index.txt
grep -w '^harriet' ./output.txt>>index.txt
grep -w '^large' ./output.txt>>index.txt
grep -w '^first' ./output.txt>>index.txt
grep -w '^love' ./output.txt>>index.txt
hadoop fs -copyFromLocal ./index.txt gs://${GS_NAME}/
