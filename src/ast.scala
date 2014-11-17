/**********************************************************************************************\
* Rapture JSON Library                                                                         *
* Version 1.0.6                                                                                *
*                                                                                              *
* The primary distribution site is                                                             *
*                                                                                              *
*   http://rapture.io/                                                                         *
*                                                                                              *
* Copyright 2010-2014 Jon Pretty, Propensive Ltd.                                              *
*                                                                                              *
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file    *
* except in compliance with the License. You may obtain a copy of the License at               *
*                                                                                              *
*   http://www.apache.org/licenses/LICENSE-2.0                                                 *
*                                                                                              *
* Unless required by applicable law or agreed to in writing, software distributed under the    *
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,    *
* either express or implied. See the License for the specific language governing permissions   *
* and limitations under the License.                                                           *
\**********************************************************************************************/
package rapture.json.jsonBackends.play

import rapture.core._
import rapture.json._
import rapture.data._

import scala.collection.mutable.{ListBuffer, HashMap}
import scala.collection.JavaConverters

import play.api.libs.json.{Json => PJson, _}

object PlayAst extends JsonBufferAst {

  override def toString = "<PlayAst>"

  override def dereferenceObject(obj: Any, element: String): Any = obj match {
    case obj: JsValue => obj \ element
    case _ => throw TypeMismatchException(getType(obj), DataTypes.Object, Vector())
  }
  
  override def getKeys(obj: Any): Iterator[String] =
    obj match {
      case obj: JsValue => obj.as[Map[String, JsValue]].keysIterator
      case _ => throw TypeMismatchException(getType(obj), DataTypes.Object, Vector())
    }
  
  override def dereferenceArray(array: Any, element: Int): Any =
    array match {
      case v: JsValue => v(element)
      case _ => throw TypeMismatchException(getType(array), DataTypes.Array, Vector())
    }

  def getArray(array: Any): List[Any] = array match {
    case v: JsValue => try v.as[List[JsValue]] catch {
      case e: Exception => throw TypeMismatchException(getType(v), DataTypes.Array, Vector())
    }
    case _ => throw TypeMismatchException(getType(array), DataTypes.Array, Vector())
  }

  def getBoolean(boolean: Any): Boolean = boolean match {
    case v: JsValue => try v.as[Boolean] catch {
      case e: Exception => throw TypeMismatchException(getType(boolean), DataTypes.Boolean, Vector())
    }
    case _ => throw TypeMismatchException(getType(boolean), DataTypes.Boolean, Vector())
  }
  
  def getBigDecimal(bigDecimal: Any): BigDecimal = bigDecimal match {
    case v: JsValue => try v.as[BigDecimal] catch {
      case e: Exception => throw TypeMismatchException(getType(bigDecimal), DataTypes.Number, Vector())
    }
    case _ => throw TypeMismatchException(getType(bigDecimal), DataTypes.Number, Vector())
  }
  
  def getDouble(double: Any): Double = double match {
    case v: JsValue => try v.as[Double] catch {
      case e: Exception => throw TypeMismatchException(getType(double), DataTypes.Number, Vector())
    }
    case _ => throw TypeMismatchException(getType(double), DataTypes.Number, Vector())
  }
  
  def getString(string: Any): String = string match {
    case v: JsValue => try v.as[String] catch {
      case e: Exception => throw TypeMismatchException(getType(string), DataTypes.String, Vector())
    }
    case _ => throw TypeMismatchException(getType(string), DataTypes.String, Vector())
  }
  
  def getObject(obj: Any): Map[String, Any] = obj match {
    case v: JsObject => v.as[Map[String, JsValue]]
    case _ => throw TypeMismatchException(getType(obj), DataTypes.Object, Vector())
  }
  
  def setObjectValue(obj: Any, name: String, value: Any): Any =
    (value, obj) match {
      case (value: JsValue, obj: JsValue) => PJson.toJson(obj.as[Map[String, JsValue]].updated(name, value))
    }
  
  def removeObjectValue(obj: Any, name: String): Any = obj match {
    case obj: JsObject => PJson.toJson(obj.as[Map[String, JsValue]] - name)
  }
  
  def addArrayValue(array: Any, value: Any): Any = array match {
    case v: JsValue => PJson.toJson(v.as[Array[JsValue]] :+ value.asInstanceOf[JsValue])
  }
  
  def setArrayValue(array: Any, index: Int, value: Any): Any = array match {
    case v: JsValue =>
      val array = v.as[Array[JsValue]]
      PJson.toJson(array.padTo(index, JsNull: JsValue).patch(index, Seq(value.asInstanceOf[JsValue]), 1))
  }
  
  def isArray(array: Any): Boolean = try {
    array match { case array: JsValue => array.as[Array[JsValue]] }
    true
  } catch {
    case e: Exception => false
  }

  def isBoolean(boolean: Any): Boolean = try {
    boolean match { case boolean: JsValue => boolean.as[Boolean] }
    true
  } catch {
    case e: ClassCastException => false
    case e: Exception => false
  }

  def isNumber(num: Any): Boolean = try {
    num match { case num: JsValue => num.as[Double] }
    true
  } catch {
    case e: ClassCastException => false
    case e: Exception => false
  }

  def isString(string: Any): Boolean = try {
    string match { case string: JsValue => string.as[String] }
    true
  } catch {
    case e: ClassCastException => false
    case e: Exception => false
  }

  def isObject(obj: Any): Boolean = obj match {
    case obj: JsValue => try { obj.as[Map[String, JsValue]]; true } catch { case e: Exception => false }
    case _ => false
  }
  
  def isNull(obj: Any): Boolean = obj match {
    case n: JsValue if n.toString == "null" => true
    case _ => false
  }
  
  def nullValue: Any = PJson.toJson(null)
  
  def fromArray(array: Seq[Any]): Any = PJson.toJson(array.map(_.asInstanceOf[JsValue]))
  def fromBoolean(boolean: Boolean): Any = PJson.toJson(boolean)
  def fromDouble(number: Double): Any = PJson.toJson(number)
  def fromBigDecimal(number: BigDecimal): Any = PJson.toJson(number)
  
  def fromObject(obj: Map[String, Any]): Any =
    PJson.toJson(obj.map { case (k, v: JsValue) => (k, v) })
  
  def fromString(string: String): Any = PJson.toJson(string)

}
