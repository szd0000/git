package com.szd.System

import com.szd.util.MyDateUtil
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by zhang on 2017/12/28.
  */
object UnichineSystem_Second {
  case class logs(phone: String, date: Int, money: Int)
  def main(args: Array[String]): Unit = {
    //SimpleDateFormat
      //1.过滤三个月数据
      //2.把日期2017-01-09改成 1701 1702  1712 1801
      //3.手机号码_1702 统计       钱数>100
      //4.按照手机号码分组 统计出个数=3

      //sql   rdd+sql count=3
      val time1 = System.currentTimeMillis()
    val startTime = MyDateUtil.getLastMonth(-3)
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
    //（）
    val phone_month_sum = lines.map(x=>{(x.split("\\|")(0)+"|"+MyDateUtil.parseToMinute(x.split("\\|")(1)),x.split("\\|")(2).toDouble)}).reduceByKey(_+_).filter(_._2 > 90)
    //phone_month_sum.foreach(x=>println(x._2))
   /* val phone_num= phone_month_sum.map(x=>(x._1.split("\\|")(0),1)).reduceByKey(_+_).filter(_._2>2)
    phone_num.foreach(x=>{println(x._1+"="+x._2)})*/
  //  println(lines.count())
    import spark.implicits._

    val logDF = phone_month_sum.map(x=>(x._1.split("\\|")(0) ,x._1.split("\\|")(1),x._2)).map(log=>logs(log._1,log._2.toInt,log._3.toInt))
      .toDF()
   // logDF.show()

    logDF.registerTempTable("logs")

    val checkLogDF=spark.sql("select phone,count(phone) from logs group by phone  having count(*) > 2")

   // checkLogDF.show()




    println(System.currentTimeMillis()-time1)
  }

}
