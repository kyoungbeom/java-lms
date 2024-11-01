package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static nextstep.qna.domain.AnswerTest.A1;
import static nextstep.users.domain.NsUserTest.JAVAJIGI;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question(JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(NsUserTest.SANJIGI, "title2", "contents2");

    @Test
    @DisplayName("답변 추가 테스트")
    public void addAnswer() {
        Q1.addAnswer(A1);

        assertThat(Q1.getAnswers().get(0)).isEqualTo(A1);
    }

    @Test
    @DisplayName("로그인 한 유저가 질문 작성자인지 테스트")
    public void isOwner() {
        assertThatCode(() -> Q1.isOwner(JAVAJIGI)).doesNotThrowAnyException();

        assertThatThrownBy(() -> Q2.isOwner(JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문 삭제 테스트")
    public void deleteQuestion() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = Q1.deleteQuestion(JAVAJIGI);

        assertAll(
                () -> assertThat(deleteHistories.get(0)).isEqualTo(new DeleteHistory(ContentType.QUESTION, 0L, JAVAJIGI, LocalDateTime.now())),
                () -> assertThat(Q1.isDeleted()).isTrue(),
                () -> assertThat(deleteHistories.get(1)).isEqualTo(new DeleteHistory(ContentType.ANSWER, null, JAVAJIGI, LocalDateTime.now())),
                () -> assertThat(A1.isDeleted()).isTrue()
        );
    }

}
