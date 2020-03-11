package repositories

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsObject, Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.JsObjectDocumentWriter
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json.collection.JSONCollectionProducer

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountsRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit ec: ExecutionContext) {

  private val idKey = "_id"

  private def collection: Future[JSONCollection] = reactiveMongoApi.database.map {
    _.collection[JSONCollection](name = "accounts")
  }

  def update[A](id: String, key: String, updates: A)(implicit format: OFormat[A]): Future[WriteResult] = collection.flatMap {
    _.update(ordered = false).one(
      q = Json.obj(idKey -> id),
      u = Json.obj(fields = "$set" -> Json.obj(key -> updates)),
      upsert = false
    ) filter (_.n == 1)
  }

  def insert[A](id: String, value: A)(implicit format: OFormat[A]): Future[WriteResult] = collection.flatMap {
    _.insert.one(Json.obj(idKey -> id) ++ Json.toJsObject(value))
  }

  def findById[A](id: String)(implicit format: OFormat[A]): Future[Option[A]] = collection.flatMap {
    _.find[JsObject, A](
      selector = Json.obj(idKey -> id),
      projection = None
    ).one[A]
  }

  def delete(id: String): Future[WriteResult] = collection.flatMap {
    _.delete.one(Json.obj(idKey -> id))
  }

  def drop: Future[Boolean] = collection.flatMap {
    _.drop(failIfNotFound = false)
  }

}
