package com.example.linebot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

// linebot 動作させるのめんどいのでテストクラスでテストするぞ
@SpringJUnitConfig
public class QuestionRepositoryTest {

  private QuestionRepository questionRepository;

  @BeforeEach
  void setDataSource() {
    // 通常起動ではSpring Bootが自動的にやってくれることだけど、テストの場合は手動でやる
    var ds = new EmbeddedDatabaseBuilder()
      .setType(EmbeddedDatabaseType.H2)
      .addScripts("classpath:schema.sql")
      .addScript("classpath:data.sql")
      .build();
    var jdbcTemplate = new JdbcTemplate(ds);
    this.questionRepository = new QuestionRepository(jdbcTemplate);
  }


  @Test
  void selectPreExamsが動く() throws JsonProcessingException {
    var returning = questionRepository.selectPreExams("英単語", 1);
    var actual = new ObjectMapper().writeValueAsString(returning);
    Assertions.assertEquals(
      "[{\"questionNumber\":1,\"monndai\":\"問題文だよ\",\"sentaku1\":\"選択肢1だよ\",\"sentaku2\":\"選択肢2だよ\",\"sentaku3\":\"選択肢3だよ\",\"sentaku4\":\"選択肢4だよ\",\"answer\":2,\"kaisetu\":\"選択肢2が正解よ\"}]",
      actual);
  }

}
