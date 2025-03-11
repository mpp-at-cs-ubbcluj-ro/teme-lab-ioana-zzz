package org.example.repository.database;

import org.example.Domain.Person;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PersonDBRepository extends AbstractDatabaseRepository<Long, Person> {



    public PersonDBRepository(Properties props) {
        super(props);


    }


    @Override
    protected Person resultSetToEntity(ResultSet rs) throws SQLException {
        return new Person(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("birth_date") != null ?
                        LocalDate.parse(rs.getString("birth_date"), DateTimeFormatter.ISO_LOCAL_DATE) : null,
                rs.getString("address")
        );
    }

    @Override
    protected void entityToPreparedStatement(Person entity, PreparedStatement pstmt) throws SQLException {
        int count = pstmt.getParameterMetaData().getParameterCount();
        switch (count) {
            case 5 -> {
                pstmt.setLong(5, entity.getId());
                pstmt.setString(1, entity.getFirstName());
                pstmt.setString(2, entity.getLastName());
                pstmt.setString(4, entity.getAddress());
                pstmt.setString(3, entity.getBirthDate() != null ?
                        entity.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : null);
            }
            case 1 -> {
                pstmt.setLong(1, entity.getId());
            }
        }
    }

    @Override
    protected String getTableName() {
        return "people";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO people (first_name, last_name, birth_date, address, id) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE people SET first_name = ?, last_name = ?, birth_date = ?, address = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM people WHERE id = ?";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM people WHERE id = ?";
    }

    
    public List<Person> findAllPeopleLastName(String lastName) {
        List<Person> entities = new ArrayList<>();
        String query = "SELECT * FROM " + getTableName() + " WHERE " + "last_name = " + "'" + lastName + "'";

        try (Connection connection = dbUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Person entity = resultSetToEntity(resultSet);
                entities.add(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;

    }
}
