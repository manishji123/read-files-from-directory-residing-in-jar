package com.manish

import java.io.{BufferedReader, InputStreamReader}
import java.nio.file._
import java.util.Collections

import scala.collection.JavaConverters._

object ReadFilesFromDirectoryResidingInJar {

  val directory: String= "configurationData/manish/"
  val fileExtension: String= ".txt"

  lazy val uri = getClass.getClassLoader.getResource(directory).toURI

  lazy val fileSystem: Option[FileSystem] = {
    uri.getScheme match {
      case "jar" => Some(FileSystems.newFileSystem(uri, Collections.emptyMap[String, Any]))
      case _ => None
    }
  }

  lazy val getAllFiles: List[Path] = {
    val directoryPath = fileSystem.map(_.getPath(directory)).getOrElse(Paths.get(uri))
    Files.walk(directoryPath, 1).iterator().asScala.toList.filter(path => path.toString endsWith fileExtension)
  }

  def run() : Unit = {
    getAllFiles.foreach(path=> processFile(path))
  }


  def processFile(path: Path) = {
    println(s"File name : ${buildFileRecourcePath(path)}")
    val is= getClass.getClassLoader.getResource(buildFileRecourcePath(path)).openStream()

    println(s"File contents  ........ ")
    val byteArray= Util.cleanly(is)(_.close()) { is=>
      val reader= new BufferedReader(new InputStreamReader(is))
      reader.lines().forEach(x=>println(x))
    }
    println("##################################### ")
  }

  def buildFileRecourcePath(path: Path): String={
    directory+path.getFileName.toString
  }


  def main(args: Array[String]): Unit = {
    run()
  }

}
