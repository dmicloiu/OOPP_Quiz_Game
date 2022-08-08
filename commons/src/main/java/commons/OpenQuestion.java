package commons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * This class represents the open questions found in the game.
 */
@Entity
public class OpenQuestion extends Question {

    /**
     * The automatically-generated id used by the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * The correct answer to this open question.
     */
    private long correct;

    /**
     * The default constructor of the open question.
     */
    private OpenQuestion() {}

    /**
     * Creates a new open question class.
     *
     * @param baseActivity the activity this question is based on
     * @param prompt the prompt displayed alongside this question
     */
    @JsonCreator
    public OpenQuestion(@JsonProperty("baseActivity") Activity baseActivity,
                        @JsonProperty("prompt") String prompt) {
        super(baseActivity, prompt);
        this.correct = baseActivity.getConsumption_in_wh();
    }

    /**
     * Returns the correct answer to this open question.
     *
     * @return the correct answer to this question expressed as a long
     */
    public long getCorrect() {
        return correct;
    }

    /**
     * Chages the correct answer to this question to a new one.
     *
     * @param correct the new correct answer
     */
    public void setCorrect(long correct) {
        if (correct <= 0)
            throw new IllegalArgumentException("Energy consumption cannot be negative or none!");
        this.correct = correct;
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
