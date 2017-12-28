package com.unichina

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.Properties

import com.szd.util.{MyDateUtil, MySQLSaveUtil}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhang on 2017/12/28.
  */
object UnichineSystem_Third {

  case class Data(phone: String,month:String,money:Double)

  def main(args: Array[String]): Unit = {
    //SimpleDateFormat
    //1.过滤三个月数据
    //2.把日期2017-01-09改成 1701 1702  1712 1801
    //3.手机号码_1702 统计       钱数>100
    //4.按照手机号码分组 统计出个数=3

    //sql   rdd+sql count=3
    val time1 = System.currentTimeMillis()
    var startTime = MyDateUtil.getLastMonth(-3)
    val endTime =MyDateUtil.getLastMonth(-1)
    println(startTime)
    println(endTime)
    val  conf = new SparkConf().setAppName("UnichineSystem").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    val lines=sc.textFile("F:\\01_BigData\\data.txt").filter(x=>{
      //1.数据清洗
      //println((MyDateUtil.parseToMinute(x.split("\\|")(1)) >=startTime) && (MyDateUtil.parseToMinute(x.split("\\|")(1)) <=endTime) )
      (MyDateUtil.parseToMinute(x.split("\\|")(1)) >=startTime) && (MyDateUtil.parseToMinute(x.split("\\|")(1)) <=endTime)
    })
    import spark.implicits._
    val phone_month_money = lines.map(x=>{(x.split("\\|")(0),MyDateUtil.parseToMinute(x.split("\\|")(1)),x.split("\\|")(2).toDouble)}).map(log=>{
      Data(log._1,log._2,log._3.toDouble)
    }).toDF().cache()

    phone_month_money.createTempView("datas")

    val phone_month_sum = spark.sql("select phone,month,sum(money) as mon_money from datas where month >="+MyDateUtil.getLastMonth(-3)+" and month<="+endTime+" group by phone,month order by phone");
    phone_month_sum.createTempView("month_sum")

    val phone_3month_sum =spark.sql("select phone,count(*) as count from month_sum where mon_money > 90 group by phone");

    phone_3month_sum.createTempView("phone_month_count")

    val result = spark.sql("select phone,count from phone_month_count where count>2")

    result.rdd.foreachPartition(MySQLSaveUtil.myFun)
    /*spark.sql("select phone from (select phone,count(*) as count from " +
      "(select phone,month,sum(money) as mon_money from datas where month >="+startTime+" and month<="+endTime+" group by phone,month) a " +
      " where mon_money > 90 group by phone) b where count >2 ").show()*/
    /*val prop = new Properties()
     prop.put("user", "root")
     prop.put("password", "root")
     result.write.mode("append").jdbc("jdbc:mysql://localhost:3306/db_spark", "tb_phones", prop)*/
    //result1.write.mode("overwrite").jdbc("jdbc:mysql://localhost:3306/db_spark", "tb_details", prop)

    //startTime =  MyDateUtil.getLastMonth(-6)
    //val phone_6month_sum = spark.sql("select phone,month,sum(money) as mon_money from datas where month >="+startTime+" and month<="+endTime+" and phone in (select phone from phone_month_count where count>2) group by phone,month order by phone ").rdd;
    // phone_6month_sum.printSchema()
    //phone_6month_sum.foreachPartition(MySQLSaveUtil.myPhoneDetail)
    println(System.currentTimeMillis()-time1)
  }

}
