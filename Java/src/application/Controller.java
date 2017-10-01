package application;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/products")
public class Controller {
	
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JsonCreator
	public Response updateProductEndpoint(String data) {
		ObjectMapper mapper = new ObjectMapper();
		Product product;
		try {
			product = mapper.readValue(data, Product.class);
			System.out.println(product.getProductId());
		} catch (Exception e) {
			Map<String, String> reply = DataService.setReplyErrorMessage("400", "Json format invalid", "Only the following field are allowed: 	productName, productCode, description, price, starRating");
			return Response.status(400).entity(reply).type(MediaType.APPLICATION_JSON).build();
		}
		DataService dataService = new DataService();
		System.out.println(product);
		return dataService.updateProduct(product);  
	}
	
    @DELETE
    @Path("/{id}/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JsonCreator
	public Response removeProductEndpoint(@PathParam("id") String id) {
		DataService dataService = new DataService();
		return dataService.removeProduct(id);
	}
    
	/**
	 * Endpoint for creating a Product.
	 * @param data
	 * @return
	 */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JsonCreator
    public Response insertCutsomerEndpoint(String data	) {
		ObjectMapper mapper = new ObjectMapper();
		Product product;
		try {
			product = mapper.readValue(data, Product.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Map<String, String> reply = DataService.setReplyErrorMessage("400", "Json format invalid", "Only the following field are allowed: productName, productCode, description, price, starRating");
			return Response.status(400).entity(reply).type(MediaType.APPLICATION_JSON).build();
		}
		DataService dataService = new DataService();
		System.out.println(product);
		return dataService.insertProduct(product);  
    }
		
	/**
	 * retrieves all the Products from the DB. It will use the DataService class for that.
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductsEndpoint() {
		DataService dataService = new DataService();
		Response obj = dataService.getProducts();
		return obj;

	}
	
	/**
	 * retrieves a single Product from the local DB. It will use the DataService class for that.
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductsIdEndpoint(@PathParam("id") String id) {
		DataService dataService = new DataService();
		Response obj = dataService.getProduct(id);
		System.out.println(obj);
		return obj;	
	}

}
