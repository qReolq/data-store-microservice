package qreol.project.datastoremicroservice.service;

public interface CDCEventConsumer {

    void handle(String message);

}
