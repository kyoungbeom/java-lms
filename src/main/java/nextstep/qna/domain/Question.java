    package nextstep.qna.domain;

    import nextstep.qna.CannotDeleteException;
    import nextstep.users.domain.NsUser;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    public class Question {
        private Long id;

        private String title;

        private String contents;

        private NsUser writer;

        private Answers answers;

        private boolean deleted = false;

        private LocalDateTime createdDate = LocalDateTime.now();

        private LocalDateTime updatedDate;

        public Question() {
        }

        public Question(NsUser writer, String title, String contents) {
            this(0L, writer, title, contents);
        }

        public Question(Long id, NsUser writer, String title, String contents) {
            this.id = id;
            this.writer = writer;
            this.title = title;
            this.contents = contents;
            this.answers = new Answers();
        }

        public void addAnswer(Answer answer) {
            answers.add(answer, this);
        }

        public void isOwner(NsUser loginUser) throws CannotDeleteException {
            if (!loginUser.equals(writer)) {
                throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
            }
        }

        public List<DeleteHistory> deleteQuestion(NsUser loginUser) throws CannotDeleteException {
            isOwner(loginUser);

            List<DeleteHistory> deleteHistories = new ArrayList<>();
            deleted = true;
            deleteHistories.add(DeleteHistory.createDeleteHistory(ContentType.QUESTION, id, writer));
            answers.deleteAnswers(deleteHistories, writer);

            return deleteHistories;
        }

        public Long getId() {
            return id;
        }

        public NsUser getWriter() {
            return writer;
        }

        public List<Answer> getAnswers() {
            return answers.getAnswers();
        }

        public boolean isDeleted() {
            return deleted;
        }

        @Override
        public String toString() {
            return "Question [id=" + getId() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
        }

    }
