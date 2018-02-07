package com.psl.app;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.json.simple.JSONObject;

import com.psl.fabric.util.CAUtility;

@Path("/api")
public class Application {

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(RegistrationRequestData request) {

		System.out.println("Input"+request);
		JSONObject response = new JSONObject();
		String enrollSecret = "";
		try {
			enrollSecret = CAUtility.registerUser(request.getUserName(), request.getUserOrg(), request.getUserAffiliatiaon());
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", e.getMessage());
			return Response.status(500).entity(response).build();
		}
		
		response.put("message", enrollSecret);
		return Response.status(201).entity(response).build();
		
	}
	
	@POST
	@Path("/enroll")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response enrollUser(EnrollRequestData request) {

		JSONObject response = new JSONObject();
		System.out.println("Input: "+ request);
		try {
			CAUtility.enrollUser(request.getUserName(), request.getEnrollmentSecret(), request.getUserOrg());
		} catch (EnrollmentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.put("message", e.getMessage());
			return Response.status(500).entity(response).build();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.put("message", e.getMessage());
			return Response.status(500).entity(response).build();
		}
		response.put("message", "Enrolled Successfully");
		return Response.status(201).entity(response).build();
		
	}

}
