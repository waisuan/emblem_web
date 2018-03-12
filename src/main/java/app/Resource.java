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

    get(API_CONTEXT + "/machines/:type/:value", "application/json", (request, response)

    -> service.getMachine(request.params(":type"), request.params(":value")), new JsonTransformer());

    get(API_CONTEXT + "/machines", "application/json", (request, response)

    -> service.getAllMachines(), new JsonTransformer());

    post(API_CONTEXT + "/machines", "application/json", (request, response) -> {
      Machine machine = new Gson().fromJson(request.body(), Machine.class);
      service.insertMachine(machine);
      response.status(201);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    put(API_CONTEXT + "/machines", "application/json", (request, response) -> {
      Machine machine = new Gson().fromJson(request.body(), Machine.class);
      service.updateMachine(machine);
      response.status(200);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    delete(API_CONTEXT + "/machines/:serialNumber", "application/json", (request, response) -> {
      service.deleteMachine(request.params(":serialNumber"));
      response.status(200);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    get(API_CONTEXT + "/history/:serialNumber", "application/json", (request, response)

    -> service.getHistory(request.params(":serialNumber")), new JsonTransformer());

    get(API_CONTEXT + "/history/:serialNumber/:workOrderNumber", "application/json", (request, response)

    -> service.getHistory(request.params(":serialNumber"), request.params(":workOrderNumber")), new JsonTransformer());

    post(API_CONTEXT + "/history", "application/json", (request, response) -> {
      MaintenanceHistory history = new Gson().fromJson(request.body(), MaintenanceHistory.class);
      service.insertHistory(history);
      response.status(201);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    put(API_CONTEXT + "/history", "application/json", (request, response) -> {
      MaintenanceHistory history = new Gson().fromJson(request.body(), MaintenanceHistory.class);
      service.updateHistory(history);
      response.status(200);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    delete(API_CONTEXT + "/history/:serialNumber/:workOrderNumber", "application/json", (request, response) -> {
      service.deleteHistory(request.params(":serialNumber"), request.params(":workOrderNumber"));
      response.status(200);
      // response.status(400);
      return response;
    }, new JsonTransformer());
  }
}
