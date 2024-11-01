package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Answers {
    private final List<Answer> values = new ArrayList<>();

    public void add(Answer answer, Question question) {
        answer.toQuestion(question);
        values.add(answer);
    }

    public void deleteAnswers(List<DeleteHistory> deleteHistories, NsUser writer) throws CannotDeleteException {
        hasOtherUserAnswer(writer);

        for(Answer answer : values) {
            deleteHistories.add(answer.deleteAnswer(writer));
        }
    }

    private void hasOtherUserAnswer(NsUser writer) throws CannotDeleteException {
        for(Answer answer : values) {
            answer.isOwner(writer);
        }
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(values);
    }
    
}
