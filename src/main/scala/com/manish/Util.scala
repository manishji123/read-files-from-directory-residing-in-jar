package com.manish

object Util {

  def getStackTraceString(e: Throwable) : String={
    e.getStackTrace.map(t=>"\t"+t.toString).mkString(sys.props("line.separator"))
  }

  def cleanly[A,B](resource: => A)(cleanup: A => Unit)(code: A => B): B = {
    val r = resource
    try { code(r) }
    finally {
      if (null != r) {
        try {
          cleanup(r)
        } catch {
          case e: Exception => None
        }
      }
    }
  }

}
