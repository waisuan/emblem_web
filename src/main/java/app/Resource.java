package app;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;

import model.Machine;
import model.MaintenanceHistory;

public class Resource {
  private static final String API_CONTEXT = "/api";

  private final Service service;

  public Resource(Service service) {
    this.service = service;
    setupEndpoints();
  }

  // https://github.com/shekhargulati/todoapp-spark/blob/master/src/main/resources/public/index.html
  private void setupEndpoints() {
    post(API_CONTEXT + "/machines", "application/json", (request, response) -> {
      System.out.println(request.body());
      Machine machine = new Gson().fromJson(request.body(), Machine.class);
      service.insertNewMachine(machine);
      response.status(201);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    get(API_CONTEXT + "/machines/:type/:value", "application/json", (request, response)

    -> service.getMachine(request.params(":type"), request.params(":value")), new JsonTransformer());

    get(API_CONTEXT + "/machines", "application/json", (request, response)

    -> service.getAllMachines(), new JsonTransformer());

    // put(API_CONTEXT + "/machines/:id", "application/json",
    // (request, response) -> service.updateMachine(request.params(":id"), request.body()), new JsonTransformer());

    put(API_CONTEXT + "/machines/:id", "application/json", (request, response) -> {
      System.out.println(request.params(":id"));
      System.out.println(request.body());
      Machine machine = new Gson().fromJson(request.body(), Machine.class);
      service.updateMachine(request.params(":id"), machine);
      response.status(200);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    delete(API_CONTEXT + "/machines/:id", "application/json", (request, response) -> {
      System.out.println(request.params(":id"));
      service.deleteMachine(request.params(":id"));
      response.status(200);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    // get(API_CONTEXT + "/newMachine", "application/json", (request, response)
    //
    // -> service.getAllMachines(), new JsonTransformer());
    //

    get(API_CONTEXT + "/history/:id", "application/json", (request, response)

    -> service.getHistory(request.params(":id")), new JsonTransformer());

    post(API_CONTEXT + "/history", "application/json", (request, response) -> {
      System.out.println(request.body());
      MaintenanceHistory history = new Gson().fromJson(request.body(), MaintenanceHistory.class);
      service.insertNewHistory(history);
      response.status(201);
      // response.status(400);
      return response;
    }, new JsonTransformer());
  }
}
