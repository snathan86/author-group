Spark job to parse large json and apply transformations and compute and persist results

To compile :

    sbt compile
    
To run :
   
    sbt compile assembly
   
    
    spark-submit --class "medline.articles.Main" --master local[2] target/scala-2.11/medline.jar /tmp/exercise/input/med.json >> /tmp/spark_job.log
    
    Note:
         1) if the spark is not properly configured in the system then give pull path of spark bin
            eg. ~/sparkPlace/spark-2.1.0-bin-hadoop2.7/bin/spark-submit --class "medline.articles.Main" --master local[2] libs_local/medline.jar /tmp/exercise/input/med.json >> /tmp/spark_job.log
    
         2) change the input file path based on your placement, eg.it will look for the input file in /tmp/exercise/input/med.json
         
         
         
