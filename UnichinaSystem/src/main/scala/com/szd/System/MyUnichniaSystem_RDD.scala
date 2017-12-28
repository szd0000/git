package com.szd.System

import com.szd.util.MyDateUtil
import org.apache.spark.sql.SparkSession
/*
1、创建SparkSession ，
2、读取数据并进行数据清洗
3、截串
4、过滤
 */
object MyUnichniaSystem_RDD {
  def main(args: Array[String]): Unit = {

   val spark : SparkSession = SparkSession.builder()
      .appName("")
      .master("local")
      .getOrCreate()
    //前三个月时间点
    val start_time = MyDateUtil.getLastMonth(-3)
    //上个月时间点
    val end_time = MyDateUtil.getLastMonth(-1)

    val lines = spark.read.textFile("F:\\01_BigData\\data.txt").rdd.filter(x =>{
      //取出最近三个月的数据
      (MyDateUtil.parseToMinute(x.split("\\|")(1))) >= start_time &&
        (MyDateUtil.parseToMinute(x.split("\\|")(1))) <= end_time
    })
    import spark.implicits._
    //18888106861|2017-06-26|3.3702765904702385|04
    //(K,V)((手机号+月份)，话费)
    val phone_month_num = lines.map(x=>((x.split("\\|")(0)+"//|"+ //手机号
      MyDateUtil.parseToMinute(x.split("\\|")(1))),//月份  ===> 1706
      x.split("\\|")(2)))//话费
      .reduceByKey(_+_).filter(_._2.toDouble > 100)

    val phone_num = phone_month_num.map(x=>(x._1.split("\\|")(0),1)).reduceByKey(_+_).filter(_._2 > 2)
    phone_num.foreach(x => println(x._1+":"+x._2))

  }
}
