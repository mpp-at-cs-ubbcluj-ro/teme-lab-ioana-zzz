package org.example.repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Domain.Entity;
import org.example.Utils.JdbcUtils;
import org.example.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {


    protected JdbcUtils dbUtils;

    protected static final Logger logger= LogManager.getLogger();

    public AbstractDatabaseRepository(Properties props) {
        logger.info("Initializing" + getClass() + " with properties: {} ", props);
        dbUtils = new JdbcUtils(props);


    }

    protected abstract E resultSetToEntity(ResultSet rs) throws SQLException;
    protected abstract void entityToPreparedStatement(E entity, PreparedStatement preparedStatement) throws SQLException;
    protected abstract String getInsertQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();
    protected abstract String getSelectByIdQuery();
    protected abstract String getTableName();



    @Override
    public Optional<E> findOne(ID id) {
        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getSelectByIdQuery())) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                E entity = resultSetToEntity(resultSet);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<E> findAll() {
        List<E> entities = new ArrayList<>();
        String query = "SELECT * FROM " + getTableName();

        try (Connection connection = dbUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                E entity = resultSetToEntity(resultSet);
                entities.add(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getInsertQuery())) {
            entityToPreparedStatement(entity, preparedStatement);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> entityToDelete = findOne(id);
        if (entityToDelete.isEmpty()) {
            return Optional.empty();
        }

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getDeleteQuery())) {
            entityToPreparedStatement(entityToDelete.get(), preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entityToDelete;
    }

    @Override
    public Optional<E> update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }

        Optional<E> existingEntity = findOne(entity.getId());
        if (existingEntity.isEmpty()) {
            return Optional.of(entity);
        }

        try (Connection connection = dbUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getUpdateQuery())) {
            entityToPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
