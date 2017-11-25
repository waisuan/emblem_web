package app;

import static spark.Spark.get;
import static spark.Spark.post;

public class Resource {
  private static final String API_CONTEXT = "/api";

  private final Service service;

  public Resource(Service service) {
    this.service = service;
    setupEndpoints();
  }

  private void setupEndpoints() {
    post(API_CONTEXT + "/machines", "application/json", (request, response) -> {
      System.out.println(request.body());
      // todoService.createNewTodo(request.body());
      response.status(201);
      // response.status(400);
      return response;
    }, new JsonTransformer());

    get(API_CONTEXT + "/machines/:type/:value", "application/json", (request, response)

    -> service.getMachine(request.params(":type"), request.params(":value")), new JsonTransformer());

    get(API_CONTEXT + "/machines", "application/json", (request, response)

    -> service.getAllMachines(), new JsonTransformer());

    // put(API_CONTEXT + "/todos/:id", "application/json", (request, response)
    //
    // -> todoService.update(request.params(":id"), request.body()), new JsonTransformer());

    // get(API_CONTEXT + "/newMachine", "application/json", (request, response)
    //
    // -> service.getAllMachines(), new JsonTransformer());
  }
}
