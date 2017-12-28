/*
package com.szd.System

import java.text.SimpleDateFormat

import org.apache.spark.sql.SparkSession

object PhoneNum extends App {

    val spark :SparkSession = SparkSession
      .builder()
      .master("local")
      .appName("phone")
      .getOrCreate()
    val lines = spark.read.textFile("F:\\01_BigData\\data.txt")

    case class Phone(month:Int,phone_num:String,money:Int)
    //导入隐式转换
    import spark.implicits._

    //    10/16|7|18888405369

    //(K,V):((月份+手机号)，话费)
    val arr = lines.map(lines=>((lines.split("\\|")(0).split("\\/")(0),lines.split("\\|")(2)),//K
      lines.split("\\|")(1).toInt))//V
    //求和 过滤话费出大于100 的数据
    val arr1 = arr.rdd.reduceByKey(_+_).filter(_._2 > 120)

    val DF = arr1.map(line => Phone(line._1._1.toInt,line._1._2.toString,line._2.toInt)).toDF()
  //创建临时表
    DF.registerTempTable("Phone")
  //当前月份
  val time = System.currentTimeMillis()
  val sdf = new SimpleDateFormat("yyyy-MM-dd")
  val now_month = sdf.format(time).split("\\-")(1).toInt
  //val str_sql= "(select phone_num , month from Phone where month in (5,6,7)) as mon_1"
  //val str1 = "select phone_num from (select phone_num , month from Phone where month in ("+now_month+","+(now_month - 1)+","+(now_month - 2)+") as mon_1 where count(mon_1.month) > 2"
  val str2 ="select phone_num from " +
    "(select phone_num , count(month) as num  from " +
    "(select phone_num , month from Phone " +
    "where month in ("+now_month+","+(now_month - 1)+","+(now_month - 2)+")) group by phone_num) as mon_1 " +
    "where mon_1.num >2 "
  //val str3 = "select phone_num , count(month) as num  from (select phone_num , month from Phone where month in (5,6,7)) group by phone_num"
  val sql = spark.sql(str2)

    sql.show()

}
*/
