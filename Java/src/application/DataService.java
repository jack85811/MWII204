package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jersey.repackaged.com.google.common.collect.Maps;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.glassfish.grizzly.http.server.util.Mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class DataService  {
	private String host = "localhost";
	private int port =27017;
	MongoClient mongoClient = new MongoClient(new ServerAddress(host, port));
    DB db = mongoClient.getDB("mycustomers");
    DBCollection coll = db.getCollection("customers");

	
	public Response getProducts(){
		try {
			ObjectMapper mapper = new ObjectMapper();
			try {
				connectToServer();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while connecting to the DB", "be sure that it is up an running")).header("Access-Control-Allow-Origin", "*")
						.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
						.allow("OPTIONS").build();
			}
			/*DBCursor cursor = coll.find();
			List<DBObject> all = cursor.toArray();*/
			
			DBCursor cursor = coll.find();
			List<DBObject> all = new ArrayList<>();
			while(cursor.hasNext()){
				DBObject currentCursorObject = cursor.next();
				BasicDBObject testObject = new BasicDBObject();
				testObject.putAll(currentCursorObject);
				testObject.put("productId", currentCursorObject.get("_id").toString());
				all.add(testObject);
			}
			
			
			return Response.ok(all).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		} catch (Exception e) {
			Map<String, String> reply = setReplyErrorMessage("404", "Products do not exist", "Please be sure to create some");
			return Response.status(404).entity(reply).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}

	}
	
	public Response getProduct(String objectId){
		try {
			connectToServer();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while connecting to the DB", "be sure that it is up an running")).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
		BasicDBObject query = new BasicDBObject();
		try {
		    query.put("_id", new ObjectId(objectId));
			DBObject obj = coll.findOne(query);
			obj.put("productId", objectId);
			System.out.println(obj);
			return Response.ok(obj).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		} catch (Exception e) {
			Map<String, String> reply = setReplyErrorMessage("404", "Product with id " + objectId + " does not exist", "only " + String.valueOf(getProductCount()) + " Product exist");
			return Response.status(404).entity(reply).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
	}
	
	public long getProductCount(){
		long obj = coll.count();
		return obj;
	}
	
	public Response insertProduct(Product product){
		try {
			connectToServer();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while connecting to the DB", "be surethat it is up an running")).build();
		}
		BasicDBObject document = new BasicDBObject();
		document.put("productName", product.getProductName());
		document.put("productCode", product.getProductCode());
		document.put("description", product.getDescription());
		document.put("price", product.getPrice());
		document.put("starRating", product.getStarRating());
		try {
			coll.insert(document);
		    return Response.ok().build(); 
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while inserting the data to the DB", "be surethat you use the proper Json Product structure")).build();
		}	
	}
	
	public Response updateProduct(Product product){
		try {
			connectToServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while connecting to the DB", "be sure that it is up an running")).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
		BasicDBObject document = setDocument(product);
		BasicDBObject searchQuery;
		try {
			searchQuery = new BasicDBObject().append("_id", new ObjectId(product.getProductId()));
		} catch (Exception e) {
			Map<String, String> reply = setReplyErrorMessage("404", "customer with id " + product.getProductId() + " does not exist", "only " + String.valueOf(getProductCount()) + " Product exist");
			return Response.status(404).entity(reply).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
		try {
			coll.update(searchQuery, document);
			return Response.ok().header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while updating the data to the DB", "be sure that you use the proper Json Product structure")).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
	}
	
	public Response removeProduct(String id){
		try {
			connectToServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while connecting to the DB", "be sure that it is up an running")).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
		BasicDBObject searchQuery;
		try {
			searchQuery = new BasicDBObject().append("_id", new ObjectId(id));
		} catch (Exception e) {
			Map<String, String> reply = setReplyErrorMessage("404", "Product with id " + id + " does not exist", "Be sure to use the correct id of the product");
			return Response.status(404).entity(reply).type(MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
		try {
			coll.remove(searchQuery);
			return Response.ok().header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity(setReplyErrorMessage("500", "Something happend while removing the data to the DB", "be sure that you use the proper Json Product structzre")).header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
					.allow("OPTIONS").build();
		}
	}
	
	
	
	 private BasicDBObject setDocument(Product product) {
		BasicDBObject document = new BasicDBObject();
		if (product.getProductName() != null) {
			document.append("productName", product.getProductName());
		}
		if (product.getProductCode() != null) {
			document.append("productCode", product.getProductCode());
		}
		if (product.getDescription() != null) {
			document.append("description", product.getDescription());
		}
		if (product.getPrice() != 0.0) {
			document.append("price", product.getPrice());
		}
		if (product.getStarRating() != 0.0) {
			document.append("starRating", product.getStarRating());
		}
		BasicDBObject setQuery = new BasicDBObject(); 
		setQuery.append("$set", document);
		return setQuery;
	}

	public void connectToServer() throws Exception{
	        try {
	        	mongoClient.getAddress();

	        } catch (Exception e) {
	        	throw new Exception(e) ;
			}
	    }
	
	/**
	 * timestamp function to set the time for the error message
	 * @return
	 */
	private static String timestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(new Date());
	}
	/**
	 * function for setting a Default Error message
	 * @param status
	 * @param message
	 * @param information
	 * @return
	 */
	public static Map<String, String> setReplyErrorMessage(String status, String message, String information) {
        Map<String, String> entity = Maps.newHashMap();
        entity.put("Status Code", status);
        entity.put("Message", message);
        entity.put("Information", information);
        entity.put("timestamp", timestamp());
        return entity;
	}
}
