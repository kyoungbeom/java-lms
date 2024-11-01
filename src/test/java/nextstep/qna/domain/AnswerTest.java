package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static nextstep.users.domain.NsUserTest.JAVAJIGI;
import static nextstep.users.domain.NsUserTest.SANJIGI;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 삭제 테스트")
    public void deleteAnswer() {
        assertAll(
                () -> assertThat(A1.deleteAnswer(JAVAJIGI)).isEqualTo(new DeleteHistory(ContentType.ANSWER, null, JAVAJIGI, LocalDateTime.now())),
                () -> assertThat(A1.isDeleted()).isTrue(),
                () -> assertThatThrownBy(() -> A2.isOwner(JAVAJIGI))
                        .isInstanceOf(CannotDeleteException.class)
                        .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.")
        );
    }

    @Test
    @DisplayName("질문 작성자가 답변 작성자와 같은지 테스트")
    public void isOwner() {
        assertThatCode(() -> A1.isOwner(JAVAJIGI))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> A2.isOwner(JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                    .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
