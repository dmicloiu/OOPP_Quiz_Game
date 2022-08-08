package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * The activity class housing the data taken from the database, to be converted into questions.
 */
@Entity
@Table(name = "Activity")
public class Activity {

    /**
     * The automatically-generated id used by the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "activityID", unique = true)
    public long activityID; // the auto-generated activity id (e.g. 1)

    @Column(name = "id")
    public String id; // the id of the activity in the ActivityBank (e.g 65-Aquarium)

    /**
     * The title of this activity, e.g. "taking a shower for 10 minutes."
     */
    @Column(name = "title")
    public String title;

    /**
     * The source of information used in this activity.
     */
    @Lob
    @Column(name = "source")
    public String source;

    /**
     * The path to the image_path to be displayed alongside this activity.
     */
    @Column(name = "image_path")
    public String image_path;

    /**
     * The consumption_in_wh value associated with the title of this activity, taken
     * from the source in the source field.
     */
    @Column(name = "consumption_in_wh")
    public long consumption_in_wh;

    /**
     * The default constructor for this class.
     */
    Activity() {}

    /**
     * Constructs a new instance of Activity, which handles the data taken and put
     * into the database, from which questions for the game are to be generated.
     *
     * @param title the title of the activity as provided by the course staff
     * @param source the source of the information contained in this activity
     * @param image_path the image_path illustrating the piece of information in this activity
     * @param consumption_in_wh the consumption_in_wh consumption associated with this activity
     */
    public Activity(String id, String title, String source,
                    String image_path, long consumption_in_wh) {
        this.id = id;
        this.title = title;
        this.source = source;
        this.image_path = image_path;
        this.consumption_in_wh = consumption_in_wh;
    }

    /**
     * Getter for the activity ID
     * @return the id if the activity
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the activity ID
     * @param id the new ID of the activity
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the title of the activity
     * @return the title of the activity
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title of the activity
     * @param title the new title of the activity
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the source
     * @return the source of the activity
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter for the source activity
     * @param source the new source of the activity
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter for the image path
     * @return the image oath of the activity
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     * Setter for the image path
     * @param image_path the new image path of the activity
     */
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }


    /**
     * Getter for the ActivityID
     * @return the ActivityID of a given activity instance
     */
    public long getActivityID() {
        return activityID;
    }

    /**
     * Setter for the activityID
     * @param id the activityID of a given actvity instance
     */
    public void setActivityID(long id) {
        this.activityID = id;
    }


    /**
     * Getter for the energy consumption of the activity
     * @return energy consumption of the activity
     */
    public long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    /**
     * Setter for the energy consumption of the activity
     * @param consumption_in_wh the new energy consumption
     */
    public void setConsumption_in_wh(long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
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
        return "{\n" +
                "\"title\": " + "\"" + this.title + "\"" + ",\n" +
                "\"source\": " + "\"" + this.source + "\"" + ",\n" +
                "\"image_path\": " + "\"" + this.image_path + "\"" + ",\n" +
                "\"consumption_in_wh\": " + this.consumption_in_wh +
                "\n}";
    }
}
