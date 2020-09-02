package com.example.linebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

// spring-jdbc を使いたいクラスにつける
@Repository
public class QuestionRepository {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public QuestionRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<PreExam> selectPreExams(String kamoku, int questionNumber) {
    // switch文めんどいので、とりあえず英単語が来たとする
    String sql = "select * from 英単語 where questionNumber = ?";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PreExam.class), questionNumber);
  }
}
