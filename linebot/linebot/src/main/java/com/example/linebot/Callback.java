package com.example.linebot;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
    public class Callback {

        private static final Logger log = LoggerFactory.getLogger(Callback.class);
        private int n;
        String kaigyou_code_1 = System.lineSeparator();

        private final QuestionRepository questionRepository;

    // QuestionRepositoryをDIする
        @Autowired
        public Callback(QuestionRepository questionRepository) {
            this.questionRepository = questionRepository;
        }

    // フォローイベントに対応する
        @EventMapping
        public TextMessage handleFollow(FollowEvent event) {
            // 実際の開発ではユーザIDを返信せず、フォロワーのユーザIDをデータベースに格納しておくなど
            String userId = event.getSource().getUserId();
            //return replyFirst("あなたのユーザIDは " + userId);
            return replyFirst("登録ありがとうございます！このアカウントは一問一答形式の勉強アカウントです。");
        }

        // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
        @EventMapping
        public Message handleMessage(MessageEvent<TextMessageContent> event) throws Exception{
            TextMessageContent tmc = event.getMessage();
            String text = tmc.getText();

            switch (text) {
                case "英単語":
                case "社会":
                case "化学":
                    return reply(text);
                case "1":
                case "2":
                case "3":
                case "4":
                    return answer(text);
                default:
                    return replyFirst("入力されたものが正しくありません。もう一度入力してください");
            }
        }
            //switchでメソッドを仕分ける。教科選択if(text.equals("英単語")||text.equals("社会")||text.equals("化学"))、解答、
            // それ以外はもう一度入力してくださいの出力


        // 返答メッセージを作る（教科選択）
        private TextMessage reply(String text) throws Exception{

//            Question question = new Question(text);

            n = new java.util.Random().nextInt(20);
            List<PreExam> returning = questionRepository.selectPreExams(text, n);
            return new TextMessage(returning.get(n).getMonndai() +kaigyou_code_1
                    + returning.get(n).getSentaku1() +kaigyou_code_1
                    + returning.get(n).getSentaku2() +kaigyou_code_1
                    + returning.get(n).getSentaku3() +kaigyou_code_1
                    + returning.get(n).getSentaku4());
        }

        //解答表示
        private TextMessage answer(String text) throws Exception{
//            Question question = new Question(text);
            List<PreExam> returning = questionRepository.selectPreExams(text, n);
            return new TextMessage(returning.get(0).getAnswer() + returning.get(0).getKaisetu());
        }

        // 返答メッセージを作る
        private TextMessage replyFirst(String text) {
            String textFirst ="学習を始めるには英単語・社会・化学のいずれかを入力してください。回答は1・2・3・4のいずれかを入力してください。";
            return new TextMessage(text + textFirst);
        }
    
        /*// 返答メッセージを作る
        private TextMessage replyFirst(String text) {
            return new TextMessage(text);
        }*/

        /*// 確認メッセージをpush
        @GetMapping("confirm")
        public String pushConfirm() {
            String text = "質問だよ";
            try {
                Message msg = new TemplateMessage(text,
                        new ConfirmTemplate("いいかんじ？",
                                new PostbackAction("おけまる", "CY"),
                                new PostbackAction("やばたん", "CN")));
                PushMessage pMsg = new PushMessage(userId, msg);
                BotApiResponse resp = client.pushMessage(pMsg).get();
                log.info("Sent messages: {}", resp);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            return text;
        }*/
    }


