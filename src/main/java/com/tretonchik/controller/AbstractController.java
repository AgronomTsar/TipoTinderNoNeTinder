package com.tretonchik.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.tretonchik.models.Model;
import com.tretonchik.service.Service;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import org.sqlite.date.ExceptionUtils;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractController<T extends Model<U>, U> implements Controller<T, U> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    private Service<T, U> service;
    private ObjectMapper objectMapper;
    private final Class<T> modelClass;
    public AbstractController(Service<T, U> service, ObjectMapper objectMapper, Class<T> modelClass) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.modelClass = modelClass;
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    public Service<T, U> getService() {
        return service;
    }

    public void setService(Service<T, U> service) {
        this.service = service;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void getOne(Context context, U id) {
        Model<U> model = service.findById(id);
        try {
            context.result(objectMapper.writeValueAsString(model));
        } catch (JsonProcessingException e) {
            System.out.println("error");
        }
    }

    @Override
    public void getAll(Context context) throws SQLException {
        List<T> models = service.findAll();
        try {
            context.result(objectMapper.writeValueAsString(models));
        } catch (JsonProcessingException e) {
            System.out.println("error");
        }
    }

    @Override
    public void postOne(Context context) {
        try {
            T model = objectMapper.readValue(context.body(), modelClass);
            service.save(model);
            context.status(201);
        } catch (JsonProcessingException e) {
            System.out.println("error");
        }
    }

    @Override
    public void patchOne(Context context, U id) {
        try {
            T model = objectMapper.readValue(context.body(), modelClass);
            model.setId(id);
            service.update(model);
        } catch (JsonProcessingException e) {
           System.out.println("error");
        }
    }

    @Override
    public void deleteOne(Context context, U id) {
        service.deleteById(id);
        context.status(204);
    }

}
