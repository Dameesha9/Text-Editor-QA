package bll;

public interface Command {
    boolean execute();
    String getResult();
}
