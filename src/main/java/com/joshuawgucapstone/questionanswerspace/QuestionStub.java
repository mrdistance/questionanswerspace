package com.joshuawgucapstone.questionanswerspace;

public class QuestionStub
{
        private int Id;

        private String Text;

        private String Answer;

        private boolean IsPublic;

        public QuestionStub(int id, String text, String answer, boolean isPublic){
                this.Id = id;
                this.Text = text;
                this.Answer = answer;
                this.IsPublic = isPublic;
        }

        public int getId() {
                return Id;
        }

        public void setId(int id) {
                Id = id;
        }

        public String getText() {
                return Text;
        }

        public void setText(String text) {
                Text = text;
        }

        public String getAnswer() {
                return Answer;
        }

        public void setAnswer(String answer) {
                Answer = answer;
        }

        public boolean isPublic() {
                return IsPublic;
        }

        public void setPublic(boolean aPublic) {
                IsPublic = aPublic;
        }

        public String ToString() {return Text+"\n"+Answer;}

        @Override
        public boolean equals(Object o) {

                // If the object is compared with itself then return true
                if (o == this) {
                        return true;
                }

        /* Check if o is an instance of QuestionStub or not
          "null instanceof [type]" also returns false */
                if (!(o instanceof QuestionStub)) {
                        return false;
                }

                // typecast o to Complex so that we can compare data members
                QuestionStub stub = (QuestionStub) o;

                // Compare the data members and return accordingly
                return stub.getText().equals(Text) && stub.getAnswer().equals(Answer);
        }
}
