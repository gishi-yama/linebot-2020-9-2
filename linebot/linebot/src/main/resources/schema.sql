-- よくわかんないけど、PreExamクラスを見るに多分こういうテーブルがありそう

DROP TABLE IF EXISTS 英単語 CASCADE;

create table 英単語 (
    questionNumber integer primary key,
    monndai varchar(128),
    sentaku1 varchar(64),
    sentaku2 varchar(64),
    sentaku3 varchar(64),
    sentaku4 varchar(64),
    answer integer,
    kaisetu varchar(128)
);