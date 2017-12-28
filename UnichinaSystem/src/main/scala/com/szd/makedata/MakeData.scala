package com.szd.makedata

import java.io.FileWriter

import scala.util.Random

object MakeData extends App {
  val ih = "188881068"
  val random = new Random()
  val fw = new FileWriter("F:\\01_BigData\\data.txt", true)
  for (i <- 61 to 71) {//手机号后两位
    for (j <- 1 to 12) {//月份
      var month = j.toString
      if (month.length == 1) {
        month = "0" + month
      }
      if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10 || j == 12) {
        for (k <- 1 to 31) {
          createData(month, k, i.toString)
        }
      }
      if (j == 4 || j == 6 || j == 9 || j == 11) {
        for (k <- 1 to 30) {
          createData(month, k, i.toString)
        }
      }
      if (j == 2) {
        for (k <- 1 to 29) {
          createData(month, k, i.toString)
        }
      }
    }
  }

  def createData(month: String, day: Int, phone: String) = {
    var days = day.toString
    if (days.length == 1) {
      days = "0" + days
    }
    val time = "2017-" + month + "-" + days
    val phones = ih+phone
    var consum = random.nextDouble() + 4
    if (random.nextInt() % 2 == 0) {
      consum = consum + random.nextInt(4)
    } else {
      consum = consum - random.nextInt(2)
    }
    var log = phones + "|" + time + "|" + consum +"|"+ "04"+"\r\n"
    fw.write(log)
  }
  fw.close()
  println("ok")
}
