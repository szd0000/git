package com.szd.util

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.sql.Row

/**
  * Created by zhang on 2017/12/28.
  */
object MySQLSaveUtil {

  def myFun(iterator: Iterator[Row]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into tb_phones(phone, count) values (?, ?)"
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spark","root", "123")
      iterator.foreach(data => {
        ps = conn.prepareStatement(sql)
        ps.setString(1, data.getAs[String]("phone"))
        ps.setInt(2,  data.getAs[Long]("count").asInstanceOf[Int])
        ps.executeUpdate()
      }
      )
    } catch {
      case e: Exception => println("")
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }


def myPhoneDetail(iterator: Iterator[Row]): Unit = {
  var conn: Connection = null
  var ps: PreparedStatement = null
  val sql = "insert into table_moble(phone,date,Money) values (?, ?,?)"
  try{
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spark", "root", "123")
    iterator.foreach(data => {
      ps = conn.prepareStatement(sql)
      ps.setString(1, data.getAs[String]("phone"))
      ps.setString(2, data.getAs[String]("month"))
      ps.setFloat(3, data.getAs[Double]("mon_money").asInstanceOf[Float])
      ps.executeUpdate()
    }
    )
  }
  catch {
    case e: Exception => println(e)
  } finally {
    if (ps != null) {
      ps.close()
    }
    if (conn != null) {
      conn.close()
    }
  }
}


}
