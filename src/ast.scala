/**********************************************************************************************\
* Rapture JSON Library                                                                         *
* Version 1.0.8                                                                                *
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
    case obj: JsValue => obj \ element match {
      case v: JsUndefined => throw null
      case v => v
    }
    case _ => throw TypeMismatchException(getType(obj), DataTypes.Object, Vector())
  }
  
  override def getKeys(obj: Any): Iterator[String] =
    obj match {
      case obj: JsObject => obj.keys.iterator
      case _ => throw TypeMismatchException(getType(obj), DataTypes.Object, Vector())
    }
  
  override def dereferenceArray(array: Any, element: Int): Any =
    array match {
      case v: JsValue => v(element)
      case _ => throw TypeMismatchException(getType(array), DataTypes.Array, Vector())
    }

  def getArray(array: Any): List[Any] = array match {
    case v: JsValue => v.asOpt[List[JsValue]].getOrElse(throw TypeMismatchException(getType(v), DataTypes.Array, Vector()))
    case _ => throw TypeMismatchException(getType(array), DataTypes.Array, Vector())
  }

  def getBoolean(boolean: Any): Boolean = boolean match {
    case v: JsValue => v.asOpt[Boolean].getOrElse(throw TypeMismatchException(getType(v), DataTypes.Boolean, Vector()))
    case _ => throw TypeMismatchException(getType(boolean), DataTypes.Boolean, Vector())
  }
  
  def getBigDecimal(bigDecimal: Any): BigDecimal = bigDecimal match {
    case v: JsValue => v.asOpt[BigDecimal].getOrElse(throw TypeMismatchException(getType(v), DataTypes.Number, Vector()))
    case _ => throw TypeMismatchException(getType(bigDecimal), DataTypes.Number, Vector())
  }
  
  def getDouble(double: Any): Double = double match {
    case v: JsValue => v.asOpt[Double].getOrElse(throw TypeMismatchException(getType(v), DataTypes.Number, Vector()))
    case _ => throw TypeMismatchException(getType(double), DataTypes.Number, Vector())
  }
  
  def getString(string: Any): String = string match {
    case v: JsValue => v.asOpt[String].getOrElse(throw TypeMismatchException(getType(string), DataTypes.String, Vector()))
    case _ => throw TypeMismatchException(getType(string), DataTypes.String, Vector())
  }
  
  def getObject(obj: Any): Map[String, Any] = obj match {
    case v: JsValue => v.asOpt[Map[String, JsValue]].getOrElse(throw TypeMismatchException(getType(v), DataTypes.Object, Vector()))
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
    array match {
      case array: JsUndefined => false
      case array: JsValue =>
        array.as[Array[JsValue]]
        true
    }
  } catch { case e: Exception => false }

  def isBoolean(boolean: Any): Boolean = try {
    boolean match {
      case boolean: JsUndefined => false
      case boolean: JsValue =>
        boolean.as[Boolean]
        true
    }
  } catch { case e: Exception => false }

  def isNumber(num: Any): Boolean = try {
    num match {
      case num: JsUndefined => false
      case num: JsValue =>
        num.as[Double]
        true
    }
  } catch { case e: Exception => false }

  def isString(string: Any): Boolean = try {
    string match {
      case string: JsUndefined => false
      case string: JsValue =>
        string.as[String]
        true
    }
  } catch { case e: Exception => false }

  def isObject(obj: Any): Boolean = try {
    obj match {
      case obj: JsUndefined => false
      case obj: JsValue =>
        obj.as[Map[String, JsValue]]
        true
    }
  } catch { case e: Exception => false }
  
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
