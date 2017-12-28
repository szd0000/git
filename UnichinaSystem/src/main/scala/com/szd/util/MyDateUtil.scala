package com.szd.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import org.apache.commons.lang3.time.FastDateFormat

object MyDateUtil {
  //SimpleDateFormat 与FastDateFormat 区别是SimpleDateFormat线程不安全 SimpleDateFormat线程安全
  val YYYYMMDDHHMMSS_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
  val TARGE_FORMAT = FastDateFormat.getInstance("yyMM");

  /**
    * 根据字符串返回对应的时间戳
    * @param time
    * @return
    */
  def getTime(time: String)={
    YYYYMMDDHHMMSS_FORMAT.parse(time).getTime
  }

  /**
    * 传入一个 2017-07-08 返回 1707
    * @param time
    * @return
    */
  def parseToMinute(time:String)={
    TARGE_FORMAT.format(new Date(getTime(time)))
  }


  /**
    *
    * @param time 1707
    * @return
    */
  def parseDateToMinute(time:Date)={
    TARGE_FORMAT.format(time)
  }

  /**
    * 获取对应前几个月的月份
    * @param month -1代表上一个月 -3前三个月
    * @return
    */
  def getLastMonth(month: Int): String = {

    val date = new Date();
    val calendar = Calendar.getInstance();
    //对calendar 设置date对应的日期（当前时间）
    calendar.setTime(date);
    //calendar.add(Calendar.YEAR, -1);//当前时间减去一年，即一年前的时间
    calendar.add(Calendar.MONTH, month);
    //当前时间减去一个月，即一个月前的时间
    // print(calendar.getTime());//获取一年前的时间，或者一个月前的时间 yyMM
    val times = MyDateUtil.parseDateToMinute(calendar.getTime);


    //返回值
    times
  }

  def main(args: Array[String]): Unit = {
      println(getLastMonth(-1))
      println(getLastMonth(-3))
      println(parseToMinute("2017-01-09"))

  }






}
