package app;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import exception.RecordAlreadyExistsException;
import exception.RecordDoesNotExistException;
import exception.RecordWasRecentlyUpdatedException;
import model.Machine;
import model.MaintenanceHistory;
import model.ResultWrapper;
import model.User;

public class Resource {
  private static final String API_CONTEXT = "/api";

  private final Service service;

  public Resource(Service service) {
    this.service = service;
    setupEndpoints();
  }

  // https://github.com/shekhargulati/todoapp-spark/blob/master/src/main/resources/public/index.html
  private void setupEndpoints() {

    get(API_CONTEXT + "/login/:username/:password", "application/json", (request, response) -> {
      User user = null;
      try {
        response.status(200);
        user = service.login(request.params(":username"), request.params(":password"));
      } catch (RecordAlreadyExistsException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("GET (/LOGIN):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return user;
    }, new JsonTransformer());

    get(API_CONTEXT + "/logout/:username", "application/json", (request, response) -> {
      try {
        response.status(200);
        service.logout(request.params(":username"));
      } catch (RecordAlreadyExistsException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("GET (/LOGOUT):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return response;
    }, new JsonTransformer());

    // get(API_CONTEXT + "/machines/:type/:value", "application/json",
    // (request, response) -> service.getMachine(request.params(":type"), request.params(":value")),
    // new JsonTransformer());

    get(API_CONTEXT + "/machines", "application/json", (request, response) -> {
      ResultWrapper wrapper = new ResultWrapper();
      try {
        response.status(200);
        wrapper = service.getAllMachines();
      } catch (Exception e) {
        System.err.println("GET (/machines):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return wrapper;
    }, new JsonTransformer());

    get(API_CONTEXT + "/machines/due", "application/json", (request, response) -> {
      List<Machine> machinesDue = new ArrayList<Machine>();
      try {
        response.status(200);
        machinesDue = service.getAllMachinesDue();
      } catch (Exception e) {
        System.err.println("GET (/machines/due):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return machinesDue;
    }, new JsonTransformer());

    // /machines/:serialNumber/:lastUpdated/:hardfailure
    get(API_CONTEXT + "/machines/:serialNumber", "application/json", (request, response) -> {
      ResultWrapper wrapper = new ResultWrapper();
      try {
        response.status(200);
        wrapper = service.getOneMachine(request.params(":serialNumber"), request.queryParams("lastUpdated"),
            request.queryParams("hardfailure"));
      } catch (RecordWasRecentlyUpdatedException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("GET (/machines/:serialNumber/:lastUpdated):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return wrapper;
    }, new JsonTransformer());

    post(API_CONTEXT + "/machines", "application/json", (request, response) -> {
      Machine machine = new Gson().fromJson(request.body(), Machine.class);
      ResultWrapper wrapper = new ResultWrapper();
      try {
        wrapper = service.insertMachine(machine);
        response.status(201);
      } catch (RecordAlreadyExistsException re) {
        response.status(409);
      } catch (Exception e) {
        System.err.println("POST (/machines):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return wrapper;
    }, new JsonTransformer());

    put(API_CONTEXT + "/machines", "application/json", (request, response) -> {
      Machine machine = new Gson().fromJson(request.body(), Machine.class);
      ResultWrapper wrapper = new ResultWrapper();
      try {
        wrapper = service.updateMachine(machine);
        response.status(200);
      } catch (RecordWasRecentlyUpdatedException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("PUT (/machines):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return wrapper;
    }, new JsonTransformer());

    delete(API_CONTEXT + "/machines/:serialNumber/:lastUpdated", "application/json", (request, response) -> {
      try {
        service.deleteMachine(request.params(":serialNumber"), request.params(":lastUpdated"));
        response.status(200);
      } catch (RecordWasRecentlyUpdatedException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("DELETE (/machines):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return response;
    }, new JsonTransformer());

    get(API_CONTEXT + "/history/:serialNumber", "application/json", (request, response) -> {
      List<MaintenanceHistory> history = new ArrayList<MaintenanceHistory>();
      try {
        response.status(200);
        history = service.getHistory(request.params(":serialNumber"));
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("GET (/history/:serialNumber):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return history;
    }, new JsonTransformer());

    get(API_CONTEXT + "/history/:serialNumber/:workOrderNumber", "application/json", (request, response) -> {
      ResultWrapper wrapper = new ResultWrapper();
      try {
        response.status(200);
        wrapper = service.getHistory(request.params(":serialNumber"), request.params(":workOrderNumber"),
            request.queryParams("lastUpdated"), request.queryParams(":hardfailure") != null ? true : false, true);
      } catch (RecordWasRecentlyUpdatedException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("GET (/history/:serialNumber/:workOrderNumber):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return wrapper;
    }, new JsonTransformer());

    post(API_CONTEXT + "/history", "application/json", (request, response) -> {
      MaintenanceHistory history = new Gson().fromJson(request.body(), MaintenanceHistory.class);
      try {
        service.insertHistory(history);
        response.status(201);
      } catch (RecordAlreadyExistsException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("POST (/history):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return response;
    }, new JsonTransformer());

    put(API_CONTEXT + "/history", "application/json", (request, response) -> {
      MaintenanceHistory history = new Gson().fromJson(request.body(), MaintenanceHistory.class);
      try {
        history = service.updateHistory(history);
        response.status(200);
      } catch (RecordWasRecentlyUpdatedException re) {
        response.status(409);
      } catch (RecordDoesNotExistException re) {
        response.status(404);
      } catch (Exception e) {
        System.err.println("PUT (/history):: " + e.toString());
        e.printStackTrace();
        response.status(400);
      }
      return history;
    }, new JsonTransformer());

    delete(API_CONTEXT + "/history/:serialNumber/:workOrderNumber/:lastUpdated", "application/json",
        (request, response) -> {
          try {
            service.deleteHistory(request.params(":serialNumber"), request.params(":workOrderNumber"),
                request.params(":lastUpdated"));
            response.status(200);
          } catch (RecordWasRecentlyUpdatedException re) {
            response.status(409);
          } catch (RecordDoesNotExistException re) {
            response.status(404);
          } catch (Exception e) {
            System.err.println("DELETE (/history):: " + e.toString());
            e.printStackTrace();
            response.status(400);
          }
          return response;
        }, new JsonTransformer());
  }
}
