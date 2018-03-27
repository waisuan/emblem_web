package model;

import java.util.ArrayList;
import java.util.List;

public class ResultWrapper {
  Machine machine;
  String message;
  List<Machine> machines;
  List<Machine> machinesDue;
  MaintenanceHistory history;
  List<MaintenanceHistory> allHistory;

  public ResultWrapper() {
    this.message = "";
    machines = new ArrayList<Machine>();
    machinesDue = new ArrayList<Machine>();
    this.allHistory = new ArrayList<MaintenanceHistory>();
  }

  public Machine getMachine() {
    return machine;
  }

  public void setMachine(Machine machine) {
    this.machine = machine;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<Machine> getMachines() {
    return machines;
  }

  public void setMachines(List<Machine> machines) {
    this.machines = machines;
  }

  public List<Machine> getMachinesDue() {
    return machinesDue;
  }

  public void setMachinesDue(List<Machine> machinesDue) {
    this.machinesDue = machinesDue;
  }

  public MaintenanceHistory getHistory() {
    return history;
  }

  public void setHistory(MaintenanceHistory history) {
    this.history = history;
  }

  public List<MaintenanceHistory> getAllHistory() {
    return allHistory;
  }

  public void setAllHistory(List<MaintenanceHistory> allHistory) {
    this.allHistory = allHistory;
  }
}
