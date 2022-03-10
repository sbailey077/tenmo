package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer addNewTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfer(" +
                "employee_id, date_worked, hours_worked, billable, description) " +
                " VALUES (?, ?, ?, ?, ?) RETURNING timesheet_id;";
    }
}
