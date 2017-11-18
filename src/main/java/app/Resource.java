package app;

import static spark.Spark.get;

public class Resource {
  private static final String API_CONTEXT = "/api";

  private final Service service;

  public Resource(Service service) {
    this.service = service;
    setupEndpoints();
  }

  private void setupEndpoints() {
    // post(API_CONTEXT + "/todos", "application/json", (request, response) -> {
    // todoService.createNewTodo(request.body());
    // response.status(201);
    // return response;
    // }, new JsonTransformer());

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
