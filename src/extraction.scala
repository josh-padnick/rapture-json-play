/**********************************************************************************************\
* Rapture JSON Library                                                                         *
* Version 1.0.7                                                                                *
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

import rapture.json._
import rapture.data._

import play.api.libs.json._

trait Extractors {
  implicit val jsValueExtractor: JsonCastExtractor[JsValue] =
    JsonCastExtractor(PlayAst, DataTypes.Undefined)
  
  implicit val jsObjectExtractor: JsonCastExtractor[JsObject] =
    JsonCastExtractor(PlayAst, DataTypes.Object)
  
  implicit val jsArrayExtractor: JsonCastExtractor[JsArray] =
    JsonCastExtractor(PlayAst, DataTypes.Array)
}