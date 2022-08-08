package commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * This class represents the multiple-choice questions in the game.
 * As such, it inherits directly from the abstract Question class.
 * Since there are multiple types of multiple-choice questions, the
 * enum Type is used to distinguish between them.
 */
@Entity
public class MCQuestion extends Question {

    /**
     * The automatically-generated id used by the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * The array of answers of this multiple-choice question.
     */
    @OneToMany(cascade = CascadeType.PERSIST)
    @OrderColumn
    private Activity[] answers;
    /**
     * The correct answer to this question. This is a field, because
     * in the case of an as much as question, the correct answer
     * will not be the same as the base activity of the question.
     * For the integer and string varieties of the multiple-choice
     * question, this field must be equal to the base activity.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    private Activity correct;
    /**
     * The type of this multiple-choice question. Note: this should
     * NOT be set to OPEN, despite this being an option in the enum.
     */
    private Type type;

    /**
     * The default constructor for this multiple choice question.
     */
    protected MCQuestion() {}

    /**
     * Constructs a new multiple-choice question object.
     *
     * @param baseActivity the activity this question is based on
     * @param prompt the prompt displayed to the user in the game
     * @param answers the three possible answers to this question to
     *                be displayed in the game
     * @param correct the correct answer to this question
     * @param type the type of this multiple-choice question (NOT OPEN!!!)
     */
    @JsonCreator
    public MCQuestion(@JsonProperty("baseActivity") Activity baseActivity,
                      @JsonProperty("prompt") String prompt,
                      @JsonProperty("answers") Activity[] answers,
                      @JsonProperty("correct") Activity correct,
                      @JsonProperty("type") Type type) {
        super(baseActivity, prompt);
        if (answers.length != 3)
            throw new IllegalArgumentException(
                "Always three answers, there are, no more, no less."
            );
        this.answers = answers;
        this.correct = correct;
        if (type == Type.OPEN)
            throw new IllegalArgumentException("A multiple-choice question cannot be open!");
        this.type = type;
    }

    /**
     * Returns the possible answers to this question as an array of activities.
     *
     * @return all three possible answers to this question
     */
    public Activity[] getAnswers() {
        return answers;
    }

    /**
     * Changes the array of possible answers to this question to
     * a provided one.
     *
     * @param answers the new set of answers for this question
     */
    public void setAnswers(Activity[] answers) {
        if (answers.length != 3)
            throw new IllegalArgumentException(
                "Always three answers, there are, no more, no less."
            );
        this.answers = answers;
    }

    /**
     * Returns the correct answer to this question.
     *
     * @return the activity representing the correct answer to this question
     */
    public Activity getCorrect() {
        return correct;
    }

    /**
     * Changes the correct answer to a new one (you shouldn't be doing this)
     *
     * @param correct the new correct answer of this question (seriously, why)
     */
    public void setCorrect(Activity correct) {
        this.correct = correct;
    }

    /**
     * Returns the type of this question (it really cannot be OPEN...)
     *
     * @return the type of this multiple choice question
     */
    public Type getType() {
        return type;
    }

    /**
     * Changes the type of this question (you know which type is unavailable ;)) )
     * This shouldn't be used without regenerating the prompt to fit the new type.
     *
     * @param type the new type of this question
     */
    public void setType(Type type) {
        if (type == Type.OPEN)
            throw new IllegalArgumentException("A multiple-choice question cannot be open!");
        this.type = type;
    }

    /**
     * The equals method, which uses the apache commons library to check if this object is identical
     * to object o.
     *
     * @param o the object to compare this to
     *
     * @return boolean indicating whether this is equal to o
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * This method uses the apache commons library to generate the hash code for this class.
     *
     * @return the integer hash code of this object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * The method uses the apache commons library to generate a string representation of this
     * object.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
