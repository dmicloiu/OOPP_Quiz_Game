package commons;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * This abstract class serves as an encapsulation to all questions generated for any game.
 * Both multiple-choice and open question are direct child classes of this class, meaning
 * that a game can use an array or list of questions without extensive type checking or
 * designating both as "Objects."
 */
@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(MCQuestion.class),
    @JsonSubTypes.Type(OpenQuestion.class)
})
public abstract class Question {

    /**
     * The automatically-generated id used by the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * The activity this question is based on.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    private Activity baseActivity;
    /**
     * The prompt that will appear on the screen when this question is
     * displayed. Note: the prompt should be generated using
     * Utils.generatePrompt and the baseActivity parameter for this
     * question.
     */
    private String prompt;

    /**
     * The default constructor for this question.
     */
    Question() {}

    /**
     * Used by child classes to construct a new Question object.
     *
     * @param baseActivity the activity this question is based on
     * @param prompt the prompt the game displays alongside this question
     */
    public Question(Activity baseActivity, String prompt) {
        this.baseActivity = baseActivity;
        this.prompt = prompt;
    }

    /**
     * Returns the activity this question is based on.
     *
     * @return the activity object this question encapsulates
     */
    public Activity getBaseActivity() {
        return baseActivity;
    }

    /**
     * Changes the activity this question is based on to
     * a provided one.
     *
     * @param baseActivity the new base activity of this object
     */
    public void setBaseActivity(Activity baseActivity) {
        this.baseActivity = baseActivity;
    }

    /**
     * Returns the prompt of this question.
     *
     * @return the prompt displayed on the screen alongside this question
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Changes the prompt of this question to a provided string.
     *
     * @param prompt the new prompt of this question
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
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
     * The method represents the object as a JSON string.
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return baseActivity.toString() +
            "\"prompt\": " + "\"" + this.getPrompt() + "\"" + "\n}";
    }

}
