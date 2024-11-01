package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static nextstep.qna.domain.AnswerTest.A1;
import static nextstep.qna.domain.AnswerTest.A2;
import static nextstep.qna.domain.QuestionTest.Q1;
import static nextstep.users.domain.NsUserTest.JAVAJIGI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AnswersTest {

    @Test
    @DisplayName("질문에 대한 답변들 삭제 테스트_성공")
    public void deleteAnswers_success() throws CannotDeleteException {
        Answers answers = new Answers();
        answers.add(A1, Q1);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        answers.deleteAnswers(deleteHistories, JAVAJIGI);

        assertThat(A1.isDeleted()).isTrue();
        assertThat(deleteHistories.get(0)).isEqualTo(new DeleteHistory(ContentType.ANSWER, null, JAVAJIGI, LocalDateTime.now()));
    }

    @Test
    @DisplayName("질문에 대한 답변들 삭제 테스트_실패")
    public void deleteAnswers_fail() throws CannotDeleteException {
        Answers answers = new Answers();
        answers.add(A2, Q1);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        assertThatThrownBy(() -> answers.deleteAnswers(deleteHistories, JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
